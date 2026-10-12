package com.compi.backend.ast.codegen;

import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.exprressions.BinaryOperationNodeAST;
import com.compi.backend.ast.exprressions.OperationKind;
import com.compi.backend.ast.exprressions.UnaryOperationNodeAST;
import com.compi.backend.ast.statements.*;
import com.compi.backend.c3d.QuadrupleOp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Control de flujo con cortocircuito, caída de casos y destinos de continuación.
 */
final class AstControlEmitter {
    final Deque<String> breakLabels = new ArrayDeque<>();
    final Deque<String> continueLabels = new ArrayDeque<>();
    private final AstCodeGenerator g;

    AstControlEmitter(AstCodeGenerator generator) {
        g = generator;
    }

    void jump(AbstractExpressionNodeAST condition, String yes, String no) {
        if (condition instanceof UnaryOperationNodeAST unary && unary.operator().equals("!")) {
            jump(unary.operand(), no, yes);
        } else if (condition instanceof BinaryOperationNodeAST binary && binary.kind() == OperationKind.LOGICAL) {
            String next = g.c3d.newLabel();
            if (binary.operator().equals("&&")) jump(binary.left(), next, no);
            else jump(binary.left(), yes, next);
            g.c3d.emitLabel(next);
            jump(binary.right(), yes, no);
        } else {
            if (condition instanceof BinaryOperationNodeAST binary && binary.kind() == OperationKind.RELATIONAL) {
                String left = g.memory.capture(g.emit(binary.left()));
                g.c3d.emit(relational(binary.operator()), left, g.emit(binary.right()), yes);
            } else g.c3d.emit(QuadrupleOp.IF_TRUE, g.emit(condition), null, yes);
            g.c3d.emitGoto(no);
        }
    }

    private QuadrupleOp relational(String operator) {
        return switch (operator) {
            case "==" -> QuadrupleOp.IF_EQ;
            case "!=" -> QuadrupleOp.IF_NE;
            case "<" -> QuadrupleOp.IF_LT;
            case ">" -> QuadrupleOp.IF_GT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> throw new IllegalStateException("Comparación desconocida: " + operator);
        };
    }

    String booleanValue(AbstractExpressionNodeAST n) {
        String result = g.c3d.newTemp();
        String yes = g.c3d.newLabel(), no = g.c3d.newLabel(), end = g.c3d.newLabel();
        jump(n, yes, no);
        g.c3d.emitLabel(yes);
        g.c3d.emitAssign(result, "1");
        g.c3d.emitGoto(end);
        g.c3d.emitLabel(no);
        g.c3d.emitAssign(result, "0");
        g.c3d.emitLabel(end);
        return result;
    }

    void conditional(IfNodeAST n) {
        String end = g.c3d.newLabel();
        branch(n.condition(), n.thenBody(), end);
        for (ElseIfNodeAST alternative : n.elseIfs()) branch(alternative.condition(), alternative.body(), end);
        g.emit(n.elseBody());
        g.c3d.emitLabel(end);
    }

    private void branch(AbstractExpressionNodeAST condition, com.compi.backend.ast.AbstractStatementNodeAST body, String end) {
        String yes = g.c3d.newLabel(), no = g.c3d.newLabel();
        jump(condition, yes, no);
        g.c3d.emitLabel(yes);
        g.emit(body);
        g.c3d.emitGoto(end);
        g.c3d.emitLabel(no);
    }

    void forLoop(ForNodeAST n) {
        g.enter(n);
        String test = g.c3d.newLabel(), body = g.c3d.newLabel(), step = g.c3d.newLabel(), end = g.c3d.newLabel();
        breakLabels.push(end);
        continueLabels.push(step);
        try {
            g.emit(n.initializer());
            g.c3d.emitLabel(test);
            if (n.condition() == null) g.c3d.emitGoto(body);
            else jump(n.condition(), body, end);
            g.c3d.emitLabel(body);
            g.emit(n.body());
            g.c3d.emitLabel(step);
            g.emit(n.update());
            g.c3d.emitGoto(test);
            g.c3d.emitLabel(end);
        } finally {
            breakLabels.pop();
            continueLabels.pop();
            g.table.exitScope();
        }
    }

    void whileLoop(WhileNodeAST n) {
        String test = g.c3d.newLabel(), body = g.c3d.newLabel(), end = g.c3d.newLabel();
        breakLabels.push(end);
        continueLabels.push(test);
        try {
            g.c3d.emitLabel(test);
            jump(n.condition(), body, end);
            g.c3d.emitLabel(body);
            g.emit(n.body());
            g.c3d.emitGoto(test);
            g.c3d.emitLabel(end);
        } finally {
            breakLabels.pop();
            continueLabels.pop();
        }
    }

    void doLoop(DoWhileNodeAST n) {
        String body = g.c3d.newLabel(), test = g.c3d.newLabel(), end = g.c3d.newLabel();
        breakLabels.push(end);
        continueLabels.push(test);
        try {
            g.c3d.emitLabel(body);
            g.emit(n.body());
            g.c3d.emitLabel(test);
            jump(n.condition(), body, end);
            g.c3d.emitLabel(end);
        } finally {
            breakLabels.pop();
            continueLabels.pop();
        }
    }

    void selection(SwitchNodeAST n) {
        String end = g.c3d.newLabel(), fallback = n.defaultBody() == null ? end : g.c3d.newLabel();
        String value = g.memory.capture(g.emit(n.subject()));
        List<String> labels = new ArrayList<>();
        for (SwitchCaseNodeAST branch : n.cases()) {
            String label = g.c3d.newLabel();
            labels.add(label);
            g.c3d.emit(QuadrupleOp.IF_EQ, value, g.emit(branch.value()), label);
        }
        g.c3d.emitGoto(fallback);
        breakLabels.push(end);
        boolean selectionContinue = g.language.equals("y") && continueLabels.isEmpty();
        if (selectionContinue) continueLabels.push(end);
        try {
            for (int i = 0; i < n.cases().size(); i++) {
                g.c3d.emitLabel(labels.get(i));
                g.emit(n.cases().get(i).body());
                if (g.language.equals("y")) g.c3d.emitGoto(end);
            }
            if (n.defaultBody() != null) {
                g.c3d.emitLabel(fallback);
                g.emit(n.defaultBody());
            }
            g.c3d.emitLabel(end);
        } finally {
            breakLabels.pop();
            if (selectionContinue) continueLabels.pop();
        }
    }
}
