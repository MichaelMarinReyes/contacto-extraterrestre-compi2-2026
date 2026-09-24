package com.compi.backend.languages.zetariano.ast.expressions;

import com.compi.backend.languages.base.ast.ArithmeticOperation;
import com.compi.backend.languages.base.ast.NodeAST;
import com.compi.backend.languages.zetariano.ast.ZetarianVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperationZet extends ArithmeticOperation {

    @Builder
    public ArithmeticOperationZet(NodeAST left, String operator, NodeAST right, int line, int column) {
        super(left, operator, right, line, column);
    }

    public Object accept(ZetarianVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
