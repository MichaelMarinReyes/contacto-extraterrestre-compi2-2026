package com.compi.backend.ast.exprressions;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ArrayLiteralNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ArrayLiteralNodeAST extends AbstractExpressionNodeAST {

    private final List<AbstractExpressionNodeAST> elements;

    public ArrayLiteralNodeAST(int line, int column, List<AbstractExpressionNodeAST> elements) {
        super(line, column);
        this.elements = elements;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(elements);
        return children;
    }

    public List<AbstractExpressionNodeAST> elements() {
        return elements;
    }
}
