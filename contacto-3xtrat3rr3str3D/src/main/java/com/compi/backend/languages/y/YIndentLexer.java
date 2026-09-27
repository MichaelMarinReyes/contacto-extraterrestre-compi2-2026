package com.compi.backend.languages.y;

import com.compi.YLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class YIndentLexer extends YLexer {
    private final Stack<Integer> indentStack = new Stack<>();
    private final Queue<Token> pendingTokens = new LinkedList<>();
    private boolean atStartOfLine = true;
    private Token lastToken = null;
    private int bracketLevel = 0;

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
            if (bracketLevel == 0 && !emptyOrCommentLine()) {
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

            lastToken = token;
            return token;
        }

        if (token.getType() == YLexer.LLAVE_IZQ || token.getType() == YLexer.CORCHETE_IZQ
                || token.getType() == YLexer.PARENTESIS_IZQ) {
            bracketLevel++;
        } else if (token.getType() == YLexer.LLAVE_DER || token.getType() == YLexer.CORCHETE_DER
                || token.getType() == YLexer.PARENTESIS_DER) {
            if (bracketLevel > 0) {
                bracketLevel--;
            }
        }

        lastToken = token;
        return token;
    }

    private boolean emptyOrCommentLine() {
        int first = getInputStream().LA(1);
        if (first == '\n' || first == '\r' || first == IntStream.EOF) {
            return true;
        }
        if (first == '/') {
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
