package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SwitchCaseY extends NodeASTY {
    private NodeASTY value;
    private List<NodeASTY> body;

    @Builder
    public SwitchCaseY(NodeASTY value, List<NodeASTY> body, int line, int column) {
        super(line, column);
        this.value = value;
        this.body = body;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}

