package com.compi.backend.symbols;

import java.util.Objects;

public class Type {
    public static final Type INT = new Type(DataType.INT);
    public static final Type DOUBLE = new Type(DataType.DOUBLE);
    public static final Type CHAR = new Type(DataType.CHAR);
    public static final Type BOOLEAN = new Type(DataType.BOOLEAN);
    public static final Type STRING = new Type(DataType.STRING);
    public static final Type VOID = new Type(DataType.VOID);
    public static final Type NULL = new Type(DataType.NULL);
    public static final Type UNKNOWN = new Type(DataType.UNKNOWN);

    private final DataType dataType;
    private final String customTypeName;
    private final Type elementType;
    private final int dimensions;

    public Type(DataType dataType) {
        this(dataType, null, null, 0);
    }

    public Type(DataType dataType, String customTypeName) {
        this(dataType, customTypeName, null, 0);
    }

    public Type(DataType dataType, String customTypeName, Type elementType, int dimensions) {
        this.dataType = dataType;
        this.customTypeName = customTypeName;
        this.elementType = elementType;
        this.dimensions = dimensions;
    }

    public static Type array(Type elemType, int dimensions) {
        return new Type(DataType.ARRAY, null, elemType, dimensions);
    }

    public static Type structType(String name) {
        return new Type(DataType.STRUCT, name, null, 0);
    }

    public static Type classType(String name) {
        return new Type(DataType.CLASS, name, null, 0);
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getCustomTypeName() {
        return customTypeName;
    }

    public Type getElementType() {
        return elementType;
    }

    public int getDimensions() {
        return dimensions;
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
        if (this.dataType == DataType.NULL && (target.isClass() || target.isArray() || target.isStruct())) {
            return true;
        }
        // Conversión implícita de INT a DOUBLE
        if (this.dataType == DataType.INT && target.dataType == DataType.DOUBLE) {
            return true;
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
}
