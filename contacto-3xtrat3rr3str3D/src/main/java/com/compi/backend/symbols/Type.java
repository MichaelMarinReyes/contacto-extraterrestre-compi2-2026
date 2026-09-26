package com.compi.backend.symbols;

import java.util.Arrays;
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

    /** Tamaño de una dimension que todavia no se sabe, como el de un `[]` vacio. */
    public static final int TAMANO_DESCONOCIDO = -1;

    private final DataType dataType;
    private final String customTypeName;
    private final Type elementType;
    private final int dimensions;

    /**
     * Longitud de cada dimension, de la mas exterior a la mas interior.
     *
     * <p>Es lo que distingue una matriz de verdad: {@code int[3][2]} son seis
     * celdas, no "un arreglo de arreglos". Solo lo rellenan los lenguajes que
     * declaran el tamano en la declaracion; los que usan {@code []} sin numero
     * (Zetariano, al estilo de Java) lo deducen del inicializador.</p>
     */
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

    public static Type array(Type elemType, int dimensions) {
        return new Type(DataType.ARRAY, null, elemType, dimensions, null);
    }

    /**
     * Arreglo con el tamano de cada dimension conocido.
     *
     * @param sizes una longitud por dimension, de la exterior a la interior
     */
    public static Type arrayOf(Type elemType, int[] sizes) {
        return new Type(DataType.ARRAY, null, elemType, sizes.length, sizes);
    }

    /**
     * Copia de este tipo con otros tamanos, para cuando el inicializador deduce
     * la forma que la declaracion dejaba abierta ({@code int[][] m = {...}}).
     */
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

    /**
     * Longitudes de las dimensiones, o null si el tipo no es un arreglo.
     *
     * <p>Si el arreglo se declaro sin tamanos, devuelve un vector lleno de
     * {@link #TAMANO_DESCONOCIDO} con el numero de dimensiones correcto.</p>
     */
    public int[] getSizes() {
        if (dataType != DataType.ARRAY) {
            return null;
        }
        if (sizes != null) {
            return sizes.clone();
        }
        int[] desconocidos = new int[Math.max(0, dimensions)];
        Arrays.fill(desconocidos, TAMANO_DESCONOCIDO);
        return desconocidos;
    }

    /**
     * Cuantas celdas ocupa el arreglo al aplanarlo.
     *
     * <p>El enunciado pide que los arreglos se guarden aplanados, asi que una
     * matriz {@code [3][2]} son seis celdas contiguas. Si alguna dimension no se
     * conoce todavia devuelve {@link #TAMANO_DESCONOCIDO}: no se puede reservar
     * sitio sin saber cuanto.</p>
     */
    public int totalSize() {
        if (dataType != DataType.ARRAY) {
            return 0;
        }
        int[] s = getSizes();
        int total = 1;
        for (int n : s) {
            if (n <= 0) {
                return TAMANO_DESCONOCIDO;
            }
            total *= n;
        }
        return total;
    }

    /** true si el arreglo tiene todas sus dimensiones declaradas. */
    public boolean tieneTamanos() {
        return dataType == DataType.ARRAY && totalSize() > 0;
    }

    /**
     * Tipo que queda tras quitar las primeras {@code niveles} dimensiones.
     *
     * <p>Es lo que hace un acceso: en {@code int[2][3]}, {@code m[0]} sigue siendo
     * un arreglo (una fila) y {@code m[0][1]} ya es un entero. Pedir mas
     * dimensiones de las que hay devuelve el tipo del elemento, que ya no es un
     * arreglo.</p>
     */
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

    /** Tipo del elemento al que se llega al quitar todas las dimensiones. */
    public Type elemento() {
        return dataType == DataType.ARRAY && elementType != null ? elementType : this;
    }

    /** true si las dos formas son la misma, sin mirar los tamanos. */
    public boolean mismaForma(Type otro) {
        if (otro == null) {
            return false;
        }
        return Objects.equals(elementType, otro.elementType)
                && dimensions == otro.dimensions;
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
        // Conversión implícita de INT a DOUBLE
        if (this.dataType == DataType.INT && target.dataType == DataType.DOUBLE) {
            return true;
        }
        // Un arreglo solo es asignable a otro si tiene la misma forma. El tamano
        // no importa: `int[] a` y `int[] b` se pueden copiar igual, y en Java
        // tampoco se comprueba.
        if (this.dataType == DataType.ARRAY && target.dataType == DataType.ARRAY) {
            return mismaForma(target);
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

    /**
     * El tipo escrito en espanol, que es como se enseña en la tabla de simbolos.
     *
     * <p>Los nombres propios ({@code Contador}, una estructura o una clase) se
     * dejan tal cual: son identificadores del fuente, no palabras del lenguaje.
     * Los basicos si se traducen, porque {@code boolean} o {@code void} no le
     * dicen nada a quien lee la tabla en espanol.</p>
     */
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
            // int, double y char se escriben igual en espanol.
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
