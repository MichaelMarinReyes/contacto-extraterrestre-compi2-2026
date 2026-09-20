package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;

public class PigLatinC3DVisitor extends PigLatinBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;

    public PigLatinC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitVariables(PigLatinParser.VariablesContext ctx) {
        for (PigLatinParser.DeclaracionContext d : ctx.declaracion()) {
            visit(d);
        }
        return null;
    }

    @Override
    public String visitDeclaracion(PigLatinParser.DeclaracionContext ctx) {
        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = visit(ctx.expresion());
            if (s != null) {
                c3d.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), val, null);
            }
        }
        return null;
    }

    @Override
    public String visitMaior(PigLatinParser.MaiorContext ctx) {
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, "main");
        symbolTable.enterScope("maior", 0);

        for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, "main");
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitImprimir_sentencia(PigLatinParser.Imprimir_sentenciaContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String childText = ctx.getChild(i).getText();
            if (">".equals(childText) || ">>".equals(childText) || ";".equals(childText)) {
                continue;
            }

            if (childText.startsWith("\"")) {
                // Cadena literal
                String content = childText.substring(1, childText.length() - 1);
                String strStart = c3d.newTemp();
                c3d.emitAssign(strStart, "H");
                for (int c = 0; c < content.length(); c++) {
                    c3d.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int) content.charAt(c)), null);
                    c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
                }
                c3d.emit(QuadrupleOp.HEAP_SET, "H", "0", null);
                c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
                c3d.emit(QuadrupleOp.PRINT_STR, strStart, null, null);
            } else {
                Symbol s = symbolTable.resolve(childText);
                if (s != null) {
                    String temp = c3d.newTemp();
                    c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                    c3d.emit(QuadrupleOp.PRINT_INT, temp, null, null);
                }
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    @Override
    public String visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx) {
        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = visit(ctx.expresion());
            if (s != null) {
                c3d.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), val, null);
            }
        }
        return null;
    }

    @Override
    public String visitSi_sentencia(PigLatinParser.Si_sentenciaContext ctx) {
        String trueLabel = c3d.newLabel();
        String falseLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        String cond = visit(ctx.condicion());
        c3d.emit(QuadrupleOp.IF_TRUE, cond, null, trueLabel);
        c3d.emitGoto(falseLabel);

        c3d.emitLabel(trueLabel);
        for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }
        c3d.emitGoto(endLabel);

        c3d.emitLabel(falseLabel);
        c3d.emitLabel(endLabel);
        return null;
    }

    @Override
    public String visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.termino() != null && ctx.termino().size() == 1) {
            return visit(ctx.termino(0));
        }

        if (!ctx.operacion_aritmetica().isEmpty()) {
            String current = visit(ctx.termino(0));
            for (int i = 1; i < ctx.termino().size(); i++) {
                String next = visit(ctx.termino(i));
                String op = ctx.operacion_aritmetica(i - 1).getText();
                String temp = c3d.newTemp();
                QuadrupleOp qOp = "+".equals(op) ? QuadrupleOp.ADD :
                        "-".equals(op) ? QuadrupleOp.SUB :
                        "*".equals(op) ? QuadrupleOp.MUL : QuadrupleOp.DIV;
                c3d.emit(qOp, current, next, temp);
                current = temp;
            }
            return current;
        }

        return "0";
    }

    @Override
    public String visitTermino(PigLatinParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";

        if (ctx.VARIABLE() != null) {
            String name = ctx.VARIABLE().getText();
            Symbol s = symbolTable.resolve(name);
            if (s != null) {
                String temp = c3d.newTemp();
                c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                return temp;
            }
            return name;
        }

        if (ctx.llamada_funcion() != null) {
            String funcName = ctx.llamada_funcion().VARIABLE().getText();
            String retTemp = c3d.newTemp();
            c3d.emit(QuadrupleOp.CALL, funcName, "0", retTemp);
            return retTemp;
        }

        return "0";
    }
}
