package com.compi.frontend;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Resaltado sintactico para PigLatin, Y y Zetariano.
 *
 * Trabaja sobre el {@link StyledDocument} del editor: primero limpia el rango
 * completo con el estilo por defecto y luego aplica un estilo por categoria
 * (palabra clave, tipo, cadena, comentario, numero, constante).
 */
public class SyntaxHighlighter {

    private static final int MAX_DOC_LENGTH = 120_000;

    private final JTextPane pane;
    private final Map<String, Style> styles = new LinkedHashMap<>();
    private final Map<String, Language> languages = new LinkedHashMap<>();

    private String languageId = "pig";
    private boolean running;
    /** true si ya hay un apply() en la cola de eventos. */
    private boolean pending;
    /** Texto del ultimo resaltado, para no repetirlo si no cambio nada. */
    private String lastText;

    public SyntaxHighlighter(JTextPane pane) {
        this.pane = pane;
        registerLanguages();
        buildStyles();
        setLanguage(languageId);
    }

    // ====================== Definicion de lenguajes ======================

    private void registerLanguages() {
        languages.put("pig", new Language(
                List.of("esto", "series", "structura", "finis", "FINIS", "si", "aliter", "dum",
                        "facere", "per", "perge", "interrumpe", "actio", "ratio", "reddere",
                        "import", "novus", "MAIOR", "VARIABILES", "MUNERA"),
                List.of("numerus", "textum", "decimalis", "littera"),
                List.of("verum", "falsus")));

        languages.put("y", new Language(
                List.of("definir", "estructura", "si", "entonces", "sino", "contrario", "elegir",
                        "caso", "siempre", "romper", "continuar", "retornar", "para", "mientras",
                        "hacer", "imprimir", "leer"),
                List.of("entero", "cadena", "flotante", "caracter", "bool"),
                List.of("verdadero", "falso")));

        languages.put("zet", new Language(
                List.of("public", "private", "protected", "class", "return", "new", "if", "else",
                        "while", "for", "read", "print", "void", "this", "static"),
                List.of("entero", "decimalis", "numerus", "textum", "littera", "boolean", "double",
                        "float", "int", "char", "String"),
                List.of("verdadero", "falso", "true", "false", "null", "este")));
    }

    // ====================== Estilos ======================

    private void buildStyles() {
        styles.clear();
        styles.put("default", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));
        StyleConstants.setForeground(styles.get("default"), UiTheme.fg());
        styles.put("keyword", style("kw", new Color(0xCF8E6D), true, false));
        styles.put("type", style("ty", new Color(0x56A8F5), false, false));
        styles.put("constant", style("co", new Color(0xC77DBB), false, true));
        styles.put("string", style("st", new Color(0x6AAB73), false, false));
        styles.put("number", style("nu", new Color(0x2AACB8), false, false));
        styles.put("comment", style("cm", new Color(0x7A7E85), false, true));
    }

    private Style style(String key, Color color, boolean bold, boolean italic) {
        Style s = pane.addStyle(key, null);
        StyleConstants.setForeground(s, color);
        StyleConstants.setBold(s, bold);
        StyleConstants.setItalic(s, italic);
        return s;
    }

    public void setLanguage(String id) {
        this.languageId = languages.containsKey(id) ? id : "pig";
        // El texto no cambio pero las reglas si: hay que volver a pintar.
        this.lastText = null;
    }

    public String getLanguage() {
        return languageId;
    }

    // ====================== Resaltado ======================

    /**
     * Relanza el resaltado en el hilo de Swing.
     *
     * <p>Se descarta la peticion si ya hay un resaltado en marcha: de lo
     * contrario los {@code setCharacterAttributes} que aplica el propio
     * resaltador se realimentarian sin fin.</p>
     */
    public void rehighlight() {
        if (running || pending) {
            return;
        }
        pending = true;
        SwingUtilities.invokeLater(() -> {
            pending = false;
            apply();
        });
    }

    /** Aplica el resaltado sobre el documento actual. */
    public void apply() {
        if (running) {
            return;
        }
        StyledDocument doc = pane.getStyledDocument();
        if (doc == null) {
            return;
        }
        String text;
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            return;
        }
        if (text.length() > MAX_DOC_LENGTH) {
            return;
        }
        // Solo un cambio de texto obliga a repintar: los cambios de estilo que
        // llegan despues son los que aplica este mismo metodo.
        if (text.equals(lastText)) {
            return;
        }
        lastText = text;

        running = true;
        try {
            int len = text.length();
            if (len > 0) {
                doc.setCharacterAttributes(0, len, styles.get("default"), true);
            }

            // 1. Comentarios de bloque y de linea
            paint(doc, text, "##[\\s\\S]*?##", "comment");
            paint(doc, text, "//[^\\n]*", "comment");
            paint(doc, text, "/\\*[\\s\\S]*?\\*/", "comment");

            // 2. Cadenas y caracteres
            paint(doc, text, "\"(?:\\\\.|[^\"\\\\\\n])*\"", "string");
            paint(doc, text, "'(?:\\\\.|[^'\\\\])'", "string");

            // 3. Numeros
            paint(doc, text, "\\b\\d+\\.\\d+\\b", "number");
            paint(doc, text, "\\b\\d+\\b", "number");

            // 4. Secciones o literales propios del lenguaje
            if ("pig".equals(languageId)) {
                paint(doc, text, "\\b(VARIABILES|MUNERA|MAIOR)\\b", "constant");
            } else if ("y".equals(languageId)) {
                paint(doc, text, "(?m)^\\s*%\\w+", "constant");
            }

            // 5. Palabras clave, tipos y constantes
            Language lang = languages.get(languageId);
            paintWords(doc, text, lang.keywords(), "keyword");
            paintWords(doc, text, lang.types(), "type");
            paintWords(doc, text, lang.constants(), "constant");

        } finally {
            running = false;
        }
    }

    private void paint(StyledDocument doc, String text, String regex, String styleKey) {
        Style s = styles.get(styleKey);
        if (s == null) {
            return;
        }
        Matcher m = Pattern.compile(regex).matcher(text);
        while (m.find()) {
            if (m.end() > m.start()) {
                doc.setCharacterAttributes(m.start(), m.end() - m.start(), s, false);
            }
        }
    }

    private void paintWords(StyledDocument doc, String text, List<String> words, String styleKey) {
        if (words == null || words.isEmpty()) {
            return;
        }
        StringBuilder regex = new StringBuilder("\\b(");
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                regex.append('|');
            }
            regex.append(Pattern.quote(words.get(i)));
        }
        regex.append(")\\b");
        paint(doc, text, regex.toString(), styleKey);
    }

    /** Definicion de las palabras reservadas de un lenguaje. */
    private record Language(List<String> keywords, List<String> types, List<String> constants) {
    }
}
