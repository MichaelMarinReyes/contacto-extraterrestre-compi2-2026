package com.compi.backend.ast.semantic;

import com.compi.backend.ast.*;
import com.compi.backend.ast.decl.*;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.symbols.*;
import java.util.List;

/** Registra firmas antes de cuerpos y conserva los frames existentes de funciones. */
final class AstDefinitionAnalyzer {
    private final AstSemanticAnalyzer a;
    AstDefinitionAnalyzer(AstSemanticAnalyzer analyzer) { a = analyzer; }

    void registerProgram(ProgramNodeAST program) {
        for (AbstractDefinitionNodeAST node : program.definitions()) {
            if (node instanceof ClassDefinitionNodeAST cls) {
                if (a.table.getClass(cls.name()) != null) a.report("La clase «" + cls.name() + "» ya está declarada", cls);
                else a.table.addClass(a.symbol(cls.name(), Type.classType(cls.name()), SymbolCategory.CLASS, 0, false, cls));
            }
        }
        for (AbstractDefinitionNodeAST node : program.definitions()) {
            if (node instanceof StructDefinitionNodeAST struct) structure(struct);
        }
        for (AbstractDefinitionNodeAST node : program.definitions()) {
            if (node instanceof FunctionDefinitionNodeAST function) registerFunction(function);
        }
        Symbol previous = a.currentClass;
        for (AbstractDefinitionNodeAST node : program.definitions()) {
            if (node instanceof ClassDefinitionNodeAST cls) {
                a.currentClass = a.table.getClass(cls.name());
                registerClassFields(cls);
                registerClassMethods(cls);
            }
        }
        a.currentClass = previous;
    }

    void structure(StructDefinitionNodeAST n) {
        if (a.model.symbolOf(n) != null) return;
        if (a.table.getStructInCurrentScope(n.name()) != null) { a.report("La estructura «" + n.name() + "» ya está declarada", n); return; }
        Symbol owner = a.symbol(n.name(), Type.structType(n.name()), SymbolCategory.STRUCT, 0, false, n);
        a.table.addStruct(owner);
        for (FieldDefinitionNodeAST field : n.fields()) {
            Type type = a.declarations.declared(field.type(), field.dimensions(), field);
            if (owner.getMember(field.name()) != null) { a.report("El campo «" + field.name() + "» está repetido", field); continue; }
            Symbol symbol = a.symbol(field.name(), type, SymbolCategory.FIELD, owner.getMembers().size(), false, field);
            owner.addMember(symbol);
            a.result(field, type);
            a.model.scope(field, a.table.getCurrentScope());
        }
    }

    private void registerFunction(FunctionDefinitionNodeAST n) {
        if (a.table.getFunction(n.name()) != null) { a.report("La función «" + n.name() + "» ya está declarada", n); return; }
        Symbol function = signature(n.name(), n.returnType(), SymbolCategory.FUNCTION, n.parameters(), n, "func_" + n.name(), 0);
        a.table.addFunction(function);
    }

    private Symbol signature(String name, Type type, SymbolCategory category, List<ParameterNodeAST> parameters,
                             AbstractNodeAST node, String scope, int firstOffset) {
        Type resolved = a.resolveType(type, node);
        Symbol symbol = a.symbol(name, resolved, category, 0, false, node);
        symbol.setReturnType(resolved);
        for (ParameterNodeAST parameter : parameters) {
            Type paramType = a.resolveType(parameter.type(), parameter);
            if (symbol.getParameters().stream().anyMatch(p -> p.getName().equals(parameter.name()))) {
                a.report("Parámetro «" + parameter.name() + "» repetido", parameter);
            }
            Symbol param = a.symbol(parameter.name(), paramType, SymbolCategory.PARAMETER, firstOffset++, false, parameter);
            param.setScope(scope); param.setByReference(parameter.byReference());
            symbol.addParameter(param);
            a.result(parameter, paramType);
        }
        return symbol;
    }

