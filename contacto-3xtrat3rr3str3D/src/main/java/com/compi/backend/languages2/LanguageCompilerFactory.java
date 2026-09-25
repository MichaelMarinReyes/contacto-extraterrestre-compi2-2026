package com.compi.backend.languages2;

import com.compi.backend.errors.CompilationError;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registro de lenguajes soportados y punto unico de resolucion.
 *
 * Acepta identificadores ("pig", "piglatin", "y", "zet", "zetariano") y tambien
 * nombres de archivo ("main.z") para que el frontend pueda deducir el lenguaje
 * automaticamente al abrir un archivo.
 */
public final class LanguageCompilerFactory {

    private static final Map<String, LanguageCompiler> BY_ID = new LinkedHashMap<>();
    private static final List<LanguageCompiler> ALL = List.of(
            new PigLatinCompiler(),
            new YCompiler(),
            new ZetarianoCompiler()
    );

    static {
        for (LanguageCompiler c : ALL) {
            BY_ID.put(c.id(), c);
        }
        BY_ID.put("piglatin", BY_ID.get("pig"));
        BY_ID.put("zetariano", BY_ID.get("zet"));
    }

    private LanguageCompilerFactory() {
    }

    public static List<LanguageCompiler> all() {
        return ALL;
    }

    /**
     * Resuelve un compilador por identificador o por nombre de archivo.
     *
     * <p>Si el nombre no es un identificador conocido se interpreta como
     * archivo: primero se mira la extension y, si no ayuda, el nombre sin
     * extension ("y" o "zet" siguen siendo validos como nombre de archivo).</p>
     *
     * @return el compilador o null si el lenguaje no esta soportado
     */
    public static LanguageCompiler getCompiler(String languageOrFileName) {
        if (languageOrFileName == null || languageOrFileName.isBlank()) {
            return null;
        }
        String key = languageOrFileName.trim().toLowerCase();
        LanguageCompiler direct = BY_ID.get(key);
        if (direct != null) {
            return direct;
        }
        // Intentar por extension: "main.z" -> "zet"
        int slash = Math.max(key.lastIndexOf('/'), key.lastIndexOf('\\'));
        int dot = key.lastIndexOf('.');
        if (dot > slash && dot < key.length() - 1) {
            String ext = key.substring(dot + 1);
            LanguageCompiler byExt = byExtension(ext);
            if (byExt != null) {
                return byExt;
            }
            // Ultimo recurso: el nombre sin extension ("y", "zetariano.y.prueba").
            return BY_ID.get(key.substring(slash + 1, dot));
        }
        return null;
    }

    private static LanguageCompiler byExtension(String ext) {
        return switch (ext) {
            case "pig", "lat", "piglatin" -> BY_ID.get("pig");
            case "y" -> BY_ID.get("y");
            case "z", "zet" -> BY_ID.get("zet");
            default -> null;
        };
    }

    /**
     * Deduce el lenguaje a partir del nombre de un archivo.
     */
    public static String detectLanguageId(String fileName) {
        LanguageCompiler c = getCompiler(fileName);
        return c == null ? null : c.id();
    }

    /**
     * Filtra los errores de una lista por tipo.
     */
    public static List<CompilationError> filter(List<CompilationError> errors, String typeName) {
        if (errors == null) {
            return List.of();
        }
        return errors.stream()
                .filter(e -> e.getType().equalsIgnoreCase(typeName))
                .toList();
    }
}
