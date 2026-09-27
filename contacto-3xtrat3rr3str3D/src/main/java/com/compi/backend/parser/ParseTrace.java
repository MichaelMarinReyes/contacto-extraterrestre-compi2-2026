package com.compi.backend.parser;

import com.compi.backend.StackAction;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class ParseTrace {

    private static final int MAX_TEXT = 32;

    private ParseTrace() {
    }

    public static List<ParseStep> build(ParseTree tree, Vocabulary vocabulary,
                                        String[] ruleNames) {
        List<ParseStep> pasos = new ArrayList<>();
        if (tree != null) {
            ParseTreeWalker.DEFAULT.walk(new Tracer(pasos, vocabulary, ruleNames), tree);
        }
        return pasos;
    }

    private static String ruleName(ParserRuleContext ctx, String[] ruleNames) {
        int index = ctx.getRuleIndex();
        if (ruleNames != null && index >= 0 && index < ruleNames.length) {
            return ruleNames[index];
        }
        String name = ctx.getClass().getSimpleName();
        if (name.endsWith("Context") && name.length() > "Context".length()) {
            name = name.substring(0, name.length() - "Context".length());
        }
        if (name.isEmpty()) {
            return "regla";
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    private static String trim(String text) {
        if (text == null) {
            return "";
        }
        String clean = text.replace("\n", "\\n").replace("\r", "");
        if (clean.length() <= MAX_TEXT) {
            return clean;
        }
        return clean.substring(0, MAX_TEXT - 3) + "...";
    }

    private static class Tracer implements ParseTreeListener {

        private final List<ParseStep> pasos;
        private final Vocabulary vocabulary;
        private final String[] ruleNames;
        private final List<String> stack = new ArrayList<>();
        private final Deque<Integer> marcas = new ArrayDeque<>();
        private int number = 0;

        Tracer(List<ParseStep> pasos, Vocabulary vocabulary, String[] ruleNames) {
            this.pasos = pasos;
            this.vocabulary = vocabulary;
            this.ruleNames = ruleNames;
            stack.add(ParseStep.MARK_START);
        }

        private static boolean isPushed(Token token) {
            return token.getType() != Token.EOF && token.getChannel() == Token.DEFAULT_CHANNEL;
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
            marcas.push(stack.size());
        }

        @Override
        public void visitTerminal(TerminalNode node) {
            shift(node, false);
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
            shift(node, true);
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            int mark = marcas.isEmpty() ? stack.size() : marcas.pop();
            List<String> consumidos = new ArrayList<>();
            while (stack.size() > mark) {
                consumidos.add(0, stack.remove(stack.size() - 1));
            }
            String rule = ruleName(ctx, ruleNames);
            Token start = ctx.getStart();
            number++;
            stack.add(rule);
            pasos.add(ParseStep.builder()
                    .number(number)
                    .type(StackAction.REDUCE)
                    .symbol(rule)
                    .line(start == null ? 0 : start.getLine())
                    .column(start == null ? 0 : start.getCharPositionInLine() + 1)
                    .invalid(false)
                    .stack(stack)
                    .consumed(consumidos)
                    .build());
        }

        private void shift(TerminalNode node, boolean invalid) {
            Token token = node.getSymbol();
            if (token == null || !isPushed(token)) {
                return;
            }
            number++;
            String lexema = trim(token.getText());
            stack.add(lexema);
            pasos.add(ParseStep.builder()
                    .number(number)
                    .type(StackAction.SHIFT)
                    .symbol(lexema)
                    .terminal(tokenName(token))
                    .line(token.getLine())
                    .column(token.getCharPositionInLine() + 1)
                    .invalid(invalid)
                    .stack(stack)
                    .consumed(List.of())
                    .build());
        }

        private String tokenName(Token token) {
            if (vocabulary == null) {
                return null;
            }
            String name = vocabulary.getSymbolicName(token.getType());
            if (name == null) {
                name = vocabulary.getLiteralName(token.getType());
            }
            return name;
        }
    }
}
