package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.symbols.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: FieldDefinitionNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class FieldDefinitionNodeAST extends AbstractNodeAST {

    private final Type type;
    private final String name;
    private final List<AbstractExpressionNodeAST> dimensions;

    public FieldDefinitionNodeAST(int line, int column, Type type, String name, List<AbstractExpressionNodeAST> dimensions) {
        super(line, column);
        this.type = type;
        this.name = name;
        this.dimensions = dimensions;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(dimensions);
        return children;
    }

    public Type type() {
        return type;
    }

    public String name() {
        return name;
    }

    public List<AbstractExpressionNodeAST> dimensions() {
        return dimensions;
    }
}
