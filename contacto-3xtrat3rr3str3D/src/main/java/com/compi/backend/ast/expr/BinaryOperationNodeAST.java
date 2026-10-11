package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: BinaryOperationNodeAST. Datos puros; la logica vive en los visitantes. */
public class BinaryOperationNodeAST extends AbstractExpressionNodeAST {

    private final AbstractExpressionNodeAST left;
    private final String operator;
    private final AbstractExpressionNodeAST right;
    private final OperationKind kind;

    public BinaryOperationNodeAST(int line, int column, AbstractExpressionNodeAST left, String operator, AbstractExpressionNodeAST right, OperationKind kind) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.kind = kind;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        return children;
    }

    public AbstractExpressionNodeAST left() {
        return left;
    }

    public String operator() {
        return operator;
    }

    public AbstractExpressionNodeAST right() {
        return right;
    }

    public OperationKind kind() {
        return kind;
    }
}
