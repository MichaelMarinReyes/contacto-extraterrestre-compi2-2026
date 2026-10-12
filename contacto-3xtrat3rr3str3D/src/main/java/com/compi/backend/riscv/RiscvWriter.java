package com.compi.backend.riscv;

/**
 * Acumulador de líneas del ensamblador generado. Las instrucciones se indentan
 * y las etiquetas quedan al inicio de la línea.
 */
final class RiscvWriter {

    private final StringBuilder out = new StringBuilder();

    void line(String text) {
        out.append("    ").append(text).append('\n');
    }

    void label(String name) {
        out.append(name).append(":\n");
    }

    void comment(String text) {
        out.append("# ").append(text).append('\n');
    }

    String content() {
        return out.toString();
    }
}
