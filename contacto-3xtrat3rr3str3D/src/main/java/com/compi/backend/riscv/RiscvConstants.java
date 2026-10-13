package com.compi.backend.riscv;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Agrupa los literales numéricos del programa en un banco de constantes que se
 * emite una sola vez dentro de la sección {@code .data} como valores
 * {@code .double}. RARS no reconoce la directiva {@code .rodata}, así que las
 * constantes viven en datos inicializados para que el ensamblador embebido las
 * acepte sin advertencias.
 */
final class RiscvConstants {

    private final Map<String, String> labels = new LinkedHashMap<>();

    static boolean isNumeric(String value) {
        return value != null && value.matches("-?[0-9]+(\\.[0-9]+)?");
    }

    /** Etiqueta única por literal; normaliza los enteros a su forma decimal. */
    String label(String value) {
        String key = normalize(value);
        return labels.computeIfAbsent(key, ignored -> "cst" + labels.size());
    }

    private static String normalize(String value) {
        boolean plainInteger = value.indexOf('.') < 0
                && value.indexOf('e') < 0 && value.indexOf('E') < 0;
        return plainInteger ? value + ".0" : value;
    }

    String section() {
        StringBuilder sb = new StringBuilder(".align 3\n");
        for (Map.Entry<String, String> entry : labels.entrySet()) {
            sb.append(entry.getValue()).append(": .double ").append(entry.getKey()).append('\n');
        }
        return sb.toString();
    }
}
