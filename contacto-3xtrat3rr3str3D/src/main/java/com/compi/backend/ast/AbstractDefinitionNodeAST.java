package com.compi.backend.ast;

/**
 * Categoria abstracta de nodos de definition.
 */
public abstract class AbstractDefinitionNodeAST extends AbstractNodeAST {

    protected AbstractDefinitionNodeAST(int line, int column) {
        super(line, column);
    }
}
