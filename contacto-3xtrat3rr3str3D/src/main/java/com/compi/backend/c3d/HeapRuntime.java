package com.compi.backend.c3d;

import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

/** Convención común: cada campo ocupa una celda y los agregados guardan referencias. */
public final class HeapRuntime {
    private HeapRuntime() { }

    public static Symbol definition(SymbolTable symbols, Type type) {
        if (type == null) return null;
        return type.isClass() ? symbols.getClass(type.getCustomTypeName())
                : type.isStruct() ? symbols.getStruct(type.getCustomTypeName()) : null;
    }

    public static int fieldOffset(Symbol owner, String name) {
        int offset = 0;
        for (Symbol field : owner.getMembers()) {
            if (field.getCategory() != SymbolCategory.FIELD) continue;
            if (field.getName().equals(name)) return offset;
            offset++;
        }
        throw new IllegalStateException("No existe el campo «" + name + "» en «" + owner.getName() + "»");
    }

    public static String read(C3DGenerator generator, String address) {
        String value = generator.newTemp();
        generator.emit(QuadrupleOp.HEAP_GET, address, null, value);
        return value;
    }

    public static String allocateObject(C3DGenerator generator, Symbol owner) {
        int count = (int) owner.getMembers().stream()
                .filter(member -> member.getCategory() == SymbolCategory.FIELD).count();
        String base = ArrayRuntime.allocate(generator, Math.max(count, 1));
        for (int i = 0; i < count; i++) {
            generator.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(generator, base, i), "0", null);
        }
        return base;
    }
}
