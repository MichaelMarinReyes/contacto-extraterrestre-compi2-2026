package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: BreakNodeAST. Datos puros; la logica vive en los visitantes. */
public class BreakNodeAST extends AbstractStatementNodeAST {


    public BreakNodeAST(int line, int column) {
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
