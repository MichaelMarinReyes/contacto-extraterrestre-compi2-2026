package com.compi.backend.ast;

import java.util.List;

/**
 * Base de todos los nodos: posicion y recorrido generico de hijos.
 */
public abstract class AbstractNodeAST implements ASTNode {

    private final int line;
    private final int column;
    private String sourceFile;

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

    public String sourceFile() {
        return sourceFile;
    }

    /**
     * Asocia el archivo al árbol completo, sin conservar contextos de ANTLR.
     */
    public void attachSource(String fileName) {
        sourceFile = fileName;
        for (ASTNode child : children()) {
            if (child instanceof AbstractNodeAST node) node.attachSource(fileName);
        }
    }

    /**
     * Nodos hijos directos, en orden de aparicion.
     */
    public abstract List<ASTNode> children();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + line + ":" + column;
    }
}
