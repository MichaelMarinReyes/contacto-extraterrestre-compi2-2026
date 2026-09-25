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

    // ===================== Condicionales =====================

    @Override
    public String visitCondicion(PigLatinParser.CondicionContext ctx) {
        if (ctx.conjuncion() != null) {
            return visit(ctx.conjuncion());
        }
        return visit(ctx.condicion());
    }

    @Override
    public String visitConjuncion(PigLatinParser.ConjuncionContext ctx) {
        if (ctx.conjuncion() != null) {
            String acc = visit(ctx.conjuncion());
            String right = visit(ctx.negacion_logica());
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.AND, acc, right, temp);
            return temp;
        }
        return visit(ctx.negacion_logica());
    }

    @Override
    public String visitNegacion_logica(PigLatinParser.Negacion_logicaContext ctx) {
        if (ctx.NEGACION() != null) {
            String inner = visit(ctx.negacion_logica());
            String temp = c3d.newTemp();
            // non x  ->  x == 0
            c3d.emit(QuadrupleOp.IF_EQ, inner, "0", temp);
            return temp;
        }
        return visit(ctx.primaria_logica());
    }

    @Override
    public String visitPrimaria_logica(PigLatinParser.Primaria_logicaContext ctx) {
        if (ctx.expresion().size() >= 2) {
            String left = visit(ctx.expresion(0));
            String right = visit(ctx.expresion(1));
            String symbol = ctx.operador_relacional().getText();
            QuadrupleOp op = switch (symbol) {
                case "==" -> QuadrupleOp.IF_EQ;
                case "!=" -> QuadrupleOp.IF_NE;
                case "<" -> QuadrupleOp.IF_LT;
                case "<=" -> QuadrupleOp.IF_LE;
                case ">" -> QuadrupleOp.IF_GT;
                case ">=" -> QuadrupleOp.IF_GE;
                default -> QuadrupleOp.IF_EQ;
            };
            String temp = c3d.newTemp();
            c3d.emit(op, left, right, temp);
            return temp;
        }
        if (ctx.expresion() != null) {
            return visit(ctx.expresion(0));
        }
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";
        if (ctx.VARIABLE() != null) {
            Symbol s = symbolTable.resolve(ctx.VARIABLE().getText());
            if (s != null) {
                String temp = c3d.newTemp();
                c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                return temp;
            }
        }
        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }
        return "0";
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
