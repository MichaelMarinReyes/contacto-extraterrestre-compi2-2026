package com.compi.backend.languages.base.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
