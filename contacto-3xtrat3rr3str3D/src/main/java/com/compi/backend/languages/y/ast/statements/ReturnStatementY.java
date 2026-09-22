package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnStatementY extends NodeASTY {
    private NodeASTY expression;

    @Builder
    public ReturnStatementY(NodeASTY expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
