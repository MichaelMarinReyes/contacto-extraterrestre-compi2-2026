package com.compi.backend.languages.y;

import com.compi.YLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Lexer de Y que convierte la sangria del codigo en tokens INDENT y DEDENT.
 *
 * Solo las lineas que realmente contienen codigo deciden la sangria:
 *  - una linea en blanco, o que solo tiene un comentario, no abre ni cierra
 *    bloques (su tabulacion es irrelevante);
 *  - dentro de parentesis, corchetes o llaves la sangria tambien es
 *    irrelevante, asi que alli no se generan INDENT ni DEDENT. Eso permite
 *    escribir inicializadores de arreglos en varias lineas.
 */
public class YIndentLexer extends YLexer {
    private final Stack<Integer> indentStack = new Stack<>();
    private final Queue<Token> pendingTokens = new LinkedList<>();
    private boolean atStartOfLine = true;
    private Token lastToken = null;
    private int nivelCorchetes = 0;

    public YIndentLexer(CharStream input) {
        super(input);
        indentStack.push(0);
    }

    @Override
    public Token nextToken() {
        if (!pendingTokens.isEmpty()) {
            Token t = pendingTokens.poll();
            lastToken = t;
            return t;
        }

        Token token = super.nextToken();

        if (token.getType() == Token.EOF) {
            // Generar DEDENT para cualquier nivel restante de indentación
            while (indentStack.size() > 1) {
                indentStack.pop();
                pendingTokens.add(createToken(com.compi.YParser.DEDENT, ""));
            }
            if (!pendingTokens.isEmpty()) {
                pendingTokens.add(token);
                Token t = pendingTokens.poll();
                lastToken = t;
                return t;
            }
            lastToken = token;
            return token;
        }

        if (token.getType() == YLexer.NUEVA_LINEA) {
            // La sangria solo cuenta si la linea que empieza aqui tiene codigo
            // y no estamos dentro de parentesis, corchetes o llaves.
            if (nivelCorchetes == 0 && !lineaVaciaNiSoloComentario()) {
                int spaces = calculateIndent(token.getText());

                int currentIndent = indentStack.peek();
                if (spaces > currentIndent) {
                    indentStack.push(spaces);
                    pendingTokens.add(createToken(com.compi.YParser.INDENT, ""));
                } else if (spaces < currentIndent) {
                    while (!indentStack.isEmpty() && indentStack.peek() > spaces) {
                        indentStack.pop();
                        pendingTokens.add(createToken(com.compi.YParser.DEDENT, ""));
                    }
                }
            }

            // Si hay tokens en la cola (INDENT o DEDENT), retornamos la NUEVA_LINEA y dejamos los tokens pendientes
            lastToken = token;
            return token;
        }

        if (token.getType() == YLexer.LLAVE_IZQ || token.getType() == YLexer.CORCHETE_IZQ
                || token.getType() == YLexer.PARENTESIS_IZQ) {
            nivelCorchetes++;
        } else if (token.getType() == YLexer.LLAVE_DER || token.getType() == YLexer.CORCHETE_DER
                || token.getType() == YLexer.PARENTESIS_DER) {
            if (nivelCorchetes > 0) {
                nivelCorchetes--;
            }
        }

        lastToken = token;
        return token;
    }

    /**
     * La linea que empieza despues del salto de linea actual esta vacia o
     * contiene nada mas que un comentario? En ese caso su sangria no cuenta.
     */
    private boolean lineaVaciaNiSoloComentario() {
        int primero = getInputStream().LA(1);
        if (primero == '\n' || primero == '\r' || primero == IntStream.EOF) {
            return true;
        }
        if (primero == '/') {
            int segundo = getInputStream().LA(2);
            return segundo == '/' || segundo == '*';
        }
        return false;
    }

    private int calculateIndent(String text) {
        int count = 0;
        int lastNewline = Math.max(text.lastIndexOf('\n'), text.lastIndexOf('\r'));
        String trailing = (lastNewline >= 0) ? text.substring(lastNewline + 1) : text;

        for (int i = 0; i < trailing.length(); i++) {
            char c = trailing.charAt(i);
            if (c == ' ') count += 1;
            else if (c == '\t') count += 4;
        }
        return count;
    }

    private Token createToken(int type, String text) {
        CommonToken token = new CommonToken(type, text);
        if (lastToken != null) {
            token.setLine(lastToken.getLine());
            token.setCharPositionInLine(lastToken.getCharPositionInLine());
        }
        return token;
    }
}
