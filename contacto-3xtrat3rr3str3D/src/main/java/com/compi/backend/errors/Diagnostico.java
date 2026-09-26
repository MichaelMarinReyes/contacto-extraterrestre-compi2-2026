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

/**
 * Catalogo de mensajes de error, todos escritos en el mismo idioma y con el
 * mismo estilo.
 *
 * <p>Centraliza el texto de los errores por dos motivos. El primero es que se
 * lean igual: cuando el analizador de un lenguaje dice "Variable no declarada" y
 * el de otro dice "Identificador no encontrado", quien lee la tabla ve dos
 * maneras de decir lo mismo. El segundo es que no se vea el texto tecnico de
 * ANTLR ({@code mismatched input 'x' expecting {MAIOR, FINIS}}), que no le dice
 * nada a quien esta aprendiendo el lenguaje.</p>
 *
 * <p>Nada aqui decide si algo es un error: eso lo hacen los visitantes, que
 * llaman a estas funciones cuando lo detectan.</p>
 */
public final class Diagnostico {

    /** Se abre y se cierra el dato con estos, para que se distinga de la frase. */
    private static final char ABIERTO = '«';
    private static final char CERRADO = '»';

    /** Cuantos simbolos se nombran antes de resumir el resto con una cuenta. */
    private static final int MAXIMO_EN_LISTA = 5;

    // Formas en las que ANTLR redacta sus mensajes. Se reconocen por el principio
    // y se traducen una a una; lo que no encaje en ninguna cae en el generico,
    // que conserva la parte final del original para no perder informacion.
    private static final Pattern PREFIJO_DE_REGLA =
            Pattern.compile("^rule\\s+(\\w+)\\s+(.*)$", Pattern.DOTALL);
    private static final Pattern LEXICO =
            Pattern.compile("^token recognition error at: '(.*)'$", Pattern.DOTALL);
    private static final Pattern SIN_ALTERNATIVA =
            Pattern.compile("^no viable alternative at input '(.*)'$", Pattern.DOTALL);
    private static final Pattern NO_COINCIDE =
            Pattern.compile("^mismatched input '(.*)' expecting (\\{.*}|\\S+)$", Pattern.DOTALL);
    private static final Pattern SOBRA =
            Pattern.compile("^extraneous input '(.*)' expecting (\\{.*}|\\S+)$", Pattern.DOTALL);
    private static final Pattern FALTA =
            Pattern.compile("^missing (.+?) at '(.*)'$", Pattern.DOTALL);
    private static final Pattern CONJUNTO = Pattern.compile("\\{([^}]*)}");
    private static final Pattern CON_LITERAL = Pattern.compile("'(.*)'");

    /**
     * Los simbolos que no son una palabra suelta: en la gramatica son un patron
     * y en el fuente no hay nada que escribir, asi que se nombran por lo que son.
     *
     * <p>Estan los tres lenguajes juntos porque el mismo patron se llama igual
     * en todos y asi la tabla dice lo mismo se mire el archivo que se mire.</p>
     */
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

    private Diagnostico() {
    }

    // ====================== Lexico y sintactico ======================

    /**
     * Traduce a espanol el mensaje que devuelve ANTLR.
     *
     * <p>La traduccion no es palabra por palabra: se dice que se esperaba una
     * cosa y que se encontro otra, que es justo lo que hay que corregir. Los
     * nombres de token ({@code MAIOR}) se cambian por el texto que se escribe en
     * el fuente ({@code «MAIOR»}), que es como lo ve quien esta escribiendo.</p>
     *
     * @param vocabulario del lexer o del parser, para traducir los nombres de token
     * @param msg         mensaje original de ANTLR
     * @param token       token ofensor, o null si el error es del lexer
     * @return el mensaje ya en espanol
     */
    public static String desdeAntlr(Vocabulary vocabulario, String msg, Token token) {
        if (msg == null || msg.isBlank()) {
            return "Error de sintaxis sin detalle";
        }
        String original = msg.trim();
        String texto = original;

        // "rule sentency extra input ..." solo dice en que regla se llego; el
        // problema es el que viene detras, asi que lo del principio se quita.
        Matcher regla = PREFIJO_DE_REGLA.matcher(texto);
        if (regla.matches()) {
            texto = regla.group(2).trim();
        }

        Matcher lexical = LEXICO.matcher(texto);
        if (lexical.matches()) {
            return "Carácter no reconocido: " + dato(recortado(lexical.group(1)));
        }

        Matcher alternativa = SIN_ALTERNATIVA.matcher(texto);
        if (alternativa.matches()) {
            return "Instrucción no reconocida: " + dato(recortado(alternativa.group(1)))
                    + ". Ese texto no encaja en ninguna regla del lenguaje";
        }

        Matcher sobrante = SOBRA.matcher(texto);
        if (sobrante.matches()) {
            return "Aquí no se esperaba " + encontrado(sobrante.group(1))
                    + "; se esperaba " + lista(vocabulario, sobrante.group(2));
        }

        Matcher coincidencia = NO_COINCIDE.matcher(texto);
        if (coincidencia.matches()) {
            return "Se esperaba " + lista(vocabulario, coincidencia.group(2))
                    + ", pero se encontró " + encontrado(coincidencia.group(1));
        }

        Matcher ausente = FALTA.matcher(texto);
        if (ausente.matches()) {
            return "Falta " + lista(vocabulario, ausente.group(1))
                    + " antes de " + encontrado(ausente.group(2));
        }

        // Un mensaje que no se reconoce no se tira: se enseña la parte que
        // explica el problema, sin el prefijo en ingles que no aporta nada.
        return "Error de sintaxis: " + cola(original);
    }

