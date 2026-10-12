package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractDefinitionNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: StructDefinitionNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class StructDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final String name;
    private final List<FieldDefinitionNodeAST> fields;

    public StructDefinitionNodeAST(int line, int column, String name, List<FieldDefinitionNodeAST> fields) {
        super(line, column);
        this.name = name;
        this.fields = fields;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(fields);
        return children;
    }

    public String name() {
        return name;
    }

    public List<FieldDefinitionNodeAST> fields() {
        return fields;
    }
}
