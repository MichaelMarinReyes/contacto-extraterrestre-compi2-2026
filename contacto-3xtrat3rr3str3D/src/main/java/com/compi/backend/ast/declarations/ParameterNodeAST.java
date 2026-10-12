package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.symbols.Type;

import java.util.List;

/**
 * Nodo del AST: ParameterNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ParameterNodeAST extends AbstractNodeAST {

    private final Type type;
    private final String name;
    private final boolean byReference;

    public ParameterNodeAST(int line, int column, Type type, String name, boolean byReference) {
        super(line, column);
        this.type = type;
        this.name = name;
        this.byReference = byReference;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        return List.of();
    }

    public Type type() {
        return type;
    }

    public String name() {
        return name;
    }

    public boolean byReference() {
        return byReference;
    }
}
