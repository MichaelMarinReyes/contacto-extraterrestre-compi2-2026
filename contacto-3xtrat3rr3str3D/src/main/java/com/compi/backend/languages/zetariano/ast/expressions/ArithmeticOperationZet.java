package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.zetariano.ast.NodeASTZet;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperationZet extends NodeASTZet {
    private NodeASTZet left;
    private String operator;
    private NodeASTZet right;

    @Builder
    public ArithmeticOperationZet(NodeASTZet left, String operator, NodeASTZet right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
