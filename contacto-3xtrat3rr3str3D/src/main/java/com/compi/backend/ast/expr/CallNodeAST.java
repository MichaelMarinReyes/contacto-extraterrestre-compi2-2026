package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.symbols.Symbol;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: CallNodeAST. Datos puros; la logica vive en los visitantes. */
public class CallNodeAST extends AbstractExpressionNodeAST {

    private final String name;
    private final List<AbstractExpressionNodeAST> arguments;
    private final Symbol resolved;

    public CallNodeAST(int line, int column, String name, List<AbstractExpressionNodeAST> arguments, Symbol resolved) {
        super(line, column);
        this.name = name;
        this.arguments = arguments;
        this.resolved = resolved;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(arguments);
        return children;
    }

    public String name() {
        return name;
    }

    public List<AbstractExpressionNodeAST> arguments() {
        return arguments;
    }

    public Symbol resolved() {
        return resolved;
    }
}
