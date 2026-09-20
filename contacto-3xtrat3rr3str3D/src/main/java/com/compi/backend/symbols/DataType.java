package com.compi.backend.symbols;

public enum DataType {
    INT,
    DOUBLE,
    CHAR,
    BOOLEAN,
    STRING,
    STRUCT,
    CLASS,
    ARRAY,
    VOID,
    NULL,
    UNKNOWN;

    public boolean isNumeric() {
        return this == INT || this == DOUBLE;
    }

    public boolean isPrimitive() {
        return this == INT || this == DOUBLE || this == CHAR || this == BOOLEAN || this == STRING;
    }
}
