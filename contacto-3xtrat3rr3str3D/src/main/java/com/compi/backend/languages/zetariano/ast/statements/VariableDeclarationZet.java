package com.compi.backend.languages.zetariano.ast.statements;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableDeclarationZet extends NodeASTZet {
    private String dataType;
    private String name;
    private NodeASTZet initializer;

    @Builder
    public VariableDeclarationZet(String dataType, String name, NodeASTZet initializer, int line, int column) {
        super(line, column);
        this.dataType = dataType;
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
