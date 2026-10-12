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
    private final boolean atStartOfLine = true;
    private Token lastToken = null;
    private int bracketLevel = 0;

    public YIndentLexer(CharStream input) {
        super(input);
        indentStack.push(0);
    }

    @Override
    public Token nextToken() {
        Token pending = nextPending();
        if (pending != null) {
            return pending;
        }

        Token token = super.nextToken();

        if (token.getType() == Token.EOF) {
            return handleEof(token);
        }

        if (token.getType() == YLexer.NUEVA_LINEA) {
            return handleNewLine(token);
        }

        trackBrackets(token);
        lastToken = token;
        return token;
    }

    private Token nextPending() {
        if (pendingTokens.isEmpty()) {
            return null;
        }
        Token token = pendingTokens.poll();
        lastToken = token;
        return token;
    }

    private Token handleEof(Token eof) {
        while (indentStack.size() > 1) {
            indentStack.pop();
            pendingTokens.add(createToken(com.compi.YParser.DEDENT, ""));
        }
        if (!pendingTokens.isEmpty()) {
            pendingTokens.add(eof);
            return nextPending();
        }
        lastToken = eof;
        return eof;
    }

    private Token handleNewLine(Token token) {
        if (bracketLevel == 0 && !emptyOrCommentLine()) {
            adjustIndent(calculateIndent(token.getText()));
        }
        lastToken = token;
        return token;
    }

    private void adjustIndent(int spaces) {
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

    private void trackBrackets(Token token) {
        int type = token.getType();
        if (type == YLexer.LLAVE_IZQ || type == YLexer.CORCHETE_IZQ
                || type == YLexer.PARENTESIS_IZQ) {
            bracketLevel++;
        } else if (type == YLexer.LLAVE_DER || type == YLexer.CORCHETE_DER
                || type == YLexer.PARENTESIS_DER) {
            if (bracketLevel > 0) {
                bracketLevel--;
            }
        }
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
