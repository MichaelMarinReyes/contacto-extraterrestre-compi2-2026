package com.compi.backend.ast.codegen;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.semantic.SemanticModel;
import com.compi.backend.ast.statements.*;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

/**
 * Generación compartida sobre el AST comprobado; no declara ni resuelve otra vez los símbolos.
 */
public final class AstCodeGenerator extends AbstractASTVisitor<String> {
    final SymbolTable table;
    final SemanticModel model;
    final C3DGenerator c3d;
    final String language;
    final AstMemoryEmitter memory;
    final AstExpressionEmitter expressions;
    final AstAggregateEmitter aggregates;
    final AstControlEmitter control;
    final AstDefinitionEmitter definitions;
    ClassDefinitionNodeAST currentClass;
    private String value;

    public AstCodeGenerator(SymbolTable table, SemanticModel model, C3DGenerator c3d, String language) {
        this.table = table;
        this.model = java.util.Objects.requireNonNull(model, "Se requiere el modelo semántico del AST");
        this.c3d = c3d;
        this.language = language;
        memory = new AstMemoryEmitter(this);
        expressions = new AstExpressionEmitter(this);
        aggregates = new AstAggregateEmitter(this);
        control = new AstControlEmitter(this);
        definitions = new AstDefinitionEmitter(this);
    }

    public void generate(ProgramNodeAST program) {
        table.beginCodeGeneration();
        try {
            assignTypeTags();
            emit(program);
        } finally {
            table.endCodeGeneration();
        }
    }

    /** Asigna una marca de tipo única por clase para el despacho virtual. */
    private void assignTypeTags() {
        int tag = 1;
        for (Symbol clazz : table.getClasses()) {
            clazz.setTypeTag(tag++);
        }
    }

    String emit(ASTNode node) {
        if (node == null) return null;
        node.accept(this);
        return value;
    }

    private String result(String result) {
        value = result;
        return result;
    }

    Type type(ASTNode node) {
        return model.typeOf(node);
    }

    Symbol symbol(ASTNode node) {
        Symbol symbol = model.symbolOf(node);
        if (symbol == null) throw new IllegalStateException("El nodo no tiene un símbolo semántico: " + node);
        return symbol;
    }

    void enter(AbstractNodeAST node) {
        table.enterAstScope("", 0, node);
    }

    @Override
    public String visit(ProgramNodeAST n) {
        n.definitions().forEach(this::emit);
        n.globals().forEach(this::emit);
        if (language.equals("pig")) {
            c3d.emit(QuadrupleOp.FUNCTION_START, null, null, "main");
            enter(n);
            try {
                n.statements().forEach(this::emit);
            } finally {
                table.exitScope();
            }
            c3d.emit(QuadrupleOp.FUNCTION_END, null, null, "main");
        } else n.statements().forEach(this::emit);
        return result(null);
    }

    @Override
    public String visit(BlockNodeAST n) {
        n.statements().forEach(this::emit);
        return result(null);
    }

    @Override
    public String visit(DeclarationNodeAST n) {
        aggregates.declare(n);
        return result(null);
    }

    @Override
    public String visit(DefinitionStatementNodeAST n) {
        emit(n.definition());
        return result(null);
    }

    @Override
    public String visit(ExpressionStatementNodeAST n) {
        emit(n.expression());
        return result(null);
    }

    @Override
    public String visit(AssignmentNodeAST n) {
        return result(expressions.assign(n.target(), n.operator(), n.value()));
    }

    @Override
    public String visit(AssignmentExpressionNodeAST n) {
        return result(expressions.assign(n.target(), n.operator(), n.value()));
    }

    @Override
    public String visit(PrintNodeAST n) {
        for (AbstractExpressionNodeAST expression : n.expressions()) {
            c3d.emit(expressions.printOp(type(expression)), emit(expression), null, null);
        }
        if (n.newline()) c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return result(null);
    }

    @Override
    public String visit(ReadStatementNodeAST n) {
        if (n.target() != null) memory.write(memory.place(n.target()), expressions.read(type(n.target())));
        return result(null);
    }

