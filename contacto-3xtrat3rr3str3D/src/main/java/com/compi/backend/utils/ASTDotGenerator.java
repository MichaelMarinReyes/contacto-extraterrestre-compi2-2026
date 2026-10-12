package com.compi.backend.utils;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractNodeAST;
import com.compi.backend.ast.decl.*;
import com.compi.backend.ast.expr.*;
import com.compi.backend.ast.stmt.*;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Convierte el AST propio en un grafo DOT (Graphviz): nodos {@code n<id>}
 * y aristas {@code n<id> -> n<id>} compatibles con el panel gráfico.
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
        String detail = detail(node);
        return name + (detail.isEmpty() ? "" : " · " + detail) + " @" + node.line() + ":" + node.column();
    }

    private String detail(AbstractNodeAST node) {
        return switch (node) {
            case IdentifierNodeAST n -> n.name();
            case LiteralNodeAST n -> n.kind() + " " + String.valueOf(n.value());
            case BinaryOperationNodeAST n -> n.operator();
            case UnaryOperationNodeAST n -> n.operator();
            case AssignmentNodeAST n -> n.operator();
            case AssignmentExpressionNodeAST n -> n.operator();
            case IncrementNodeAST n -> (n.prefix() ? "prefijo " : "posfijo ") + n.operator();
            case DeclarationNodeAST n -> n.name() + ": " + n.type().label() + dimensions(n.dimensions().size());
            case AttributeDefinitionNodeAST n -> n.name() + ": " + n.type().label() + dimensions(n.dimensions().size());
            case FieldDefinitionNodeAST n -> n.name() + ": " + n.type().label() + dimensions(n.dimensions().size());
            case ParameterNodeAST n -> n.name() + ": " + n.type().label() + (n.byReference() ? " &" : "");
            case FunctionDefinitionNodeAST n -> n.name() + ": " + n.returnType().label();
            case MethodDefinitionNodeAST n -> n.name() + ": " + n.returnType().label();
            case ClassDefinitionNodeAST n -> n.name();
            case StructDefinitionNodeAST n -> n.name();
            case MemberAccessNodeAST n -> n.member();
            case CallNodeAST n -> n.name();
            case NewObjectNodeAST n -> n.typeName();
            case NewArrayNodeAST n -> n.elementType().label();
            case InstanceInitNodeAST n -> n.typeName() + (n.named().isEmpty() ? "" : " " + n.named().keySet());
            case ImportNodeAST n -> n.path();
            case ReadExpressionNodeAST n -> n.kind().toString();
            case PrintNodeAST n -> n.newline() ? "con salto" : "sin salto";
            default -> "";
        };
    }

    private String dimensions(int count) {
        return "[]".repeat(count);
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
