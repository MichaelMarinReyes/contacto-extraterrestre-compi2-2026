package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: ElseIfNodeAST. Datos puros; la logica vive en los visitantes. */
public class ElseIfNodeAST extends AbstractNodeAST {

    private final AbstractExpressionNodeAST condition;
    private final AbstractStatementNodeAST body;

    public ElseIfNodeAST(int line, int column, AbstractExpressionNodeAST condition, AbstractStatementNodeAST body) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(condition);
        children.add(body);
        return children;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
