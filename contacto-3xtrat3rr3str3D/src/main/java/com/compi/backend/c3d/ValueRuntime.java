package com.compi.backend.c3d;

import com.compi.backend.symbols.Type;

/** Conversión explícita al concatenar texto y valores numéricos. */
public final class ValueRuntime {
    private ValueRuntime() { }
    public static String asText(C3DGenerator generator, String value, Type type) {
        if (Type.STRING.equals(type)) return value;
        String result = generator.newTemp();
        generator.emit(QuadrupleOp.PARAM, value, null, null);
        generator.emit(QuadrupleOp.CALL, Type.CHAR.equals(type) ? "char_to_text" : "number_to_text", "1", result);
        return result;
    }
}
