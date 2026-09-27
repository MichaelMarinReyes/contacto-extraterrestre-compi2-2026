package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.ArrayRuntime;
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

    public YC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);

        symbolTable.enterScope("func_" + funcName, 0);

        if (ctx.parametros() != null) {
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                Type parameterType = nameType(paramCtx.tipo_dato().getText());
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
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "bool";
        String typeName = ctx.tipo_dato() != null ? ctx.tipo_dato().getText()
                : (ctx.ID().size() >= 2 ? ctx.ID(0).getText() : null);
        int[] sizes = new int[ctx.CORCHETE_IZQ().size()];
        List<TerminalNode> numeros = ctx.NUMERO_ENTERO();
        for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
            sizes[i] = Integer.parseInt(numeros.get(i).getText());
        }
        Type base = typeName == null ? Type.UNKNOWN : nameType(typeName);
        Type type = sizes.length == 0 ? base : Type.arrayOf(base, sizes);

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declared = new Symbol(varName, type, SymbolCategory.VARIABLE,
                offset, false).at(ctx).markWorking();
        symbolTable.define(declared);
        Symbol sym = symbolTable.resolve(varName);
        if (sym == null) {
            sym = declared;
        }

        Symbol struct = typeName == null ? null : symbolTable.getStruct(typeName);
        if (struct != null && sizes.length == 0) {
            return declareStruct(sym, struct, ctx.expresion_inicializacion());
        }

        if (type.isArray()) {
            return declareArray(sym, type, ctx.expresion_inicializacion());
        }

        if (ctx.expresion_inicializacion() != null && ctx.expresion_inicializacion().expresion() != null) {
            c3d.emitAssign(sym.getName(), visit(ctx.expresion_inicializacion().expresion()));
        }
        return null;
    }

    private Type nameType(String name) {
        return switch (name.toLowerCase()) {
            case "entero" -> Type.INT;
            case "flotante" -> Type.DOUBLE;
            case "cadena" -> Type.STRING;
            case "caracter" -> Type.CHAR;
            case "bool" -> Type.BOOLEAN;
            default -> Type.structType(name);
        };
    }

    private String declareStruct(Symbol sym, Symbol struct,
                                      YParser.Expresion_inicializacionContext inicializador) {
        String base = ArrayRuntime.allocate(c3d, celdasDe(struct));
        c3d.emitAssign(sym.getName(), base);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            fillFields(base, inicializador.elemento_lista(), struct, 0);
        } else if (inicializador != null && inicializador.expresion() != null) {
            c3d.emitAssign(sym.getName(), visit(inicializador.expresion()));
        }
        return null;
    }

    private int celdasDe(Type type) {
        if (type != null && type.isArray()) {
            return Math.max(type.totalSize(), 1);
        }
        return 1;
    }

    private int celdasDe(Symbol struct) {
        int total = 0;
        for (Symbol field : struct.getMembers()) {
            total += celdasDe(field.getType());
        }
        return Math.max(total, 1);
    }

    private void fillFields(String base, List<YParser.Elemento_listaContext> elementos,
                              Symbol struct, int cell) {
        int i = 0;
        for (Symbol field : struct.getMembers()) {
            if (i >= elementos.size()) {
                return;
            }
            YParser.Elemento_listaContext element = elementos.get(i);
            Type fieldType = field.getType();
            String start = ArrayRuntime.add(c3d, base, cell);

            if (element.LLAVE_IZQ() != null && fieldType != null && fieldType.isArray()) {
                int[] sizes = fieldType.getSizes();
                fill(start, element.elemento_lista(), sizes, new int[sizes.length], 0);
            } else if (element.LLAVE_IZQ() != null && fieldType != null && fieldType.isStruct()) {

                Symbol anidada = symbolTable.getStruct(fieldType.getCustomTypeName());
                if (anidada != null) {
                    fillFields(start, element.elemento_lista(), anidada, 0);
                }
            } else {
                c3d.emit(QuadrupleOp.HEAP_SET, start, visit(element.expresion()), null);
            }

            cell += celdasDe(fieldType);
            i++;
        }
    }

    private String declareArray(Symbol sym, Type type,
                                   YParser.Expresion_inicializacionContext inicializador) {
        int celdas = type.totalSize();
        if (celdas <= 0) {
            return null;
        }
        String base = ArrayRuntime.allocate(c3d, celdas);
        c3d.emitAssign(sym.getName(), base);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            int[] sizes = type.getSizes();
            fill(base, inicializador.elemento_lista(), sizes, new int[sizes.length], 0);
        }
        return null;
    }

    private void fill(String base, List<YParser.Elemento_listaContext> elementos, int[] sizes,
                        int[] indices, int dimension) {
        for (int i = 0; i < elementos.size() && dimension < indices.length; i++) {
            YParser.Elemento_listaContext element = elementos.get(i);
            indices[dimension] = i;
            if (element.LLAVE_IZQ() != null) {
                fill(base, element.elemento_lista(), sizes, indices, dimension + 1);
            } else {
                String data = visit(element.expresion());
                ArrayRuntime.writeConstant(c3d, base, indices, sizes, data);
            }
        }
    }

    @Override
    public String visitAsignacion(YParser.AsignacionContext ctx) {
        String val = ctx.expresion().isEmpty() ? "0" : visit(ctx.expresion(ctx.expresion().size() - 1));

        if (ctx.acceso_miembro() != null) {
            if (writeOnAccess(ctx.acceso_miembro(), val)) {
                return null;
            }
        }
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(varName);
            if (sym == null) {
                return null;
            }
            c3d.emitAssign(sym.getName(), val);
        }
        return null;
    }

    private boolean writeOnAccess(YParser.Acceso_miembroContext ctx, String valor) {
        String address = accessAddress(ctx);
        if (address == null) {
            return false;
        }
        c3d.emit(QuadrupleOp.HEAP_SET, address, valor, null);
        return true;
    }

    private String accessAddress(YParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID(0).getText());
        if (base == null) {
            return null;
        }
        String address = leerVariable(base);
        Type type = base.getType();

        for (Object step : accessSteps(ctx)) {
            if (step instanceof String fieldName) {
                Symbol field = fieldOf(type, fieldName);
                if (field == null) {
                    return null;
                }
                address = ArrayRuntime.add(c3d, address, fieldPosition(field)[0]);
                type = field.getType();
            } else {
                if (type == null || !type.isArray() || !type.hasSizes()) {
                    return null;
                }
                address = ArrayRuntime.address(c3d, address,
                        List.of(visit((YParser.ExpresionContext) step)), type.getSizes());
                type = type.desindexar(1);
            }
        }
        if (type == null || type.isArray() || type.isStruct()) {
            return null;
        }
        return address;
    }

    private List<Object> accessSteps(YParser.Acceso_miembroContext ctx) {
        List<Object> pasos = new ArrayList<>();
        int i = 1;
        while (i < ctx.getChildCount()) {
            if (".".equals(ctx.getChild(i).getText())) {
                pasos.add(ctx.getChild(i + 1).getText());
                i += 2;
            } else {
                pasos.add(ctx.getChild(i + 1));
                i += 3;
            }
        }
        return pasos;
    }

    private Symbol fieldOf(Type type, String name) {
        if (type == null || !type.isStruct()) {
            return null;
        }
        Symbol struct = symbolTable.getStruct(type.getCustomTypeName());
        return struct == null ? null : struct.getMember(name);
    }

    private int[] fieldPosition(Symbol field) {
        Symbol struct = symbolTable.getStruct(field.getScope());
        if (struct == null) {
            return new int[]{0};
        }
        int cell = 0;
        for (Symbol other : struct.getMembers()) {
            if (other == field) {
                return new int[]{cell};
            }
            cell += celdasDe(other.getType());
        }
        return new int[]{0};
    }

    private String leerVariable(Symbol sym) {
        return sym.getName();
    }

    @Override
    public String visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String valor = readCell(ctx);
        if (valor != null) {
            return valor;
        }

        return ctx.ID(0).getText();
    }

    private String readCell(YParser.Acceso_miembroContext ctx) {
        String address = accessAddress(ctx);
        if (address == null) {
            return null;
        }
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.HEAP_GET, address, null, valor);
        return valor;
    }

    @Override
    public String visitSi_sentencia(YParser.Si_sentenciaContext ctx) {
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

            emitConditionalJump(condiciones.get(i), trueLabel, falseLabel);
            c3d.emitLabel(trueLabel);
            visit(bloques.get(i));
            c3d.emitGoto(endLabel);
            if (!alFinal) {
                c3d.emitLabel(falseLabel);
            }
        }

        if (sinSi != null) {
            visit(sinSi.bloque());
            c3d.emitGoto(endLabel);
        }
        if (opposite != null) {
            visit(opposite);
        }
        c3d.emitLabel(endLabel);

        return null;
    }

    private void emitConditionalJump(YParser.CondicionContext condition,
                                        String trueValue, String falseValue) {
        emitExpressionJump(condition.expresion(), trueValue, falseValue);
    }

    private void emitExpressionJump(YParser.ExpresionContext expression,
                                        String trueValue, String falseValue) {
        if (expression.operador_negacion() != null) {
            emitExpressionJump(expression.expresion(0), falseValue, trueValue);
            return;
        }
        if (expression.operador_logico() != null && expression.expresion().size() == 2) {
            boolean esY = "&&".equals(expression.operador_logico().getText());
            String next = c3d.newLabel();
            if (esY) {
                emitExpressionJump(expression.expresion(0), next, falseValue);
            } else {
                emitExpressionJump(expression.expresion(0), trueValue, next);
            }
            c3d.emitLabel(next);
            emitExpressionJump(expression.expresion(1), trueValue, falseValue);
            return;
        }
        if (expression.operador_relacional() != null && expression.expresion().size() == 2) {
            c3d.emit(relacionalDe(expression.operador_relacional().getText()),
                    visit(expression.expresion(0)), visit(expression.expresion(1)), trueValue);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(expression), null, trueValue);
        }
        if (falseValue != null) {
            c3d.emitGoto(falseValue);
        }
    }

    private QuadrupleOp relacionalDe(String operator) {
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
        return QuadrupleOp.IF_EQ;
    }

    @Override
    public String visitMientras_sentencia(YParser.Mientras_sentenciaContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(condLabel);
        emitConditionalJump(ctx.condicion(), bodyLabel, endLabel);

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
        String condLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        visit(ctx.bloque());

        c3d.emitLabel(condLabel);
        emitConditionalJump(ctx.condicion(), condLabel, null);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitPara_sentencia(YParser.Para_sentenciaContext ctx) {
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
        emitConditionalJump(ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(incrementLabel);
        visit(ctx.incremento_decremento());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
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
        boolean isArray = sym.getType() != null && sym.getType().isArray();

        String antes = isArray ? readCell(access) : leerVariable(sym);
        if (antes == null) {
            return null;
        }
        String after = c3d.newTemp();
        c3d.emit(ctx.SUMA_ABREVIADA() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                antes, "1", after);

        if (isArray) {
            writeOnAccess(access, after);
            return null;
        }
        c3d.emitAssign(sym.getName(), after);
        return null;
    }

    @Override
    public String visitElegir_sentencia(YParser.Elegir_sentenciaContext ctx) {
        String endLabel = c3d.newLabel();
        YParser.Siempre_bloqueContext always = ctx.siempre_bloque();
        String defaultLabel = always != null ? c3d.newLabel() : endLabel;

        breakLabels.push(endLabel);
        continueLabels.push(endLabel);

        String valor = visit(ctx.expresion());
        List<YParser.Caso_bloqueContext> casos = ctx.caso_bloque();
        List<String> etiquetas = new ArrayList<>();
        for (YParser.Caso_bloqueContext caseOf : casos) {
            String label = c3d.newLabel();
            etiquetas.add(label);
            c3d.emit(QuadrupleOp.IF_EQ, valor, visit(caseOf.expresion()), label);
        }
        c3d.emitGoto(defaultLabel);

        for (int i = 0; i < casos.size(); i++) {
            c3d.emitLabel(etiquetas.get(i));
            YParser.Caso_bloqueContext caseOf = casos.get(i);
            visit(caseOf.bloque_interno_opcional());

            if (caseOf.ROMPER() == null && !terminaConRomper(caseOf.bloque_interno_opcional())) {
                c3d.emitGoto(endLabel);
            }
        }
        if (always != null) {
            c3d.emitLabel(defaultLabel);
            visit(always.bloque_interno_opcional());
        }
        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
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
                if (isString(exprCtx)) {
                    c3d.emit(QuadrupleOp.PRINT_STR, val, null, null);
                } else {
                    c3d.emit(QuadrupleOp.PRINT_INT, val, null, null);
                }
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    private boolean isString(YParser.ExpresionContext ctx) {
        if (ctx.leer_funcion() != null) {
            return true;
        }
        if (ctx.sumatoria() == null || ctx.sumatoria().productoria().size() != 1
                || ctx.sumatoria().productoria(0).termino().size() != 1) {
            return false;
        }
        YParser.TerminoContext term = ctx.sumatoria().productoria(0).termino(0);
        if (term.CADENA_TEXTO() != null) {
            return true;
        }
        if (term.ID() != null) {
            Symbol sym = symbolTable.resolve(term.ID().getText());
            return sym != null && Type.STRING.equals(sym.getType());
        }
        if (term.acceso_miembro() != null) {
            return Type.STRING.equals(accessType(term.acceso_miembro()));
        }
        return false;
    }

    private Type accessType(YParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID(0).getText());
        Type type = base == null ? null : base.getType();
        for (Object step : accessSteps(ctx)) {
            if (type == null) {
                return Type.UNKNOWN;
            }
            if (step instanceof String fieldName) {
                Symbol field = fieldOf(type, fieldName);
                type = field == null ? Type.UNKNOWN : field.getType();
            } else if (type.isArray() && type.hasSizes()) {
                type = type.desindexar(1);
            } else {
                type = Type.UNKNOWN;
            }
        }
        return type;
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
            QuadrupleOp qOp = "==".equals(op) ? QuadrupleOp.IF_EQ :
                    "!=".equals(op) ? QuadrupleOp.IF_NE :
                    "<".equals(op) ? QuadrupleOp.IF_LT :
                    ">".equals(op) ? QuadrupleOp.IF_GT : QuadrupleOp.IF_GE;

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
        boolean isText = isText(ctx.productoria(0));
        for (int i = 1; i < ctx.productoria().size(); i++) {
            YParser.ProductoriaContext next = ctx.productoria(i);
            String op = ctx.operador_aditivo(i - 1).getText();
            boolean concat = "+".equals(op) && (isText || isText(next));
            String temp = c3d.newTemp();
            c3d.emit(concat ? QuadrupleOp.CONCAT
                    : "+".equals(op) ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                    acumulado, visit(next), temp);
            acumulado = temp;
            isText = concat;
        }
        return acumulado;
    }

    @Override
    public String visitProductoria(YParser.ProductoriaContext ctx) {
        String acumulado = visit(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            String op = ctx.operador_multiplicativo(i - 1).getText();
            String temp = c3d.newTemp();
            c3d.emit("*".equals(op) ? QuadrupleOp.MUL : QuadrupleOp.DIV,
                    acumulado, visit(ctx.termino(i)), temp);
            acumulado = temp;
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
            return ctx.ID().getText();
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
}
