package com.compi.backend.languages.zetariano.ast.statements;
import com.compi.backend.languages.zetariano.ast.NodeASTZet;

import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WhileStatementZet extends NodeASTZet {
    private NodeASTZet condition;
    private List<NodeASTZet> body;

    @Builder
    public WhileStatementZet(NodeASTZet condition, List<NodeASTZet> body, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
