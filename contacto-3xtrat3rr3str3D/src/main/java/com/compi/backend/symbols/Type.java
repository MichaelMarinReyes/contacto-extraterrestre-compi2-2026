package com.compi.backend.symbols;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class Type {
    public static final Type INT = new Type(DataType.INT);
    public static final Type DOUBLE = new Type(DataType.DOUBLE);
    public static final Type CHAR = new Type(DataType.CHAR);
    public static final Type BOOLEAN = new Type(DataType.BOOLEAN);
    public static final Type STRING = new Type(DataType.STRING);
    public static final Type VOID = new Type(DataType.VOID);
    public static final Type NULL = new Type(DataType.NULL);
    public static final Type UNKNOWN = new Type(DataType.UNKNOWN);

    public static final int UNKNOWN_SIZE = -1;

    private final DataType dataType;
    private final String customTypeName;
    private final Type elementType;
    private final int dimensions;

    @Getter(AccessLevel.NONE)
    private final int[] sizes;

    public Type(DataType dataType) {
        this(dataType, null, null, 0, null);
    }

    public Type(DataType dataType, String customTypeName) {
        this(dataType, customTypeName, null, 0, null);
    }

    public Type(DataType dataType, String customTypeName, Type elementType, int dimensions) {
        this(dataType, customTypeName, elementType, dimensions, null);
    }

    public Type(DataType dataType, String customTypeName, Type elementType, int dimensions,
                int[] sizes) {
        this.dataType = dataType;
        this.customTypeName = customTypeName;
        this.elementType = elementType;
        this.dimensions = dimensions;
        this.sizes = sizes == null ? null : sizes.clone();
    }

    public static boolean isKnown(Type t) {
        return t != null && t != UNKNOWN;
    }

    public static Type array(Type elemType, int dimensions) {
        return new Type(DataType.ARRAY, null, elemType, dimensions, null);
    }

    public static Type arrayOf(Type elemType, int[] sizes) {
        return new Type(DataType.ARRAY, null, elemType, sizes.length, sizes);
    }

    public Type conTamanos(int[] nuevos) {
        if (dataType != DataType.ARRAY) {
            return this;
        }
        return new Type(DataType.ARRAY, null, elementType, nuevos.length, nuevos);
    }

    public static Type structType(String name) {
        return new Type(DataType.STRUCT, name, null, 0, null);
    }

    public static Type classType(String name) {
        return new Type(DataType.CLASS, name, null, 0, null);
    }

    public int[] getSizes() {
        if (dataType != DataType.ARRAY) {
            return null;
        }
        if (sizes != null) {
            return sizes.clone();
        }
        int[] desconocidos = new int[Math.max(0, dimensions)];
        Arrays.fill(desconocidos, UNKNOWN_SIZE);
        return desconocidos;
    }

    public int totalSize() {
        if (dataType != DataType.ARRAY) {
            return 0;
        }
        int[] s = getSizes();
        int total = 1;
        for (int n : s) {
            if (n <= 0) {
                return UNKNOWN_SIZE;
            }
            total *= n;
        }
        return total;
    }

    public boolean hasSizes() {
        return dataType == DataType.ARRAY && totalSize() > 0;
    }

    public Type desindexar(int niveles) {
        if (dataType != DataType.ARRAY) {
            return this;
        }
        int[] s = getSizes();
        int quitadas = Math.min(Math.max(0, niveles), s.length);
        if (quitadas >= s.length) {
            return elementType;
        }
        return arrayOf(elementType, Arrays.copyOfRange(s, quitadas, s.length));
    }


    public boolean sameShape(Type other) {
        if (other == null) {
            return false;
        }
        return Objects.equals(elementType, other.elementType)
                && dimensions == other.dimensions;
    }

    public boolean isArray() {
        return dataType == DataType.ARRAY;
    }

    public boolean isStruct() {
        return dataType == DataType.STRUCT;
    }

    public boolean isClass() {
        return dataType == DataType.CLASS;
    }

    public boolean isNumeric() {
        return dataType.isNumeric();
    }

    public boolean isAssignableTo(Type target) {
        if (this.equals(target)) return true;
        if (target == null) return false;
        if (this.dataType == DataType.NULL && (target.isClass() || target.isArray() || target.isStruct())) {
            return true;
        }

        if (this.dataType == DataType.INT && target.dataType == DataType.DOUBLE) {
            return true;
        }

        if (this.dataType == DataType.ARRAY && target.dataType == DataType.ARRAY) {
            return sameShape(target);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return dimensions == type.dimensions &&
                dataType == type.dataType &&
                Objects.equals(customTypeName, type.customTypeName) &&
                Objects.equals(elementType, type.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, customTypeName, elementType, dimensions);
    }

    @Override
    public String toString() {
        if (dataType == DataType.ARRAY) {
            return elementType.toString() + "[]".repeat(Math.max(0, dimensions));
        }
        if (dataType == DataType.STRUCT || dataType == DataType.CLASS) {
            return customTypeName != null ? customTypeName : dataType.name();
        }
        return dataType.name().toLowerCase();
    }

    public String label() {
        if (dataType == DataType.ARRAY) {
            String base = elementType == null ? "desconocido" : elementType.label();
            return base + "[]".repeat(Math.max(1, dimensions));
        }
        if (dataType == DataType.STRUCT || dataType == DataType.CLASS) {
            return customTypeName != null ? customTypeName : labelDe(dataType);
        }
        return labelDe(dataType);
    }

    private static String labelDe(DataType type) {
        return switch (type) {

            case INT -> "int";
            case DOUBLE -> "double";
            case CHAR -> "char";
            case BOOLEAN -> "booleano";
            case STRING -> "cadena";
            case VOID -> "vacío";
            case NULL -> "nulo";
            case STRUCT -> "estructura";
            case CLASS -> "clase";
            case ARRAY -> "arreglo";
            case UNKNOWN -> "desconocido";
        };
    }
}
