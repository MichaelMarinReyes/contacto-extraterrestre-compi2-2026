package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberAccessPig extends NodeASTPig {
    private NodeASTPig object;
    private String property;

    @Builder
    public MemberAccessPig(NodeASTPig object, String property, int line, int column) {
        super(line, column);
        this.object = object;
        this.property = property;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
