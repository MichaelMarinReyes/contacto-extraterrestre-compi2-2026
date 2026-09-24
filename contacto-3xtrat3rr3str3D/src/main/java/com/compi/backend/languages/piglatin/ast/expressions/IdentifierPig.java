package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifierPig extends NodeASTPig {
    private String name;

    @Builder
    public IdentifierPig(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
