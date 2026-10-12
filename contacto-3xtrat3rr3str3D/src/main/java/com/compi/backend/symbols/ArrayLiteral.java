package com.compi.backend.symbols;

/** Completa la distribución de un arreglo a partir del tipo de su inicializador. */
public final class ArrayLiteral {

    private ArrayLiteral() {
    }

    public static Type completeSizes(Type declaredType, Type obtenido) {
        if (declaredType == null || !declaredType.isArray()) {
            return declaredType;
        }
        if (declaredType.hasSizes() || obtenido == null || !obtenido.isArray()) {
            return declaredType;
        }
        return declaredType.conTamanos(obtenido.getSizes());
    }
}
