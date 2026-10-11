package com.compi.backend.utils;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractNodeAST;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Convierte el AST propio en un grafo DOT (Graphviz), con el mismo formato que
 * {@link ParseTreeDotGenerator}: nodos {@code n<id>} y aristas {@code n<id> -> n<id>}.
 */
public class ASTDotGenerator {

    private final Map<AbstractNodeAST, Integer> ids = new IdentityHashMap<>();
    private final StringBuilder sb = new StringBuilder();
    private int counter = 0;

    public String build(AbstractNodeAST root, String title) {
        sb.setLength(0);
        ids.clear();
        counter = 0;

        sb.append("digraph ").append(sanitize(title)).append(" {\n");
        sb.append("  rankdir=TB;\n");
        sb.append("  node [shape=box, style=\"rounded,filled\", fillcolor=\"#1f2430\", ");
        sb.append("color=\"#5ccfe6\", fontcolor=\"#e6e6e6\", fontname=\"monospace\", fontsize=10];\n");
        sb.append("  edge [color=\"#5ccfe6\", arrowsize=0.7];\n\n");

        if (root != null) {
            visit(root);
        } else {
            sb.append("  vacio [label=\"(sin AST)\"];\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    private int visit(AbstractNodeAST node) {
        Integer existing = ids.get(node);
        if (existing != null) {
            return existing;
        }
        int id = counter++;
        ids.put(node, id);

        sb.append("  n").append(id)
                .append(" [label=\"").append(escape(label(node)))
                .append("\"];\n");

        for (ASTNode child : node.children()) {
            if (child == null) {
                continue;
            }
            int childId = visit((AbstractNodeAST) child);
            sb.append("  n").append(id).append(" -> n").append(childId).append(";\n");
        }
        return id;
    }

    private String label(AbstractNodeAST node) {
        String name = node.getClass().getSimpleName();
        if (name.endsWith("NodeAST") && name.length() > "NodeAST".length()) {
            name = name.substring(0, name.length() - "NodeAST".length());
        }
        return name + " @" + node.line() + ":" + node.column();
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ");
    }

    private String sanitize(String s) {
        if (s == null || s.isBlank()) {
            return "AST";
        }
        return s.replaceAll("[^A-Za-z0-9_]", "_");
    }
}