package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.ValueRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

public class PigLatinC3DVisitor extends PigLatinBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private final Deque<String> loopExit = new ArrayDeque<>();
    private final Deque<String> loopContinue = new ArrayDeque<>();
    private final PigLatinConditionEmitter conditions;
    private final PigLatinObjectEmitter objects;

    public PigLatinC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
        this.conditions = new PigLatinConditionEmitter(c3d);
        this.objects = new PigLatinObjectEmitter(symbolTable, c3d);
    }

    @Override
    public String visitVariables(PigLatinParser.VariablesContext ctx) {
        for (PigLatinParser.DeclaracionContext d : ctx.declaracion()) {
            visit(d);
        }
        for (PigLatinParser.Arreglo_declaracionContext a : ctx.arreglo_declaracion()) {
            visit(a);
        }
        return null;
    }

    @Override
    public String visitDeclaracion(PigLatinParser.DeclaracionContext ctx) {
        if (ctx.arreglo_declaracion() != null) {
            return visit(ctx.arreglo_declaracion());
        }
        Symbol target = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (ctx.NOVUS() != null) {
            c3d.emitAssign(target.getStorageName(), objects.newObject(this, ctx.VARIABLE(1).getText(), ctx.argumentos()));
            return null;
        }
        if (ctx.structura_instanciacion() != null) {
            Symbol definition = HeapRuntime.definition(symbolTable, target.getType());
            c3d.emitAssign(target.getStorageName(), objects.initializeStruct(this, definition, ctx.structura_instanciacion()));
            return null;
        }

        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            if (symbolTable.resolve(varName) != null) {
                c3d.emitAssign(target.getStorageName(), visit(ctx.expresion()));
            }
        }

        if (!ctx.VARIABLE().isEmpty() && ctx.condicion() != null) {
            c3d.emitAssign(target.getStorageName(), conditions.booleanValue(this, ctx.condicion()));
        }

        if (!ctx.VARIABLE().isEmpty() && ctx.CADENA_TEXTO() != null) {
            c3d.emitAssign(target.getStorageName(), ctx.CADENA_TEXTO().getText());
        }
        if (!ctx.VARIABLE().isEmpty() && ctx.CARACTER() != null) {
            c3d.emitAssign(target.getStorageName(), charOf(ctx.CARACTER().getText()));
        }
        return null;
    }

    @Override
    public String visitMaior(PigLatinParser.MaiorContext ctx) {
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, "main");
        symbolTable.enterScope("maior", 0, ctx);

        for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, "main");
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitImprimir_sentencia(PigLatinParser.Imprimir_sentenciaContext ctx) {
        for (PigLatinParser.ExpresionContext expression : ctx.expresion()) {
            Type type = symbolTable.typeOfSyntax(expression);
            c3d.emit(printOf(type), visit(expression), null, null);
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    @Override
    public String visitSentencia(PigLatinParser.SentenciaContext ctx) {
        if (ctx.VARIABLE() != null && ctx.acceso_miembro() == null
                && (ctx.SUMA_ABREVIADA() != null || ctx.RESTA_ABREVIADA() != null)) {
            increment(ctx.VARIABLE().getText(), ctx.SUMA_ABREVIADA() != null);
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx) {
        if (ctx.NOVUS() != null) {
            Symbol target = symbolTable.resolve(ctx.VARIABLE(0).getText());
            c3d.emitAssign(target.getStorageName(), objects.newObject(this, ctx.VARIABLE(1).getText(), ctx.argumentos()));
            return null;
        }
        if (ctx.acceso_miembro() != null) {
            if (ctx.expresion() != null) {
                objects.writeElement(this, ctx.acceso_miembro(), visit(ctx.expresion()));
            } else if (ctx.condicion() != null) {
                objects.writeElement(this, ctx.acceso_miembro(), conditions.booleanValue(this, ctx.condicion()));
            }
            return null;
        }
        if (!ctx.VARIABLE().isEmpty() && (ctx.expresion() != null || ctx.condicion() != null)) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = ctx.expresion() != null
                    ? visit(ctx.expresion())
                    : conditions.booleanValue(this, ctx.condicion());
            if (s != null) {
                c3d.emitAssign(s.getStorageName(), val);
            }
        }
        return null;
    }

    @Override
    public String visitSi_sentencia(PigLatinParser.Si_sentenciaContext ctx) {
        String fin = c3d.newLabel();
        conditions.emitStringIf(this, ctx, fin);
        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_dum(PigLatinParser.Ciclo_dumContext ctx) {
        String test = c3d.newLabel();
        String body = c3d.newLabel();
        String fin = c3d.newLabel();

        c3d.emitLabel(test);
        conditions.emitConditionalJump(this, ctx.condicion(), body, fin);

        c3d.emitLabel(body);
        loopExit.push(fin);
        loopContinue.push(test);
        conditions.visitStatements(this, ctx.sentencia());
        loopExit.pop();
        loopContinue.pop();
        c3d.emitGoto(test);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_facere(PigLatinParser.Ciclo_facereContext ctx) {
        String body = c3d.newLabel();
        String test = c3d.newLabel();
        String fin = c3d.newLabel();

        c3d.emitLabel(body);
        loopExit.push(fin);
        loopContinue.push(test);
        conditions.visitStatements(this, ctx.sentencia());
        loopExit.pop();
        loopContinue.pop();

        c3d.emitLabel(test);
        conditions.emitConditionalJump(this, ctx.condicion(), body, fin);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_per(PigLatinParser.Ciclo_perContext ctx) {
        symbolTable.enterScope("for_" + ctx.getStart().getStartIndex(), 0, ctx);
        String test = c3d.newLabel();
        String body = c3d.newLabel();
        String step = c3d.newLabel();
        String fin = c3d.newLabel();

        if (ctx.inicializacion_per() != null) {
            visit(ctx.inicializacion_per());
        }
        c3d.emitLabel(test);
        conditions.emitConditionalJump(this, ctx.condiciones_per().condicion(), body, fin);

        c3d.emitLabel(body);
        loopExit.push(fin);
        loopContinue.push(step);
        conditions.visitStatements(this, ctx.sentencia());
        loopExit.pop();
        loopContinue.pop();

        c3d.emitLabel(step);
        if (ctx.incremento_per() != null) {
            visit(ctx.incremento_per());
        }
        c3d.emitGoto(test);

        c3d.emitLabel(fin);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitInicializacion_per(PigLatinParser.Inicializacion_perContext ctx) {
        if (ctx.VARIABLE() == null || ctx.expresion() == null) {
            return null;
        }
        Symbol s = symbolTable.resolve(ctx.VARIABLE().getText());
        if (s != null) {
            c3d.emitAssign(s.getStorageName(), visit(ctx.expresion()));
        }
        return null;
    }

    @Override
    public String visitIncremento_per(PigLatinParser.Incremento_perContext ctx) {
        if (ctx.VARIABLE() != null
                && (ctx.SUMA_ABREVIADA() != null || ctx.RESTA_ABREVIADA() != null)) {
            increment(ctx.VARIABLE().getText(), ctx.SUMA_ABREVIADA() != null);
            return null;
        }
        if (ctx.VARIABLE() != null && ctx.expresion() != null) {
            Symbol s = symbolTable.resolve(ctx.VARIABLE().getText());
            if (s != null) {
                c3d.emitAssign(s.getStorageName(), visit(ctx.expresion()));
            }
        }
        return null;
    }

    private void increment(String name, boolean sum) {
        if (symbolTable.resolve(name) == null) {
            return;
        }
        String temp = c3d.newTemp();
        String storage = symbolTable.resolve(name).getStorageName();
        c3d.emit(sum ? QuadrupleOp.ADD : QuadrupleOp.SUB, storage, "1", temp);
        c3d.emitAssign(storage, temp);
    }

    @Override
    public String visitLeer_sentencia(PigLatinParser.Leer_sentenciaContext ctx) {
        if (ctx.VARIABLE() != null) {
            Symbol symbol = symbolTable.resolve(ctx.VARIABLE().getText());
            c3d.emit(Type.STRING.equals(symbol.getType()) ? QuadrupleOp.READ_STR
                    : Type.CHAR.equals(symbol.getType()) ? QuadrupleOp.READ_CHAR : QuadrupleOp.READ,
                    symbol.getStorageName(), null, null);
        }
        return null;
    }

    @Override
    public String visitSalto_sentencia(PigLatinParser.Salto_sentenciaContext ctx) {
        if (ctx.PERGE() != null && !loopContinue.isEmpty()) {
            c3d.emitGoto(loopContinue.peek());
        } else if (ctx.INTERRUMPE() != null && !loopExit.isEmpty()) {
            c3d.emitGoto(loopExit.peek());
        }
        return null;
    }


    @Override
    public String visitCondicion(PigLatinParser.CondicionContext ctx) {
        return conditions.booleanValue(this, ctx);
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
            QuadrupleOp op = conditions.relationalOp(symbol);
            String temp = c3d.newTemp();
            c3d.emit(op, left, right, temp);
            return temp;
        }

        if (!ctx.expresion().isEmpty()) {
            return visit(ctx.expresion(0));
        }
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";
        if (ctx.VARIABLE() != null) {
            return symbolTable.resolve(ctx.VARIABLE().getText()).getStorageName();
        }
        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }
        return "0";
    }

    @Override
    public String visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.suma_resta().isEmpty()) {
            return visit(ctx.producto(0));
        }
        String current = visit(ctx.producto(0));
        Type currentType = symbolTable.typeOfSyntax(ctx.producto(0));
        boolean isText = Type.STRING.equals(currentType);
        for (int i = 1; i < ctx.producto().size(); i++) {
            PigLatinParser.ProductoContext factor = ctx.producto(i);
            String next = visit(factor);
            String op = ctx.suma_resta(i - 1).getText();
            String temp = c3d.newTemp();
            Type nextType = symbolTable.typeOfSyntax(factor);
            boolean concat = "+".equals(op) && (isText || Type.STRING.equals(nextType));
            if (concat) {
                current = ValueRuntime.asText(c3d, current, currentType);
                next = ValueRuntime.asText(c3d, next, nextType);
            }
            c3d.emit(concat ? QuadrupleOp.CONCAT
                    : "+".equals(op) ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                    current, next, temp);
            current = temp;
            isText = concat;
            currentType = concat ? Type.STRING : com.compi.backend.symbols.TypeCompatibility.checkArithmetic(currentType, nextType, op);
        }
        return current;
    }

    @Override
    public String visitProducto(PigLatinParser.ProductoContext ctx) {
        if (ctx.multiplicacion().isEmpty()) {
            return visit(ctx.termino(0));
        }
        String current = visit(ctx.termino(0));
        Type currentType = symbolTable.typeOfSyntax(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            String next = visit(ctx.termino(i));
            String op = ctx.multiplicacion(i - 1).getText();
            String temp = c3d.newTemp();
            Type nextType = symbolTable.typeOfSyntax(ctx.termino(i));
            boolean integerDivision = Type.INT.equals(currentType) && Type.INT.equals(nextType);
            c3d.emit("*".equals(op) ? QuadrupleOp.MUL : integerDivision ? QuadrupleOp.INT_DIV : QuadrupleOp.DIV,
                    current, next, temp);
            current = temp;
            currentType = com.compi.backend.symbols.TypeCompatibility.checkArithmetic(currentType, nextType, op);
        }
        return current;
    }

    @Override
    public String visitTermino(PigLatinParser.TerminoContext ctx) {
        if (ctx.NOVUS() != null) return objects.newObject(this, ctx.VARIABLE().getText(), ctx.argumentos());
        if (ctx.expresion() != null) return visit(ctx.expresion());
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";

        if (ctx.CADENA_TEXTO() != null) {
            String temp = c3d.newTemp();
            c3d.emitAssign(temp, ctx.CADENA_TEXTO().getText());
            return temp;
        }
        if (ctx.CARACTER() != null) {
            return charOf(ctx.CARACTER().getText());
        }

        if (ctx.VARIABLE() != null) {
            return symbolTable.resolve(ctx.VARIABLE().getText()).getStorageName();
        }

        if (ctx.acceso_miembro() != null) {
            String element = objects.readElement(this, ctx.acceso_miembro());
            return element != null ? element : ctx.acceso_miembro().VARIABLE(0).getText();
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.MENOS() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NEG, visit(ctx.termino()), null, temp);
            return temp;
        }

        return "0";
    }

    @Override
    public String visitArreglo_declaracion(PigLatinParser.Arreglo_declaracionContext ctx) {
        objects.emitInitializer(this, ctx);
        return null;
    }

    private QuadrupleOp printOf(Type type) {
        if (type != null && type.getDataType() != null) {
            switch (type.getDataType()) {
                case STRING:
                    return QuadrupleOp.PRINT_STR;
                case DOUBLE:
                    return QuadrupleOp.PRINT_FLOAT;
                case CHAR:
                    return QuadrupleOp.PRINT_CHAR;
                default:
                    return QuadrupleOp.PRINT_INT;
            }
        }
        return QuadrupleOp.PRINT_INT;
    }

    private String charOf(String literal) {
        String content = literal.substring(1, literal.length() - 1);
        if (content.isEmpty()) {
            return "0";
        }
        return String.valueOf((int) content.charAt(0));
    }

    @Override
    public String visitAcceso_miembro(PigLatinParser.Acceso_miembroContext ctx) {
        String element = objects.readElement(this, ctx);
        if (element != null) {
            return element;
        }

        return ctx.VARIABLE(0).getText();
    }

    @Override
    public String visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        int argumentos = 0;
        String name = ctx.VARIABLE() == null ? null : ctx.VARIABLE().getText();
        if (ctx.acceso_miembro() != null) {
            PigLatinParser.Acceso_miembroContext access = ctx.acceso_miembro();
            Symbol base = symbolTable.resolve(access.VARIABLE(0).getText());
            Type receiverType = base.getType();
            String receiver = base.getStorageName();
            if (access.getChildCount() > 3) {
                receiver = HeapRuntime.read(c3d, objects.accessAddress(this, access, access.getChildCount() - 2));
                for (int i = 1; i < access.getChildCount() - 2;) {
                    if (".".equals(access.getChild(i).getText())) {
                        receiverType = HeapRuntime.definition(symbolTable, receiverType)
                                .getMember(access.getChild(i + 1).getText()).getType();
                        i += 2;
                    } else {
                        receiverType = receiverType.desindexar(1);
                        i += 3;
                    }
                }
            }
            name = com.compi.backend.c3d.FunctionRuntime.name(receiverType.getCustomTypeName(),
                    access.VARIABLE(access.VARIABLE().size() - 1).getText(), ctx.argumentos() == null ? 0 : ctx.argumentos().expresion().size());
            c3d.emit(QuadrupleOp.PARAM, receiver, null, null);
            argumentos++;
        }
        if (ctx.argumentos() != null) {
            for (PigLatinParser.ExpresionContext arg : ctx.argumentos().expresion()) {
                c3d.emit(QuadrupleOp.PARAM, visit(arg), null, null);
                argumentos++;
            }
        }
        String ret = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, name, String.valueOf(argumentos), ret);
        return ret;
    }
}