    @Override
    public String visit(ReturnNodeAST n) {
        c3d.emitReturn(emit(n.expression()));
        return result(null);
    }

    @Override
    public String visit(IfNodeAST n) {
        control.conditional(n);
        return result(null);
    }

    @Override
    public String visit(ElseIfNodeAST n) {
        throw new IllegalStateException("La rama debe emitirse desde su condicional");
    }

    @Override
    public String visit(ForNodeAST n) {
        control.forLoop(n);
        return result(null);
    }

    @Override
    public String visit(WhileNodeAST n) {
        control.whileLoop(n);
        return result(null);
    }

    @Override
    public String visit(DoWhileNodeAST n) {
        control.doLoop(n);
        return result(null);
    }

    @Override
    public String visit(SwitchNodeAST n) {
        control.selection(n);
        return result(null);
    }

    @Override
    public String visit(SwitchCaseNodeAST n) {
        emit(n.body());
        return result(null);
    }

    @Override
    public String visit(BreakNodeAST n) {
        c3d.emitGotoTop(control.breakLabels);
        return result(null);
    }

    @Override
    public String visit(ContinueNodeAST n) {
        c3d.emitGotoTop(control.continueLabels);
        return result(null);
    }

    @Override
    public String visit(ClassDefinitionNodeAST n) {
        definitions.classBody(n);
        return result(null);
    }

    @Override
    public String visit(FunctionDefinitionNodeAST n) {
        definitions.function(n);
        return result(null);
    }

    @Override
    public String visit(MethodDefinitionNodeAST n) {
        definitions.method(n);
        return result(null);
    }

    @Override
    public String visit(ConstructorDefinitionNodeAST n) {
        definitions.constructor(n);
        return result(null);
    }

    @Override
    public String visit(AttributeDefinitionNodeAST n) {
        if (n.initializer() != null)
            memory.store(symbol(n), aggregates.initializer(n.initializer(), symbol(n).getType()));
        return result(null);
    }

    @Override
    public String visit(StructDefinitionNodeAST n) {
        return result(null);
    }

    @Override
    public String visit(FieldDefinitionNodeAST n) {
        return result(null);
    }

    @Override
    public String visit(ParameterNodeAST n) {
        return result(null);
    }

    @Override
    public String visit(ImportNodeAST n) {
        return result(null);
    }

    @Override
    public String visit(LiteralNodeAST n) {
        return result(expressions.literal(n));
    }

    @Override
    public String visit(IdentifierNodeAST n) {
        return result(memory.load(symbol(n)));
    }

    @Override
    public String visit(MemberAccessNodeAST n) {
        return result(memory.read(memory.place(n)));
    }

    @Override
    public String visit(IndexNodeAST n) {
        var place = memory.place(n);
        return result(type(n).isArray() ? place.address() : memory.read(place));
    }

    @Override
    public String visit(BinaryOperationNodeAST n) {
        return result(expressions.binary(n));
    }

    @Override
    public String visit(UnaryOperationNodeAST n) {
        return result(expressions.unary(n));
    }

    @Override
    public String visit(IncrementNodeAST n) {
        return result(expressions.increment(n));
    }

    @Override
    public String visit(TernaryNodeAST n) {
        return result(expressions.ternary(n));
    }

    @Override
    public String visit(CallNodeAST n) {
        return result(expressions.call(n));
    }

    @Override
    public String visit(ReadExpressionNodeAST n) {
        return result(expressions.read(language.equals("y") ? Type.STRING : Type.UNKNOWN));
    }

    @Override
    public String visit(NewObjectNodeAST n) {
        return result(aggregates.newObject(n));
    }

    @Override
    public String visit(NewArrayNodeAST n) {
        return result(ArrayRuntime.allocateArray(c3d, n.sizes().stream().map(this::emit).toList()));
    }

    @Override
    public String visit(ArrayLiteralNodeAST n) {
        return result(aggregates.initializer(n, type(n)));
    }

    @Override
    public String visit(InstanceInitNodeAST n) {
        return result(aggregates.instance(n, type(n)));
    }
}