    void classBody(ClassDefinitionNodeAST n) {
        Symbol previous = a.currentClass;
        a.currentClass = a.table.getClass(n.name());
        a.enter("class_" + n.name(), 0, n);
        try {
            publishFields(n);
            for (AbstractNodeAST member : n.members()) a.evaluate(member);
        } finally { a.table.exitScope(); a.currentClass = previous; }
    }

    private void registerClassFields(ClassDefinitionNodeAST n) {
        int offset = 0;
        for (AbstractNodeAST member : n.members()) if (member instanceof AttributeDefinitionNodeAST field) {
            Type type = a.declarations.declared(field.type(), field.dimensions(), field);
            Symbol symbol = a.symbol(field.name(), type, SymbolCategory.FIELD, offset++, false, field);
            if (a.currentClass.getMember(field.name()) != null) a.report("El atributo «" + field.name() + "» ya está declarado", field);
            a.currentClass.addMember(symbol);
        }
    }

    private void publishFields(ClassDefinitionNodeAST n) {
        for (AbstractNodeAST member : n.members()) {
            if (member instanceof AttributeDefinitionNodeAST field) a.table.define(a.model.symbolOf(field));
        }
    }

    private void registerClassMethods(ClassDefinitionNodeAST n) {
        for (AbstractNodeAST member : n.members()) {
            if (member instanceof MethodDefinitionNodeAST method) registerMethod(method.name(), method.returnType(),
                    SymbolCategory.METHOD, method.parameters(), method);
            else if (member instanceof ConstructorDefinitionNodeAST constructor) {
                String name = constructor.name().isEmpty() ? n.name() : constructor.name();
                if (!name.equals(n.name())) a.report("El constructor debe llamarse «" + n.name() + "»", constructor);
                registerMethod(name, Type.VOID, SymbolCategory.CONSTRUCTOR, constructor.parameters(), constructor);
            }
        }
    }

    private void registerMethod(String name, Type type, SymbolCategory category, List<ParameterNodeAST> parameters, AbstractNodeAST node) {
        if (a.currentClass.getCallable(name, parameters.size()) != null) {
            a.report("Firma repetida de «" + name + "» con " + parameters.size() + " parámetros", node); return;
        }
        String scope = (category == SymbolCategory.CONSTRUCTOR ? "constructor_" : "method_")
                + FunctionRuntime.name(a.currentClass.getName(), name, parameters.size());
        a.currentClass.addMember(signature(name, type, category, parameters, node, scope, 1));
    }

    void functionBody(FunctionDefinitionNodeAST n) {
        Symbol function = a.model.symbolOf(n);
        if (function == null) return;
        body(function, "func_" + n.name(), 0, n, n.body());
    }
    void methodBody(MethodDefinitionNodeAST n) {
        Symbol method = a.model.symbolOf(n);
        if (method != null) body(method, "method_" + FunctionRuntime.name(a.currentClass.getName(), n.name(), n.parameters().size()), 1, n, n.body());
    }
    void constructorBody(ConstructorDefinitionNodeAST n) {
        Symbol constructor = a.model.symbolOf(n);
        if (constructor != null) body(constructor, "constructor_" + FunctionRuntime.name(a.currentClass.getName(), constructor.getName(),
                n.parameters().size()), 1, n, n.body());
    }

    private void body(Symbol symbol, String name, int offset, AbstractNodeAST node, AbstractStatementNodeAST body) {
        Type previous = a.returnType;
        boolean wasCallable = a.callable;
        a.returnType = symbol.getReturnType(); a.callable = true;
        a.enter(name, offset, node);
        try {
            if (a.currentClass != null) a.table.define(a.symbol("this", a.currentClass.getType(), SymbolCategory.VARIABLE, 0, false, node));
            a.model.symbol(node, symbol);
            for (ASTNode child : node.children()) {
                if (child instanceof ParameterNodeAST) a.model.scope(child, a.table.getCurrentScope());
            }
            for (Symbol parameter : symbol.getParameters()) {
                a.table.getCurrentScope().allocateOffset(1);
                if (!a.table.define(parameter)) a.report("Parámetro «" + parameter.getName() + "» repetido", node);
            }
            a.evaluate(body);
        } finally { a.table.exitScope(); a.returnType = previous; a.callable = wasCallable; }
    }
}
