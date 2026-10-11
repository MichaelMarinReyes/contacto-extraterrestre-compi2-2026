package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Emite condiciones con cortocircuito y materializa sus valores booleanos. */
final class ZetarianoConditionEmitter {
    private final C3DGenerator c3d;
    private final ZetarianoBaseVisitor<String> visitor;

    ZetarianoConditionEmitter(C3DGenerator c3d, ZetarianoBaseVisitor<String> visitor) {
        this.c3d = c3d;
        this.visitor = visitor;
    }

    String emitIf(ZetarianoParser.SentenciaIfContext ctx) {
        String end = c3d.newLabel();
        var conditions = ctx.expresion();
        var blocks = ctx.bloque();
        int count = conditions.size();
        for (int i = 0; i < count; i++) {
            String yes = c3d.newLabel();
            boolean last = i == count - 1 && blocks.size() == count;
            String no = last ? end : c3d.newLabel();
            emitConditionalJump(conditions.get(i), yes, no);
            c3d.emitLabel(yes);
            visitor.visit(blocks.get(i));
            c3d.emitGoto(end);
            if (!last) c3d.emitLabel(no);
        }
        for (int i = count; i < blocks.size(); i++) visitor.visit(blocks.get(i));
        c3d.emitLabel(end);
        return null;
    }

    void emitConditionalJump(ZetarianoParser.ExpresionContext condition, String yes, String no) {
        if (condition instanceof ZetarianoParser.NotExprContext negated) {
            emitConditionalJump(negated.expresion(), no, yes);
            return;
        }
        if (condition instanceof ZetarianoParser.AndExprContext conjunction) {
            String next = c3d.newLabel();
            emitConditionalJump(conjunction.expresion(0), next, no);
            c3d.emitLabel(next);
            emitConditionalJump(conjunction.expresion(1), yes, no);
            return;
        }
        if (condition instanceof ZetarianoParser.OrExprContext disjunction) {
            String next = c3d.newLabel();
            emitConditionalJump(disjunction.expresion(0), yes, next);
            c3d.emitLabel(next);
            emitConditionalJump(disjunction.expresion(1), yes, no);
            return;
        }
        if (condition instanceof ZetarianoParser.ParenExprContext parentheses) {
            emitConditionalJump(parentheses.expresion(), yes, no);
            return;
        }
        if (condition instanceof ZetarianoParser.RelationalExprContext relational) {
            c3d.emit(relationalOp(relational.getChild(1).getText()),
                    visitor.visit(relational.expresion(0)), visitor.visit(relational.expresion(1)), yes);
        } else if (condition instanceof ZetarianoParser.EqualityExprContext equality) {
            c3d.emit(equalityOp(equality.getChild(1).getText()),
                    visitor.visit(equality.expresion(0)), visitor.visit(equality.expresion(1)), yes);
        } else c3d.emit(QuadrupleOp.IF_TRUE, visitor.visit(condition), null, yes);
        if (no != null) c3d.emitGoto(no);
    }

    private QuadrupleOp relationalOp(String operator) {
        return switch (operator) {
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> QuadrupleOp.IF_EQ;
        };
    }

    private QuadrupleOp equalityOp(String operator) {
        return "!=".equals(operator) ? QuadrupleOp.IF_NE : QuadrupleOp.IF_EQ;
    }

    String compare(ZetarianoParser.ExpresionContext leftContext,
                   ZetarianoParser.ExpresionContext rightContext, String operator) {
        String left = visitor.visit(leftContext);
        String right = visitor.visit(rightContext);
        QuadrupleOp op = "==".equals(operator) || "!=".equals(operator)
                ? equalityOp(operator) : relationalOp(operator);
        String result = c3d.newTemp();
        String yes = c3d.newLabel(), no = c3d.newLabel(), end = c3d.newLabel();
        c3d.emit(op, left, right, yes);
        c3d.emitGoto(no);
        assignBoolean(result, yes, no, end);
        return result;
    }

    String booleanValue(ZetarianoParser.ExpresionContext ctx) {
        String result = c3d.newTemp();
        String yes = c3d.newLabel(), no = c3d.newLabel(), end = c3d.newLabel();
        emitConditionalJump(ctx, yes, no);
        assignBoolean(result, yes, no, end);
        return result;
    }

    private void assignBoolean(String result, String yes, String no, String end) {
        c3d.emitLabel(yes);
        c3d.emitAssign(result, "1");
        c3d.emitGoto(end);
        c3d.emitLabel(no);
        c3d.emitAssign(result, "0");
        c3d.emitLabel(end);
    }

    String emitSwitch(ZetarianoParser.SentenciaSwitchContext ctx, Deque<String> breakLabels) {
        String end = c3d.newLabel();
        String fallback = ctx.defectoSwitch() == null ? end : c3d.newLabel();
        String value = visitor.visit(ctx.expresion());
        List<String> labels = new ArrayList<>();
        for (var branch : ctx.casoSwitch()) {
            String label = c3d.newLabel();
            labels.add(label);
            c3d.emit(QuadrupleOp.IF_EQ, value, visitor.visit(branch.expresion()), label);
        }
        c3d.emitGoto(fallback);
        breakLabels.push(end);
        for (int i = 0; i < ctx.casoSwitch().size(); i++) {
            var branch = ctx.casoSwitch(i);
            c3d.emitLabel(labels.get(i));
            branch.sentencia().forEach(visitor::visit);
            if (branch.BREAK() != null) c3d.emitGoto(end);
        }
        if (ctx.defectoSwitch() != null) {
            c3d.emitLabel(fallback);
            ctx.defectoSwitch().sentencia().forEach(visitor::visit);
        }
        breakLabels.pop();
        c3d.emitLabel(end);
        return null;
    }
}
