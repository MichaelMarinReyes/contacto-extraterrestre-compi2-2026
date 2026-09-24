package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessArrayPig extends NodeASTPig {
    private NodeASTPig array;
    private NodeASTPig index;

    @Builder
    public AccessArrayPig(NodeASTPig array, NodeASTPig index, int line, int column) {
        super(line, column);
        this.array = array;
        this.index = index;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}