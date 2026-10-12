package com.compi.backend.ast;

/**
 * Un nodo del AST propio.
 */
public interface ASTNode {

    /**
     * Doble despacho: invoca el metodo del visitante correspondiente a este tipo.
     */
    public void accept(ASTVisitor<?> visitor);

    public int line();

    public int column();
}
