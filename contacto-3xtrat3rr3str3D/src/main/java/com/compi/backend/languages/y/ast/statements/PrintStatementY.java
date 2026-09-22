package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;

public class PrintStatementY extends NodeASTY {
    private NodeASTY expression;

    @Builder
    public PrintStatementY(NodeASTY expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
