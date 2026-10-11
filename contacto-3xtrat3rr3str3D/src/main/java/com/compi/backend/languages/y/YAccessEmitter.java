package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Emisión de C3D para accesos a miembros y elementos en Y: cálculo de la
 * dirección de un acceso, lectura/escritura de celdas y su tipo.
 */
final class YAccessEmitter {

    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;

    YAccessEmitter(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    boolean writeOnAccess(YBaseVisitor<String> self, YParser.Acceso_miembroContext ctx, String valor) {
        String address = accessAddress(self, ctx);
        if (address == null) {
            return false;
        }
        c3d.emit(QuadrupleOp.HEAP_SET, address, valor, null);
        return true;
    }

    String accessAddress(YBaseVisitor<String> self, YParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID(0).getText());
        if (base == null) {
            return null;
        }
        String address = loadVariable(base);
        Type type = base.getType();

        List<Object> steps = accessSteps(ctx);
        for (int index = 0; index < steps.size(); index++) {
            Object step = steps.get(index);
            if (step instanceof String fieldName) {
                Symbol field = fieldOf(type, fieldName);
                if (field == null) {
                    return null;
                }
                Symbol owner = HeapRuntime.definition(symbolTable, type);
                address = ArrayRuntime.add(c3d, address, HeapRuntime.fieldOffset(owner, fieldName));
                type = field.getType();
                if (index + 1 < steps.size() && (type.isArray() || type.isStruct())) {
                    address = HeapRuntime.read(c3d, address);
                }
            } else {
                if (type == null || !type.isArray()) {
                    return null;
                }
                address = ArrayRuntime.address(c3d, address,
                        List.of(self.visit((YParser.ExpresionContext) step)), type.getSizes());
                type = type.desindexar(1);
            }
        }
        return address;
    }

    private List<Object> accessSteps(YParser.Acceso_miembroContext ctx) {
        List<Object> pasos = new ArrayList<>();
        int i = 1;
        while (i < ctx.getChildCount()) {
            if (".".equals(ctx.getChild(i).getText())) {
                pasos.add(ctx.getChild(i + 1).getText());
                i += 2;
            } else {
                pasos.add(ctx.getChild(i + 1));
                i += 3;
            }
        }
        return pasos;
    }

    private Symbol fieldOf(Type type, String name) {
        if (type == null || !type.isStruct()) {
            return null;
        }
        Symbol struct = symbolTable.getStruct(type.getCustomTypeName());
        return struct == null ? null : struct.getMember(name);
    }

    String readCell(YBaseVisitor<String> self, YParser.Acceso_miembroContext ctx) {
        String address = accessAddress(self, ctx);
        if (address == null) {
            return null;
        }
        Type type = new YSemanticVisitor(symbolTable).visit(ctx);
        if (type != null && type.isArray()
                && "]".equals(ctx.getChild(ctx.getChildCount() - 1).getText())) {
            return address;
        }
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.HEAP_GET, address, null, valor);
        return valor;
    }

    String loadVariable(Symbol sym) {
        return sym.getStorageName();
    }
}
