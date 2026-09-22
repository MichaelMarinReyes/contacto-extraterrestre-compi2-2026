package com.compi.backend.languages.y.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NodeASTY {
    protected int line;
    protected int column;

    public NodeASTY(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Método de aceptación para el patrón Visitor exclusivo del lenguaje Y.
     *
     * @param visitor El visitante correspondiente (YVisitorCustom)
     * @param arg     Argumento opcional para el paso de parámetros en el visitor
     * @return        Resultado de la visita
     */
    public abstract Object accept(YVisitorCustom visitor, Object arg);
}
