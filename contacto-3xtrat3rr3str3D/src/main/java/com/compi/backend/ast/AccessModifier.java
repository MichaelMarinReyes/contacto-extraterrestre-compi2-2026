package com.compi.backend.ast;

/**
 * Visibilidad de un miembro de clase. La ausencia de modificador se representa como
 * {@link #DEFAULT} (acceso de paquete).
 */
public enum AccessModifier {
    PUBLIC,
    PRIVATE,
    PROTECTED,
    DEFAULT;

    /** Interpreta el modificador declarado o {@link #DEFAULT} cuando no hay ninguno. */
    public static AccessModifier from(String text) {
        if (text == null) return DEFAULT;
        return switch (text) {
            case "public" -> PUBLIC;
            case "private" -> PRIVATE;
            case "protected" -> PROTECTED;
            default -> DEFAULT;
        };
    }
}
