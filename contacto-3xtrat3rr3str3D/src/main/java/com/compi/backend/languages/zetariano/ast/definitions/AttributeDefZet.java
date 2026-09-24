package com.compi.backend.languages.zetariano.ast.definitions;
import com.compi.backend.languages.zetariano.ast.NodeASTZet;

import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeDefZet extends NodeASTZet {
    private String dataType;
    private String name;

    @Builder
    public AttributeDefZet(String dataType, String name, int line, int column) {
        super(line, column);
        this.dataType = dataType;
        this.name = name;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
