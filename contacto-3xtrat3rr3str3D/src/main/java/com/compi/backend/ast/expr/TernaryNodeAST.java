package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: TernaryNodeAST. Datos puros; la logica vive en los visitantes. */
public class TernaryNodeAST extends AbstractExpressionNodeAST {

    private final AbstractExpressionNodeAST condition;
    private final AbstractExpressionNodeAST thenExpr;
    private final AbstractExpressionNodeAST elseExpr;

    public TernaryNodeAST(int line, int column, AbstractExpressionNodeAST condition, AbstractExpressionNodeAST thenExpr, AbstractExpressionNodeAST elseExpr) {
        super(line, column);
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(condition);
        children.add(thenExpr);
        children.add(elseExpr);
        return children;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }

    public AbstractExpressionNodeAST thenExpr() {
        return thenExpr;
    }

    public AbstractExpressionNodeAST elseExpr() {
        return elseExpr;
    }
}
