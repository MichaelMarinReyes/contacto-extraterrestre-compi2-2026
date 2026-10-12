package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ReadStatementNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ReadStatementNodeAST extends AbstractStatementNodeAST {

    private final AbstractExpressionNodeAST target;

    public ReadStatementNodeAST(int line, int column, AbstractExpressionNodeAST target) {
        super(line, column);
        this.target = target;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(target);
        return children;
    }

    public AbstractExpressionNodeAST target() {
        return target;
    }
}
