package com.compi.backend.languages.zetariano.ast.statements;
import com.compi.backend.languages.zetariano.ast.NodeASTZet;

import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnStatementZet extends NodeASTZet {
    private NodeASTZet expression;

    @Builder
    public ReturnStatementZet(NodeASTZet expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
