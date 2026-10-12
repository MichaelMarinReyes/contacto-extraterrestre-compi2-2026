package com.compi.backend.ast;

/**
 * Categoria abstracta de nodos de expression.
 */
public abstract class AbstractExpressionNodeAST extends AbstractNodeAST {

    protected AbstractExpressionNodeAST(int line, int column) {
        super(line, column);
    }
}
