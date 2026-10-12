package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.List;

/**
 * Nodo del AST: ContinueNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ContinueNodeAST extends AbstractStatementNodeAST {


    public ContinueNodeAST(int line, int column) {
        super(line, column);
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        return List.of();
    }
}
