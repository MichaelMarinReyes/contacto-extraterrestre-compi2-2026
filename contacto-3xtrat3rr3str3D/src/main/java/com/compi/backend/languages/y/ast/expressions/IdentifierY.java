package com.compi.backend.languages.y.ast.expressions;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifierY extends NodeASTY {
    private String name;

    @Builder
    public IdentifierY(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
