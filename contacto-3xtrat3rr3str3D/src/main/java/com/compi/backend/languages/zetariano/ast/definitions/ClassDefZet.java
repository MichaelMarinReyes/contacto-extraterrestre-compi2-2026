package com.compi.backend.languages.zetariano.ast.definitions;
import com.compi.backend.languages.zetariano.ast.NodeASTZet;

import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClassDefZet extends NodeASTZet {
    private String name;
    private List<AttributeDefZet> attributes;
    private List<NodeASTZet> methods;

    @Builder
    public ClassDefZet(String name, List<AttributeDefZet> attributes, List<NodeASTZet> methods, int line, int column) {
        super(line, column);
        this.name = name;
        this.attributes = attributes;
        this.methods = methods;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
