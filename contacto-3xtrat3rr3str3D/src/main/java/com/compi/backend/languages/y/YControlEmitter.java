package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Emisión de C3D para el control de flujo de Y: la cadena «si … sino si … sino»,
 * el salto condicional con cortocircuito y la estructura «elegir».
 */
final class YControlEmitter {

    private final C3DGenerator c3d;

    YControlEmitter(C3DGenerator c3d) {
        this.c3d = c3d;
    }

    void emitIf(YBaseVisitor<String> self, YParser.Si_sentenciaContext ctx) {
        List<YParser.CondicionContext> condiciones = new ArrayList<>();
        List<YParser.BloqueContext> bloques = new ArrayList<>();

        condiciones.add(ctx.condicion());
        bloques.add(ctx.bloque());
        for (YParser.Sino_si_bloqueContext elseIf : ctx.sino_si_bloque()) {
            condiciones.add(elseIf.condicion());
            bloques.add(elseIf.bloque());
        }

        YParser.Sino_bloqueContext sinSi = ctx.sino_bloque();
        if (sinSi != null && sinSi.condicion() != null) {
            condiciones.add(sinSi.condicion());
            bloques.add(sinSi.bloque());
            sinSi = null;
        }
        YParser.BloqueContext opposite =
                ctx.contrario_bloque() == null ? null : ctx.contrario_bloque().bloque();

        String endLabel = c3d.newLabel();
        for (int i = 0; i < condiciones.size(); i++) {
            boolean last = i == condiciones.size() - 1;
            String trueLabel = c3d.newLabel();
            boolean alFinal = last && sinSi == null && opposite == null;
            String falseLabel = alFinal ? endLabel : c3d.newLabel();

            emitConditionalJump(self, condiciones.get(i), trueLabel, falseLabel);
            c3d.emitLabel(trueLabel);
            self.visit(bloques.get(i));
            c3d.emitGoto(endLabel);
            if (!alFinal) {
                c3d.emitLabel(falseLabel);
            }
        }

        if (sinSi != null) {
            self.visit(sinSi.bloque());
            c3d.emitGoto(endLabel);
        }
        if (opposite != null) {
            self.visit(opposite);
        }
        c3d.emitLabel(endLabel);
    }

    void emitConditionalJump(YBaseVisitor<String> self, YParser.CondicionContext condition,
                             String trueValue, String falseValue) {
        emitExpressionJump(self, condition.expresion(), trueValue, falseValue);
    }

    private void emitExpressionJump(YBaseVisitor<String> self, YParser.ExpresionContext expression,
                                    String trueValue, String falseValue) {
        if (expression.operador_negacion() != null) {
            emitExpressionJump(self, expression.expresion(0), falseValue, trueValue);
            return;
        }
        if (expression.operador_logico() != null && expression.expresion().size() == 2) {
            boolean esY = "&&".equals(expression.operador_logico().getText());
            String next = c3d.newLabel();
            if (esY) {
                emitExpressionJump(self, expression.expresion(0), next, falseValue);
            } else {
                emitExpressionJump(self, expression.expresion(0), trueValue, next);
            }
            c3d.emitLabel(next);
            emitExpressionJump(self, expression.expresion(1), trueValue, falseValue);
            return;
        }
        if (expression.operador_relacional() != null && expression.expresion().size() == 2) {
            c3d.emit(relationalOp(expression.operador_relacional().getText()),
                    self.visit(expression.expresion(0)), self.visit(expression.expresion(1)), trueValue);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, self.visit(expression), null, trueValue);
        }
        if (falseValue != null) {
            c3d.emitGoto(falseValue);
        }
    }

    QuadrupleOp relationalOp(String operator) {
        if ("==".equals(operator)) {
            return QuadrupleOp.IF_EQ;
        }
        if ("!=".equals(operator)) {
            return QuadrupleOp.IF_NE;
        }
        if ("<".equals(operator)) {
            return QuadrupleOp.IF_LT;
        }
        if (">".equals(operator)) {
            return QuadrupleOp.IF_GT;
        }
        if ("<=".equals(operator)) {
            return QuadrupleOp.IF_LE;
        }
        if (">=".equals(operator)) {
            return QuadrupleOp.IF_GE;
        }
        return QuadrupleOp.IF_EQ;
    }

    void emitSelection(YBaseVisitor<String> self, YParser.Elegir_sentenciaContext ctx,
                      Deque<String> breakLabels, Deque<String> continueLabels) {
        String endLabel = c3d.newLabel();
        YParser.Siempre_bloqueContext always = ctx.siempre_bloque();
        String defaultLabel = always != null ? c3d.newLabel() : endLabel;

        breakLabels.push(endLabel);
        boolean selectionContinue = continueLabels.isEmpty();
        if (selectionContinue) {
            continueLabels.push(endLabel);
        }

        String valor = self.visit(ctx.expresion());
        List<YParser.Caso_bloqueContext> casos = ctx.caso_bloque();
        List<String> etiquetas = new ArrayList<>();
        for (YParser.Caso_bloqueContext caseOf : casos) {
            String label = c3d.newLabel();
            etiquetas.add(label);
            c3d.emit(QuadrupleOp.IF_EQ, valor, self.visit(caseOf.expresion()), label);
        }
        c3d.emitGoto(defaultLabel);

        for (int i = 0; i < casos.size(); i++) {
            c3d.emitLabel(etiquetas.get(i));
            YParser.Caso_bloqueContext caseOf = casos.get(i);
            self.visit(caseOf.bloque_interno_opcional());

            if (caseOf.ROMPER() == null && !terminaConRomper(caseOf.bloque_interno_opcional())) {
                c3d.emitGoto(endLabel);
            }
        }
        if (always != null) {
            c3d.emitLabel(defaultLabel);
            self.visit(always.bloque_interno_opcional());
        }
        c3d.emitLabel(endLabel);

        breakLabels.pop();
        if (selectionContinue) {
            continueLabels.pop();
        }
    }

    private boolean terminaConRomper(YParser.Bloque_interno_opcionalContext body) {
        List<YParser.SentenciaContext> sentencias = body.sentencia();

        for (int i = sentencias.size() - 1; i >= 0; i--) {
            YParser.SentenciaContext statement = sentencias.get(i);
            if (statement.getChildCount() == 1
                    && statement.getChild(0) instanceof TerminalNode) {
                continue;
            }
            return statement.romper_sentencia() != null;
        }
        return false;
    }
}
