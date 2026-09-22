package com.compi.backend.languages.piglatin.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NodeASTPig {
    protected int line;
    protected int column;

    public NodeASTPig() {
        this.line = 0;
        this.column = 0;
    }

    public NodeASTPig(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Método abstracto para implementar el Patrón Visitor.
     */
    public abstract Object accept(PigLatinVisitorCustom visitor, Object arg);
}