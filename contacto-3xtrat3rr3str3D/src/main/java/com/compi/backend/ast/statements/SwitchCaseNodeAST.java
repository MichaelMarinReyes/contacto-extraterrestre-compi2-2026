package com.compi.backend.ast.statements;

import com.compi.backend.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: SwitchCaseNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class SwitchCaseNodeAST extends AbstractNodeAST {

    private final AbstractExpressionNodeAST value;
    private final AbstractStatementNodeAST body;

    public SwitchCaseNodeAST(int line, int column, AbstractExpressionNodeAST value, AbstractStatementNodeAST body) {
        super(line, column);
        this.value = value;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(value);
        children.add(body);
        return children;
    }

    public AbstractExpressionNodeAST value() {
        return value;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
