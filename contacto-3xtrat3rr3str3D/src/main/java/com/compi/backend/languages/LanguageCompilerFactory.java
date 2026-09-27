package com.compi.backend.languages;

import com.compi.backend.errors.CompilationError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LanguageCompilerFactory {

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
            for (String ext : c.extensions()) {
                registerExtension(ext, c.id());
            }
        }
        BY_ID.put("piglatin", BY_ID.get("pig"));
        BY_ID.put("zetariano", BY_ID.get("zet"));
    }

    private LanguageCompilerFactory() {
    }

    private static void registerExtension(String extension, String languageId) {
        String ext = extension.startsWith(".")
                ? extension.substring(1) : extension;
        BY_EXTENSION.put(ext.toLowerCase(Locale.ROOT), languageId);
    }

    public static List<LanguageCompiler> all() {
        return ALL;
    }

    public static List<String> allowedExtensions() {
        return List.copyOf(BY_EXTENSION.keySet());
    }

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

    public static boolean hasAllowedExtension(String fileName) {
        return extensionOf(fileName) != null;
    }

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

    public static String byExtensionLanguage(String extension) {
        if (extension == null) {
            return null;
        }
        return BY_EXTENSION.get(extension.trim().toLowerCase(Locale.ROOT));
    }

    public static LanguageCompiler getCompiler(String languageOrFileName) {
        if (languageOrFileName == null || languageOrFileName.isBlank()) {
            return null;
        }
        String key = languageOrFileName.trim().toLowerCase(Locale.ROOT);
        LanguageCompiler direct = BY_ID.get(key);
        if (direct != null) {
            return direct;
        }
        String ext = extensionOf(key);
        return ext == null ? null : BY_ID.get(BY_EXTENSION.get(ext));
    }

    public static String detectLanguageId(String fileName) {
        String ext = extensionOf(fileName);
        return ext == null ? null : BY_EXTENSION.get(ext);
    }

    public static List<CompilationError> filter(List<CompilationError> errors, String typeName) {
        if (errors == null) {
            return List.of();
        }
        return errors.stream()
                .filter(e -> e.getType().equalsIgnoreCase(typeName))
                .toList();
    }
}
