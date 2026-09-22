package com.compi.backend.languages.y.ast.expressions;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperationY extends NodeASTY {
    private NodeASTY left;
    private String operator;
    private NodeASTY right;

    @Builder
    public ArithmeticOperationY(NodeASTY left, String operator, NodeASTY right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
