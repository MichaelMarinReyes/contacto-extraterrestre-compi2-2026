package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Emisión de C3D para las condiciones lógicas de PigLatin: saltos con
 * cortocircuito, el valor booleano de una condición y la cadena «si … aliter».
 */
final class PigLatinConditionEmitter {

    private final C3DGenerator c3d;

    PigLatinConditionEmitter(C3DGenerator c3d) {
        this.c3d = c3d;
    }

    void emitStringIf(PigLatinBaseVisitor<String> self,
                      PigLatinParser.Si_sentenciaContext ctx, String fin) {
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        emitConditionalJump(self, ctx.condicion(), trueValue, falseValue);

        c3d.emitLabel(trueValue);
        visitStatements(self, ifStatements(ctx));
        c3d.emitGoto(fin);

        c3d.emitLabel(falseValue);
        List<PigLatinParser.Aliter_bloqueContext> aliteres = ctx.aliter_bloque();
        for (PigLatinParser.Aliter_bloqueContext branch : aliteres) {
            emitBranchWithCondition(self, branch, fin);
        }
        visitStatements(self, elseStatements(ctx));
    }

    void emitBranchWithCondition(PigLatinBaseVisitor<String> self,
                                 PigLatinParser.Aliter_bloqueContext aliter, String fin) {
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        emitConditionalJump(self, aliter.condicion(), trueValue, falseValue);

        c3d.emitLabel(trueValue);
        visitStatements(self, aliter.sentencia());
        c3d.emitGoto(fin);

        c3d.emitLabel(falseValue);
    }

    void emitConditionalJump(PigLatinBaseVisitor<String> self,
                             PigLatinParser.CondicionContext condition,
                             String trueValue, String falseValue) {
        if (condition.condicion() != null) {
            String next = c3d.newLabel();
            emitConditionalJump(self, condition.condicion(), trueValue, next);
            c3d.emitLabel(next);
            emitShortCircuitJump(self, condition.conjuncion(), trueValue, falseValue);
            return;
        }
        emitShortCircuitJump(self, condition.conjuncion(), trueValue, falseValue);
    }

    private void emitShortCircuitJump(PigLatinBaseVisitor<String> self,
                                      PigLatinParser.ConjuncionContext conjunction,
                                      String trueValue, String falseValue) {
        if (conjunction.conjuncion() != null) {
            String next = c3d.newLabel();
            emitShortCircuitJump(self, conjunction.conjuncion(), next, falseValue);
            c3d.emitLabel(next);
            emitNegationJump(self, conjunction.negacion_logica(), trueValue, falseValue);
            return;
        }
        emitNegationJump(self, conjunction.negacion_logica(), trueValue, falseValue);
    }

    private void emitNegationJump(PigLatinBaseVisitor<String> self,
                                  PigLatinParser.Negacion_logicaContext negation,
                                  String trueValue, String falseValue) {
        if (negation.NEGACION() != null) {
            emitNegationJump(self, negation.negacion_logica(), falseValue, trueValue);
            return;
        }
        emitPrimaryJump(self, negation.primaria_logica(), trueValue, falseValue);
    }

    private void emitPrimaryJump(PigLatinBaseVisitor<String> self,
                                 PigLatinParser.Primaria_logicaContext primary,
                                 String trueValue, String falseValue) {
        if (primary.PARENTESIS_IZQ() != null) {
            emitConditionalJump(self, primary.condicion(), trueValue, falseValue);
            return;
        }
        if (primary.expresion().size() == 2 && primary.operador_relacional() != null) {
            c3d.emit(relationalOp(primary.operador_relacional().getText()),
                    self.visit(primary.expresion(0)), self.visit(primary.expresion(1)), trueValue);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, self.visit(primary), null, trueValue);
        }
        if (falseValue != null) {
            c3d.emitGoto(falseValue);
        }
    }

    String booleanValue(PigLatinBaseVisitor<String> self,
                         PigLatinParser.CondicionContext condition) {
        String temp = c3d.newTemp();
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        String fin = c3d.newLabel();
        emitConditionalJump(self, condition, trueValue, falseValue);
        c3d.emitLabel(trueValue);
        c3d.emitAssign(temp, "1");
        c3d.emitGoto(fin);
        c3d.emitLabel(falseValue);
        c3d.emitAssign(temp, "0");
        c3d.emitLabel(fin);
        return temp;
    }

    QuadrupleOp relationalOp(String operator) {
        return switch (operator) {
            case "==" -> QuadrupleOp.IF_EQ;
            case "!=" -> QuadrupleOp.IF_NE;
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> QuadrupleOp.IF_EQ;
        };
    }

    void visitStatements(PigLatinBaseVisitor<String> self,
                         List<PigLatinParser.SentenciaContext> sentencias) {
        for (PigLatinParser.SentenciaContext s : sentencias) {
            self.visit(s);
        }
    }

    private List<PigLatinParser.SentenciaContext> ifStatements(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof PigLatinParser.Aliter_bloqueContext
                    || "aliter".equals(child.getText())) {
                break;
            }
            if (child instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }

    private List<PigLatinParser.SentenciaContext> elseStatements(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        boolean afterElif = false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if ("aliter".equals(child.getText())) {
                afterElif = true;
            }
            if (afterElif && child instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }
}
