package com.compi.backend.ast.statements;

import com.compi.backend.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ElseIfNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ElseIfNodeAST extends AbstractNodeAST {

    private final AbstractExpressionNodeAST condition;
    private final AbstractStatementNodeAST body;

    public ElseIfNodeAST(int line, int column, AbstractExpressionNodeAST condition, AbstractStatementNodeAST body) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(condition);
        children.add(body);
        return children;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
