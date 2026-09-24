package com.compi.backend.languages.base.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArithmeticOperation extends NodeAST {
    private NodeAST left;
    private String operator;
    private NodeAST right;

    public ArithmeticOperation(NodeAST left, String operator, NodeAST right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
