package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
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

        symbolTable.enterScope("class_" + className, 0);

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

        Symbol ctorSymbol = new Symbol(name, Type.VOID, SymbolCategory.CONSTRUCTOR).at(ctx);

        symbolTable.enterScope("constructor_" + name, 1);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                String pName = pCtx.ID().getText();
                Type pType = resolveType(pCtx.tipoDato().getText());
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol pSym = new Symbol(pName, pType, SymbolCategory.PARAMETER, offset, false)
                        .at(pCtx);
                ctorSymbol.addParameter(pSym);
                symbolTable.define(pSym);
            }
        }

        if (currentClass != null) {
            currentClass.addMember(ctorSymbol);
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

        Symbol methodSymbol = new Symbol(methodName, returnType, SymbolCategory.METHOD).at(ctx);
        methodSymbol.setReturnType(returnType);

        symbolTable.enterScope("method_" + methodName, 1);
        defineThis(ctx);

        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                String pName = pCtx.ID().getText();
                Type pType = resolveType(pCtx.tipoDato().getText());
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol pSym = new Symbol(pName, pType, SymbolCategory.PARAMETER, offset, false)
                        .at(pCtx);
                methodSymbol.addParameter(pSym);
                symbolTable.define(pSym);
            }
        }

        if (currentClass != null) {
            currentClass.addMember(methodSymbol);
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

        if (dimensiones > 0 && !varType.hasSizes()) {
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

            return Type.classType(className);
        }
        return cls.getType();
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

        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() != null) {
                String memberName = mac.ID().getText();
                if (current.isClass()) {
                    Symbol cls = symbolTable.getClass(current.getCustomTypeName());
                    if (cls != null) {
                        Symbol member = cls.getMember(memberName);
                        if (member != null) {

                            current = member.getType();
                        }
                    }
                }
            } else {
                for (ZetarianoParser.ExpresionContext index : mac.expresion()) {
                    visit(index);
                    if (!current.isArray()) {
                        errors.add(Diagnostic.notAnArray(baseName, current, ctx));
                        return Type.UNKNOWN;
                    }
                    checkIndex(index, current, baseName);
                    current = current.desindexar(1);
                }
            }
        }
        return current;
    }

    private void checkIndex(ZetarianoParser.ExpresionContext index, Type array,
                                 String name) {

        String text = index.getText();
        if (!text.matches("\\d+")) {
            return;
        }
        int[] sizes = array.getSizes();
        int valor = Integer.parseInt(text);
        if (sizes.length > 0 && sizes[0] > 0 && valor >= sizes[0]) {
            errors.add(Diagnostic.indexOutOfRange(name, valor, sizes[0], index));
        }
    }

    private void defineThis(ParserRuleContext owner) {
        if (currentClass != null) {
            symbolTable.define(new Symbol("this", currentClass.getType(),
                    SymbolCategory.VARIABLE, 0, false).at(owner));
        }
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
