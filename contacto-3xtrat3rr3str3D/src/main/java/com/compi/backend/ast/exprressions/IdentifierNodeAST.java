package com.compi.backend.ast.exprressions;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.symbols.Symbol;

import java.util.List;

/**
 * Nodo del AST: IdentifierNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class IdentifierNodeAST extends AbstractExpressionNodeAST {

    private final String name;
    private final Symbol resolved;

    public IdentifierNodeAST(int line, int column, String name, Symbol resolved) {
        super(line, column);
        this.name = name;
        this.resolved = resolved;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        return List.of();
    }

    public String name() {
        return name;
    }

    public Symbol resolved() {
        return resolved;
    }
}
