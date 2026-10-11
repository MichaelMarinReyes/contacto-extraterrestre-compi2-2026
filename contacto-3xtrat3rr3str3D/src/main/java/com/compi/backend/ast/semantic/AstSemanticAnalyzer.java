package com.compi.backend.ast.semantic;

import com.compi.backend.ast.*;
import com.compi.backend.ast.decl.*;
import com.compi.backend.ast.expr.*;
import com.compi.backend.ast.stmt.*;
import com.compi.backend.errors.*;
import com.compi.backend.symbols.*;
import java.util.List;

/** Visitante semántico compartido: solo consume nodos del AST propio. */
public final class AstSemanticAnalyzer extends AbstractASTVisitor<Type> {
    final SymbolTable table;
    final SemanticModel model = new SemanticModel();
    final List<CompilationError> errors;
    final String language;
    final AstExpressionAnalyzer expressions;
    final AstDeclarationAnalyzer declarations;
    final AstDefinitionAnalyzer definitions;
    Symbol currentClass;
    Type returnType = Type.VOID;
    int loopDepth;
    int switchDepth;
    boolean callable;

    public AstSemanticAnalyzer(SymbolTable table, String language, List<CompilationError> errors) {
        this.table = table;
        this.language = language;
        this.errors = errors;
        expressions = new AstExpressionAnalyzer(this);
        declarations = new AstDeclarationAnalyzer(this);
        definitions = new AstDefinitionAnalyzer(this);
    }

    public SemanticModel analyze(ProgramNodeAST program) {
        evaluate(program);
        return model;
    }

    Type evaluate(ASTNode node) {
        if (node == null) return Type.VOID;
        model.scope(node, table.getCurrentScope());
        node.accept(this);
        return model.typeOf(node);
    }

    Type result(ASTNode node, Type type) { model.type(node, type); return type; }

    void report(String message, ASTNode node) {
        CompilationError error = new CompilationError(ErrorType.SEMANTIC, message, node.line(), node.column());
        if (node instanceof AbstractNodeAST ast && ast.sourceFile() != null) error.inFile(ast.sourceFile());
        errors.add(error);
    }

    Symbol symbol(String name, Type type, SymbolCategory category, int offset, boolean global, AbstractNodeAST node) {
        Symbol symbol = new Symbol(name, type, category, offset, global);
        symbol.setLine(node.line());
        symbol.setColumn(node.column() + 1);
        symbol.inLanguage(switch (language) { case "pig" -> "PigLatin"; case "y" -> "Y"; default -> "Zetariano"; });
        if (node.sourceFile() != null) symbol.inFile(node.sourceFile());
        model.symbol(node, symbol);
        return symbol;
    }

    Type resolveType(Type type, ASTNode node) {
        if (type.isArray()) return Type.arrayOf(resolveType(type.getElementType(), node), type.getSizes());
        if (type.isClass() || type.isStruct()) {
            String name = type.getCustomTypeName();
            Type resolved = table.resolveType(name);
            if (resolved == null) { report("Tipo «" + name + "» no definido", node); return Type.UNKNOWN; }
            return resolved;
        }
        return type;
    }

    void compatible(Type expected, Type actual, ASTNode node) {
        if (Type.isKnown(expected) && Type.isKnown(actual) && !actual.isAssignableTo(expected)) {
            report("Tipos incompatibles: se esperaba «" + expected.label() + "» y se obtuvo «" + actual.label() + "»", node);
        }
    }

    void condition(AbstractExpressionNodeAST node) {
        Type type = evaluate(node);
        if (Type.isKnown(type) && !Type.BOOLEAN.equals(type)) report("La condición debe ser booleana", node);
    }

    void enter(String name, int offset, AbstractNodeAST node) {
        table.enterAstScope(name, offset, node);
        model.scope(node, table.getCurrentScope());
    }

    @Override public Type visit(ProgramNodeAST n) {
        definitions.registerProgram(n);
        for (AbstractDefinitionNodeAST definition : n.definitions()) evaluate(definition);
        for (AbstractStatementNodeAST global : n.globals()) declarations.global(global);
        if (language.equals("pig")) enter("maior", 0, n);
        try { for (AbstractStatementNodeAST statement : n.statements()) evaluate(statement); }
        finally { if (language.equals("pig")) table.exitScope(); }
        return result(n, Type.VOID);
    }

    @Override public Type visit(BlockNodeAST n) {
        // Los bloques ordinarios conservan el ámbito del lenguaje; las funciones y for crean uno.
        for (AbstractStatementNodeAST statement : n.statements()) evaluate(statement);
        return result(n, Type.VOID);
    }

    @Override public Type visit(DeclarationNodeAST n) { return result(n, declarations.declare(n, false)); }
    @Override public Type visit(DefinitionStatementNodeAST n) { return result(n, evaluate(n.definition())); }
    @Override public Type visit(ExpressionStatementNodeAST n) { return result(n, evaluate(n.expression())); }
    @Override public Type visit(AssignmentNodeAST n) { return result(n, expressions.assign(n.target(), n.operator(), n.value(), n)); }
    @Override public Type visit(PrintNodeAST n) {
        n.expressions().forEach(this::evaluate);
        return result(n, Type.VOID);
    }
    @Override public Type visit(ReadStatementNodeAST n) {
        if (n.target() != null) { expressions.requireTarget(n.target()); evaluate(n.target()); }
        return result(n, Type.VOID);
    }

