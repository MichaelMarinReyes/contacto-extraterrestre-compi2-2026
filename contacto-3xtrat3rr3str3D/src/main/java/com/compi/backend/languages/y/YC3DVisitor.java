package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;

import java.util.Stack;

public class YC3DVisitor extends YBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private final Stack<String> breakLabels = new Stack<>();
    private final Stack<String> continueLabels = new Stack<>();

    public YC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);

        symbolTable.enterScope("func_" + funcName, 0);

        // Si la función tiene parámetros, se leen desde el stack relativo a P
        if (ctx.parametros() != null) {
            int paramIndex = 0;
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                String paramName = paramCtx.ID().getText();
                Symbol paramSymbol = symbolTable.resolve(paramName);
                if (paramSymbol != null) {
                    String paramTemp = c3d.newTemp();
                    String stackPosTemp = c3d.newTemp();
                    c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(paramIndex), stackPosTemp);
                    c3d.emit(QuadrupleOp.STACK_GET, stackPosTemp, null, paramTemp);
                }
                paramIndex++;
            }
        }

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, funcName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitDeclaracion_variable(YParser.Declaracion_variableContext ctx) {
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "bool";
        Symbol sym = symbolTable.resolve(varName);

        if (ctx.expresion_inicializacion() != null && ctx.expresion_inicializacion().expresion() != null) {
            String val = visit(ctx.expresion_inicializacion().expresion());
            if (sym != null) {
                String targetPos = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), targetPos);
                c3d.emit(QuadrupleOp.STACK_SET, targetPos, val, null);
            }
        }
        return null;
    }

    @Override
    public String visitAsignacion(YParser.AsignacionContext ctx) {
        String val = ctx.expresion().isEmpty() ? "0" : visit(ctx.expresion(ctx.expresion().size() - 1));
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(varName);
            if (sym != null) {
                String targetPos = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), targetPos);
                c3d.emit(QuadrupleOp.STACK_SET, targetPos, val, null);
            }
        }
        return null;
    }

    @Override
    public String visitSi_sentencia(YParser.Si_sentenciaContext ctx) {
        String cond = visit(ctx.condicion());
        String trueLabel = c3d.newLabel();
        String falseLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        c3d.emit(QuadrupleOp.IF_TRUE, cond, null, trueLabel);
        c3d.emitGoto(falseLabel);

        c3d.emitLabel(trueLabel);
        visit(ctx.bloque());
        c3d.emitGoto(endLabel);

        c3d.emitLabel(falseLabel);
        if (ctx.contrario_bloque() != null) {
            visit(ctx.contrario_bloque().bloque());
        }
        c3d.emitLabel(endLabel);

        return null;
    }

    @Override
    public String visitMientras_sentencia(YParser.Mientras_sentenciaContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(condLabel);
        String cond = visit(ctx.condicion());
        c3d.emit(QuadrupleOp.IF_TRUE, cond, null, bodyLabel);
        c3d.emitGoto(endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        if (ctx.expresion() != null) {
            String val = visit(ctx.expresion());
            // El valor de retorno se coloca convencionalmente en stack[P]
            c3d.emit(QuadrupleOp.STACK_SET, "P", val, null);
        }
        c3d.emit(QuadrupleOp.RETURN, null, null, null);
        return null;
    }

    @Override
    public String visitRomper_sentencia(YParser.Romper_sentenciaContext ctx) {
        if (!breakLabels.isEmpty()) {
            c3d.emitGoto(breakLabels.peek());
        }
        return null;
    }

    @Override
    public String visitContinuar_sentencia(YParser.Continuar_sentenciaContext ctx) {
        if (!continueLabels.isEmpty()) {
            c3d.emitGoto(continueLabels.peek());
        }
        return null;
    }

    @Override
    public String visitImprimir_sentencia(YParser.Imprimir_sentenciaContext ctx) {
        if (ctx.argumentos() != null) {
            for (YParser.ExpresionContext exprCtx : ctx.argumentos().expresion()) {
                String val = visit(exprCtx);
                c3d.emit(QuadrupleOp.PRINT_INT, val, null, null);
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    @Override
    public String visitExpresion(YParser.ExpresionContext ctx) {
        if (!ctx.termino().isEmpty()) {
            return visit(ctx.termino(0));
        }

        if (!ctx.operador_aritmetico().isEmpty()) {
            String left = visit(ctx.expresion(0));
            String right = visit(ctx.expresion(1));
            String temp = c3d.newTemp();
            String op = ctx.operador_aritmetico(0).getText();
            QuadrupleOp qOp = "+".equals(op) ? QuadrupleOp.ADD :
                    "-".equals(op) ? QuadrupleOp.SUB :
                    "*".equals(op) ? QuadrupleOp.MUL : QuadrupleOp.DIV;
            c3d.emit(qOp, left, right, temp);
            return temp;
        }

        if (ctx.operador_relacional() != null) {
            String left = visit(ctx.expresion(0));
            String right = visit(ctx.expresion(1));
            String temp = c3d.newTemp();
            String trueLabel = c3d.newLabel();
            String endLabel = c3d.newLabel();

            String op = ctx.operador_relacional().getText();
            QuadrupleOp qOp = "==".equals(op) ? QuadrupleOp.IF_EQ :
                    "!=".equals(op) ? QuadrupleOp.IF_NE :
                    "<".equals(op) ? QuadrupleOp.IF_LT :
                    ">".equals(op) ? QuadrupleOp.IF_GT : QuadrupleOp.IF_GE;

            c3d.emit(qOp, left, right, trueLabel);
            c3d.emitAssign(temp, "0");
            c3d.emitGoto(endLabel);
            c3d.emitLabel(trueLabel);
            c3d.emitAssign(temp, "1");
            c3d.emitLabel(endLabel);

            return temp;
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        return "0";
    }

    @Override
    public String visitLlamada_funcion(YParser.Llamada_funcionContext ctx) {
        String funcName = ctx.ID().getText();
        if (ctx.argumentos() != null) {
            for (YParser.ExpresionContext argCtx : ctx.argumentos().expresion()) {
                String argVal = visit(argCtx);
                c3d.emit(QuadrupleOp.PARAM, argVal, null, null);
            }
        }
        String retTemp = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, funcName, String.valueOf(ctx.argumentos() != null ? ctx.argumentos().expresion().size() : 0), retTemp);
        return retTemp;
    }

    @Override
    public String visitTermino(YParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERDADERO() != null) return "1";
        if (ctx.FALSO() != null) return "0";

        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(name);
            if (sym != null) {
                String temp = c3d.newTemp();
                String posTemp = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), posTemp);
                c3d.emit(QuadrupleOp.STACK_GET, posTemp, null, temp);
                return temp;
            }
            return name;
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        }

        return "0";
    }
}
