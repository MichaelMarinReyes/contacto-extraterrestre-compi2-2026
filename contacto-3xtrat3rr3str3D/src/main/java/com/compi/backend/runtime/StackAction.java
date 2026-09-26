package com.compi.backend.runtime;

/**
 * Accion que una instruccion de la maquina abstracta ejecuta sobre la pila de
 * procesos. Permite distinguir en la visualizacion los desplazamientos de las
 * reducciones.
 */
public enum StackAction {

    /** La instruccion apila un valor sin consumir nada de la pila. */
    SHIFT("shift", "Desplazamiento (shift)"),

    /** La instruccion desapila operandos, con o sin resultado que apilar. */
    REDUCE("reduce", "Reducción (reduce)"),

    /** La instruccion no altera la pila: etiquetas, saltos, llamadas. */
    NEUTRAL("neutro", "Sin cambio en la pila");

    private final String tag;
    private final String label;

    StackAction(String tag, String label) {
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
