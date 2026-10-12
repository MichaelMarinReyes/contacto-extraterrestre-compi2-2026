package com.compi.backend.ast;

/**
 * Categoria abstracta de nodos de statement.
 */
public abstract class AbstractStatementNodeAST extends AbstractNodeAST {

    protected AbstractStatementNodeAST(int line, int column) {
        super(line, column);
    }
}
