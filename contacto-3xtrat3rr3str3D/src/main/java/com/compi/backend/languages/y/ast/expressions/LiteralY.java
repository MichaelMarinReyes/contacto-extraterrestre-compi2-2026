package com.compi.backend.languages.y.ast.expressions;

import com.compi.backend.languages.base.ast.Literal;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteralY extends Literal {

    @Builder
    public LiteralY(Object value, int line, int column) {
        super(value, line, column);
    }

    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
