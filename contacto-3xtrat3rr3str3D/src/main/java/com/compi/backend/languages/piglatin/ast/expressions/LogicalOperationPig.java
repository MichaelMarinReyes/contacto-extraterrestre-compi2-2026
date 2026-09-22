package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogicalOperationPig extends NodeASTPig {
    private NodeASTPig left;
    private String operator;
    private NodeASTPig right;

    @Builder
    public LogicalOperationPig(NodeASTPig left, String operator, NodeASTPig right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
