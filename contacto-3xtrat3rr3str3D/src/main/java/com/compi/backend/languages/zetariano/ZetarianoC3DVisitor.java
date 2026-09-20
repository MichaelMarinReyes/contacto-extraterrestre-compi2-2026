package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;

import java.util.Stack;

public class ZetarianoC3DVisitor extends ZetarianoBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private String currentClassName = "";
    private final Stack<String> breakLabels = new Stack<>();
    private final Stack<String> continueLabels = new Stack<>();

    public ZetarianoC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitClaseDef(ZetarianoParser.ClaseDefContext ctx) {
        currentClassName = ctx.ID().getText();
        symbolTable.enterScope("class_" + currentClassName, 0);

        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            visit(mc);
        }

        symbolTable.exitScope();
        currentClassName = "";
        return null;
    }

    @Override
    public String visitConstructorDef(ZetarianoParser.ConstructorDefContext ctx) {
        String funcName = currentClassName + "_" + ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);
        symbolTable.enterScope("constructor_" + funcName, 1);

        // Stack[P] contiene el puntero 'this' hacia el objeto en el Heap
        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, funcName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitMetodoDef(ZetarianoParser.MetodoDefContext ctx) {
        String methodName = currentClassName + "_" + ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, methodName);
        symbolTable.enterScope("method_" + methodName, 1);

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, methodName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitDeclaracionVariable(ZetarianoParser.DeclaracionVariableContext ctx) {
        String varName = ctx.ID().getText();
        Symbol s = symbolTable.resolve(varName);

        if (ctx.expresion() != null) {
            String val = visit(ctx.expresion());
            if (s != null) {
                String pos = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(s.getOffset()), pos);
                c3d.emit(QuadrupleOp.STACK_SET, pos, val, null);
            }
        }
        return null;
    }

    @Override
    public String visitAsignacion(ZetarianoParser.AsignacionContext ctx) {
        String val = visit(ctx.expresion(ctx.expresion().size() - 1));
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Symbol s = symbolTable.resolve(varName);
            if (s != null) {
                String pos = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(s.getOffset()), pos);
                c3d.emit(QuadrupleOp.STACK_SET, pos, val, null);
            }
        } else if (ctx.accesoMiembro() != null) {
            // Asignación a miembro de objeto en heap: obj.prop = val
            String baseObj = ctx.accesoMiembro().ID().getText();
            Symbol baseSym = symbolTable.resolve(baseObj);
            if (baseSym != null && ctx.accesoMiembro().miembroAcceso(0).ID() != null) {
                String memberName = ctx.accesoMiembro().miembroAcceso(0).ID().getText();
                Symbol cls = symbolTable.getClass(baseSym.getType().getCustomTypeName());
                if (cls != null) {
                    Symbol field = cls.getMember(memberName);
                    if (field != null) {
                        String heapPtr = c3d.newTemp();
                        String basePos = c3d.newTemp();
                        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(baseSym.getOffset()), basePos);
                        c3d.emit(QuadrupleOp.STACK_GET, basePos, null, heapPtr);

                        String fieldHeapPos = c3d.newTemp();
                        c3d.emit(QuadrupleOp.ADD, heapPtr, String.valueOf(field.getOffset()), fieldHeapPos);
                        c3d.emit(QuadrupleOp.HEAP_SET, fieldHeapPos, val, null);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String visitSentenciaIf(ZetarianoParser.SentenciaIfContext ctx) {
        String cond = visit(ctx.expresion(0));
        String trueLabel = c3d.newLabel();
        String falseLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        c3d.emit(QuadrupleOp.IF_TRUE, cond, null, trueLabel);
        c3d.emitGoto(falseLabel);

        c3d.emitLabel(trueLabel);
        visit(ctx.bloque(0));
        c3d.emitGoto(endLabel);

        c3d.emitLabel(falseLabel);
        if (ctx.ELSE() != null) {
            visit(ctx.bloque(ctx.bloque().size() - 1));
        }
        c3d.emitLabel(endLabel);
        return null;
    }

    @Override
    public String visitSentenciaWhile(ZetarianoParser.SentenciaWhileContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(condLabel);
        String cond = visit(ctx.expresion());
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
    public String visitRetorno(ZetarianoParser.RetornoContext ctx) {
        if (ctx.expresion() != null) {
            String val = visit(ctx.expresion());
            c3d.emit(QuadrupleOp.STACK_SET, "P", val, null);
        }
        c3d.emit(QuadrupleOp.RETURN, null, null, null);
        return null;
    }

    @Override
    public String visitRomper(ZetarianoParser.RomperContext ctx) {
        if (!breakLabels.isEmpty()) {
            c3d.emitGoto(breakLabels.peek());
        }
        return null;
    }

    @Override
    public String visitContinuar(ZetarianoParser.ContinuarContext ctx) {
        if (!continueLabels.isEmpty()) {
            c3d.emitGoto(continueLabels.peek());
        }
        return null;
    }

    @Override
    public String visitLlamadaFuncionSemilla(ZetarianoParser.LlamadaFuncionSemillaContext ctx) {
        if (ctx.PRINTLN() != null || ctx.PRINT() != null) {
            if (ctx.argumentos() != null) {
                for (ZetarianoParser.ExpresionContext eCtx : ctx.argumentos().expresion()) {
                    String val = visit(eCtx);
                    c3d.emit(QuadrupleOp.PRINT_STR, val, null, null);
                }
            }
            if (ctx.PRINTLN() != null) {
                c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
            }
            return null;
        }
        return null;
    }

    @Override
    public String visitNewObjectExpr(ZetarianoParser.NewObjectExprContext ctx) {
        String className = ctx.ID().getText();
        Symbol cls = symbolTable.getClass(className);
        int fieldCount = (cls != null) ? cls.getMembers().size() : 2;

        // Reservar memoria en Heap: t_heap = H; H = H + size;
        String heapStart = c3d.newTemp();
        c3d.emitAssign(heapStart, "H");
        c3d.emit(QuadrupleOp.ADD, "H", String.valueOf(fieldCount), "H");

        // Llamar constructor pasando la dirección en Heap como parámetro 0
        String constructorName = className + "_" + className;
        c3d.emit(QuadrupleOp.PARAM, heapStart, null, null);
        if (ctx.argumentos() != null) {
            for (ZetarianoParser.ExpresionContext argCtx : ctx.argumentos().expresion()) {
                String argVal = visit(argCtx);
                c3d.emit(QuadrupleOp.PARAM, argVal, null, null);
            }
        }
        c3d.emit(QuadrupleOp.CALL, constructorName, String.valueOf(1 + (ctx.argumentos() != null ? ctx.argumentos().expresion().size() : 0)), null);

        return heapStart;
    }

    @Override
    public String visitAddSubExpr(ZetarianoParser.AddSubExprContext ctx) {
        String left = visit(ctx.expresion(0));
        String right = visit(ctx.expresion(1));
        String temp = c3d.newTemp();
        c3d.emit(ctx.PLUS() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB, left, right, temp);
        return temp;
    }

    @Override
    public String visitMulDivModExpr(ZetarianoParser.MulDivModExprContext ctx) {
        String left = visit(ctx.expresion(0));
        String right = visit(ctx.expresion(1));
        String temp = c3d.newTemp();
        QuadrupleOp op = ctx.MUL() != null ? QuadrupleOp.MUL : (ctx.DIV() != null ? QuadrupleOp.DIV : QuadrupleOp.MOD);
        c3d.emit(op, left, right, temp);
        return temp;
    }

    @Override
    public String visitIntLiteralExpr(ZetarianoParser.IntLiteralExprContext ctx) {
        return ctx.LITERAL_ENTERO().getText();
    }

    @Override
    public String visitDoubleLiteralExpr(ZetarianoParser.DoubleLiteralExprContext ctx) {
        return ctx.LITERAL_DECIMAL().getText();
    }

    @Override
    public String visitStringLiteralExpr(ZetarianoParser.StringLiteralExprContext ctx) {
        String raw = ctx.CADENA_TEXTO().getText();
        String content = raw.substring(1, raw.length() - 1);

        // Guardar caracteres de la cadena en el Heap terminando en 0
        String strStart = c3d.newTemp();
        c3d.emitAssign(strStart, "H");

        for (int i = 0; i < content.length(); i++) {
            c3d.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int) content.charAt(i)), null);
            c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
        }
        c3d.emit(QuadrupleOp.HEAP_SET, "H", "0", null);
        c3d.emit(QuadrupleOp.ADD, "H", "1", "H");

        return strStart;
    }

    @Override
    public String visitIdExpr(ZetarianoParser.IdExprContext ctx) {
        String name = ctx.ID().getText();
        Symbol s = symbolTable.resolve(name);
        if (s != null) {
            String temp = c3d.newTemp();
            String pos = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(s.getOffset()), pos);
            c3d.emit(QuadrupleOp.STACK_GET, pos, null, temp);
            return temp;
        }
        return name;
    }

    @Override
    public String visitTrueExpr(ZetarianoParser.TrueExprContext ctx) {
        return "1";
    }

    @Override
    public String visitFalseExpr(ZetarianoParser.FalseExprContext ctx) {
        return "0";
    }

    @Override
    public String visitNullExpr(ZetarianoParser.NullExprContext ctx) {
        return "-1";
    }
}
