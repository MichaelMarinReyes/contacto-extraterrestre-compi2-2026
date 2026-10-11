package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * Emisión de C3D para declaraciones de variables, arreglos y estructuras en Y:
 * definición de símbolos, reserva de memoria, inicializadores y lectura.
 */
final class YDeclarationEmitter {

    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;

    YDeclarationEmitter(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    Type nameType(String name) {
        return switch (name.toLowerCase()) {
            case "entero" -> Type.INT;
            case "flotante" -> Type.DOUBLE;
            case "cadena" -> Type.STRING;
            case "caracter" -> Type.CHAR;
            case "bool" -> Type.BOOLEAN;
            default -> Type.structType(name);
        };
    }

    void emitDeclaration(YBaseVisitor<String> self, YParser.Declaracion_variableContext ctx) {
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "bool";
        String typeName = ctx.tipo_dato() != null ? ctx.tipo_dato().getText()
                : (ctx.ID().size() >= 2 ? ctx.ID(0).getText() : null);
        int[] sizes = new int[ctx.CORCHETE_IZQ().size()];
        List<TerminalNode> numeros = ctx.NUMERO_ENTERO();
        for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
            sizes[i] = Integer.parseInt(numeros.get(i).getText());
        }
        Type base = typeName == null ? Type.UNKNOWN : nameType(typeName);
        Type type = sizes.length == 0 ? base : Type.arrayOf(base, sizes);

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declared = new Symbol(varName, type, SymbolCategory.VARIABLE,
                offset, false).at(ctx).markWorking();
        symbolTable.define(declared);
        Symbol sym = symbolTable.resolve(varName);
        if (sym == null) {
            sym = declared;
        }

        Symbol struct = typeName == null ? null : symbolTable.getStruct(typeName);
        if (struct != null && sizes.length == 0) {
            declareStruct(self, sym, struct, ctx.expresion_inicializacion());
            return;
        }

        if (type.isArray()) {
            declareArray(self, sym, type, ctx.expresion_inicializacion());
            return;
        }

        if (ctx.expresion_inicializacion() != null
                && ctx.expresion_inicializacion().expresion() != null) {
            String value = ctx.expresion_inicializacion().expresion().getText().equals("leer()")
                    ? readValue(type) : self.visit(ctx.expresion_inicializacion().expresion());
            c3d.emitAssign(sym.getStorageName(), value);
        }
    }

    private void declareStruct(YBaseVisitor<String> self, Symbol sym, Symbol struct,
                               YParser.Expresion_inicializacionContext inicializador) {
        String base = ArrayRuntime.allocate(c3d, cellCount(struct));
        c3d.emitAssign(sym.getStorageName(), base);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            fillFields(self, base, inicializador.elemento_lista(), struct, 0);
        } else if (inicializador != null && inicializador.expresion() != null) {
            c3d.emitAssign(sym.getStorageName(), self.visit(inicializador.expresion()));
        }
    }

    private int cellCount(Type type) {
        return 1;
    }

    private int cellCount(Symbol struct) {
        int total = 0;
        for (Symbol field : struct.getMembers()) {
            total += cellCount(field.getType());
        }
        return Math.max(total, 1);
    }

    private void fillFields(YBaseVisitor<String> self, String base,
                            List<YParser.Elemento_listaContext> elementos,
                            Symbol struct, int cell) {
        int i = 0;
        for (Symbol field : struct.getMembers()) {
            if (i >= elementos.size()) {
                return;
            }
            YParser.Elemento_listaContext element = elementos.get(i);
            Type fieldType = field.getType();
            String start = ArrayRuntime.add(c3d, base, cell);

            if (element.LLAVE_IZQ() != null && fieldType != null && fieldType.isArray()) {
                int[] sizes = fieldType.getSizes();
                String array = ArrayRuntime.allocateArray(c3d, fieldType.getSizes());
                fill(self, array, element.elemento_lista(), sizes, new int[sizes.length], 0);
                c3d.emit(QuadrupleOp.HEAP_SET, start, array, null);
            } else if (element.LLAVE_IZQ() != null && fieldType != null && fieldType.isStruct()) {
                Symbol anidada = symbolTable.getStruct(fieldType.getCustomTypeName());
                if (anidada != null) {
                    String nested = HeapRuntime.allocateObject(c3d, anidada);
                    fillFields(self, nested, element.elemento_lista(), anidada, 0);
                    c3d.emit(QuadrupleOp.HEAP_SET, start, nested, null);
                }
            } else {
                c3d.emit(QuadrupleOp.HEAP_SET, start, self.visit(element.expresion()), null);
            }

            cell += cellCount(fieldType);
            i++;
        }
    }

    private void declareArray(YBaseVisitor<String> self, Symbol sym, Type type,
                              YParser.Expresion_inicializacionContext inicializador) {
        int celdas = type.totalSize();
        if (celdas <= 0) {
            return;
        }
        String base = ArrayRuntime.allocateArray(c3d, type.getSizes());
        c3d.emitAssign(sym.getStorageName(), base);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            int[] sizes = type.getSizes();
            fill(self, base, inicializador.elemento_lista(), sizes, new int[sizes.length], 0);
        }
    }

    private void fill(YBaseVisitor<String> self, String base,
                      List<YParser.Elemento_listaContext> elementos, int[] sizes,
                      int[] indices, int dimension) {
        for (int i = 0; i < elementos.size() && dimension < indices.length; i++) {
            YParser.Elemento_listaContext element = elementos.get(i);
            indices[dimension] = i;
            if (element.LLAVE_IZQ() != null) {
                fill(self, base, element.elemento_lista(), sizes, indices, dimension + 1);
            } else {
                String data = self.visit(element.expresion());
                ArrayRuntime.writeConstant(c3d, base, indices, sizes, data);
            }
        }
    }

    String readValue(Type type) {
        String result = c3d.newTemp();
        c3d.emit(Type.STRING.equals(type) ? QuadrupleOp.READ_STR : QuadrupleOp.READ,
                result, null, null);
        return result;
    }
}
