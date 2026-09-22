package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PerLoop extends NodeASTPig {
    private NodeASTPig initialization;
    private NodeASTPig condition;
    private NodeASTPig update;
    private List<NodeASTPig> body;

    @Builder
    public PerLoop(NodeASTPig initialization, NodeASTPig condition, NodeASTPig update, List<NodeASTPig> body, int line, int column) {
        super(line, column);
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
