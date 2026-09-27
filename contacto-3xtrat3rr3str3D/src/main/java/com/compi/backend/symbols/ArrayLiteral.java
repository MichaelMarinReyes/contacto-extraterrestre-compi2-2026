package com.compi.backend.symbols;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ArrayLiteral {

    private ArrayLiteral() {
    }

    public static CompilationError validate(String name, Type declaredType, Type obtenido,
                                           ParserRuleContext ctx) {
        if (declaredType == null || obtenido == null || !declaredType.isArray() || !obtenido.isArray()) {
            return null;
        }

        int declaradas = declaredType.getDimensions();
        int encontradas = obtenido.getDimensions();
        if (declaradas != encontradas) {
            return Diagnostic.initializerShape(name, declaradas, encontradas, ctx);
        }

        int[] esperados = declaredType.getSizes();
        int[] hallados = obtenido.getSizes();
        for (int d = 0; d < declaradas; d++) {

            if (esperados[d] > 0 && esperados[d] != hallados[d]) {
                return Diagnostic.filasDesiguales(name, d, esperados[d], hallados[d], ctx);
            }
        }

        Type elementDeclared = declaredType.getElementType();
        Type elementObtained = obtenido.getElementType();
        if (elementDeclared != null && elementObtained != null
                && !elementObtained.isAssignableTo(elementDeclared)) {
            return Diagnostic.incompatibleElement(name, elementDeclared, elementObtained, ctx);
        }
        return null;
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
