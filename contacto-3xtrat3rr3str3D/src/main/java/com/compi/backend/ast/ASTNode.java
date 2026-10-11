package com.compi.backend.ast;

/** Un nodo del AST propio. */
public interface ASTNode {

    /** Doble despacho: invoca el metodo del visitante correspondiente a este tipo. */
    void accept(ASTVisitor<?> visitor);

    int line();

    int column();
}
