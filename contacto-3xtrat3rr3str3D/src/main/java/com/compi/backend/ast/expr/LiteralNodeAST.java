package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: LiteralNodeAST. Datos puros; la logica vive en los visitantes. */
public class LiteralNodeAST extends AbstractExpressionNodeAST {

    private final Object value;
    private final LiteralKind kind;

    public LiteralNodeAST(int line, int column, Object value, LiteralKind kind) {
        super(line, column);
        this.value = value;
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

    public Object value() {
        return value;
    }

    public LiteralKind kind() {
        return kind;
    }
}
