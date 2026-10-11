package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

import java.util.List;

/**
 * Emisión de C3D para objetos, arreglos y estructuras en PigLatin: relleno del
 * inicializador de un arreglo, acceso a elementos y creación de objetos.
 */
final class PigLatinObjectEmitter {

    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;

    PigLatinObjectEmitter(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    void emitInitializer(PigLatinBaseVisitor<String> self,
                           PigLatinParser.Arreglo_declaracionContext ctx) {
        Symbol s = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (s == null || s.getType() == null || !s.getType().isArray()) {
            return;
        }
        Type type = s.getType();
        if (type.totalSize() <= 0) {
            return;
        }
        if (ctx.elemento_arreglo() != null) {
            int[] sizes = type.getSizes();
            String base = ArrayRuntime.allocateArray(c3d, type.getSizes());
            c3d.emitAssign(s.getStorageName(), base);
            fill(self, base, ctx.elemento_arreglo(), sizes, new int[sizes.length], 0);
        } else {
            String base = ArrayRuntime.allocateArray(c3d, type.getSizes());
            c3d.emitAssign(s.getStorageName(), base);
            if (ctx.elemento_arreglo_struct() != null) {
                int index = 0;
                Symbol definition = HeapRuntime.definition(symbolTable, type.getElementType());
                for (var initializer : ctx.elemento_arreglo_struct().structura_instanciacion()) {
                    c3d.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(c3d, base, index++),
                            initializeStruct(self, definition, initializer), null);
                }
            }
        }
    }

    private void fill(PigLatinBaseVisitor<String> self, String name,
                      PigLatinParser.Elemento_arregloContext ctx, int[] sizes,
                      int[] indices, int dimension) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        for (int i = 0; i < valores.size(); i++) {
            PigLatinParser.Elemento_arreglo_valorContext valor = valores.get(i);
            if (dimension >= indices.length) {
                break;
            }
            indices[dimension] = i;
            if (valor.LLAVE_IZQ() != null) {
                if (valor.elemento_arreglo() != null) {
                    fill(self, name, valor.elemento_arreglo(), sizes, indices, dimension + 1);
                }
            } else {
                String data = self.visit(valor.expresion());
                ArrayRuntime.writeConstant(c3d, name, indices, sizes, data);
            }
        }
    }

    String readElement(PigLatinBaseVisitor<String> self,
                       PigLatinParser.Acceso_miembroContext ctx) {
        String address = accessAddress(self, ctx, ctx.getChildCount());
        Type type = symbolTable.typeOfSyntax(ctx);
        boolean partialArray = type != null && type.isArray()
                && "]".equals(ctx.getChild(ctx.getChildCount() - 1).getText());
        return partialArray ? address : HeapRuntime.read(c3d, address);
    }

    void writeElement(PigLatinBaseVisitor<String> self,
                      PigLatinParser.Acceso_miembroContext ctx, String valor) {
        c3d.emit(QuadrupleOp.HEAP_SET, accessAddress(self, ctx, ctx.getChildCount()), valor, null);
    }

    String accessAddress(PigLatinBaseVisitor<String> self,
                         PigLatinParser.Acceso_miembroContext access, int limit) {
        Symbol base = symbolTable.resolve(access.VARIABLE(0).getText());
        Type type = base.getType();
        String value = base.getStorageName();
        String address = value;
        for (int i = 1; i < limit;) {
            if (".".equals(access.getChild(i).getText())) {
                String name = access.getChild(i + 1).getText();
                Symbol owner = HeapRuntime.definition(symbolTable, type);
                address = ArrayRuntime.add(c3d, value, HeapRuntime.fieldOffset(owner, name));
                type = owner.getMember(name).getType();
                i += 2;
                if (i < limit) {
                    value = HeapRuntime.read(c3d, address);
                }
            } else {
                address = ArrayRuntime.address(c3d, value, List.of(self.visit(access.getChild(i + 1))), type.getSizes());
                type = type.desindexar(1);
                i += 3;
                value = i < limit && !type.isArray() ? HeapRuntime.read(c3d, address) : address;
            }
        }
        return address;
    }

    String newObject(PigLatinBaseVisitor<String> self, String name,
                     PigLatinParser.ArgumentosContext arguments) {
        Symbol owner = symbolTable.getClass(name);
        String base = HeapRuntime.allocateObject(c3d, owner);
        c3d.emit(QuadrupleOp.PARAM, base, null, null);
        int count = 1;
        if (arguments != null) {
            for (PigLatinParser.ExpresionContext argument : arguments.expresion()) {
                c3d.emit(QuadrupleOp.PARAM, self.visit(argument), null, null);
                count++;
            }
        }
        c3d.emit(QuadrupleOp.CALL, FunctionRuntime.name(name, name, count - 1),
                String.valueOf(count), null);
        return base;
    }

    String initializeStruct(PigLatinBaseVisitor<String> self, Symbol definition,
                            PigLatinParser.Structura_instanciacionContext initializer) {
        String base = HeapRuntime.allocateObject(c3d, definition);
        if (!initializer.expresion().isEmpty()) {
            for (int i = 0; i < initializer.expresion().size(); i++) {
                c3d.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(c3d, base, i),
                        self.visit(initializer.expresion(i)), null);
            }
        } else {
            for (PigLatinParser.Atributo_asignacionContext field : initializer.atributo_asignacion()) {
                String name = field.VARIABLE(0).getText();
                String value;
                if (field.expresion() != null) {
                    value = self.visit(field.expresion());
                } else if (field.structura_instanciacion() != null) {
                    value = initializeStruct(self,
                            HeapRuntime.definition(symbolTable, definition.getMember(name).getType()),
                            field.structura_instanciacion());
                } else if (field.NOVUS() != null) {
                    value = newObject(self, field.VARIABLE(1).getText(), field.argumentos());
                } else {
                    Type type = definition.getMember(name).getType();
                    value = ArrayRuntime.allocateArray(c3d, type.getSizes());
                    if (field.arreglo_literal().elemento_arreglo() != null) {
                        fill(self, value, field.arreglo_literal().elemento_arreglo(),
                                type.getSizes(), new int[type.getDimensions()], 0);
                    }
                }
                c3d.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(c3d, base,
                        HeapRuntime.fieldOffset(definition, name)), value, null);
            }
        }
        return base;
    }
}
