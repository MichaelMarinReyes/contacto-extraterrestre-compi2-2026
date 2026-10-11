package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: ReadExpressionNodeAST. Datos puros; la logica vive en los visitantes. */
public class ReadExpressionNodeAST extends AbstractExpressionNodeAST {

    private final ReadKind kind;

    public ReadExpressionNodeAST(int line, int column, ReadKind kind) {
        super(line, column);
        this.kind = kind;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        return List.of();
    }

    public ReadKind kind() {
        return kind;
    }
}
