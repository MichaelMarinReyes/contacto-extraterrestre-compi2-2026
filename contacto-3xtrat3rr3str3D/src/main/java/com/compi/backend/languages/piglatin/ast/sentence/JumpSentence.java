package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JumpSentence extends NodeASTPig {
    private String jumpType;
    private NodeASTPig expression;

    @Builder
    public JumpSentence(String jumpType, NodeASTPig expression, int line, int column) {
        super(line, column);
        this.jumpType = jumpType;
        this.expression = expression;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
