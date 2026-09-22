package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreakStatementY extends NodeASTY {
    @Builder
    public BreakStatementY(int line, int column) {
        super(line, column);
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
