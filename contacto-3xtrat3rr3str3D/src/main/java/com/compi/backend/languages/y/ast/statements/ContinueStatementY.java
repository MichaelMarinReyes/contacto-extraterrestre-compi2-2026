package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;

public class ContinueStatementY extends NodeASTY {
    @Builder
    public ContinueStatementY(int line, int column) {
        super(line, column);
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
