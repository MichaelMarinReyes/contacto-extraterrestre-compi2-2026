package com.compi.backend.errors;

import com.compi.backend.symbols.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

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

    private Diagnostic() {
    }

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

    private static CompilationError en(String message, int line, int column) {
        return new CompilationError(ErrorType.SEMANTIC, message, line, column);
    }

    private static CompilationError en(String message, ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return en(message, -1, -1);
        }
        return en(message, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    private static String type(Type t) {
        return t == null ? "?" : t.label();
    }

    public static CompilationError undeclaredVariable(String name, ParserRuleContext ctx) {
        return en("Variable no declarada: " + name, ctx);
    }

    public static CompilationError undefinedType(String name, ParserRuleContext ctx) {
        return en("Tipo no definido: " + name
                + ". No es un tipo primitivo ni una estructura declarada", ctx);
    }

    public static CompilationError alreadyDeclaredVariable(String name, ParserRuleContext ctx) {
        return en("Variable ya declarada: " + name
                + ". En este ámbito ya existe un símbolo con ese nombre", ctx);
    }

    public static CompilationError globalAlreadyDeclared(String name,
                                                             ParserRuleContext ctx) {
        return en("Variable global ya declarada: " + name, ctx);
    }

    public static CompilationError undefinedFunction(String name, ParserRuleContext ctx) {
        return en("Llamada a función no definida: " + name, ctx);
    }

    public static CompilationError unresolvedSymbol(String name, ParserRuleContext ctx) {
        return en("Símbolo no encontrado: " + name
                + ". No hay ninguna variable, función ni tipo con ese nombre", ctx);
    }

    public static CompilationError memberDoesNotExist(String member, String type,
                                                    ParserRuleContext ctx) {
        return en("Miembro inexistente: " + member
                + ". El tipo " + type + " no tiene ese atributo", ctx);
    }

    public static CompilationError incompatibleAssignment(Type target, Type origin,
                                                           ParserRuleContext ctx) {
        return en("Asignación incompatible: no se puede asignar " + data(type(origin))
                + " a una variable de tipo " + data(type(target)), ctx);
    }

    public static CompilationError inicializacionIncompatible(String name, Type esperado,
                                                              Type obtenido,
                                                              ParserRuleContext ctx) {
        return en("Tipo incompatible en la declaración de " + name + ": se esperaba "
                + data(type(esperado)) + " y se escribió " + data(type(obtenido)), ctx);
    }

    public static CompilationError wrongReturnType(Type esperado, Type obtenido,
                                                        ParserRuleContext ctx) {
        return en("Tipo de retorno erróneo: se esperaba " + data(type(esperado))
                + " pero se retorna " + data(type(obtenido)), ctx);
    }

    public static CompilationError duplicateParameter(String name, ParserRuleContext ctx) {
        return en("Parámetro duplicado: " + name
                + ". La función ya declara otro parámetro con ese nombre", ctx);
    }

    public static CompilationError invalidConstructorName(String name, String clazz,
                                                              ParserRuleContext ctx) {
        return en("Nombre de constructor erróneo: " + data(name)
                + ". El constructor debe llamarse como la clase: " + data(clazz), ctx);
    }

    public static CompilationError nonBooleanCondition(String condition, ParserRuleContext ctx) {
        return en("La condición " + data(condition) + " debe ser booleana", ctx);
    }

    public static CompilationError nonBooleanNegationOperator(ParserRuleContext ctx) {
        return en("El operador " + data("!") + " solo se puede aplicar a una expresión booleana",
                ctx);
    }

    public static CompilationError nonNumericMinusOperator(ParserRuleContext ctx) {
        return en("El operador " + data("-") + " solo se puede aplicar a una expresión numérica",
                ctx);
    }

    public static CompilationError notAnArray(String name, Type type,
                                                ParserRuleContext ctx) {
        return en("No se puede indexar " + data(name) + ": es de tipo "
                + data(type(type)) + " y un arreglo es lo unico que lleva índice", ctx);
    }

    public static CompilationError indexDimensions(String name, int indices,
                                                        int declaradas,
                                                        ParserRuleContext ctx) {
        return en("El acceso " + data(name) + " usa " + data(String.valueOf(indices))
                + " índice" + (indices == 1 ? "" : "s")
                + " pero el arreglo tiene " + data(String.valueOf(declaradas))
                + " dimensión" + (declaradas == 1 ? "" : "es"), ctx);
    }

    public static CompilationError initializerShape(String name, int declaradas,
                                                         int encontradas,
                                                         ParserRuleContext ctx) {
        return en("La forma no coincide con la declaración de " + data(name) + ": se declararon "
                + data(String.valueOf(declaradas)) + " dimensión"
                + (declaradas == 1 ? "" : "es") + " pero el inicializador tiene "
                + data(String.valueOf(encontradas)), ctx);
    }

    public static CompilationError filasDesiguales(String name, int row, int esperadas,
                                                   int encontradas,
                                                   ParserRuleContext ctx) {
        return en("La fila " + data(String.valueOf(row)) + " de " + data(name) + " tiene "
                + data(String.valueOf(encontradas)) + " elemento"
                + (encontradas == 1 ? "" : "s") + " y se declararon "
                + data(String.valueOf(esperadas)), ctx);
    }

    public static CompilationError incompatibleElement(String name, Type esperado,
                                                         Type obtenido,
                                                         ParserRuleContext ctx) {
        return en("Elemento de " + data(name) + " de tipo " + data(type(esperado))
                + ": no se puede guardar un valor de tipo " + data(type(obtenido)), ctx);
    }

    public static CompilationError indexOutOfRange(String name, int index, int limit,
                                                      ParserRuleContext ctx) {
        return en("Índice fuera de rango: " + data(name) + "[" + data(String.valueOf(index))
                + "] y la dimensión va de 0 a " + data(String.valueOf(limit - 1)), ctx);
    }

    public static CompilationError sizelessArray(String name,
                                                    ParserRuleContext ctx) {
        return en("No se puede reservar memoria para " + data(name)
                + ": su tamaño no se conoce. Decláralo con el tamaño"
                + " (por ejemplo [3][2]) o inicialízalo con valores", ctx);
    }
}
