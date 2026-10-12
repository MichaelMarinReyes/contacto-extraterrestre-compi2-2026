package com.compi.backend.ast.statements;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: SwitchNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class SwitchNodeAST extends AbstractStatementNodeAST {

    private final AbstractExpressionNodeAST subject;
    private final List<SwitchCaseNodeAST> cases;
    private final AbstractStatementNodeAST defaultBody;

    public SwitchNodeAST(int line, int column, AbstractExpressionNodeAST subject, List<SwitchCaseNodeAST> cases, AbstractStatementNodeAST defaultBody) {
        super(line, column);
        this.subject = subject;
        this.cases = cases;
        this.defaultBody = defaultBody;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.add(subject);
        children.addAll(cases);
        children.add(defaultBody);
        return children;
    }

    public AbstractExpressionNodeAST subject() {
        return subject;
    }

    public List<SwitchCaseNodeAST> cases() {
        return cases;
    }

    public AbstractStatementNodeAST defaultBody() {
        return defaultBody;
    }
}
