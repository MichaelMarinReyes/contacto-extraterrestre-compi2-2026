package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: IfNodeAST. Datos puros; la logica vive en los visitantes. */
public class IfNodeAST extends AbstractStatementNodeAST {

    private final AbstractExpressionNodeAST condition;
    private final AbstractStatementNodeAST thenBody;
    private final List<ElseIfNodeAST> elseIfs;
    private final AbstractStatementNodeAST elseBody;

    public IfNodeAST(int line, int column, AbstractExpressionNodeAST condition, AbstractStatementNodeAST thenBody, List<ElseIfNodeAST> elseIfs, AbstractStatementNodeAST elseBody) {
        super(line, column);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseIfs = elseIfs;
        this.elseBody = elseBody;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(condition);
        children.add(thenBody);
        children.addAll(elseIfs);
        children.add(elseBody);
        return children;
    }

    public AbstractExpressionNodeAST condition() {
        return condition;
    }

    public AbstractStatementNodeAST thenBody() {
        return thenBody;
    }

    public List<ElseIfNodeAST> elseIfs() {
        return elseIfs;
    }

    public AbstractStatementNodeAST elseBody() {
        return elseBody;
    }
}
