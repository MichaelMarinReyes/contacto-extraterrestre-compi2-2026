package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForStatementY extends NodeASTY {
    private NodeASTY initialization;
    private NodeASTY condition;
    private NodeASTY update;
    private List<NodeASTY> body;

    @Builder
    public ForStatementY(NodeASTY initialization, NodeASTY condition, NodeASTY update, List<NodeASTY> body, int line, int column) {
        super(line, column);
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
