package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteralZet extends NodeASTZet {
    private Object value;

    @Builder
    public LiteralZet(Object value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
