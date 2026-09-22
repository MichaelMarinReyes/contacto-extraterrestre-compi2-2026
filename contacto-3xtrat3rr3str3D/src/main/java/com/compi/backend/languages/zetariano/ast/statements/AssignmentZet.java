package com.compi.backend.languages.zetariano.ast.statements;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentZet extends NodeASTZet {
    private NodeASTZet target;
    private NodeASTZet value;

    @Builder
    public AssignmentZet(NodeASTZet target, NodeASTZet value, int line, int column) {
        super(line, column);
        this.target = target;
        this.value = value;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
