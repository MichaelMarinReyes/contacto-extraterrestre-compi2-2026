package com.compi.backend.utils;

import java.util.IdentityHashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Genera una representacion DOT de cualquier {@link ParseTree} de ANTLR.
 *
 * Se usa como fallback generico cuando el lenguaje no provee un visitor
 * Graphviz especifico para su AST.
 */
public class ParseTreeDotGenerator {

    private final Map<ParseTree, Integer> ids = new IdentityHashMap<>();
    private final StringBuilder sb = new StringBuilder();
    private int counter = 0;

    /**
     * Construye el DOT completo del arbol.
     *
     * @param tree  raiz del arbol de parseo
     * @param title titulo del grafo
     * @return String con el codigo DOT
     */
    public String build(ParseTree tree, String title) {
        sb.setLength(0);
        ids.clear();
        counter = 0;

        sb.append("digraph ").append(sanitize(title)).append(" {\n");
        sb.append("  rankdir=TB;\n");
        sb.append("  node [shape=box, style=\"rounded,filled\", fillcolor=\"#2b2b2b\", ");
        sb.append("color=\"#6c707e\", fontcolor=\"#e6e6e6\", fontname=\"monospace\", fontsize=10];\n");
        sb.append("  edge [color=\"#7f8b99\", arrowsize=0.7];\n\n");

        if (tree != null) {
            visit(tree);
        } else {
            sb.append("  vacio [label=\"(sin arbol)\"];\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    private int visit(ParseTree node) {
        Integer existing = ids.get(node);
        if (existing != null) {
            return existing;
        }
        int id = counter++;
        ids.put(node, id);

        sb.append("  n").append(id)
          .append(" [label=\"").append(escape(label(node))).append("\"];\n");

        for (int i = 0; i < node.getChildCount(); i++) {
            ParseTree child = node.getChild(i);
            int childId = visit(child);
            sb.append("  n").append(id).append(" -> n").append(childId).append(";\n");
        }
        return id;
    }

    private String label(ParseTree node) {
        if (node instanceof TerminalNode) {
            String text = node.getText();
            if (text == null) {
                return "EOF";
            }
            text = text.replace("\n", "\\n").replace("\t", " ");
            if (text.length() > 28) {
                text = text.substring(0, 28) + "...";
            }
            return "'" + text + "'";
        }
        if (node instanceof ErrorNode) {
            return "<error>";
        }
        if (node instanceof ParserRuleContext) {
            ParserRuleContext ctx = (ParserRuleContext) node;
            String name = ruleName(ctx);
            if (ctx.exception != null) {
                return name + " (!)";
            }
            return name;
        }
        return node.getClass().getSimpleName();
    }

    private String ruleName(ParserRuleContext ctx) {
        // El runtime de ANTLR 4.13 ya no expone getRuleNames() en el contexto,
        // asi que se deriva el nombre de la clase <Regla>Context.
        String simple = ctx.getClass().getSimpleName();
        if (simple.endsWith("Context") && simple.length() > "Context".length()) {
            simple = simple.substring(0, simple.length() - "Context".length());
        }
        if (!simple.isEmpty()) {
            return Character.toLowerCase(simple.charAt(0)) + simple.substring(1);
        }
        return ctx.getClass().getSimpleName();
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ");
    }

    private String sanitize(String s) {
        if (s == null || s.isBlank()) {
            return "Grafo";
        }
        return s.replaceAll("[^A-Za-z0-9_]", "_");
    }
}