    /** Texto que se enseña entre comillas angulares. */
    private static String dato(String valor) {
        return ABIERTO + (valor == null || valor.isBlank() ? "?" : valor) + CERRADO;
    }

    /**
     * Lo que se encontró en el fuente.
     *
     * <p>Un token de los que no son una palabra suelta ({@code <EOF>}) se dice
     * con palabras: «<EOF>» es un nombre interno de ANTLR y en el fuente no hay
     * nada escrito asi, simplemente se acabó el archivo.</p>
     */
    private static String encontrado(String valor) {
        String limpio = valor == null ? "" : valor.trim();
        return switch (limpio) {
            case "<EOF>", "" -> "el final del archivo";
            default -> dato(recortado(limpio));
        };
    }

    /** Si el texto es largo se recorta, porque va en una celda de la tabla. */
    private static String recortado(String valor) {
        if (valor == null) {
            return "?";
        }
        String limpio = valor.replaceAll("\\s+", " ").trim();
        return limpio.length() <= 40 ? limpio : limpio.substring(0, 39) + "…";
    }

    /**
     * Junta los simbolos esperados en una frase: «A», «A» o «B», «A», «B» o «C».
     *
     * @param conjunto lo que dice ANTLR: un nombre suelto o uno entre llaves
     */
    private static String lista(Vocabulary vocabulario, String conjunto) {
        List<String> nombres = new ArrayList<>();
        Matcher llaves = CONJUNTO.matcher(conjunto);
        if (llaves.find()) {
            for (String parte : llaves.group(1).split(",")) {
                if (!parte.isBlank()) {
                    nombres.add(parte.trim());
                }
            }
        } else {
            nombres.add(conjunto.trim());
        }

        List<String> textos = new ArrayList<>(nombres.size());
        for (String nombre : nombres) {
            textos.add(esperado(vocabulario, nombre));
        }
        if (textos.isEmpty()) {
            return "otra construcción";
        }
        if (textos.size() > MAXIMO_EN_LISTA) {
            return String.join(", ", textos.subList(0, MAXIMO_EN_LISTA))
                    + " y " + (textos.size() - MAXIMO_EN_LISTA) + " más";
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

    /**
     * Como se nombra en la tabla lo que se esperaba.
     *
     * <p>Un token que en la gramatica es una palabra suelta ({@code 'si'}) se
     * enseña como esa palabra. Uno que es un patron ({@code ID}) no se puede
     * escribir, asi que se dice que clase de cosa es: «un identificador».</p>
     */
    private static String esperado(Vocabulary vocabulario, String nombre) {
        String simbolico = simbolicoDe(nombre);
        String clase = CLASES.get(simbolico);
        if (clase != null) {
            return dato(clase);
        }
        return dato(literal(vocabulario, nombre));
    }

    /** Nombre del simbolo tal cual lo nombra la gramatica, sin comillas. */
    private static String simbolicoDe(String nombre) {
        if (nombre == null) {
            return "";
        }
        Matcher conLiteral = CON_LITERAL.matcher(nombre.trim());
        return (conLiteral.matches() ? conLiteral.group(1) : nombre).trim();
    }

    /**
     * Texto que hay que escribir en el fuente para ese simbolo.
     *
     * <p>ANTLR nombra los simbolos como los declara la gramatica
     * ({@code IMPORT}), pero en el fuente se escribe {@code import}. Con el
     * vocabulario se recupera el texto real; si no se puede, se deja el nombre
     * que dio ANTLR.</p>
     */
    private static String literal(Vocabulary vocabulario, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "?";
        }
        String simbolico = simbolicoDe(nombre);
        String texto = nombre.trim();
        if (vocabulario != null) {
            for (int tipo = 0; tipo <= vocabulario.getMaxTokenType(); tipo++) {
                if (simbolico.equals(vocabulario.getSymbolicName(tipo))) {
                    String declarado = vocabulario.getLiteralName(tipo);
                    if (declarado != null && declarado.length() > 1
                            && declarado.startsWith("'") && declarado.endsWith("'")) {
                        texto = declarado.substring(1, declarado.length() - 1);
                    }
                    break;
                }
            }
        }
        return texto;
    }

    /** Lo que explica el problema, quitando el prefijo en ingles del mensaje. */
    private static String cola(String original) {
        int corte = original.indexOf(" at '");
        String texto = corte > 0 ? original.substring(0, corte) : original;
        int espacio = texto.indexOf(' ');
        if (espacio > 0) {
            texto = texto.substring(espacio + 1);
        }
        return texto.isBlank() ? original : texto;
    }

    // ====================== Semantico ======================

    private static CompilationError en(String mensaje, int linea, int columna) {
        return new CompilationError(ErrorType.SEMANTICO, mensaje, linea, columna);
    }

    private static CompilationError en(String mensaje, ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return en(mensaje, -1, -1);
        }
        return en(mensaje, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    private static String tipo(Type t) {
        return t == null ? "?" : t.label();
    }

    public static CompilationError variableNoDeclarada(String nombre, ParserRuleContext ctx) {
        return en("Variable no declarada: " + nombre, ctx);
    }

    public static CompilationError variableYaDeclarada(String nombre, ParserRuleContext ctx) {
        return en("Variable ya declarada: " + nombre
                + ". En este ámbito ya existe un símbolo con ese nombre", ctx);
    }

    public static CompilationError variableGlobalYaDeclarada(String nombre,
                                                             ParserRuleContext ctx) {
        return en("Variable global ya declarada: " + nombre, ctx);
    }

    public static CompilationError funcionNoDefinida(String nombre, ParserRuleContext ctx) {
        return en("Llamada a función no definida: " + nombre, ctx);
    }

    public static CompilationError simboloNoResuelto(String nombre, ParserRuleContext ctx) {
        return en("Símbolo no encontrado: " + nombre
                + ". No hay ninguna variable, función ni tipo con ese nombre", ctx);
    }

    public static CompilationError miembroNoExiste(String miembro, String tipo,
                                                    ParserRuleContext ctx) {
        return en("Miembro inexistente: " + miembro
                + ". El tipo " + tipo + " no tiene ese atributo", ctx);
    }

    public static CompilationError asignacionIncompatible(Type destino, Type origen,
                                                           ParserRuleContext ctx) {
        return en("Asignación incompatible: no se puede asignar " + dato(tipo(origen))
                + " a una variable de tipo " + dato(tipo(destino)), ctx);
    }

    public static CompilationError inicializacionIncompatible(String nombre, Type esperado,
                                                              Type obtenido,
                                                              ParserRuleContext ctx) {
        return en("Tipo incompatible en la declaración de " + nombre + ": se esperaba "
                + dato(tipo(esperado)) + " y se escribió " + dato(tipo(obtenido)), ctx);
    }

    public static CompilationError tipoDeRetornoErroneo(Type esperado, Type obtenido,
                                                        ParserRuleContext ctx) {
        return en("Tipo de retorno erróneo: se esperaba " + dato(tipo(esperado))
                + " pero se retorna " + dato(tipo(obtenido)), ctx);
    }

    public static CompilationError parametroDuplicado(String nombre, ParserRuleContext ctx) {
        return en("Parámetro duplicado: " + nombre
                + ". La función ya declara otro parámetro con ese nombre", ctx);
    }

    public static CompilationError nombreDeConstructorErroneo(String nombre, String clase,
                                                              ParserRuleContext ctx) {
        return en("Nombre de constructor erróneo: " + dato(nombre)
                + ". El constructor debe llamarse como la clase: " + dato(clase), ctx);
    }

    public static CompilationError condicionNoBooleana(String condicion, ParserRuleContext ctx) {
        return en("La condición " + dato(condicion) + " debe ser booleana", ctx);
    }

    public static CompilationError operadorNegacionNoBooleano(ParserRuleContext ctx) {
        return en("El operador " + dato("!") + " solo se puede aplicar a una expresión booleana",
                ctx);
    }
}
