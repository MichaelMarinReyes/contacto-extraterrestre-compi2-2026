package com.compi.backend.ast.exprressions;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: MemberAccessNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class MemberAccessNodeAST extends AbstractExpressionNodeAST {

    private final AbstractExpressionNodeAST target;
    private final String member;
    private final Symbol resolved;

    public MemberAccessNodeAST(int line, int column, AbstractExpressionNodeAST target, String member, Symbol resolved) {
        super(line, column);
        this.target = target;
        this.member = member;
        this.resolved = resolved;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(target);
        return children;
    }

    public AbstractExpressionNodeAST target() {
        return target;
    }

    public String member() {
        return member;
    }

    public Symbol resolved() {
        return resolved;
    }
}
