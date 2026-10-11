package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class ZetarianoSemanticVisitor extends ZetarianoBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();
    private Symbol currentClass = null;
    private Type currentMethodReturnType = Type.VOID;

    public ZetarianoSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        Symbol receiver = symbolTable.resolve("this");
        if (receiver != null) currentClass = symbolTable.getClass(receiver.getType().getCustomTypeName());
    }

    private static int widthOf(int[] shape) {
        return shape.length > 0 ? shape[0] : 1;
    }

    private static int countIndices(ZetarianoParser.AccesoMiembroContext ctx) {
        int indices = 0;
        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() == null) {
                indices += mac.expresion().size();
            }
        }
        return indices;
    }

    private static boolean hasIndices(ZetarianoParser.AccesoMiembroContext ctx) {
        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() != null) {
                return true;
            }
        }
        return false;
    }


    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitClaseDef(ZetarianoParser.ClaseDefContext ctx) {
        String className = ctx.ID().getText();
        currentClass = new Symbol(className, Type.classType(className), SymbolCategory.CLASS)
                .at(ctx);
        symbolTable.addClass(currentClass);

        symbolTable.enterScope("class_" + className, 0, ctx);

        int fieldOffset = 0;
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.atributoDef() != null) {
                ZetarianoParser.AtributoDefContext attrCtx = mc.atributoDef();
                String attrName = attrCtx.ID().getText();

                Type attrType = attrCtx.LBRACK().isEmpty()
                        ? resolveType(attrCtx.tipoDato().getText())
                        : Type.arrayOf(resolveType(attrCtx.tipoDato().getText()),
                        new int[attrCtx.LBRACK().size()]);
                Symbol attrSym = new Symbol(attrName, attrType, SymbolCategory.FIELD,
                        fieldOffset++, false).at(attrCtx);
                currentClass.addMember(attrSym);
                symbolTable.define(attrSym);
            }
        }

        // Registra las firmas antes de comprobar cuerpos: permite llamadas hacia adelante y recursión.
        for (ZetarianoParser.MiembroClaseContext member : ctx.miembroClase()) {
            if (member.metodoDef() != null) {
                var method = member.metodoDef();
                registerCallable(method.ID().getText(), resolveType(method.tipoDato().getText()),
                        SymbolCategory.METHOD, method.parametros(), method);
            } else if (member.constructorDef() != null) {
                var constructor = member.constructorDef();
                registerCallable(constructor.ID().getText(), Type.VOID, SymbolCategory.CONSTRUCTOR,
                        constructor.parametros(), constructor);
            }
        }
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.constructorDef() != null) {
                visit(mc.constructorDef());
            } else if (mc.metodoDef() != null) {
                visit(mc.metodoDef());
            }
        }

        symbolTable.exitScope();
        currentClass = null;
        return Type.VOID;
    }

    @Override
    public Type visitConstructorDef(ZetarianoParser.ConstructorDefContext ctx) {
        String name = ctx.ID().getText();
        if (currentClass != null && !name.equals(currentClass.getName())) {
            errors.add(Diagnostic.invalidConstructorName(
                    name, currentClass.getName(), ctx));
        }

        int count = ctx.parametros() == null ? 0 : ctx.parametros().parametro().size();
        Symbol ctorSymbol = currentClass.getCallable(name, count);

        currentMethodReturnType = Type.VOID;
        symbolTable.enterScope("constructor_" + FunctionRuntime.name(currentClass.getName(), name, count), 1, ctx);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                String pName = pCtx.ID().getText();
                Type pType = resolveType(pCtx.tipoDato().getText());
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol pSym = ctorSymbol.getParameters().stream().filter(parameter -> parameter.getName().equals(pName)).findFirst().orElseThrow();
                symbolTable.define(pSym);
            }
        }

        visit(ctx.bloque());
        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitMetodoDef(ZetarianoParser.MetodoDefContext ctx) {
        String methodName = ctx.ID().getText();
        Type returnType = resolveType(ctx.tipoDato().getText());
        currentMethodReturnType = returnType;

        int count = ctx.parametros() == null ? 0 : ctx.parametros().parametro().size();
        Symbol methodSymbol = currentClass.getCallable(methodName, count);

        symbolTable.enterScope("method_" + FunctionRuntime.name(currentClass.getName(), methodName, count), 1, ctx);
        defineThis(ctx);

        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                String pName = pCtx.ID().getText();
                Type pType = resolveType(pCtx.tipoDato().getText());
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol pSym = methodSymbol.getParameters().stream().filter(parameter -> parameter.getName().equals(pName)).findFirst().orElseThrow();
                symbolTable.define(pSym);
            }
        }

        visit(ctx.bloque());

        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracionVariable(ZetarianoParser.DeclaracionVariableContext ctx) {
        String typeStr = ctx.tipoDato().getText();
        String varName = ctx.ID().getText();
        int dimensiones = ctx.LBRACK().size();
        Type varType = dimensiones == 0
                ? resolveType(typeStr)
                : Type.arrayOf(resolveType(typeStr), new int[dimensiones]);

        Type initType = Type.UNKNOWN;
        if (ctx.expresion() != null) {
            if (ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext array) {
                initType = arrayLiteralType(varName, array.literalArreglo(), ctx);
                varType = ArrayLiteral.completeSizes(varType, initType);
                if (varType.hasSizes()) {
                    CompilationError error =
                            ArrayLiteral.validate(varName, varType, initType, ctx);
                    if (error != null) {
                        errors.add(error);
                    }
                }
            } else {
                initType = visit(ctx.expresion());
            }
            if (Type.isKnown(initType) && Type.isKnown(varType) && !initType.isAssignableTo(varType)) {
                errors.add(Diagnostic.inicializacionIncompatible(
                        varName, varType, initType, ctx));
            }
        }

        if (dimensiones > 0 && !varType.hasSizes() && ctx.expresion() == null) {
            errors.add(Diagnostic.sizelessArray(varName, ctx));
        }

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol varSym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, false).at(ctx);

        if (!symbolTable.define(varSym)) {
            errors.add(Diagnostic.alreadyDeclaredVariable(varName, ctx));
        }

        return varType;
    }

    private Type arrayLiteralType(String name, ZetarianoParser.LiteralArregloContext ctx, ParserRuleContext where) {
        List<ParseTree> elementos = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof ZetarianoParser.ExpresionContext) {
                elementos.add(child);
            }
        }

        int filas = elementos.size();
        Type rowType = Type.UNKNOWN;
        int[] rowShape = new int[0];
        for (int f = 0; f < filas; f++) {
            Type type = valueType(name, elementos.get(f), where);
            if (type == null) {
                type = Type.UNKNOWN;
            }
            Type element = type.isArray() ? type.getElementType() : type;
            if (f > 0 && Type.isKnown(rowType) && Type.isKnown(element)) {
                if (rowType.isNumeric() && element.isNumeric()) {
                    if (Type.DOUBLE.equals(element)) rowType = Type.DOUBLE;
                } else if (!element.equals(rowType)) {
                    CallValidator.report("El arreglo «" + name + "» contiene elementos de tipos incompatibles", where, errors);
                }
            }

            int[] shape = type.isArray() ? type.getSizes() : new int[0];
            if (f == 0) {
                rowType = type.isArray() ? type.getElementType() : type;
                rowShape = shape;
            } else if (!java.util.Arrays.equals(shape, rowShape)) {
                errors.add(Diagnostic.filasDesiguales(name, f + 1,
                        widthOf(rowShape), widthOf(shape), where));
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = filas;
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return Type.arrayOf(rowType, sizes);
    }

    private Type valueType(String name, ParseTree valor, ParserRuleContext where) {
        if (valor instanceof ZetarianoParser.LiteralArregloExprContext anidada) {
            return arrayLiteralType(name, anidada.literalArreglo(), where);
        }
        if (valor instanceof ZetarianoParser.LiteralArregloContext direct) {
            return arrayLiteralType(name, direct, where);
        }
        return visit(valor);
    }

    @Override
    public Type visitAsignacion(ZetarianoParser.AsignacionContext ctx) {
        Type targetType = Type.UNKNOWN;
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostic.undeclaredVariable(name, ctx));
            } else {
                targetType = s.getType();
            }
        } else if (ctx.accesoMiembro() != null) {
            targetType = visit(ctx.accesoMiembro());
        }

        Type exprType = visit(ctx.expresion(ctx.expresion().size() - 1));
        if (Type.isKnown(targetType) && Type.isKnown(exprType)
                && !exprType.isAssignableTo(targetType)) {
            errors.add(Diagnostic.incompatibleAssignment(targetType, exprType, ctx));
        }

        return targetType;
    }

    @Override
    public Type visitRetorno(ZetarianoParser.RetornoContext ctx) {
        Type retType = Type.VOID;
        if (ctx.expresion() != null) {
            retType = visit(ctx.expresion());
        }

        if (Type.isKnown(currentMethodReturnType) && Type.isKnown(retType)
                && !retType.isAssignableTo(currentMethodReturnType)) {
            errors.add(Diagnostic.wrongReturnType(
                    currentMethodReturnType, retType, ctx));
        }
        return retType;
    }

    @Override
    public Type visitIntLiteralExpr(ZetarianoParser.IntLiteralExprContext ctx) {
        return Type.INT;
    }

    @Override
    public Type visitDoubleLiteralExpr(ZetarianoParser.DoubleLiteralExprContext ctx) {
        return Type.DOUBLE;
    }

    @Override
    public Type visitStringLiteralExpr(ZetarianoParser.StringLiteralExprContext ctx) {
        return Type.STRING;
    }

    @Override
    public Type visitCharLiteralExpr(ZetarianoParser.CharLiteralExprContext ctx) {
        return Type.CHAR;
    }

    @Override
    public Type visitTrueExpr(ZetarianoParser.TrueExprContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitFalseExpr(ZetarianoParser.FalseExprContext ctx) {
        return Type.BOOLEAN;
    }

    @Override
    public Type visitNullExpr(ZetarianoParser.NullExprContext ctx) {
        return Type.NULL;
    }

    @Override
    public Type visitIdExpr(ZetarianoParser.IdExprContext ctx) {
        String name = ctx.ID().getText();
        Symbol s = symbolTable.resolve(name);
        if (s == null) {
            errors.add(Diagnostic.unresolvedSymbol(name, ctx));
            return Type.UNKNOWN;
        }
        return s.getType();
    }

    @Override
    public Type visitAddSubExpr(ZetarianoParser.AddSubExprContext ctx) {
        Type t1 = visit(ctx.expresion(0));
        Type t2 = visit(ctx.expresion(1));
        return TypeCompatibility.checkArithmetic(t1, t2, ctx.PLUS() != null ? "+" : "-");
    }

    @Override
    public Type visitNegExpr(ZetarianoParser.NegExprContext ctx) {
        Type t = visit(ctx.expresion());
        if (Type.isKnown(t) && !t.isNumeric()) {
            errors.add(Diagnostic.nonNumericMinusOperator(ctx));
            return Type.UNKNOWN;
        }
        return Type.isKnown(t) ? t : Type.UNKNOWN;
    }

    @Override
    public Type visitMulDivModExpr(ZetarianoParser.MulDivModExprContext ctx) {
        Type t1 = visit(ctx.expresion(0));
        Type t2 = visit(ctx.expresion(1));
        String op = ctx.MUL() != null ? "*" : (ctx.DIV() != null ? "/" : "%");
        return TypeCompatibility.checkArithmetic(t1, t2, op);
    }

    @Override
    public Type visitRelationalExpr(ZetarianoParser.RelationalExprContext ctx) {
        Type t1 = visit(ctx.expresion(0));
        Type t2 = visit(ctx.expresion(1));
        return TypeCompatibility.checkRelational(t1, t2, "<");
    }

    @Override
    public Type visitEqualityExpr(ZetarianoParser.EqualityExprContext ctx) {
        Type t1 = visit(ctx.expresion(0));
        Type t2 = visit(ctx.expresion(1));
        return TypeCompatibility.checkEquality(t1, t2);
    }

    @Override
    public Type visitTernaryExpr(ZetarianoParser.TernaryExprContext ctx) {
        Type condType = visit(ctx.expresion(0));

        if (Type.isKnown(condType) && condType.getDataType() != DataType.BOOLEAN) {
            errors.add(Diagnostic.nonBooleanCondition("del operador ternario ? :", ctx));
        }
        Type t1 = visit(ctx.expresion(1));
        Type t2 = visit(ctx.expresion(2));
        if (!Type.isKnown(t1) || !Type.isKnown(t2)) {
            return Type.UNKNOWN;
        }
        return t1.equals(t2) ? t1 : (t1.isNumeric() && t2.isNumeric() ? Type.DOUBLE : t1);
    }

    @Override
    public Type visitNewObjectExpr(ZetarianoParser.NewObjectExprContext ctx) {
        String className = ctx.ID().getText();
        Symbol cls = symbolTable.getClass(className);
        if (cls == null) {
            errors.add(Diagnostic.undefinedType(className, ctx));
            return Type.UNKNOWN;
        }
        List<Type> arguments = ctx.argumentos() == null ? List.of() : ctx.argumentos().expresion().stream().map(this::visit).toList();
        Symbol constructor = cls.getCallable(className, arguments.size());
        if (constructor == null) CallValidator.report("No existe un constructor de «" + className + "» con " + arguments.size() + " argumentos", ctx, errors);
        else CallValidator.validate(constructor, arguments, ctx, errors);
        return cls.getType();
    }

    @Override
    public Type visitNewArrayExpr(ZetarianoParser.NewArrayExprContext ctx) {
        int[] sizes = new int[ctx.expresion().size()];
        for (int i = 0; i < sizes.length; i++) {
            visit(ctx.expresion(i));
            String size = ctx.expresion(i).getText();
            sizes[i] = size.matches("[0-9]+") ? Integer.parseInt(size) : Type.UNKNOWN_SIZE;
        }
        return Type.arrayOf(resolveType(ctx.tipoDato().getText()), sizes);
    }

    @Override
    public Type visitLlamadaFuncionSemilla(ZetarianoParser.LlamadaFuncionSemillaContext ctx) {
        if (ctx.PRINT() != null || ctx.PRINTLN() != null) {
            if (ctx.argumentos() != null) ctx.argumentos().expresion().forEach(this::visit);
            return Type.VOID;
        }
        if (ctx.READLN() != null) return Type.UNKNOWN;
        Symbol method = null;
        int count = ctx.argumentos() == null ? 0 : ctx.argumentos().expresion().size();
        String name;
        if (ctx.accesoMiembro() != null) {
            var access = ctx.accesoMiembro();
            Symbol base = symbolTable.resolve(access.ID().getText());
            if (base == null) {
                errors.add(Diagnostic.undeclaredVariable(access.ID().getText(), ctx));
                return Type.UNKNOWN;
            }
            Type type = base.getType();
            for (int i = 0; i < access.miembroAcceso().size() - 1; i++) {
                var step = access.miembroAcceso(i);
                if (step.DOT() != null) {
                    Symbol owner = symbolTable.getClass(type.getCustomTypeName());
                    Symbol field = owner == null ? null : owner.getMember(step.ID().getText());
                    if (field == null) return Type.UNKNOWN;
                    type = field.getType();
                }
                if (!step.expresion().isEmpty()) type = type.desindexar(step.expresion().size());
            }
            name = access.miembroAcceso().getLast().ID().getText();
            Symbol owner = symbolTable.getClass(type.getCustomTypeName());
            method = owner == null ? null : owner.getCallable(name, count);
        } else {
            name = ctx.ID().getText();
            if (currentClass != null) method = currentClass.getCallable(name, count);
            if (method == null) method = symbolTable.getFunction(name);
        }
        if (method == null) {
            errors.add(Diagnostic.undefinedFunction(name, ctx));
            return Type.UNKNOWN;
        }
        CallValidator.validate(method, ctx.argumentos() == null ? List.of()
                : ctx.argumentos().expresion().stream().map(this::visit).toList(), ctx, errors);
        return method.getReturnType() == null ? method.getType() : method.getReturnType();
    }

    @Override
    public Type visitSentenciaFor(ZetarianoParser.SentenciaForContext ctx) {
        symbolTable.enterScope("for_" + ctx.getStart().getStartIndex(), 0, ctx);
        try { return visitChildren(ctx); }
        finally { symbolTable.exitScope(); }
    }

    @Override
    public Type visitAccesoMiembro(ZetarianoParser.AccesoMiembroContext ctx) {
        String baseName = ctx.ID().getText();
        Symbol base = symbolTable.resolve(baseName);
        if (base == null) {
            errors.add(Diagnostic.undeclaredVariable(baseName, ctx));
            return Type.UNKNOWN;
        }

        Type current = base.getType();
        if (current == null) {
            return Type.UNKNOWN;
        }

        int indices = countIndices(ctx);
        if (indices > 0 && !hasIndices(ctx) && current.isArray()
                && indices > current.getDimensions()) {
            errors.add(Diagnostic.indexDimensions(baseName, indices,
                    current.getDimensions(), ctx));
            return Type.UNKNOWN;
        }

        return resolveChain(ctx, baseName, current);
    }

    private Type resolveChain(ZetarianoParser.AccesoMiembroContext ctx,
                              String baseName, Type current) {
        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() != null && current.isClass()) {
                Symbol cls = symbolTable.getClass(current.getCustomTypeName());
                if (cls == null) {
                    return Type.UNKNOWN;
                }
                Symbol member = cls.getMember(mac.ID().getText());
                if (member == null) {
                    return Type.UNKNOWN;
                }
                current = member.getType();
            }
            for (ZetarianoParser.ExpresionContext index : mac.expresion()) {
                Type indexType = visit(index);
                if (Type.isKnown(indexType) && !Type.INT.equals(indexType)) {
                    CallValidator.report("El índice de «" + baseName
                            + "» debe ser entero", index, errors);
                }
                if (!current.isArray()) {
                    errors.add(Diagnostic.notAnArray(baseName, current, ctx));
                    return Type.UNKNOWN;
                }
                checkIndex(index, current, baseName);
                current = current.desindexar(1);
            }
        }
        return current;
    }

    private void checkIndex(ZetarianoParser.ExpresionContext index, Type array,
                                 String name) {

        String text = index.getText();
        if (!text.matches("-?\\d+")) {
            return;
        }
        int[] sizes = array.getSizes();
        int valor = Integer.parseInt(text);
        if (sizes.length > 0 && sizes[0] > 0 && (valor < 0 || valor >= sizes[0])) {
            errors.add(Diagnostic.indexOutOfRange(name, valor, sizes[0], index));
        }
    }

    private void defineThis(ParserRuleContext owner) {
        if (currentClass != null) {
            symbolTable.define(new Symbol("this", currentClass.getType(),
                    SymbolCategory.VARIABLE, 0, false).at(owner));
        }
    }

    private void registerCallable(String name, Type type, SymbolCategory category,
                                  ZetarianoParser.ParametrosContext parameters, ParserRuleContext context) {
        int count = parameters == null ? 0 : parameters.parametro().size();
        Symbol method = new Symbol(name, type, category).at(context);
        method.setReturnType(type);
        int index = 1;
        if (parameters != null) for (var parameter : parameters.parametro()) {
            Symbol symbol = new Symbol(parameter.ID().getText(), resolveType(parameter.tipoDato().getText()),
                    SymbolCategory.PARAMETER, index++, false).at(parameter);
            symbol.setScope((category == SymbolCategory.CONSTRUCTOR ? "constructor_" : "method_")
                    + FunctionRuntime.name(currentClass.getName(), name, count));
            method.addParameter(symbol);
        }
        currentClass.addMember(method);
    }

    private Type resolveType(String text) {
        if (text == null) return Type.UNKNOWN;
        switch (text) {
            case "int":
                return Type.INT;
            case "double":
                return Type.DOUBLE;
            case "char":
                return Type.CHAR;
            case "boolean":
                return Type.BOOLEAN;
            case "String":
                return Type.STRING;
            case "void":
                return Type.VOID;
            default:
                return Type.classType(text);
        }
    }
}
