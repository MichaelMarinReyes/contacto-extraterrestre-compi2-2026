package com.compi.backend.languages.y.ast.statements;
import com.compi.backend.languages.y.ast.NodeASTY;

import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ElseIfY extends NodeASTY {
    private NodeASTY condition;
    private List<NodeASTY> body;

    @Builder
    public ElseIfY(NodeASTY condition, List<NodeASTY> body, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}