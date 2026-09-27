package com.compi.backend.symbols;

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

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
