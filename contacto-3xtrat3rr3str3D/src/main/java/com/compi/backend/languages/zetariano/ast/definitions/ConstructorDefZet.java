package com.compi.backend.languages.zetariano.ast.definitions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConstructorDefZet extends NodeASTZet {
    private List<NodeASTZet> parameters;
    private List<NodeASTZet> body;

    @Builder
    public ConstructorDefZet(List<NodeASTZet> parameters, List<NodeASTZet> body, int line, int column) {
        super(line, column);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
