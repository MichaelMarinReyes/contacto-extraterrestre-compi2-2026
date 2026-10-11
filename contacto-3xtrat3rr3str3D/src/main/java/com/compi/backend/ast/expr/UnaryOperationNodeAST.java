package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: UnaryOperationNodeAST. Datos puros; la logica vive en los visitantes. */
public class UnaryOperationNodeAST extends AbstractExpressionNodeAST {

    private final String operator;
    private final AbstractExpressionNodeAST operand;

    public UnaryOperationNodeAST(int line, int column, String operator, AbstractExpressionNodeAST operand) {
        super(line, column);
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(operand);
        return children;
    }

    public String operator() {
        return operator;
    }

    public AbstractExpressionNodeAST operand() {
        return operand;
    }
}
