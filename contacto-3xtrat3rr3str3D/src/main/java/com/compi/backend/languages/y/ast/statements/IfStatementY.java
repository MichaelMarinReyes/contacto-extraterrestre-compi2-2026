package com.compi.backend.languages.y.ast.statements;
import com.compi.backend.languages.y.ast.NodeASTY;

import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IfStatementY extends NodeASTY {
    private NodeASTY condition;
    private List<NodeASTY> thenBody;
    private List<ElseIfY> elseIfBranches;
    private List<NodeASTY> elseBody;

    @Builder
    public IfStatementY(NodeASTY condition, List<NodeASTY> thenBody, List<ElseIfY> elseIfBranches, List<NodeASTY> elseBody, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseIfBranches = elseIfBranches;
        this.elseBody = elseBody;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
