package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.base.ast.Literal;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteralPig extends Literal {
    private String type;

    @Builder
    public LiteralPig(Object value, String type, int line, int column) {
        super(value, line, column);
        this.type = type;
    }

    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
