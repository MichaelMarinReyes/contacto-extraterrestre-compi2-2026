package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: IndexNodeAST. Datos puros; la logica vive en los visitantes. */
public class IndexNodeAST extends AbstractExpressionNodeAST {

    private final AbstractExpressionNodeAST target;
    private final List<AbstractExpressionNodeAST> indices;

    public IndexNodeAST(int line, int column, AbstractExpressionNodeAST target, List<AbstractExpressionNodeAST> indices) {
        super(line, column);
        this.target = target;
        this.indices = indices;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(target);
        children.addAll(indices);
        return children;
    }

    public AbstractExpressionNodeAST target() {
        return target;
    }

    public List<AbstractExpressionNodeAST> indices() {
        return indices;
    }
}
