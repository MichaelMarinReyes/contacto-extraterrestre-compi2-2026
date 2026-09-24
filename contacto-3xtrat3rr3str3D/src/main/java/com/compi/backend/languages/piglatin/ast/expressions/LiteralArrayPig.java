package com.compi.backend.languages.piglatin.ast.expressions;
import com.compi.backend.languages.piglatin.ast.NodeASTPig;

import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LiteralArrayPig extends NodeASTPig {
    private List<NodeASTPig> elements;

    @Builder
    public LiteralArrayPig(List<NodeASTPig> elements, int line, int column) {
        super(line, column);
        this.elements = elements;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
