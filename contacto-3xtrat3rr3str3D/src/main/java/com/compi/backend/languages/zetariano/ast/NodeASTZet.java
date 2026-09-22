package com.compi.backend.languages.zetariano.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NodeASTZet {
    protected int line;
    protected int column;

    public NodeASTZet(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Método abstracto para implementar el patrón Visitor en el lenguaje Zetariano.
     */
    public abstract Object accept(ZetarianVisitorCustom visitor, Object arg);
}