package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractNodeAST;

import java.util.List;

/**
 * Nodo del AST: ImportNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ImportNodeAST extends AbstractNodeAST {

    private final String path;

    public ImportNodeAST(int line, int column, String path) {
        super(line, column);
        this.path = path;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        return List.of();
    }

    public String path() {
        return path;
    }
}
