package com.compi.backend.c3d;

import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

/**
 * Convención común: cada campo ocupa una celda y los agregados guardan referencias.
 */
public final class HeapRuntime {
    private HeapRuntime() {
    }

    public static Symbol definition(SymbolTable symbols, Type type) {
        if (type == null) return null;
        return type.isClass() ? symbols.getClass(type.getCustomTypeName())
                : type.isStruct() ? symbols.getStruct(type.getCustomTypeName()) : null;
    }

    /** Número de campos propios más los heredados de toda la jerarquía. */
    public static int fieldCount(Symbol owner) {
        int count = 0;
        for (Symbol current = owner; current != null; current = current.getParentClass()) {
            for (Symbol member : current.getMembers()) {
                if (member.getCategory() == SymbolCategory.FIELD) count++;
            }
        }
        return count;
    }

    public static int fieldOffset(Symbol owner, String name) {
        java.util.List<Symbol> chain = new java.util.ArrayList<>();
        for (Symbol current = owner; current != null; current = current.getParentClass()) chain.add(0, current);
        int offset = 0;
        for (Symbol current : chain) {
            for (Symbol field : current.getMembers()) {
                if (field.getCategory() != SymbolCategory.FIELD) continue;
                if (field.getName().equals(name)) return offset;
                offset++;
            }
        }
        throw new IllegalStateException("No existe el campo «" + name + "» en «" + owner.getName() + "»");
    }

    public static String read(C3DGenerator generator, String address) {
        String value = generator.newTemp();
        generator.emit(QuadrupleOp.HEAP_GET, address, null, value);
        return value;
    }

    public static String allocateObject(C3DGenerator generator, Symbol owner) {
        int count = fieldCount(owner);
        String base = ArrayRuntime.allocate(generator, Math.max(count, 1));
        for (int i = 0; i < count; i++) {
            generator.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(generator, base, i), "0", null);
        }
        // Marca de tipo para el despacho virtual de métodos.
        if (owner != null && owner.getCategory() == SymbolCategory.CLASS) {
            generator.emit(QuadrupleOp.TYPE_SET, base, String.valueOf(owner.getTypeTag()), null);
        }
        return base;
    }
}
