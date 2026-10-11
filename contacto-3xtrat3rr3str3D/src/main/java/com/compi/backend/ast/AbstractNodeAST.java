package com.compi.backend.ast;

import java.util.List;

/** Base de todos los nodos: posicion y recorrido generico de hijos. */
public abstract class AbstractNodeAST implements ASTNode {

    private final int line;
    private final int column;

    protected AbstractNodeAST(int line, int column) {
        this.line = line;
        this.column = column;
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public int column() {
        return column;
    }

    /** Nodos hijos directos, en orden de aparicion. */
    public abstract List<ASTNode> children();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + line + ":" + column;
    }
}
