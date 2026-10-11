package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: ExpressionStatementNodeAST. Datos puros; la logica vive en los visitantes. */
public class ExpressionStatementNodeAST extends AbstractStatementNodeAST {

    private final AbstractExpressionNodeAST expression;

    public ExpressionStatementNodeAST(int line, int column, AbstractExpressionNodeAST expression) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(expression);
        return children;
    }

    public AbstractExpressionNodeAST expression() {
        return expression;
    }
}
