package com.compi.backend.languages.y.ast.expressions;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteralY extends NodeASTY {
    private Object value;

    @Builder
    public LiteralY(Object value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
