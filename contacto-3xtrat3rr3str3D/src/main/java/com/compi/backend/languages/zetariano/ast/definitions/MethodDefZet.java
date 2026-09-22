package com.compi.backend.languages.zetariano.ast.definitions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MethodDefZet extends NodeASTZet {
    private String returnType;
    private String name;
    private List<NodeASTZet> parameters;
    private List<NodeASTZet> body;

    @Builder
    public MethodDefZet(String returnType, String name, List<NodeASTZet> parameters, List<NodeASTZet> body, int line, int column) {
        super(line, column);
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
