package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.base.ast.ArithmeticOperation;
import com.compi.backend.languages.base.ast.NodeAST;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperationPig extends ArithmeticOperation {

    @Builder
    public ArithmeticOperationPig(NodeAST left, String operator, NodeAST right, int line, int column) {
        super(left, operator, right, line, column);
    }

    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
