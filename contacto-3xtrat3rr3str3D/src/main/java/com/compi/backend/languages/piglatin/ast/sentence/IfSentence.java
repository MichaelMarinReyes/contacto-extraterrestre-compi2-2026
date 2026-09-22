package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IfSentence extends NodeASTPig {
    private NodeASTPig condition;
    private List<NodeASTPig> thenBody;
    private List<NodeASTPig> elseBody;

    @Builder
    public IfSentence(NodeASTPig condition, List<NodeASTPig> thenBody, List<NodeASTPig> elseBody, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
