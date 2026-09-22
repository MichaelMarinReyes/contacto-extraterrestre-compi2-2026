package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LiteralPig extends NodeASTPig {
    private Object value;
    private String type;

    public LiteralPig(Object value, String type, int line, int column) {
        super(line, column);
        this.value = value;
        this.type = type;
    }


    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
