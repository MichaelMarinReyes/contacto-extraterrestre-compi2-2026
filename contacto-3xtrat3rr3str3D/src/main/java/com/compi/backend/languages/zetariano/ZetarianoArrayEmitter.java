package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Emisión de C3D para literales de arreglo de Zetariano: cálculo de dimensiones
 * y relleno de celdas con los valores constantes declarados.
 */
final class ZetarianoArrayEmitter {

    private final C3DGenerator c3d;

    ZetarianoArrayEmitter(C3DGenerator c3d) {
        this.c3d = c3d;
    }

    String emitLiteral(ZetarianoBaseVisitor<String> self,
                         ZetarianoParser.LiteralArregloExprContext ctx) {
        int[] sizes = shapeOf(ctx.literalArreglo());
        int celdas = Type.arrayOf(Type.UNKNOWN, sizes).totalSize();
        if (celdas <= 0) {
            return "0";
        }
        String base = ArrayRuntime.allocateArray(c3d, sizes);
        fill(self, base, ctx.literalArreglo(), sizes, new int[sizes.length], 0);
        return base;
    }

    int[] shapeOf(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = valoresDe(ctx);
        int[] rowShape = new int[0];
        for (ParseTree valor : valores) {
            ZetarianoParser.LiteralArregloContext row = rowOf(valor);
            if (row != null) {
                rowShape = shapeOf(row);
                break;
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = valores.size();
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return sizes;
    }

    private static List<ParseTree> valoresDe(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof ZetarianoParser.ExpresionContext) {
                valores.add(child);
            }
        }
        return valores;
    }

    private static ZetarianoParser.LiteralArregloContext rowOf(ParseTree valor) {
        if (valor instanceof ZetarianoParser.LiteralArregloExprContext anidada) {
            return anidada.literalArreglo();
        }
        if (valor instanceof ZetarianoParser.LiteralArregloContext direct) {
            return direct;
        }
        return null;
    }

    private void fill(ZetarianoBaseVisitor<String> self, String base,
                      ZetarianoParser.LiteralArregloContext ctx, int[] sizes,
                      int[] indices, int dimension) {
        List<ParseTree> valores = valoresDe(ctx);
        for (int i = 0; i < valores.size() && dimension < indices.length; i++) {
            ParseTree valor = valores.get(i);
            indices[dimension] = i;
            ZetarianoParser.LiteralArregloContext row = rowOf(valor);
            if (row != null) {
                fill(self, base, row, sizes, indices, dimension + 1);
            } else {
                ArrayRuntime.writeConstant(c3d, base, indices, sizes,
                        self.visit((ZetarianoParser.ExpresionContext) valor));
            }
        }
    }
}
