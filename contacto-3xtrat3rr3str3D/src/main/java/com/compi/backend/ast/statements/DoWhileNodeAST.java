package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: DoWhileNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class DoWhileNodeAST extends AbstractStatementNodeAST {

    private final AbstractStatementNodeAST body;
    private final AbstractExpressionNodeAST condition;

    public DoWhileNodeAST(int line, int column, AbstractStatementNodeAST body, AbstractExpressionNodeAST condition) {
        super(line, column);
        this.body = body;
        this.condition = condition;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(body);
        children.add(condition);
        return children;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }
}
