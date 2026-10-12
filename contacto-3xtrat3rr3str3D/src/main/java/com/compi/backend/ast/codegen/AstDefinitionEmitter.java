package com.compi.backend.ast.codegen;

import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import com.compi.backend.ast.declarations.*;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.c3d.QuadrupleOp;

/**
 * Emite cuerpos de funciones y métodos reutilizando sus símbolos y ámbitos.
 */
final class AstDefinitionEmitter {
    private final AstCodeGenerator g;

    AstDefinitionEmitter(AstCodeGenerator generator) {
        g = generator;
    }

    void classBody(ClassDefinitionNodeAST n) {
        ClassDefinitionNodeAST previous = g.currentClass;
        g.currentClass = n;
        g.enter(n);
        try {
            for (AbstractNodeAST member : n.members()) {
                if (!(member instanceof AttributeDefinitionNodeAST)) g.emit(member);
            }
        } finally {
            g.table.exitScope();
            g.currentClass = previous;
        }
    }

    void function(FunctionDefinitionNodeAST n) {
        body(n, g.symbol(n).getName(), n.body(), false);
    }

    void method(MethodDefinitionNodeAST n) {
        body(n, FunctionRuntime.name(g.currentClass.name(), g.symbol(n).getName(), n.parameters().size()), n.body(), false);
    }

    void constructor(ConstructorDefinitionNodeAST n) {
        body(n, FunctionRuntime.name(g.currentClass.name(), g.symbol(n).getName(), n.parameters().size()), n.body(), true);
    }

    private void body(AbstractNodeAST n, String name, AbstractStatementNodeAST statements, boolean constructor) {
        g.c3d.emit(QuadrupleOp.FUNCTION_START, null, null, name);
        g.enter(n);
        try {
            if (constructor) for (AbstractNodeAST member : g.currentClass.members()) {
                if (member instanceof AttributeDefinitionNodeAST) g.emit(member);
            }
            g.emit(statements);
            g.c3d.emit(QuadrupleOp.FUNCTION_END, null, null, name);
        } finally {
            g.table.exitScope();
        }
    }
}
