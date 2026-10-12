package com.compi.backend.c3d;

/**
 * Distingue las sobrecargas por cantidad de argumentos.
 */
public final class FunctionRuntime {
    private FunctionRuntime() {
    }

    public static String name(String owner, String method, int count) {
        return owner + "_" + method + "_a" + count;
    }
}
