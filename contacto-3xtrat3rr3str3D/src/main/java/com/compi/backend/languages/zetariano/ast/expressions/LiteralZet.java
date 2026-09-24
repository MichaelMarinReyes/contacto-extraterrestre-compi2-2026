package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.base.ast.Literal;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteralZet extends Literal {

    @Builder
    public LiteralZet(Object value, int line, int column) {
        super(value, line, column);
    }

    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
