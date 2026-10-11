package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.ValueRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.DataType;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class YC3DVisitor extends YBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private final Deque<String> breakLabels = new ArrayDeque<>();
    private final Deque<String> continueLabels = new ArrayDeque<>();
    private final YDeclarationEmitter declarations;
    private final YControlEmitter control;
    private final YAccessEmitter accesses;

    public YC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
        this.declarations = new YDeclarationEmitter(symbolTable, c3d);
        this.control = new YControlEmitter(c3d);
        this.accesses = new YAccessEmitter(symbolTable, c3d);
    }

    @Override
    public String visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);

        symbolTable.enterScope("func_" + funcName, 0, ctx);

        if (ctx.parametros() != null) {
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                Type parameterType = declarations.nameType(paramCtx.tipo_dato().getText());
                if (paramCtx.CORCHETE_IZQ() != null) {
                    parameterType = Type.array(parameterType, 1);
                }
                if (paramCtx.LLAVE_IZQ() != null) {
                    parameterType = Type.structType(paramCtx.tipo_dato().getText());
                }
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                symbolTable.define(new Symbol(paramCtx.ID().getText(), parameterType,
                        SymbolCategory.PARAMETER, offset, false).at(paramCtx).markWorking());
            }
        }

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, funcName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitDeclaracion_variable(YParser.Declaracion_variableContext ctx) {
        declarations.emitDeclaration(this, ctx);
        return null;
    }

    @Override
    public String visitAsignacion(YParser.AsignacionContext ctx) {
        String val = ctx.expresion().isEmpty() ? "0" : visit(ctx.expresion(ctx.expresion().size() - 1));

        if (ctx.acceso_miembro() != null) {
            if (accesses.writeOnAccess(this, ctx.acceso_miembro(), val)) {
                return null;
            }
        }
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(varName);
            if (sym == null) {
                return null;
            }
            c3d.emitAssign(sym.getStorageName(), val);
        }
        return null;
    }

    @Override
    public String visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String valor = accesses.readCell(this, ctx);
        if (valor != null) {
            return valor;
        }

        return ctx.ID(0).getText();
    }

    @Override
    public String visitSi_sentencia(YParser.Si_sentenciaContext ctx) {
        control.emitIf(this, ctx);
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
        control.emitConditionalJump(this, ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitHacer_mientras_sentencia(YParser.Hacer_mientras_sentenciaContext ctx) {
        String bodyLabel = c3d.newLabel();
        String condLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(condLabel);
        // La condición regresa al cuerpo, no a sí misma.
        control.emitConditionalJump(this, ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitPara_sentencia(YParser.Para_sentenciaContext ctx) {
        symbolTable.enterScope("for_" + ctx.getStart().getStartIndex(), 0, ctx);
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String incrementLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(incrementLabel);

        if (ctx.declaracion_variable() != null) {
            visit(ctx.declaracion_variable());
        } else if (ctx.asignacion() != null) {
            visit(ctx.asignacion());
        }

        c3d.emitLabel(condLabel);
        control.emitConditionalJump(this, ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(incrementLabel);
        visit(ctx.incremento_decremento());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitIncremento_decremento(YParser.Incremento_decrementoContext ctx) {
        YParser.Acceso_miembroContext access = ctx.acceso_miembro();
        String name = access != null ? access.ID(0).getText()
                : (ctx.ID() == null ? null : ctx.ID().getText());
        Symbol sym = name == null ? null : symbolTable.resolve(name);
        if (sym == null) {
            return null;
        }
        boolean isArray = access != null;

        String antes = isArray ? accesses.readCell(this, access) : accesses.loadVariable(sym);
        if (antes == null) {
            return null;
        }
        String after = c3d.newTemp();
        c3d.emit(ctx.SUMA_ABREVIADA() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                antes, "1", after);

        if (isArray) {
            accesses.writeOnAccess(this, access, after);
            return null;
        }
        c3d.emitAssign(sym.getStorageName(), after);
        return null;
    }

    @Override
    public String visitElegir_sentencia(YParser.Elegir_sentenciaContext ctx) {
        control.emitSelection(this, ctx, breakLabels, continueLabels);
        return null;
    }

    @Override
    public String visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        c3d.emitReturn(ctx.expresion() == null ? null : visit(ctx.expresion()));
        return null;
    }

    @Override
    public String visitRomper_sentencia(YParser.Romper_sentenciaContext ctx) {
        c3d.emitGotoTop(breakLabels);
        return null;
    }

    @Override
    public String visitContinuar_sentencia(YParser.Continuar_sentenciaContext ctx) {
        c3d.emitGotoTop(continueLabels);
        return null;
    }

    @Override
    public String visitImprimir_sentencia(YParser.Imprimir_sentenciaContext ctx) {
        if (ctx.argumentos() != null) {
            for (YParser.ExpresionContext exprCtx : ctx.argumentos().expresion()) {
                String val = visit(exprCtx);
                Type type = expressionType(exprCtx);
                if (Type.STRING.equals(type)) {
                    c3d.emit(QuadrupleOp.PRINT_STR, val, null, null);
                } else {
                    c3d.emit(Type.DOUBLE.equals(type) ? QuadrupleOp.PRINT_FLOAT
                            : Type.CHAR.equals(type) ? QuadrupleOp.PRINT_CHAR : QuadrupleOp.PRINT_INT, val, null, null);
                }
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    @Override
    public String visitExpresion(YParser.ExpresionContext ctx) {
        if (ctx.operador_negacion() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NOT, visit(ctx.expresion(0)), null, temp);
            return temp;
        }

        if (ctx.operador_logico() != null) {
            return logical(visit(ctx.expresion(0)), ctx.operador_logico().getText(),
                    ctx.expresion(1));
        }

        if (ctx.operador_relacional() != null) {
            String left = visit(ctx.expresion(0));
            String right = visit(ctx.expresion(1));
            String temp = c3d.newTemp();
            String trueLabel = c3d.newLabel();
            String endLabel = c3d.newLabel();

            String op = ctx.operador_relacional().getText();
            QuadrupleOp qOp = control.relationalOp(op);

            String falseLabel = c3d.newLabel();
            c3d.emit(qOp, left, right, trueLabel);
            c3d.emitGoto(falseLabel);
            c3d.emitLabel(trueLabel);
            c3d.emitAssign(temp, "1");
            c3d.emitGoto(endLabel);
            c3d.emitLabel(falseLabel);
            c3d.emitAssign(temp, "0");
            c3d.emitLabel(endLabel);

            return temp;
        }

        if (ctx.sumatoria() != null) {
            return visit(ctx.sumatoria());
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.leer_funcion() != null) {
            return visit(ctx.leer_funcion());
        }

        return "0";
    }

    @Override
    public String visitSumatoria(YParser.SumatoriaContext ctx) {
        String acumulado = visit(ctx.productoria(0));
        Type currentType = expressionType(ctx.productoria(0));
        boolean isText = Type.STRING.equals(currentType);
        for (int i = 1; i < ctx.productoria().size(); i++) {
            YParser.ProductoriaContext next = ctx.productoria(i);
            String op = ctx.operador_aditivo(i - 1).getText();
            Type nextType = expressionType(next);
            boolean concat = "+".equals(op) && (isText || Type.STRING.equals(nextType));
            String right = visit(next);
            if (concat) {
                acumulado = ValueRuntime.asText(c3d, acumulado, currentType);
                right = ValueRuntime.asText(c3d, right, nextType);
            }
            String temp = c3d.newTemp();
            c3d.emit(concat ? QuadrupleOp.CONCAT
                    : "+".equals(op) ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                     acumulado, right, temp);
            acumulado = temp;
            isText = concat;
            currentType = concat ? Type.STRING : com.compi.backend.symbols.TypeCompatibility.checkArithmetic(currentType, nextType, op);
        }
        return acumulado;
    }

    @Override
    public String visitProductoria(YParser.ProductoriaContext ctx) {
        String acumulado = visit(ctx.termino(0));
        Type currentType = expressionType(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            String op = ctx.operador_multiplicativo(i - 1).getText();
            String temp = c3d.newTemp();
            Type nextType = expressionType(ctx.termino(i));
            boolean integerDivision = Type.INT.equals(currentType) && Type.INT.equals(nextType);
            c3d.emit("*".equals(op) ? QuadrupleOp.MUL : integerDivision ? QuadrupleOp.INT_DIV : QuadrupleOp.DIV,
                    acumulado, visit(ctx.termino(i)), temp);
            acumulado = temp;
            currentType = com.compi.backend.symbols.TypeCompatibility.checkArithmetic(currentType, nextType, op);
        }
        return acumulado;
    }

    private boolean isText(YParser.ProductoriaContext productoria) {
        if (productoria.termino().size() != 1) {
            return false;
        }
        YParser.TerminoContext term = productoria.termino(0);
        if (term.CADENA_TEXTO() != null) {
            return true;
        }
        if (term.ID() != null) {
            Symbol s = symbolTable.resolve(term.ID().getText());
            return s != null && s.getType() != null
                    && s.getType().getDataType() == DataType.STRING;
        }
        return false;
    }

    private String logical(String left, String operator,
                          YParser.ExpresionContext rightCtx) {
        String temp = c3d.newTemp();
        String endLabel = c3d.newLabel();
        if ("&&".equals(operator)) {
            c3d.emitAssign(temp, "0");
            c3d.emit(QuadrupleOp.IF_FALSE, left, null, endLabel);
        } else {
            c3d.emitAssign(temp, "1");
            c3d.emit(QuadrupleOp.IF_TRUE, left, null, endLabel);
        }
        c3d.emitAssign(temp, visit(rightCtx));
        c3d.emitLabel(endLabel);
        return temp;
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

    private String charOf(String literal) {
        return String.valueOf((int) literal.charAt(1));
    }

    @Override
    public String visitTermino(YParser.TerminoContext ctx) {
        if (ctx.llamada_funcion() != null) return visit(ctx.llamada_funcion());
        if (ctx.leer_funcion() != null) return visit(ctx.leer_funcion());
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERDADERO() != null) return "1";
        if (ctx.FALSO() != null) return "0";
        if (ctx.CARACTER() != null) return charOf(ctx.CARACTER().getText());
        if (ctx.CADENA_TEXTO() != null) {
            String temp = c3d.newTemp();
            c3d.emitAssign(temp, ctx.CADENA_TEXTO().getText());
            return temp;
        }

        if (ctx.ID() != null) {
            Symbol symbol = symbolTable.resolve(ctx.ID().getText());
            return symbol == null ? ctx.ID().getText() : symbol.getStorageName();
        }

        if (ctx.acceso_miembro() != null) {
            return visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        }

        if (ctx.MENOS() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NEG, visit(ctx.termino()), null, temp);
            return temp;
        }

        return "0";
    }

    private Type expressionType(ParseTree tree) {
        return symbolTable.typeOfSyntax(tree);
    }

    @Override
    public String visitLeer_funcion(YParser.Leer_funcionContext ctx) {
        return declarations.readValue(Type.STRING);
    }
}
