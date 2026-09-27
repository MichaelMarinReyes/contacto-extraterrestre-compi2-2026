package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ZetarianoC3DVisitor extends ZetarianoBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private String currentClassName = "";
    private final Deque<String> breakLabels = new ArrayDeque<>();
    private final Deque<String> continueLabels = new ArrayDeque<>();

    public ZetarianoC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitClaseDef(ZetarianoParser.ClaseDefContext ctx) {
        currentClassName = ctx.ID().getText();
        symbolTable.enterScope("class_" + currentClassName, 0);
        int offset = 0;
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.atributoDef() == null) {
                continue;
            }
            symbolTable.define(new Symbol(mc.atributoDef().ID().getText(), typeOfField(mc.atributoDef()),
                    SymbolCategory.FIELD, offset++, false).at(mc.atributoDef()).markWorking());
        }

        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            visit(mc);
        }

        symbolTable.exitScope();
        currentClassName = "";
        return null;
    }

    private void store(Symbol s, String value) {
        c3d.emitAssign(s.getName(), value);
    }

    private String load(Symbol s) {
        return s.getName();
    }

    private void defineParameter(ZetarianoParser.ParametroContext pCtx) {
        Type type = Type.UNKNOWN;
        if (pCtx.tipoDato() != null) {
            String name = pCtx.tipoDato().getText();
            type = switch (name) {
                case "int" -> Type.INT;
                case "double" -> Type.DOUBLE;
                case "boolean" -> Type.BOOLEAN;
                case "char" -> Type.CHAR;
                case "String" -> Type.STRING;
                default -> symbolTable.getClass(name) != null
                        ? Type.classType(name) : Type.UNKNOWN;
            };
        }
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        symbolTable.define(new Symbol(pCtx.ID().getText(), type,
                SymbolCategory.PARAMETER, offset, false).at(pCtx).markWorking());
    }

    private Type typeOfField(ZetarianoParser.AtributoDefContext ctx) {
        Type type = Type.UNKNOWN;
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            Symbol member = cls.getMember(ctx.ID().getText());
            if (member != null && member.getType() != null) {
                type = member.getType();
            }
        }
        if (type.isArray() && !type.hasSizes()
                && ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext array) {
            type = Type.arrayOf(type.getElementType(), formasDe(array.literalArreglo()));
        }
        return type;
    }

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
    public String visitAtributoDef(ZetarianoParser.AtributoDefContext ctx) {
        if (ctx.expresion() == null) {
            return null;
        }

        Symbol field = symbolTable.resolve(ctx.ID().getText());
        if (field != null) {
            store(field, visit(ctx.expresion()));
        }
        return null;
    }

    @Override
    public String visitDeclaracionVariable(ZetarianoParser.DeclaracionVariableContext ctx) {
        String varName = ctx.ID().getText();

        Type type = Type.UNKNOWN;
        if (ctx.LBRACK().size() > 0) {
            if (ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext array) {
                type = Type.arrayOf(Type.UNKNOWN, formasDe(array.literalArreglo()));
            } else {
                type = Type.arrayOf(Type.UNKNOWN, new int[ctx.LBRACK().size()]);
            }
        }

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declared = new Symbol(varName, type,
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
    public String visitLiteralArregloExpr(ZetarianoParser.LiteralArregloExprContext ctx) {
        int[] sizes = formasDe(ctx.literalArreglo());
        int celdas = Type.arrayOf(Type.UNKNOWN, sizes).totalSize();
        if (celdas <= 0) {
            return "0";
        }
        String base = ArrayRuntime.allocate(c3d, celdas);
        fill(base, ctx.literalArreglo(), sizes, new int[sizes.length], 0);
        return base;
    }

    private int[] formasDe(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = valoresDe(ctx);
        int[] rowShape = new int[0];
        for (ParseTree valor : valores) {
            ZetarianoParser.LiteralArregloContext row = rowOf(valor);
            if (row != null) {
                rowShape = formasDe(row);
                break;
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = valores.size();
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return sizes;
    }

    private static List<ParseTree> valoresDe(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof ZetarianoParser.ExpresionContext) {
                valores.add(child);
            }
        }
        return valores;
    }

    private static ZetarianoParser.LiteralArregloContext rowOf(ParseTree valor) {
        if (valor instanceof ZetarianoParser.LiteralArregloExprContext anidada) {
            return anidada.literalArreglo();
        }
        if (valor instanceof ZetarianoParser.LiteralArregloContext direct) {
            return direct;
        }
        return null;
    }

    private void fill(String base, ZetarianoParser.LiteralArregloContext ctx, int[] sizes,
                        int[] indices, int dimension) {
        List<ParseTree> valores = valoresDe(ctx);
        for (int i = 0; i < valores.size() && dimension < indices.length; i++) {
            ParseTree valor = valores.get(i);
            indices[dimension] = i;
            ZetarianoParser.LiteralArregloContext row = rowOf(valor);
            if (row != null) {
                fill(base, row, sizes, indices, dimension + 1);
            } else {
                ArrayRuntime.writeConstant(c3d, base, indices, sizes,
                        visit((ZetarianoParser.ExpresionContext) valor));
            }
        }
    }

    private String readCell(ZetarianoParser.AccesoMiembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID().getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return null;
        }
        int[] sizes = base.getType().getSizes();
        List<String> indices = indicesDe(ctx);
        if (!base.getType().hasSizes() || indices == null || indices.size() != sizes.length) {
            return null;
        }
        return ArrayRuntime.leer(c3d, load(base), indices, sizes);
    }

    private List<String> indicesDe(ZetarianoParser.AccesoMiembroContext ctx) {
        List<String> indices = new ArrayList<>();
        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() != null) {
                return null;
            }
            for (ZetarianoParser.ExpresionContext index : mac.expresion()) {
                indices.add(visit(index));
            }
        }
        return indices;
    }

    private boolean writeCell(ZetarianoParser.AccesoMiembroContext ctx, String valor) {
        Symbol base = symbolTable.resolve(ctx.ID().getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return false;
        }
        int[] sizes = base.getType().getSizes();
        List<String> indices = indicesDe(ctx);
        if (!base.getType().hasSizes() || indices == null || indices.size() != sizes.length) {
            return false;
        }
        ArrayRuntime.write(c3d, load(base), indices, sizes, valor);
        return true;
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
            if (writeCell(ctx.accesoMiembro(), val)) {
                return null;
            }
            Symbol field = resolveMember(ctx.accesoMiembro());
            if (field != null) {
                store(field, applyCompound(ctx, field, val));
            }
        }
        return null;
    }

    private Symbol resolveMember(ZetarianoParser.AccesoMiembroContext access) {
        if (access.miembroAcceso(0) == null || access.miembroAcceso(0).ID() == null) {
            return null;
        }
        Symbol base = symbolTable.resolve(access.ID().getText());
        if (base == null) {
            return null;
        }
        Symbol cls = symbolTable.getClass(base.getType().getCustomTypeName());
        return cls == null ? null : cls.getMember(access.miembroAcceso(0).ID().getText());
    }

    private String applyCompound(ZetarianoParser.AsignacionContext ctx, Symbol target, String val) {
        return applyCompound(ctx.getChild(1).getText(), target, val);
    }

    @Override
    public String visitAsignacionExpresion(ZetarianoParser.AsignacionExpresionContext ctx) {
        String val = visit(ctx.expresion());
        String operator = ctx.getChild(1).getText();

        if (ctx.ID() != null) {
            Symbol target = symbolTable.resolve(ctx.ID().getText());
            if (target != null) {
                store(target, applyCompound(operator, target, val));
            }
        } else if (ctx.accesoMiembro() != null) {

            if (writeCell(ctx.accesoMiembro(), val)) {
                return null;
            }
            Symbol field = resolveMember(ctx.accesoMiembro());
            if (field != null) {
                store(field, applyCompound(operator, field, val));
            }
        }
        return null;
    }

    private String applyCompound(String operator, Symbol target, String val) {
        QuadrupleOp op = switch (operator) {
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
        String cell = readCell(ctx.accesoMiembro());
        if (cell != null) {
            return cell;
        }
        Symbol field = resolveMember(ctx.accesoMiembro());
        if (field != null) {
            return load(field);
        }

        return ctx.accesoMiembro().ID().getText();
    }

    @Override
    public String visitSentenciaIf(ZetarianoParser.SentenciaIfContext ctx) {
        String fin = c3d.newLabel();

        List<ZetarianoParser.ExpresionContext> condiciones = ctx.expresion();
        List<ZetarianoParser.BloqueContext> bloques = ctx.bloque();
        int withCondition = condiciones.size();
        for (int i = 0; i < withCondition; i++) {
            String trueValue = c3d.newLabel();
            boolean last = i == withCondition - 1 && bloques.size() == withCondition;
            String falseValue = last ? fin : c3d.newLabel();

            emitConditionalJump(condiciones.get(i), trueValue, falseValue);
            c3d.emitLabel(trueValue);
            visit(bloques.get(i));
            c3d.emitGoto(fin);
            if (!last) {
                c3d.emitLabel(falseValue);
            }
        }

        for (int i = withCondition; i < bloques.size(); i++) {
            visit(bloques.get(i));
        }

        c3d.emitLabel(fin);
        return null;
    }

    private void emitConditionalJump(ZetarianoParser.ExpresionContext condition,
                                        String trueValue, String falseValue) {
        if (condition instanceof ZetarianoParser.NotExprContext negated) {
            emitConditionalJump(negated.expresion(), falseValue, trueValue);
            return;
        }
        if (condition instanceof ZetarianoParser.AndExprContext conjunction) {
            String next = c3d.newLabel();
            emitConditionalJump(conjunction.expresion(0), next, falseValue);
            c3d.emitLabel(next);
            emitConditionalJump(conjunction.expresion(1), trueValue, falseValue);
            return;
        }
        if (condition instanceof ZetarianoParser.OrExprContext disjunction) {
            String next = c3d.newLabel();
            emitConditionalJump(disjunction.expresion(0), trueValue, next);
            c3d.emitLabel(next);
            emitConditionalJump(disjunction.expresion(1), trueValue, falseValue);
            return;
        }
        if (condition instanceof ZetarianoParser.ParenExprContext inParentheses) {
            emitConditionalJump(inParentheses.expresion(), trueValue, falseValue);
            return;
        }
        if (condition instanceof ZetarianoParser.RelationalExprContext relacional) {
            c3d.emit(relacionalDe(relacional.getChild(1).getText()),
                    visit(relacional.expresion(0)), visit(relacional.expresion(1)),
                    trueValue);
        } else if (condition instanceof ZetarianoParser.EqualityExprContext equality) {
            c3d.emit(equalityOf(equality.getChild(1).getText()),
                    visit(equality.expresion(0)), visit(equality.expresion(1)),
                    trueValue);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(condition), null, trueValue);
        }
        if (falseValue != null) {
            c3d.emitGoto(falseValue);
        }
    }

    private QuadrupleOp relacionalDe(String operator) {
        return switch (operator) {
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> QuadrupleOp.IF_EQ;
        };
    }

    private QuadrupleOp equalityOf(String operator) {
        return "!=".equals(operator) ? QuadrupleOp.IF_NE : QuadrupleOp.IF_EQ;
    }

    private boolean isText(ZetarianoParser.ExpresionContext ctx) {
        if (ctx instanceof ZetarianoParser.StringLiteralExprContext) {
            return true;
        }
        if (ctx instanceof ZetarianoParser.IdExprContext id) {
            Symbol s = symbolTable.resolve(id.ID().getText());
            return s != null && s.getType() != null && Type.STRING.equals(s.getType());
        }
        return false;
    }

    private QuadrupleOp printOf(ZetarianoParser.ExpresionContext ctx) {
        if (isText(ctx)) {
            return QuadrupleOp.PRINT_STR;
        }
        if (ctx instanceof ZetarianoParser.CharLiteralExprContext) {
            return QuadrupleOp.PRINT_CHAR;
        }
        if (ctx instanceof ZetarianoParser.DoubleLiteralExprContext) {
            return QuadrupleOp.PRINT_FLOAT;
        }
        if (ctx instanceof ZetarianoParser.IdExprContext id) {
            Symbol s = symbolTable.resolve(id.ID().getText());
            if (s != null && s.getType() != null) {
                if (Type.DOUBLE.equals(s.getType())) {
                    return QuadrupleOp.PRINT_FLOAT;
                }
                if (Type.CHAR.equals(s.getType())) {
                    return QuadrupleOp.PRINT_CHAR;
                }
            }
        }
        return QuadrupleOp.PRINT_INT;
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
            emitConditionalJump(ctx.expresion(), bodyLabel, endLabel);
        } else {
            c3d.emitGoto(bodyLabel);
            c3d.emitGoto(endLabel);
        }

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
        emitConditionalJump(ctx.expresion(), bodyLabel, null);

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
        emitConditionalJump(ctx.expresion(), bodyLabel, endLabel);

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
        c3d.emitReturn(ctx.expresion() == null ? null : visit(ctx.expresion()));
        return null;
    }

    @Override
    public String visitRomper(ZetarianoParser.RomperContext ctx) {
        c3d.emitGotoTop(breakLabels);
        return null;
    }

    @Override
    public String visitContinuar(ZetarianoParser.ContinuarContext ctx) {
        c3d.emitGotoTop(continueLabels);
        return null;
    }

    @Override
    public String visitLlamadaFuncionSemilla(ZetarianoParser.LlamadaFuncionSemillaContext ctx) {
        if (ctx.PRINTLN() != null || ctx.PRINT() != null) {
            if (ctx.argumentos() != null) {
                for (ZetarianoParser.ExpresionContext eCtx : ctx.argumentos().expresion()) {
                    String val = visit(eCtx);
                    c3d.emit(printOf(eCtx), val, null, null);
                }
            }
            if (ctx.PRINTLN() != null) {
                c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
            }
            return null;
        }

        if (ctx.READLN() != null) {
            String read = c3d.newTemp();
            c3d.emit(QuadrupleOp.READ, read, null, null);
            return read;
        }

        String name = callName(ctx);
        int count = 0;
        if (ctx.argumentos() != null) {
            for (ZetarianoParser.ExpresionContext arg : ctx.argumentos().expresion()) {
                c3d.emit(QuadrupleOp.PARAM, visit(arg), null, null);
                count++;
            }
        }
        String ret = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, name, String.valueOf(count), ret);
        return ret;
    }

    private String callName(ZetarianoParser.LlamadaFuncionSemillaContext ctx) {
        String base;
        if (ctx.accesoMiembro() != null) {
            Symbol receptor = symbolTable.resolve(ctx.accesoMiembro().ID().getText());
            String clazz = receptor != null && receptor.getType() != null
                    && receptor.getType().getCustomTypeName() != null
                    ? receptor.getType().getCustomTypeName() : currentClassName;
            base = clazz + "_" + ctx.accesoMiembro().miembroAcceso(0).ID().getText();
        } else {
            base = currentClassName != null
                    && symbolTable.getClass(currentClassName) != null
                    && symbolTable.getClass(currentClassName).getMember(ctx.ID().getText()) != null
                    ? currentClassName + "_" + ctx.ID().getText()
                    : ctx.ID().getText();
        }
        return base;
    }

    @Override
    public String visitNewObjectExpr(ZetarianoParser.NewObjectExprContext ctx) {
        String className = ctx.ID().getText();
        Symbol cls = symbolTable.getClass(className);
        int fieldCount = 0;
        if (cls != null) {
            for (Symbol m : cls.getMembers()) {
                if (m.getCategory() == SymbolCategory.FIELD) {
                    fieldCount++;
                }
            }
        }

        String heapStart = c3d.newTemp();
        c3d.emitAssign(heapStart, "H");
        c3d.emit(QuadrupleOp.ADD, "H", String.valueOf(fieldCount), "H");

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
        if (ctx.PLUS() != null && (isText(ctx.expresion(0)) || isText(ctx.expresion(1)))) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.CONCAT, visit(ctx.expresion(0)), visit(ctx.expresion(1)), temp);
            return temp;
        }
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
        String temp = c3d.newTemp();
        c3d.emitAssign(temp, ctx.CADENA_TEXTO().getText());
        return temp;
    }

    @Override
    public String visitIdExpr(ZetarianoParser.IdExprContext ctx) {
        String name = ctx.ID().getText();
        Symbol s = symbolTable.resolve(name);
        if (s != null) {
            return load(s);
        }

        return name;
    }

    @Override
    public String visitParenExpr(ZetarianoParser.ParenExprContext ctx) {
        return visit(ctx.expresion());
    }

    @Override
    public String visitRelationalExpr(ZetarianoParser.RelationalExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitEqualityExpr(ZetarianoParser.EqualityExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    private String compare(ZetarianoParser.ExpresionContext leftCtx,
                          ZetarianoParser.ExpresionContext rightCtx,
                          String operator) {
        String left = visit(leftCtx);
        String right = visit(rightCtx);
        QuadrupleOp op = "==".equals(operator) || "!=".equals(operator)
                ? equalityOf(operator)
                : relacionalDe(operator);
        String temp = c3d.newTemp();
        String trueValue = c3d.newLabel();
        String falseValue = c3d.newLabel();
        String fin = c3d.newLabel();
        c3d.emit(op, left, right, trueValue);
        c3d.emitGoto(falseValue);
        c3d.emitLabel(trueValue);
        c3d.emitAssign(temp, "1");
        c3d.emitGoto(fin);
        c3d.emitLabel(falseValue);
        c3d.emitAssign(temp, "0");
        c3d.emitLabel(fin);
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
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.NOT, visit(ctx.expresion()), null, temp);
        return temp;
    }

    @Override
    public String visitNegExpr(ZetarianoParser.NegExprContext ctx) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.NEG, visit(ctx.expresion()), null, temp);
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
