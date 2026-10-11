package com.compi.backend.ast.stmt;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: PrintNodeAST. Datos puros; la logica vive en los visitantes. */
public class PrintNodeAST extends AbstractStatementNodeAST {

    private final List<AbstractExpressionNodeAST> expressions;

    public PrintNodeAST(int line, int column, List<AbstractExpressionNodeAST> expressions) {
        super(line, column);
        this.expressions = expressions;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(expressions);
        return children;
    }

    public List<AbstractExpressionNodeAST> expressions() {
        return expressions;
    }
}
