package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ForNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ForNodeAST extends AbstractStatementNodeAST {

    private final AbstractStatementNodeAST initializer;
    private final AbstractExpressionNodeAST condition;
    private final AbstractStatementNodeAST update;
    private final AbstractStatementNodeAST body;

    public ForNodeAST(int line, int column, AbstractStatementNodeAST initializer, AbstractExpressionNodeAST condition, AbstractStatementNodeAST update, AbstractStatementNodeAST body) {
        super(line, column);
        this.initializer = initializer;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(initializer);
        children.add(condition);
        children.add(update);
        children.add(body);
        return children;
    }

    public AbstractStatementNodeAST initializer() {
        return initializer;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }

    public AbstractStatementNodeAST update() {
        return update;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
