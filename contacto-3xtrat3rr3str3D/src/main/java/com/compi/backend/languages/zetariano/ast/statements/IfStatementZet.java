package com.compi.backend.languages.zetariano.ast.statements;
import com.compi.backend.languages.zetariano.ast.NodeASTZet;

import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IfStatementZet extends NodeASTZet {
    private NodeASTZet condition;
    private List<NodeASTZet> thenBody;
    private List<NodeASTZet> elseBody;

    @Builder
    public IfStatementZet(NodeASTZet condition, List<NodeASTZet> thenBody, List<NodeASTZet> elseBody, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
