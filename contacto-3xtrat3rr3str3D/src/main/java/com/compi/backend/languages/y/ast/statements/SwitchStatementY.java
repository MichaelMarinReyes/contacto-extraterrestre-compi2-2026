package com.compi.backend.languages.y.ast.statements;
import com.compi.backend.languages.y.ast.NodeASTY;

import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SwitchStatementY extends NodeASTY {
    private NodeASTY expression;
    private List<SwitchCaseY> cases;
    private List<NodeASTY> defaultBody;

    @Builder
    public SwitchStatementY(NodeASTY expression, List<SwitchCaseY> cases, List<NodeASTY> defaultBody, int line, int column) {
        super(line, column);
        this.expression = expression;
        this.cases = cases;
        this.defaultBody = defaultBody;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
