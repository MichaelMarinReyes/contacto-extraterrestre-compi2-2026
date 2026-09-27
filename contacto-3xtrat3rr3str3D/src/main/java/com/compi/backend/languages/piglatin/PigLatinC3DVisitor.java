package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.DataType;
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

    public PigLatinC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
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

        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            if (symbolTable.resolve(varName) != null) {
                c3d.emitAssign(varName, visit(ctx.expresion()));
            }
        }

        if (!ctx.VARIABLE().isEmpty() && ctx.CADENA_TEXTO() != null) {
            c3d.emitAssign(ctx.VARIABLE(0).getText(), ctx.CADENA_TEXTO().getText());
        }
        if (!ctx.VARIABLE().isEmpty() && ctx.CARACTER() != null) {
            c3d.emitAssign(ctx.VARIABLE(0).getText(), charOf(ctx.CARACTER().getText()));
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
                String temp = c3d.newTemp();
                c3d.emitAssign(temp, childText);
                c3d.emit(QuadrupleOp.PRINT_STR, temp, null, null);
            } else {
                String valor;
                Type type = null;
                if (ctx.getChild(i) instanceof PigLatinParser.Acceso_miembroContext access) {
                    String element = readElement(access);
                    valor = element != null ? element : access.VARIABLE(0).getText();
                    Symbol s = symbolTable.resolve(access.VARIABLE(0).getText());
                    type = s == null ? null : s.getType();
                } else if (ctx.getChild(i) instanceof PigLatinParser.Llamada_funcionContext call) {
                    valor = visit(call);
                } else {
                    Symbol s = symbolTable.resolve(childText);
                    if (s == null) {
                        continue;
                    }
                    valor = s.getName();
                    type = s.getType();
                }
                c3d.emit(printOf(type), valor, null, null);
            }
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
        if (ctx.acceso_miembro() != null) {
            if (ctx.expresion() != null) {
                writeElement(ctx.acceso_miembro(), visit(ctx.expresion()));
            } else if (ctx.condicion() != null) {
                writeElement(ctx.acceso_miembro(), valorBooleano(ctx.condicion()));
            }
            return null;
        }
        if (!ctx.VARIABLE().isEmpty() && (ctx.expresion() != null || ctx.condicion() != null)) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = ctx.expresion() != null
                    ? visit(ctx.expresion())
                    : valorBooleano(ctx.condicion());
            if (s != null) {
                c3d.emitAssign(s.getName(), val);
            }
        }
        return null;
    }

    @Override
    public String visitSi_sentencia(PigLatinParser.Si_sentenciaContext ctx) {
        String fin = c3d.newLabel();
        emitStringIf(ctx, fin);
        c3d.emitLabel(fin);
        return null;
    }

    private void emitStringIf(PigLatinParser.Si_sentenciaContext ctx, String fin) {
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        emitConditionalJump(ctx.condicion(), trueValue, falseValue);

        c3d.emitLabel(trueValue);
        visitStatements(sentenciasDelSi(ctx));
        c3d.emitGoto(fin);

        c3d.emitLabel(falseValue);
        List<PigLatinParser.Aliter_bloqueContext> aliteres = ctx.aliter_bloque();
        int index = 0;

        if (!aliteres.isEmpty() && aliteres.get(0).condicion() != null) {
            emitBranchWithCondition(aliteres.get(0), fin);
            index = 1;
        }
        for (; index < aliteres.size(); index++) {
            visitStatements(aliteres.get(index).sentencia());
        }
        visitStatements(sentenciasDelAliterFinal(ctx));
    }

    private void emitBranchWithCondition(PigLatinParser.Aliter_bloqueContext aliter, String fin) {
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        emitConditionalJump(aliter.condicion(), trueValue, falseValue);

        c3d.emitLabel(trueValue);
        visitStatements(aliter.sentencia());
        c3d.emitGoto(fin);

        c3d.emitLabel(falseValue);
    }

    private void emitConditionalJump(PigLatinParser.CondicionContext condition,
                                        String trueValue, String falseValue) {
        if (condition.condicion() != null) {
            String next = c3d.newLabel();
            emitConditionalJump(condition.condicion(), trueValue, next);
            c3d.emitLabel(next);
            emitShortCircuitJump(condition.conjuncion(), trueValue, falseValue);
            return;
        }
        emitShortCircuitJump(condition.conjuncion(), trueValue, falseValue);
    }

    private void emitShortCircuitJump(PigLatinParser.ConjuncionContext conjunction,
                                         String trueValue, String falseValue) {
        if (conjunction.conjuncion() != null) {
            String next = c3d.newLabel();
            emitShortCircuitJump(conjunction.conjuncion(), next, falseValue);
            c3d.emitLabel(next);
            emitNegationJump(conjunction.negacion_logica(), trueValue, falseValue);
            return;
        }
        emitNegationJump(conjunction.negacion_logica(), trueValue, falseValue);
    }

    private void emitNegationJump(PigLatinParser.Negacion_logicaContext negation,
                                       String trueValue, String falseValue) {
        if (negation.NEGACION() != null) {
            emitNegationJump(negation.negacion_logica(), falseValue, trueValue);
            return;
        }
        emitPrimaryJump(negation.primaria_logica(), trueValue, falseValue);
    }

    private void emitPrimaryJump(PigLatinParser.Primaria_logicaContext primary,
                                       String trueValue, String falseValue) {
        if (primary.PARENTESIS_IZQ() != null) {
            emitConditionalJump(primary.condicion(), trueValue, falseValue);
            return;
        }
        if (primary.expresion().size() == 2 && primary.operador_relacional() != null) {
            c3d.emit(relacionalDe(primary.operador_relacional().getText()),
                    visit(primary.expresion(0)), visit(primary.expresion(1)), trueValue);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(primary), null, trueValue);
        }
        if (falseValue != null) {
            c3d.emitGoto(falseValue);
        }
    }

    private String valorBooleano(PigLatinParser.CondicionContext condition) {
        String temp = c3d.newTemp();
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        String fin = c3d.newLabel();
        emitConditionalJump(condition, trueValue, falseValue);
        c3d.emitLabel(trueValue);
        c3d.emitAssign(temp, "1");
        c3d.emitGoto(fin);
        c3d.emitLabel(falseValue);
        c3d.emitAssign(temp, "0");
        c3d.emitLabel(fin);
        return temp;
    }

    private QuadrupleOp relacionalDe(String operator) {
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

    private void visitStatements(List<PigLatinParser.SentenciaContext> sentencias) {
        for (PigLatinParser.SentenciaContext s : sentencias) {
            visit(s);
        }
    }

    private List<PigLatinParser.SentenciaContext> sentenciasDelSi(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof PigLatinParser.Aliter_bloqueContext) {
                break;
            }
            if (child instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }

    private List<PigLatinParser.SentenciaContext> sentenciasDelAliterFinal(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        boolean afterElif = false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof PigLatinParser.Aliter_bloqueContext) {
                afterElif = true;
                continue;
            }
            if (afterElif && child instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }


    @Override
    public String visitCiclo_dum(PigLatinParser.Ciclo_dumContext ctx) {
        String test = c3d.newLabel();
        String body = c3d.newLabel();
        String fin = c3d.newLabel();

        c3d.emitLabel(test);
        emitConditionalJump(ctx.condicion(), body, fin);

        c3d.emitLabel(body);
        loopExit.push(fin);
        loopContinue.push(test);
        visitStatements(ctx.sentencia());
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
        visitStatements(ctx.sentencia());
        loopExit.pop();
        loopContinue.pop();

        c3d.emitLabel(test);
        emitConditionalJump(ctx.condicion(), body, fin);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_per(PigLatinParser.Ciclo_perContext ctx) {
        String test = c3d.newLabel();
        String body = c3d.newLabel();
        String step = c3d.newLabel();
        String fin = c3d.newLabel();

        if (ctx.inicializacion_per() != null) {
            visit(ctx.inicializacion_per());
        }
        c3d.emitLabel(test);
        emitConditionalJump(ctx.condiciones_per().condicion(), body, fin);

        c3d.emitLabel(body);
        loopExit.push(fin);
        loopContinue.push(step);
        visitStatements(ctx.sentencia());
        loopExit.pop();
        loopContinue.pop();

        c3d.emitLabel(step);
        if (ctx.incremento_per() != null) {
            visit(ctx.incremento_per());
        }
        c3d.emitGoto(test);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitInicializacion_per(PigLatinParser.Inicializacion_perContext ctx) {
        if (ctx.VARIABLE() == null || ctx.expresion() == null) {
            return null;
        }
        Symbol s = symbolTable.resolve(ctx.VARIABLE().getText());
        if (s != null) {
            c3d.emitAssign(s.getName(), visit(ctx.expresion()));
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
                c3d.emitAssign(s.getName(), visit(ctx.expresion()));
            }
        }
        return null;
    }

    private void increment(String name, boolean sum) {
        if (symbolTable.resolve(name) == null) {
            return;
        }
        String temp = c3d.newTemp();
        c3d.emit(sum ? QuadrupleOp.ADD : QuadrupleOp.SUB, name, "1", temp);
        c3d.emitAssign(name, temp);
    }

    @Override
    public String visitLeer_sentencia(PigLatinParser.Leer_sentenciaContext ctx) {
        if (ctx.VARIABLE() != null) {
            c3d.emit(QuadrupleOp.READ, ctx.VARIABLE().getText(), null, null);
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
        return valorBooleano(ctx);
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
            QuadrupleOp op = relacionalDe(symbol);
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
            return ctx.VARIABLE().getText();
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
        boolean isText = isText(ctx.producto(0));
        for (int i = 1; i < ctx.producto().size(); i++) {
            PigLatinParser.ProductoContext factor = ctx.producto(i);
            String next = visit(factor);
            String op = ctx.suma_resta(i - 1).getText();
            String temp = c3d.newTemp();
            boolean concat = "+".equals(op) && (isText || isText(factor));
            c3d.emit(concat ? QuadrupleOp.CONCAT
                    : "+".equals(op) ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                    current, next, temp);
            current = temp;
            isText = concat;
        }
        return current;
    }

    @Override
    public String visitProducto(PigLatinParser.ProductoContext ctx) {
        if (ctx.multiplicacion().isEmpty()) {
            return visit(ctx.termino(0));
        }
        String current = visit(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            String next = visit(ctx.termino(i));
            String op = ctx.multiplicacion(i - 1).getText();
            String temp = c3d.newTemp();
            c3d.emit("*".equals(op) ? QuadrupleOp.MUL : QuadrupleOp.DIV,
                    current, next, temp);
            current = temp;
        }
        return current;
    }

    private boolean isText(PigLatinParser.ProductoContext product) {
        if (product.termino().size() != 1) {
            return false;
        }
        PigLatinParser.TerminoContext term = product.termino(0);
        if (term.CADENA_TEXTO() != null) {
            return true;
        }
        if (term.VARIABLE() != null) {
            Symbol s = symbolTable.resolve(term.VARIABLE().getText());
            return s != null && s.getType() != null
                    && s.getType().getDataType() == DataType.STRING;
        }
        return false;
    }


    @Override
    public String visitTermino(PigLatinParser.TerminoContext ctx) {
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
            return ctx.VARIABLE().getText();
        }

        if (ctx.acceso_miembro() != null) {
            String element = readElement(ctx.acceso_miembro());
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
        Symbol s = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (s == null || s.getType() == null || !s.getType().isArray()) {
            return null;
        }
        Type type = s.getType();
        if (type.totalSize() <= 0) {
            return null;
        }
        if (ctx.elemento_arreglo() != null) {
            int[] sizes = type.getSizes();
            fill(s.getName(), ctx.elemento_arreglo(), sizes, new int[sizes.length], 0);
        }
        return null;
    }

    private void fill(String name, PigLatinParser.Elemento_arregloContext ctx, int[] sizes,
                        int[] indices, int dimension) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        for (int i = 0; i < valores.size(); i++) {
            PigLatinParser.Elemento_arreglo_valorContext valor = valores.get(i);
            if (dimension >= indices.length) {
                break;
            }
            indices[dimension] = i;
            if (valor.LLAVE_IZQ() != null) {
                if (valor.elemento_arreglo() != null) {
                    fill(name, valor.elemento_arreglo(), sizes, indices, dimension + 1);
                }
            } else {
                String data = visit(valor.expresion());
                c3d.emit(QuadrupleOp.ARRAY_SET, name,
                        String.valueOf(ArrayRuntime.offset(indices, sizes)), data);
            }
        }
    }

    private String readElement(PigLatinParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return null;
        }
        int[] sizes = base.getType().getSizes();
        if (!base.getType().hasSizes() || ctx.CORCHETE_IZQ().size() != sizes.length) {
            return null;
        }
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.ARRAY_GET, base.getName(),
                flatIndex(indicesDe(ctx), sizes), valor);
        return valor;
    }

    private void writeElement(PigLatinParser.Acceso_miembroContext ctx, String valor) {
        Symbol base = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return;
        }
        int[] sizes = base.getType().getSizes();
        if (!base.getType().hasSizes() || ctx.CORCHETE_IZQ().size() != sizes.length) {
            return;
        }
        c3d.emit(QuadrupleOp.ARRAY_SET, base.getName(),
                flatIndex(indicesDe(ctx), sizes), valor);
    }

    private String flatIndex(List<String> indices, int[] sizes) {
        String acumulado = null;
        for (int d = 0; d < indices.size(); d++) {
            String term = indices.get(d);
            int step = ArrayRuntime.productFrom(sizes, d + 1);
            if (step != 1) {
                String scaled = c3d.newTemp();
                c3d.emit(QuadrupleOp.MUL, term, String.valueOf(step), scaled);
                term = scaled;
            }
            if (acumulado == null) {
                acumulado = term;
            } else {
                String sum = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, acumulado, term, sum);
                acumulado = sum;
            }
        }
        return acumulado == null ? "0" : acumulado;
    }

    private String leerVariable(Symbol s) {
        return s.getName();
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

    private List<String> indicesDe(PigLatinParser.Acceso_miembroContext ctx) {
        List<String> indices = new ArrayList<>();
        for (int i = 1; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof PigLatinParser.ExpresionContext) {
                indices.add(visit(ctx.getChild(i)));
            }
        }
        return indices;
    }


    @Override
    public String visitAcceso_miembro(PigLatinParser.Acceso_miembroContext ctx) {
        String element = readElement(ctx);
        if (element != null) {
            return element;
        }

        return ctx.VARIABLE(0).getText();
    }

    @Override
    public String visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        int argumentos = 0;
        if (ctx.argumentos() != null) {
            for (PigLatinParser.ExpresionContext arg : ctx.argumentos().expresion()) {
                c3d.emit(QuadrupleOp.PARAM, visit(arg), null, null);
                argumentos++;
            }
        }
        String ret = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, callName(ctx), String.valueOf(argumentos), ret);
        return ret;
    }

    private String callName(PigLatinParser.Llamada_funcionContext ctx) {
        if (ctx.acceso_miembro() == null) {
            return ctx.VARIABLE().getText();
        }
        PigLatinParser.Acceso_miembroContext access = ctx.acceso_miembro();
        if (access.getChildCount() == 3 && ".".equals(access.getChild(1).getText())) {
            Symbol receptor = symbolTable.resolve(access.VARIABLE(0).getText());
            if (receptor != null && receptor.getType() != null
                    && receptor.getType().getCustomTypeName() != null) {
                return receptor.getType().getCustomTypeName() + "_"
                        + access.VARIABLE(1).getText();
            }
        }
        StringBuilder name = new StringBuilder(access.VARIABLE(0).getText());
        for (int i = 1; i < access.getChildCount(); i++) {
            String text = access.getChild(i).getText();
            if ("[".equals(text)) {
                name.append('_');
            } else if (")".equals(text) || "]".equals(text) || ",".equals(text)) {
                continue;
            } else if (!".".equals(text)) {
                name.append('_').append(text);
            }
        }
        return name.toString();
    }
}
