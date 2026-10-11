package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.symbols.Type;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: NewArrayNodeAST. Datos puros; la logica vive en los visitantes. */
public class NewArrayNodeAST extends AbstractExpressionNodeAST {

    private final Type elementType;
    private final List<AbstractExpressionNodeAST> sizes;

    public NewArrayNodeAST(int line, int column, Type elementType, List<AbstractExpressionNodeAST> sizes) {
        super(line, column);
        this.elementType = elementType;
        this.sizes = sizes;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(sizes);
        return children;
    }

    public Type elementType() {
        return elementType;
    }

    public List<AbstractExpressionNodeAST> sizes() {
        return sizes;
    }
}
