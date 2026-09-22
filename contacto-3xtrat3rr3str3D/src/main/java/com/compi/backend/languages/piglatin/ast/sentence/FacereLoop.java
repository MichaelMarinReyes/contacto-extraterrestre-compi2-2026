package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FacereLoop extends NodeASTPig {
    private List<NodeASTPig> body;
    private NodeASTPig condition;

    @Builder
    public FacereLoop(List<NodeASTPig> body, NodeASTPig condition, int line, int column) {
        super(line, column);
        this.body = body;
        this.condition = condition;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
