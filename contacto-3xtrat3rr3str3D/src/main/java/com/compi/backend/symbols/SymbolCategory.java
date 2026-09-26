package com.compi.backend.symbols;

/**
 * Que clase de cosa es un simbolo.
 *
 * <p>Las etiquetas que se enseñan estan en espanol porque son las que salen en la
 * tabla de simbolos de la interfaz, que es donde se leen. El nombre del enum
 * ({@code name()}) se conserva en ingles para el codigo.</p>
 */
public enum SymbolCategory {

    VARIABLE("Variable"),
    PARAMETER("Parámetro"),
    FUNCTION("Función"),
    METHOD("Método"),
    CONSTRUCTOR("Constructor"),
    STRUCT("Estructura"),
    CLASS("Clase"),
    FIELD("Campo");

    private final String label;

    SymbolCategory(String label) {
        this.label = label;
    }

    /** Etiqueta en espanol para la tabla de simbolos. */
    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
