package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.symbols.*;
import java.util.List;

/**
 * Tipos de los inicializadores de arreglos en PigLatin («series»): infiere el
 * tipo de un literal anidado y valida que todas las filas tengan la misma forma.
 */
final class PigLatinArrayAnalyzer {

    private final List<CompilationError> errors;

    PigLatinArrayAnalyzer(List<CompilationError> errors) {
        this.errors = errors;
    }

    Type arrayLiteralType(PigLatinBaseVisitor<Type> visitor, String name,
                          PigLatinParser.Elemento_arregloContext ctx) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        int filas = valores.size();
        Type rowType = Type.UNKNOWN;
        int[] rowShape = new int[0];
        for (int f = 0; f < filas; f++) {
            Type type = arrayValueType(visitor, name, valores.get(f));
            Type element = type.isArray() ? type.getElementType() : type;
            if (f > 0 && Type.isKnown(rowType) && Type.isKnown(element)) {
                if (rowType.isNumeric() && element.isNumeric()) {
                    if (Type.DOUBLE.equals(element)) {
                        rowType = Type.DOUBLE;
                    }
                } else if (!element.equals(rowType)) {
                    CallValidator.report("El arreglo «" + name + "» contiene elementos de tipos incompatibles",
                            valores.get(f), errors);
                }
            }
            int[] shape = type.isArray() ? type.getSizes() : new int[0];
            if (f == 0) {
                rowType = type.isArray() ? type.getElementType() : type;
                rowShape = shape;
            } else if (!java.util.Arrays.equals(shape, rowShape)) {
                errors.add(Diagnostic.filasDesiguales(name, f + 1,
                        widthOf(rowShape, valores.get(0)), widthOf(shape, valores.get(f)),
                        valores.get(f)));
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = filas;
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return Type.arrayOf(rowType, sizes);
    }

    Type arrayValueType(PigLatinBaseVisitor<Type> visitor, String name,
                        PigLatinParser.Elemento_arreglo_valorContext ctx) {
        if (ctx.LLAVE_IZQ() == null) {
            return visitor.visit(ctx.expresion());
        }
        if (ctx.elemento_arreglo() == null) {
            return Type.arrayOf(Type.UNKNOWN, new int[]{0});
        }
        return arrayLiteralType(visitor, name, ctx.elemento_arreglo());
    }

    static int widthOf(int[] shape, PigLatinParser.Elemento_arreglo_valorContext row) {
        if (shape.length > 0) {
            return shape[0];
        }
        if (row.LLAVE_IZQ() == null) {
            return 1;
        }
        return row.elemento_arreglo() == null ? 0
                : row.elemento_arreglo().elemento_arreglo_valor().size();
    }
}