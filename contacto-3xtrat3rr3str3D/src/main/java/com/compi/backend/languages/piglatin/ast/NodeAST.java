package com.compi.backend.languages.piglatin.ast;

public abstract class NodeAST {
    protected int line;
    protected int column;

    public NodeAST() {
        this.line = 0;
        this.column = 0;
    }

    public NodeAST(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Método abstracto para implementar el Patrón Visitor.
     */
    public abstract Object accept(PigLatinVisitorCustom visitor, Object arg);
}