package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewObjectExprZet extends NodeASTZet {
    private String className;
    private List<NodeASTZet> arguments;

    @Builder
    public NewObjectExprZet(String className, List<NodeASTZet> arguments, int line, int column) {
        super(line, column);
        this.className = className;
        this.arguments = arguments;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
