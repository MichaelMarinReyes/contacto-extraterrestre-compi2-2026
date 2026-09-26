package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

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

        // Los atributos se vuelven a declarar aqui: cuando corre esta segunda
        // pasada la tabla de simbolos ya no tiene el ambito de la clase. El
        // offset es el mismo que asigno el analizador semantico (orden de
        // declaracion), para que this.campo calcule la misma direccion. Se
        // marcan como copias de trabajo para no salir duplicados en la tabla.
        int offset = 0;
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.atributoDef() == null) {
                continue;
            }
            String fieldName = mc.atributoDef().ID().getText();
            symbolTable.define(new Symbol(fieldName, typeOfField(fieldName),
                    SymbolCategory.FIELD, offset++, false).at(mc.atributoDef()).markWorking());
        }

        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            visit(mc);
        }

        symbolTable.exitScope();
        currentClassName = "";
        return null;
    }

    // ====================== Direccionamiento ======================

    /**
     * Emite las cuartetas necesarias para obtener la direccion de un simbolo
     * y devuelve el temporal que la contiene.
     *
     * <p>Los atributos no viven en la pila sino en el heap: se alcanza el
     * puntero del objeto guardado en {@code stack[P + 0]} (la referencia
     * implicita {@code this}) y se le suma el offset del campo.</p>
     */
    private String addressOf(Symbol s) {
        if (s.getCategory() == SymbolCategory.FIELD) {
            String thisPos = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, "P", "0", thisPos);
            String objectPtr = c3d.newTemp();
            c3d.emit(QuadrupleOp.STACK_GET, thisPos, null, objectPtr);
            String fieldAddr = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, objectPtr, String.valueOf(s.getOffset()), fieldAddr);
            return fieldAddr;
        }
        String pos = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(s.getOffset()), pos);
        return pos;
    }

    /** Escribe {@code value} en la direccion de {@code s}. */
    private void store(Symbol s, String value) {
        String addr = addressOf(s);
        c3d.emit(s.getCategory() == SymbolCategory.FIELD
                ? QuadrupleOp.HEAP_SET : QuadrupleOp.STACK_SET, addr, value, null);
    }

    /** Lee el valor de {@code s} y lo devuelve como temporal. */
    private String load(Symbol s) {
        String addr = addressOf(s);
        String value = c3d.newTemp();
        c3d.emit(s.getCategory() == SymbolCategory.FIELD
                ? QuadrupleOp.HEAP_GET : QuadrupleOp.STACK_GET, addr, null, value);
        return value;
    }

    /** Declara un parametro formal en el offset que le toca. */
    private void defineParameter(ZetarianoParser.ParametroContext pCtx) {
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        symbolTable.define(new Symbol(pCtx.ID().getText(), Type.UNKNOWN,
                SymbolCategory.PARAMETER, offset, false).at(pCtx).markWorking());
    }

    /** Tipo declarado de un atributo de la clase en curso. */
    private Type typeOfField(String fieldName) {
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls == null) {
            return Type.UNKNOWN;
        }
        Symbol member = cls.getMember(fieldName);
        return member == null ? Type.UNKNOWN : member.getType();
    }

    /**
     * Declara la referencia implicita {@code this} en el offset 0, igual que
     * hace el analizador semantico.
     *
     * <p>Se anota con la posicion del metodo o constructor al que pertenece, como
     * alli. Es una copia de trabajo ({@code markWorking}) y no sale en la tabla,
     * pero asi las dos declaraciones coinciden en todo.</p>
     */
    private void defineThis(org.antlr.v4.runtime.ParserRuleContext owner) {
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            symbolTable.define(new Symbol("this", cls.getType(), SymbolCategory.VARIABLE, 0, false)
                    .at(owner).markWorking());
        }
    }

    @Override
    public String visitConstructorDef(ZetarianoParser.ConstructorDefContext ctx) {
        String funcName = currentClassName + "_" + ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);
        symbolTable.enterScope("constructor_" + funcName, 1);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                defineParameter(pCtx);
            }
        }

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
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                defineParameter(pCtx);
            }
        }

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, methodName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitDeclaracionVariable(ZetarianoParser.DeclaracionVariableContext ctx) {
        String varName = ctx.ID().getText();

        // La declaracion reserva su hueco en la pila aunque no lleve valor
        // inicial: sin esto las referencias posteriores quedarian colgando.
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declared = new Symbol(varName, Type.UNKNOWN,
                SymbolCategory.VARIABLE, offset, false).at(ctx).markWorking();
        symbolTable.define(declared);
        Symbol s = symbolTable.resolve(varName);
        if (s == null) {
            s = declared;
        }

        if (ctx.expresion() != null) {
            store(s, visit(ctx.expresion()));
        }
        return null;
    }

    @Override
    public String visitAsignacion(ZetarianoParser.AsignacionContext ctx) {
        String val = visit(ctx.expresion(ctx.expresion().size() - 1));

        if (ctx.ID() != null) {
            Symbol target = symbolTable.resolve(ctx.ID().getText());
            if (target != null) {
                store(target, applyCompound(ctx, target, val));
            }
        } else if (ctx.accesoMiembro() != null) {
            Symbol field = resolveMember(ctx.accesoMiembro());
            if (field != null) {
                store(field, applyCompound(ctx, field, val));
            }
        }
        return null;
    }

    /**
     * Resuelve {@code obj.campo} al simbolo del atributo dentro de la clase.
     *
     * @return el atributo, o null si la cadena nodesigna un campo conocido
     */
    private Symbol resolveMember(ZetarianoParser.AccesoMiembroContext acceso) {
        if (acceso.miembroAcceso(0) == null || acceso.miembroAcceso(0).ID() == null) {
            return null;
        }
        Symbol base = symbolTable.resolve(acceso.ID().getText());
        if (base == null) {
            return null;
        }
        Symbol cls = symbolTable.getClass(base.getType().getCustomTypeName());
        return cls == null ? null : cls.getMember(acceso.miembroAcceso(0).ID().getText());
    }

    /**
     * Para {@code x = v} devuelve {@code v}; para {@code x += v} genera la
     * lectura previa y la suma correspondiente.
     */
    private String applyCompound(ZetarianoParser.AsignacionContext ctx, Symbol target, String val) {
        QuadrupleOp op = switch (ctx.getChild(1).getText()) {
            case "+=" -> QuadrupleOp.ADD;
            case "-=" -> QuadrupleOp.SUB;
            case "*=" -> QuadrupleOp.MUL;
            default -> null;
        };
        if (op == null) {
            return val;
        }
        String current = load(target);
        String temp = c3d.newTemp();
        c3d.emit(op, current, val, temp);
        return temp;
    }

    @Override
    public String visitMemberAccessExpr(ZetarianoParser.MemberAccessExprContext ctx) {
        Symbol field = resolveMember(ctx.accesoMiembro());
        return field == null ? visit(ctx.accesoMiembro().ID()) : load(field);
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
    public String visitIncrementoDecremento(ZetarianoParser.IncrementoDecrementoContext ctx) {
        Symbol target = symbolTable.resolve(ctx.ID().getText());
        if (target == null) {
            return null;
        }
        String updated = c3d.newTemp();
        c3d.emit(ctx.INCREMENT() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                load(target), "1", updated);
        store(target, updated);
        return null;
    }

    @Override
    public String visitSentenciaFor(ZetarianoParser.SentenciaForContext ctx) {
        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String stepLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(stepLabel);

        c3d.emitLabel(condLabel);
        if (ctx.expresion() != null) {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(ctx.expresion()), null, bodyLabel);
        } else {
            c3d.emitGoto(bodyLabel);
        }
        c3d.emitGoto(endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(stepLabel);
        if (ctx.forUpdate() != null) {
            visit(ctx.forUpdate());
        }
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);
        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitSentenciaDoWhile(ZetarianoParser.SentenciaDoWhileContext ctx) {
        String bodyLabel = c3d.newLabel();
        String condLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(condLabel);
        c3d.emit(QuadrupleOp.IF_TRUE, visit(ctx.expresion()), null, bodyLabel);

        c3d.emitLabel(endLabel);
        breakLabels.pop();
        continueLabels.pop();
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
        // En el Heap se reserva una casilla por atributo. Los metodos y el
        // constructor tambien son miembros de la clase, pero no ocupan memoria.
        int fieldCount = 0;
        if (cls != null) {
            for (Symbol m : cls.getMembers()) {
                if (m.getCategory() == SymbolCategory.FIELD) {
                    fieldCount++;
                }
            }
        }

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
            return load(s);
        }
        // Sin simbolo no se puede emitir acceso a memoria: se deja el nombre
        // tal cual para que el error semantico previo ya lo haya reportado.
        return name;
    }

    @Override
    public String visitParenExpr(ZetarianoParser.ParenExprContext ctx) {
        return visit(ctx.expresion());
    }

    // ====================== Operadores ======================

    @Override
    public String visitRelationalExpr(ZetarianoParser.RelationalExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitEqualityExpr(ZetarianoParser.EqualityExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    /** Materializa una comparacion en un temporal con el valor 1 o 0. */
    private String compare(ZetarianoParser.ExpresionContext leftCtx,
                          ZetarianoParser.ExpresionContext rightCtx,
                          String operator) {
        String left = visit(leftCtx);
        String right = visit(rightCtx);
        QuadrupleOp op = switch (operator) {
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            case "==" -> QuadrupleOp.IF_EQ;
            case "!=" -> QuadrupleOp.IF_NE;
            default -> null;
        };
        String temp = c3d.newTemp();
        c3d.emit(op == null ? QuadrupleOp.IF_EQ : op, left, right, temp);
        return temp;
    }

    @Override
    public String visitAndExpr(ZetarianoParser.AndExprContext ctx) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.AND, visit(ctx.expresion(0)), visit(ctx.expresion(1)), temp);
        return temp;
    }

    @Override
    public String visitOrExpr(ZetarianoParser.OrExprContext ctx) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.OR, visit(ctx.expresion(0)), visit(ctx.expresion(1)), temp);
        return temp;
    }

    @Override
    public String visitNotExpr(ZetarianoParser.NotExprContext ctx) {
        // La negacion logica se expresa como "igual a cero".
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.IF_EQ, visit(ctx.expresion()), "0", temp);
        return temp;
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
