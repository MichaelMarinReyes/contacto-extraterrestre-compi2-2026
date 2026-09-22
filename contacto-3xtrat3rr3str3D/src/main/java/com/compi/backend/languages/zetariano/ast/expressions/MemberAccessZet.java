package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberAccessZet extends NodeASTZet {
    private NodeASTZet object;
    private String memberName;

    @Builder
    public MemberAccessZet(NodeASTZet object, String memberName, int line, int column) {
        super(line, column);
        this.object = object;
        this.memberName = memberName;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
