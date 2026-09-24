package com.compi.backend.languages.y.ast.expressions;

import com.compi.backend.languages.base.ast.ArithmeticOperation;
import com.compi.backend.languages.base.ast.NodeAST;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperationY extends ArithmeticOperation {

    @Builder
    public ArithmeticOperationY(NodeAST left, String operator, NodeAST right, int line, int column) {
        super(left, operator, right, line, column);
    }

    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
