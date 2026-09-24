package com.compi.backend.languages.y.ast.statements;
import com.compi.backend.languages.y.ast.NodeASTY;

import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoWhileStatementY extends NodeASTY {
    private List<NodeASTY> body;
    private NodeASTY condition;

    @Builder
    public DoWhileStatementY(List<NodeASTY> body, NodeASTY condition, int line, int column) {
        super(line, column);
        this.body = body;
        this.condition = condition;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
