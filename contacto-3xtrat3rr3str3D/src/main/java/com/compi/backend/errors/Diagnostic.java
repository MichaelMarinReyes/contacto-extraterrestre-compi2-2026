package com.compi.backend.errors;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Traduce los diagnósticos léxicos y sintácticos de ANTLR al español.
 */
public final class Diagnostic {

    private static final char OPEN_QUOTE = '«';
    private static final char CLOSE_QUOTE = '»';

    private static final int MAX_IN_LIST = 5;

    private static final Pattern RULE_PREFIX =
            Pattern.compile("^rule\\s+(\\w+)\\s+(.*)$", Pattern.DOTALL);
    private static final Pattern LEXICAL =
            Pattern.compile("^token recognition error at: '(.*)'$", Pattern.DOTALL);
    private static final Pattern NO_ALTERNATIVE =
            Pattern.compile("^no viable alternative at input '(.*)'$", Pattern.DOTALL);
    private static final Pattern MISMATCH =
            Pattern.compile("^mismatched input '(.*)' expecting (\\{.*}|\\S+)$", Pattern.DOTALL);
    private static final Pattern LEFTOVER =
            Pattern.compile("^extraneous input '(.*)' expecting (\\{.*}|\\S+)$", Pattern.DOTALL);
    private static final Pattern MISSING =
            Pattern.compile("^missing (.+?) at '(.*)'$", Pattern.DOTALL);
    private static final Pattern BRACED = Pattern.compile("\\{([^}]*)}");
    private static final Pattern WITH_LITERAL = Pattern.compile("'(.*)'");

    private static final Map<String, String> CLASES = Map.of(
            "ID", "un identificador",
            "VARIABLE", "un identificador",
            "NUMERO_ENTERO", "un número entero",
            "LITERAL_ENTERO", "un número entero",
            "NUMERO_DECIMAL", "un número con decimales",
            "LITERAL_DECIMAL", "un número con decimales",
            "CADENA_TEXTO", "un texto entre comillas",
            "CARACTER", "un carácter entre comillas simples",
            "NUEVA_LINEA", "un salto de línea",
            "LITTERA", "una letra");

    public static String fromAntlr(Vocabulary vocabulary, String msg, Token token) {
        if (msg == null || msg.isBlank()) {
            return "Error de sintaxis sin detalle";
        }
        String original = msg.trim();
        String text = original;
        Matcher rule = RULE_PREFIX.matcher(text);
        if (rule.matches()) {
            text = rule.group(2).trim();
        }

        Matcher lexical = LEXICAL.matcher(text);
        if (lexical.matches()) {
            return "Carácter no reconocido: " + data(trimmed(lexical.group(1)));
        }

        Matcher alternative = NO_ALTERNATIVE.matcher(text);
        if (alternative.matches()) {
            return "Instrucción no reconocida: " + data(trimmed(alternative.group(1)))
                    + ". Ese texto no encaja en ninguna regla del lenguaje";
        }

        Matcher leftover = LEFTOVER.matcher(text);
        if (leftover.matches()) {
            return "Aquí no se esperaba " + found(leftover.group(1))
                    + "; se esperaba " + list(vocabulary, leftover.group(2));
        }

        Matcher match = MISMATCH.matcher(text);
        if (match.matches()) {
            return "Se esperaba " + list(vocabulary, match.group(2))
                    + ", pero se encontró " + found(match.group(1));
        }

        Matcher missing = MISSING.matcher(text);
        if (missing.matches()) {
            return "Falta " + list(vocabulary, missing.group(1))
                    + " antes de " + found(missing.group(2));
        }

        return "Error de sintaxis: " + cola(original);
    }

    private static String data(String valor) {
        return OPEN_QUOTE + (valor == null || valor.isBlank() ? "?" : valor) + CLOSE_QUOTE;
    }

    private static String found(String valor) {
        String clean = valor == null ? "" : valor.trim();
        return switch (clean) {
            case "<EOF>", "" -> "el final del archivo";
            default -> data(trimmed(clean));
        };
    }

    private static String trimmed(String valor) {
        if (valor == null) {
            return "?";
        }
        String clean = valor.replaceAll("\\s+", " ").trim();
        return clean.length() <= 40 ? clean : clean.substring(0, 39) + "…";
    }

    private static String list(Vocabulary vocabulary, String braced) {
        List<String> nombres = new ArrayList<>();
        Matcher llaves = BRACED.matcher(braced);
        if (llaves.find()) {
            for (String part : llaves.group(1).split(",")) {
                if (!part.isBlank()) {
                    nombres.add(part.trim());
                }
            }
        } else {
            nombres.add(braced.trim());
        }

        List<String> textos = new ArrayList<>(nombres.size());
        for (String name : nombres) {
            textos.add(esperado(vocabulary, name));
        }
        if (textos.isEmpty()) {
            return "otra construcción";
        }
        if (textos.size() > MAX_IN_LIST) {
            return String.join(", ", textos.subList(0, MAX_IN_LIST))
                    + " y " + (textos.size() - MAX_IN_LIST) + " más";
        }
        if (textos.size() == 1) {
            return textos.get(0);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textos.size(); i++) {
            if (i > 0) {
                sb.append(i == textos.size() - 1 ? " o " : ", ");
            }
            sb.append(textos.get(i));
        }
        return sb.toString();
    }

    private static String esperado(Vocabulary vocabulary, String name) {
        String symbolic = symbolicOf(name);
        String clazz = CLASES.get(symbolic);
        if (clazz != null) {
            return data(clazz);
        }
        return data(literal(vocabulary, name));
    }

    private static String symbolicOf(String name) {
        if (name == null) {
            return "";
        }
        Matcher conLiteral = WITH_LITERAL.matcher(name.trim());
        return (conLiteral.matches() ? conLiteral.group(1) : name).trim();
    }

    private static String literal(Vocabulary vocabulary, String name) {
        if (name == null || name.isBlank()) {
            return "?";
        }
        String symbolic = symbolicOf(name);
        String text = name.trim();
        if (vocabulary != null) {
            for (int type = 0; type <= vocabulary.getMaxTokenType(); type++) {
                if (symbolic.equals(vocabulary.getSymbolicName(type))) {
                    String declaredType = vocabulary.getLiteralName(type);
                    if (declaredType != null && declaredType.length() > 1
                            && declaredType.startsWith("'") && declaredType.endsWith("'")) {
                        text = declaredType.substring(1, declaredType.length() - 1);
                    }
                    break;
                }
            }
        }
        return text;
    }

    private static String cola(String original) {
        int truncation = original.indexOf(" at '");
        String text = truncation > 0 ? original.substring(0, truncation) : original;
        int space = text.indexOf(' ');
        if (space > 0) {
            text = text.substring(space + 1);
        }
        return text.isBlank() ? original : text;
    }

}
