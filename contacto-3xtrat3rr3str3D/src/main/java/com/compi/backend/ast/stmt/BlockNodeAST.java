package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: BlockNodeAST. Datos puros; la logica vive en los visitantes. */
public class BlockNodeAST extends AbstractStatementNodeAST {

    private final List<AbstractStatementNodeAST> statements;

    public BlockNodeAST(int line, int column, List<AbstractStatementNodeAST> statements) {
        super(line, column);
        this.statements = statements;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(statements);
        return children;
    }

    public List<AbstractStatementNodeAST> statements() {
        return statements;
    }
}
