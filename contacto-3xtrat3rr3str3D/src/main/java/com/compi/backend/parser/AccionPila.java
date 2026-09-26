package com.compi.backend.parser;

/**
 * Accion que ejecuta el parser sobre su pila en un paso del trazo.
 *
 * <p>Un desplazamiento (shift) apila un simbolo en la pila: al entrar a una
 * regla o al reconocer un token. Una reduccion (reduce) sustituye los
 * simbolos que consumio la regla por el no terminal que representa.</p>
 */
public enum AccionPila {

    SHIFT("shift", "Desplazamiento"),
    REDUCE("reduce", "Reducción");

    private final String tag;
    private final String label;

    AccionPila(String tag, String label) {
        this.tag = tag;
        this.label = label;
    }

    /** Etiqueta corta que se dibuja en la grafica y se imprime en la consola. */
    public String tag() {
        return tag;
    }

    /** Descripcion legible del tipo de accion. */
    public String label() {
        return label;
    }
}
