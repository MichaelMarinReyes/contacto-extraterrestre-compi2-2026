package com.compi.backend.languages2;

import com.compi.backend.errors.CompilationError;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Registro de lenguajes soportados y punto unico de resolucion.
 *
 * <p>Es tambien la fuente de verdad de las extensiones de archivo admitidas: el
 * frontend decide que se abre a partir de estos metodos, de modo que anadir un
 * lenguaje aqui lo hace aceptable en el arbol de archivos sin tocar la
 * interfaz.</p>
 *
 * <p>Se aceptan identificadores ("pig", "y", "zet") y nombres de archivo; para
 * un archivo solo cuenta la extension, nunca el nombre sin ella.</p>
 */
public final class LanguageCompilerFactory {

    /** Extension -> identificador de lenguaje, en el orden en que se muestran. */
    private static final Map<String, String> BY_EXTENSION = new LinkedHashMap<>();

    private static final Map<String, LanguageCompiler> BY_ID = new LinkedHashMap<>();
    private static final List<LanguageCompiler> ALL = List.of(
            new PigLatinCompiler(),
            new YCompiler(),
            new ZetarianoCompiler()
    );

    static {
        for (LanguageCompiler c : ALL) {
            BY_ID.put(c.id(), c);
            // Cada compilador declara sus propias extensiones: esa es la fuente
            // de verdad de lo que el frontend puede abrir.
            for (String ext : c.extensions()) {
                registerExtension(ext, c.id());
            }
        }
        BY_ID.put("piglatin", BY_ID.get("pig"));
        BY_ID.put("zetariano", BY_ID.get("zet"));
    }

    private static void registerExtension(String extension, String languageId) {
        String ext = extension.startsWith(".")
                ? extension.substring(1) : extension;
        BY_EXTENSION.put(ext.toLowerCase(Locale.ROOT), languageId);
    }

    private LanguageCompilerFactory() {
    }

    public static List<LanguageCompiler> all() {
        return ALL;
    }

    // ====================== Extensiones admitidas ======================

    /** Extensiones de archivo aceptadas, sin punto y en minúsculas. */
    public static List<String> allowedExtensions() {
        return List.copyOf(BY_EXTENSION.keySet());
    }

    /** Etiqueta de filtro para los selectores de archivo: "*.pig, *.y, *.z". */
    public static String extensionPattern() {
        StringBuilder sb = new StringBuilder();
        for (String ext : BY_EXTENSION.keySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("*.").append(ext);
        }
        return sb.toString();
    }

    /**
     * Indica si un nombre de archivo lleva una extension admitida.
     *
     * <p>Solo se mira la ultima extension: "programa.final.pig" si entra, y
     * "pig.txt" no, porque el archivo de verdad se llama "pig.txt".</p>
     */
    public static boolean hasAllowedExtension(String fileName) {
        return extensionOf(fileName) != null;
    }

    /** Extension en minúsculas sin el punto, o null si el nombre no la tiene. */
    public static String extensionOf(String fileName) {
        if (fileName == null) {
            return null;
        }
        String name = fileName.trim().toLowerCase(Locale.ROOT);
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        int dot = name.lastIndexOf('.');
        if (dot <= slash || dot == name.length() - 1) {
            return null;
        }
        String ext = name.substring(dot + 1);
        return BY_EXTENSION.containsKey(ext) ? ext : null;
    }

    /** Extension que corresponde a un identificador de lenguaje. */
    public static String extensionOfLanguage(String languageId) {
        if (languageId == null) {
            return null;
        }
        String id = languageId.trim().toLowerCase(Locale.ROOT);
        for (Map.Entry<String, String> e : BY_EXTENSION.entrySet()) {
            if (e.getValue().equals(id)) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Identificador de lenguaje al que apunta una extension.
     *
     * @return el identificador, o null si la extension no esta admitida
     */
    public static String byExtensionLanguage(String extension) {
        if (extension == null) {
            return null;
        }
        return BY_EXTENSION.get(extension.trim().toLowerCase(Locale.ROOT));
    }

    /**
     * Resuelve un compilador por identificador o por nombre de archivo.
     *
     * <p>Si el nombre no es un identificador conocido se interpreta como
     * archivo y se decide solo por su extension.</p>
     *
     * @return el compilador o null si el lenguaje no esta soportado
     */
    public static LanguageCompiler getCompiler(String languageOrFileName) {
        if (languageOrFileName == null || languageOrFileName.isBlank()) {
            return null;
        }
        String key = languageOrFileName.trim().toLowerCase(Locale.ROOT);
        LanguageCompiler direct = BY_ID.get(key);
        if (direct != null) {
            return direct;
        }
        // Por extension: "main.z" -> zetariano
        String ext = extensionOf(key);
        return ext == null ? null : BY_ID.get(BY_EXTENSION.get(ext));
    }

    /**
     * Deduce el lenguaje a partir del nombre de un archivo.
     *
     * @return el identificador, o null si la extension no esta admitida
     */
    public static String detectLanguageId(String fileName) {
        String ext = extensionOf(fileName);
        return ext == null ? null : BY_EXTENSION.get(ext);
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
