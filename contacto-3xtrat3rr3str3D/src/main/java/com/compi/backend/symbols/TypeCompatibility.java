package com.compi.backend.symbols;

public class TypeCompatibility {

    public static Type checkArithmetic(Type t1, Type t2, String op) {
        if (t1 == null || t2 == null) return Type.UNKNOWN;
        
        // Suma de Strings (concatenación)
        if ("+".equals(op)) {
            if (t1.getDataType() == DataType.STRING || t2.getDataType() == DataType.STRING) {
                return Type.STRING;
            }
        }

        // Operaciones numéricas
        if (t1.isNumeric() && t2.isNumeric()) {
            if (t1.getDataType() == DataType.DOUBLE || t2.getDataType() == DataType.DOUBLE) {
                return Type.DOUBLE;
            }
            if ("/".equals(op)) {
                // En Zetariano '/' con ints es división entera según PDF pág. 586
                return Type.INT;
            }
            return Type.INT;
        }

        return Type.UNKNOWN;
    }

    public static Type checkRelational(Type t1, Type t2, String op) {
        if (t1 == null || t2 == null) return Type.UNKNOWN;
        
        // Números
        if (t1.isNumeric() && t2.isNumeric()) {
            return Type.BOOLEAN;
        }
        // Comparación de caracteres
        if (t1.getDataType() == DataType.CHAR && t2.getDataType() == DataType.CHAR) {
            return Type.BOOLEAN;
        }
        return Type.UNKNOWN;
    }

    public static Type checkEquality(Type t1, Type t2) {
        if (t1 == null || t2 == null) return Type.UNKNOWN;

        if (t1.equals(t2)) return Type.BOOLEAN;
        if (t1.isNumeric() && t2.isNumeric()) return Type.BOOLEAN;

        // Comparación con null para objetos o arrays
        if ((t1.getDataType() == DataType.NULL && (t2.isClass() || t2.isArray() || t2.isStruct())) ||
            (t2.getDataType() == DataType.NULL && (t1.isClass() || t1.isArray() || t1.isStruct()))) {
            return Type.BOOLEAN;
        }

        return Type.UNKNOWN;
    }

    public static Type checkLogical(Type t1, Type t2) {
        if (t1 == null || t2 == null) return Type.UNKNOWN;
        if (t1.getDataType() == DataType.BOOLEAN && t2.getDataType() == DataType.BOOLEAN) {
            return Type.BOOLEAN;
        }
        return Type.UNKNOWN;
    }
}
