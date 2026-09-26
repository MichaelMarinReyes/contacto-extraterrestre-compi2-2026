package com.compi.backend.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Construye el trazo de la pila del parser a partir del arbol de parseo.
 *
 * <p>La pila de un parser contiene simbolos de la gramatica, no acciones, y solo
 * hay dos cosas que se pueden hacer con ella:</p>
 * <ul>
 *   <li><b>shift</b>: se lee un token de la entrada y se apila. Solo entran
 *       terminales por aqui.</li>
 *   <li><b>reduce</b>: los simbolos de arriba son el lado derecho de una regla,
 *       asi que se retiran y se apila en su lugar el no terminal que la
 *       representa. Solo entran reglas por aqui.</li>
 * </ul>
 *
 * <p>El recorrido del arbol de abajo hacia arriba reproduce ese mismo orden: los
 * tokens se apilan segun aparecen en el fuente y una regla se reduce cuando se ha
 * terminado de leer todo lo que la compone. La marca de inicio se apila antes de
 * empezar, que es lo que hace la base de la pila nunca este vacia.</p>
 *
 * <p>No se apila nada al entrar a una regla: entrar a una regla no cambia la pila
 * en un parser de abajo hacia arriba, solo delimita que simbolos se retiraran
 * cuando la regla termine.</p>
 */
public final class ParseTrace {

    /** Longitud maxima del lexema de un token antes de recortarlo. */
    private static final int MAX_TEXTO = 32;

    private ParseTrace() {
    }

    /**
     * Recorre el arbol y devuelve un paso por cada shift y cada reduce.
     *
     * @param tree        arbol de parseo, puede ser null
     * @param vocabulario vocabulario del parser, para nombrar cada token; si es
     *                    null los pasos solo llevan el lexema
     * @param ruleNames   nombres de las reglas del parser, para nombrar cada
     *                    reduce; si es null se deduce del nombre de la clase del
     *                    nodo
     * @return lista de pasos, en el orden en que ocurrieron
     */
    public static List<ParseStep> build(ParseTree tree, Vocabulary vocabulario,
                                        String[] ruleNames) {
        List<ParseStep> pasos = new ArrayList<>();
        if (tree != null) {
            ParseTreeWalker.DEFAULT.walk(new Trazador(pasos, vocabulario, ruleNames), tree);
        }
        return pasos;
    }

    /** Escucha el recorrido del arbol y va apilando lo que apila el parser. */
    private static class Trazador implements ParseTreeListener {

        private final List<ParseStep> pasos;
        private final Vocabulary vocabulario;
        private final String[] ruleNames;
        /** Pila del parser: simbolos de la gramatica, de la base a la cima. */
        private final List<String> pila = new ArrayList<>();
        /** Para cada regla abierta, el tamano que tenia la pila al entrar. */
        private final Deque<Integer> marcas = new ArrayDeque<>();
        private int numero = 0;

        Trazador(List<ParseStep> pasos, Vocabulary vocabulario, String[] ruleNames) {
            this.pasos = pasos;
            this.vocabulario = vocabulario;
            this.ruleNames = ruleNames;
            pila.add(ParseStep.MARCA_INICIO);
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
            // Entrar en una regla no apila nada. Solo se apunta donde estaba la
            // pila para poder retirar despues lo que la regla haya apilado.
            marcas.push(pila.size());
        }

        @Override
        public void visitTerminal(TerminalNode node) {
            desplazar(node, false);
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
            // Un token que no encaja en la regla tambien se apila, pero avisando
            // de que el parser no pudo colocarlo.
            desplazar(node, true);
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            // Reduce: se retira de la pila todo lo que la regla apilo y se deja
            // en su lugar el no terminal que la representa.
            int marca = marcas.isEmpty() ? pila.size() : marcas.pop();
            List<String> consumidos = new ArrayList<>();
            while (pila.size() > marca) {
                consumidos.add(0, pila.remove(pila.size() - 1));
            }
            String regla = nombreDeRegla(ctx, ruleNames);
            Token inicio = ctx.getStart();
            numero++;
            pila.add(regla);
            pasos.add(new ParseStep(numero, AccionPila.REDUCE, regla, null,
                    inicio == null ? 0 : inicio.getLine(),
                    inicio == null ? 0 : inicio.getCharPositionInLine() + 1,
                    false, pila, consumidos));
        }

        /** Un shift: apila el token que se acaba de leer de la entrada. */
        private void desplazar(TerminalNode node, boolean invalido) {
            Token token = node.getSymbol();
            if (token == null || !seApila(token)) {
                return;
            }
            numero++;
            String lexema = recortar(token.getText());
            pila.add(lexema);
            pasos.add(new ParseStep(numero, AccionPila.SHIFT, lexema, nombreDeToken(token),
                    token.getLine(), token.getCharPositionInLine() + 1,
                    invalido, pila, List.of()));
        }

        /**
         * El final de archivo y el canal oculto (los comentarios de bloque) no
         * llegan nunca a la pila: el primero es un marcador de la entrada y los
         * segundos el parser se los salta.
         */
        private static boolean seApila(Token token) {
            return token.getType() != Token.EOF && token.getChannel() == Token.DEFAULT_CHANNEL;
        }

        /** Nombre del token tal y como lo llama la gramatica. */
        private String nombreDeToken(Token token) {
            if (vocabulario == null) {
                return null;
            }
            String nombre = vocabulario.getSymbolicName(token.getType());
            if (nombre == null) {
                nombre = vocabulario.getLiteralName(token.getType());
            }
            return nombre;
        }
    }

    /**
     * Nombre de la regla a la que reduce un contexto.
     *
     * <p>Se pregunta al parser por el indice de la regla, que es el dato fiable.
     * Sacarlo del nombre de la clase no sirve: cuando una regla es recursiva
     * izquierda, ANTLR la reescribe y genera una clase por cada alternativa, y en
     * Zetariano {@code expresion} aparece descompuesta en {@code IdExprContext},
     * {@code IntLiteralExprContext} y compañía. Esas clases solo son una manera de
     * guardar las alternativas, no simbolos de la gramatica, y ademas quedan
     * colgadas como hermanas, no dentro de la regla: un parser de verdad apila
     * {@code expresion}, no {@code intLiteralExpr}.</p>
     */
    private static String nombreDeRegla(ParserRuleContext ctx, String[] ruleNames) {
        int indice = ctx.getRuleIndex();
        if (ruleNames != null && indice >= 0 && indice < ruleNames.length) {
            return ruleNames[indice];
        }
        // Sin los nombres de regla no hay mas remedio que deducirlo de la clase
        String nombre = ctx.getClass().getSimpleName();
        if (nombre.endsWith("Context") && nombre.length() > "Context".length()) {
            nombre = nombre.substring(0, nombre.length() - "Context".length());
        }
        if (nombre.isEmpty()) {
            return "regla";
        }
        return Character.toLowerCase(nombre.charAt(0)) + nombre.substring(1);
    }

    /** Recorta los lexemas muy largos para que la pila se lea de un vistazo. */
    private static String recortar(String texto) {
        if (texto == null) {
            return "";
        }
        String limpio = texto.replace("\n", "\\n").replace("\r", "");
        if (limpio.length() <= MAX_TEXTO) {
            return limpio;
        }
        return limpio.substring(0, MAX_TEXTO - 3) + "...";
    }
}