    @Override public Type visit(IfNodeAST n) {
        condition(n.condition()); evaluate(n.thenBody());
        n.elseIfs().forEach(this::evaluate); evaluate(n.elseBody());
        return result(n, Type.VOID);
    }
    @Override public Type visit(ElseIfNodeAST n) {
        condition(n.condition()); evaluate(n.body()); return result(n, Type.VOID);
    }
    @Override public Type visit(WhileNodeAST n) {
        condition(n.condition()); loopDepth++;
        try { evaluate(n.body()); } finally { loopDepth--; }
        return result(n, Type.VOID);
    }
    @Override public Type visit(DoWhileNodeAST n) {
        loopDepth++;
        try { evaluate(n.body()); condition(n.condition()); } finally { loopDepth--; }
        return result(n, Type.VOID);
    }
    @Override public Type visit(ForNodeAST n) {
        enter("for_" + n.line() + "_" + n.column(), 0, n);
        loopDepth++;
        try {
            evaluate(n.initializer());
            if (n.condition() != null) condition(n.condition());
            evaluate(n.update()); evaluate(n.body());
        } finally { loopDepth--; table.exitScope(); }
        return result(n, Type.VOID);
    }
    @Override public Type visit(SwitchNodeAST n) {
        Type subject = evaluate(n.subject()); switchDepth++;
        try {
            for (SwitchCaseNodeAST branch : n.cases()) {
                compatible(subject, evaluate(branch.value()), branch.value());
                evaluate(branch);
            }
            evaluate(n.defaultBody());
        } finally { switchDepth--; }
        return result(n, Type.VOID);
    }
    @Override public Type visit(SwitchCaseNodeAST n) { evaluate(n.body()); return result(n, Type.VOID); }
    @Override public Type visit(BreakNodeAST n) {
        if (loopDepth == 0 && switchDepth == 0) report("El salto requiere un ciclo o switch/elegir", n);
        return result(n, Type.VOID);
    }
    @Override public Type visit(ContinueNodeAST n) {
        if (loopDepth == 0 && !(language.equals("y") && switchDepth > 0)) report("Continuar requiere un ciclo", n);
        return result(n, Type.VOID);
    }
    @Override public Type visit(ReturnNodeAST n) {
        Type actual = n.expression() == null ? Type.VOID : evaluate(n.expression());
        if (!callable) report("Retornar requiere una función o método", n);
        else compatible(returnType, actual, n);
        return result(n, actual);
    }
    @Override public Type visit(ImportNodeAST n) { return result(n, Type.VOID); }
    @Override public Type visit(ClassDefinitionNodeAST n) { definitions.classBody(n); return result(n, Type.VOID); }
    @Override public Type visit(StructDefinitionNodeAST n) { definitions.structure(n); return result(n, Type.VOID); }
    @Override public Type visit(FunctionDefinitionNodeAST n) { definitions.functionBody(n); return result(n, Type.VOID); }
    @Override public Type visit(MethodDefinitionNodeAST n) { definitions.methodBody(n); return result(n, Type.VOID); }
    @Override public Type visit(ConstructorDefinitionNodeAST n) { definitions.constructorBody(n); return result(n, Type.VOID); }
    @Override public Type visit(AttributeDefinitionNodeAST n) { return result(n, declarations.attribute(n)); }
    @Override public Type visit(FieldDefinitionNodeAST n) { return result(n, resolveType(n.type(), n)); }
    @Override public Type visit(ParameterNodeAST n) { return result(n, resolveType(n.type(), n)); }
    @Override public Type visit(LiteralNodeAST n) { return result(n, expressions.literal(n)); }
    @Override public Type visit(IdentifierNodeAST n) { return result(n, expressions.identifier(n)); }
    @Override public Type visit(BinaryOperationNodeAST n) { return result(n, expressions.binary(n)); }
    @Override public Type visit(UnaryOperationNodeAST n) { return result(n, expressions.unary(n)); }
    @Override public Type visit(IndexNodeAST n) { return result(n, expressions.index(n)); }
    @Override public Type visit(MemberAccessNodeAST n) { return result(n, expressions.member(n)); }
    @Override public Type visit(CallNodeAST n) { return result(n, expressions.call(n)); }
    @Override public Type visit(NewObjectNodeAST n) { return result(n, expressions.newObject(n)); }
    @Override public Type visit(NewArrayNodeAST n) { return result(n, declarations.newArray(n)); }
    @Override public Type visit(ArrayLiteralNodeAST n) { return result(n, declarations.arrayLiteral(n, Type.UNKNOWN)); }
    @Override public Type visit(InstanceInitNodeAST n) { return result(n, declarations.instance(n, Type.UNKNOWN)); }
    @Override public Type visit(TernaryNodeAST n) { return result(n, expressions.ternary(n)); }
    @Override public Type visit(IncrementNodeAST n) { return result(n, expressions.increment(n)); }
    @Override public Type visit(ReadExpressionNodeAST n) { return result(n, Type.UNKNOWN); }
    @Override public Type visit(AssignmentExpressionNodeAST n) { return result(n, expressions.assign(n.target(), n.operator(), n.value(), n)); }
}
