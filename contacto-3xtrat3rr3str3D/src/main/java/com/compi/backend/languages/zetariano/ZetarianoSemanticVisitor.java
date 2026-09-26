package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.ParserRuleContext;

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
        // Primera pasada: recolectar atributos
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.atributoDef() != null) {
                ZetarianoParser.AtributoDefContext attrCtx = mc.atributoDef();
                String attrName = attrCtx.ID().getText();
                Type attrType = resolveType(attrCtx.tipoDato().getText());
                Symbol attrSym = new Symbol(attrName, attrType, SymbolCategory.FIELD,
                        fieldOffset++, false).at(attrCtx);
                currentClass.addMember(attrSym);
                symbolTable.define(attrSym);
            }
        }

        // Segunda pasada: métodos y constructores
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
            errors.add(Diagnostico.nombreDeConstructorErroneo(
                    name, currentClass.getName(), ctx));
        }

        Symbol ctorSymbol = new Symbol(name, Type.VOID, SymbolCategory.CONSTRUCTOR).at(ctx);

        symbolTable.enterScope("constructor_" + name, 1); // 0 es 'this'
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

        symbolTable.enterScope("method_" + methodName, 1); // offset 0 reservado para 'this'
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
        Type varType = resolveType(typeStr);
        String varName = ctx.ID().getText();

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol varSym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, false).at(ctx);

        if (!symbolTable.define(varSym)) {
            errors.add(Diagnostico.variableYaDeclarada(varName, ctx));
        }

        if (ctx.expresion() != null) {
            Type initType = visit(ctx.expresion());
            if (esConocido(initType) && esConocido(varType)
                    && !initType.isAssignableTo(varType)) {
                errors.add(Diagnostico.inicializacionIncompatible(
                        varName, varType, initType, ctx));
            }
        }

        return varType;
    }

    @Override
    public Type visitAsignacion(ZetarianoParser.AsignacionContext ctx) {
        Type targetType = Type.UNKNOWN;
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostico.variableNoDeclarada(name, ctx));
            } else {
                targetType = s.getType();
            }
        } else if (ctx.accesoMiembro() != null) {
            targetType = visit(ctx.accesoMiembro());
        }

        Type exprType = visit(ctx.expresion(ctx.expresion().size() - 1));
        if (esConocido(targetType) && esConocido(exprType)
                && !exprType.isAssignableTo(targetType)) {
            errors.add(Diagnostico.asignacionIncompatible(targetType, exprType, ctx));
        }

        return targetType;
    }

    @Override
    public Type visitRetorno(ZetarianoParser.RetornoContext ctx) {
        Type retType = Type.VOID;
        if (ctx.expresion() != null) {
            retType = visit(ctx.expresion());
        }

        if (esConocido(currentMethodReturnType) && esConocido(retType)
                && !retType.isAssignableTo(currentMethodReturnType)) {
            errors.add(Diagnostico.tipoDeRetornoErroneo(
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
            errors.add(Diagnostico.simboloNoResuelto(name, ctx));
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
        // Con una condicion que ya fallo el tipo puede venir nulo: el error ya
        // esta reportado y aqui solo habria un NullPointerException.
        if (esConocido(condType) && condType.getDataType() != DataType.BOOLEAN) {
            errors.add(Diagnostico.condicionNoBooleana("del operador ternario ? :", ctx));
        }
        Type t1 = visit(ctx.expresion(1));
        Type t2 = visit(ctx.expresion(2));
        if (!esConocido(t1) || !esConocido(t2)) {
            return Type.UNKNOWN;
        }
        return t1.equals(t2) ? t1 : (t1.isNumeric() && t2.isNumeric() ? Type.DOUBLE : t1);
    }

    @Override
    public Type visitNewObjectExpr(ZetarianoParser.NewObjectExprContext ctx) {
        String className = ctx.ID().getText();
        Symbol cls = symbolTable.getClass(className);
        if (cls == null) {
            // Permitir auto-referencia o registro
            return Type.classType(className);
        }
        return cls.getType();
    }

    @Override
    public Type visitAccesoMiembro(ZetarianoParser.AccesoMiembroContext ctx) {
        String baseName = ctx.ID().getText();
        Symbol base = symbolTable.resolve(baseName);
        if (base == null) {
            errors.add(Diagnostico.variableNoDeclarada(baseName, ctx));
            return Type.UNKNOWN;
        }

        Type current = base.getType();
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
            }
        }
        return current;
    }

    /**
     * Declara la referencia implicita {@code this} en el ambito actual.
     *
     * El offset 0 queda reservado para ella, de modo que
     * {@code this.campo} se resuelve igual que cualquier acceso a miembro.
     *
     * <p>{@code this} no aparece escrito en el fuente, asi que se anota con la
     * posicion del metodo o constructor al que pertenece: es lo mas cercano que
     * hay a "donde se declara", y evita que la tabla de simbolos tenga filas sin
     * linea a las que no se pueda saltar.</p>
     */
    private void defineThis(ParserRuleContext owner) {
        if (currentClass != null) {
            symbolTable.define(new Symbol("this", currentClass.getType(),
                    SymbolCategory.VARIABLE, 0, false).at(owner));
        }
    }

    private Type resolveType(String text) {        if (text == null) return Type.UNKNOWN;
        if (text.endsWith("[]")) {
            String base = text.substring(0, text.length() - 2);
            return Type.array(resolveType(base), 1);
        }
        switch (text) {
            case "int": return Type.INT;
            case "double": return Type.DOUBLE;
            case "char": return Type.CHAR;
            case "boolean": return Type.BOOLEAN;
            case "String": return Type.STRING;
            case "void": return Type.VOID;
            default:
                return Type.classType(text);
        }
    }

    /**
     * true si el tipo se conoce de verdad.
     *
     * <p>Un tipo desconocido o nulo significa que la expresion que lo produce ya
     * fallo: el aviso de verdad se dio ahi. Comprobar los tipos con un
     * desconocido daria un segundo error, mas corto y mas generico, que solo
     * tapa el primero.</p>
     */
    private static boolean esConocido(Type t) {
        return t != null && t != Type.UNKNOWN;
    }
}
