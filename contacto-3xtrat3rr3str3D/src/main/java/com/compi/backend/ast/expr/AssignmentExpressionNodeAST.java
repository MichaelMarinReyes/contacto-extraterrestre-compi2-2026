package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: AssignmentExpressionNodeAST. Datos puros; la logica vive en los visitantes. */
public class AssignmentExpressionNodeAST extends AbstractExpressionNodeAST {

    private final AbstractExpressionNodeAST target;
    private final String operator;
    private final AbstractExpressionNodeAST value;

    public AssignmentExpressionNodeAST(int line, int column, AbstractExpressionNodeAST target, String operator, AbstractExpressionNodeAST value) {
        super(line, column);
        this.target = target;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(target);
        children.add(value);
        return children;
    }

    public AbstractExpressionNodeAST target() {
        return target;
    }

    public String operator() {
        return operator;
    }

    public AbstractExpressionNodeAST value() {
        return value;
    }
}
