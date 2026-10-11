package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.ArrayRuntime;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.c3d.HeapRuntime;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.List;

/** Emite clases, constructores, métodos y accesos a atributos de Zetariano. */
final class ZetarianoClassEmitter {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private final ZetarianoBaseVisitor<String> visitor;
    private final ZetarianoArrayEmitter arrays;
    private String currentClassName = "";
    private ZetarianoParser.ClaseDefContext currentClass;

    ZetarianoClassEmitter(SymbolTable symbolTable, C3DGenerator c3d,
                         ZetarianoBaseVisitor<String> visitor, ZetarianoArrayEmitter arrays) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
        this.visitor = visitor;
        this.arrays = arrays;
    }

    String currentClassName() {
        return currentClassName;
    }

    String emitClass(ZetarianoParser.ClaseDefContext ctx) {
        currentClassName = ctx.ID().getText();
        currentClass = ctx;
        symbolTable.enterScope("class_" + currentClassName, 0, ctx);
        int offset = 0;
        for (ZetarianoParser.MiembroClaseContext member : ctx.miembroClase()) {
            if (member.atributoDef() == null) continue;
            symbolTable.define(new Symbol(member.atributoDef().ID().getText(), typeOfField(member.atributoDef()),
                    SymbolCategory.FIELD, offset++, false).at(member.atributoDef()).markWorking());
        }
        for (ZetarianoParser.MiembroClaseContext member : ctx.miembroClase()) {
            if (member.atributoDef() == null) visitor.visit(member);
        }
        symbolTable.exitScope();
        currentClassName = "";
        return null;
    }

    void store(Symbol symbol, String value) {
        if (symbol.getCategory() == SymbolCategory.FIELD) {
            c3d.emit(QuadrupleOp.HEAP_SET, fieldAddress(symbol), value, null);
        } else c3d.emitAssign(symbol.getStorageName(), value);
    }

    String load(Symbol symbol) {
        return symbol.getCategory() == SymbolCategory.FIELD
                ? HeapRuntime.read(c3d, fieldAddress(symbol)) : symbol.getStorageName();
    }

    private String fieldAddress(Symbol field) {
        return ArrayRuntime.add(c3d, symbolTable.resolve("this").getStorageName(),
                HeapRuntime.fieldOffset(symbolTable.getClass(currentClassName), field.getName()));
    }

    private void defineParameter(ZetarianoParser.ParametroContext ctx) {
        Type type = Type.UNKNOWN;
        if (ctx.tipoDato() != null) {
            String name = ctx.tipoDato().getText();
            type = switch (name) {
                case "int" -> Type.INT;
                case "double" -> Type.DOUBLE;
                case "boolean" -> Type.BOOLEAN;
                case "char" -> Type.CHAR;
                case "String" -> Type.STRING;
                default -> symbolTable.getClass(name) != null ? Type.classType(name) : Type.UNKNOWN;
            };
        }
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        symbolTable.define(new Symbol(ctx.ID().getText(), type,
                SymbolCategory.PARAMETER, offset, false).at(ctx).markWorking());
    }

    private Type typeOfField(ZetarianoParser.AtributoDefContext ctx) {
        Type type = Type.UNKNOWN;
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            Symbol member = cls.getMember(ctx.ID().getText());
            if (member != null && member.getType() != null) type = member.getType();
        }
        if (type.isArray() && !type.hasSizes()
                && ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext array) {
            type = Type.arrayOf(type.getElementType(), arrays.shapeOf(array.literalArreglo()));
        }
        return type;
    }

    private void defineThis(ParserRuleContext owner) {
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            symbolTable.define(new Symbol("this", cls.getType(), SymbolCategory.VARIABLE, 0, false)
                    .at(owner).markWorking());
        }
    }

    String emitConstructor(ZetarianoParser.ConstructorDefContext ctx) {
        String name = FunctionRuntime.name(currentClassName, ctx.ID().getText(),
                ctx.parametros() == null ? 0 : ctx.parametros().parametro().size());
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, name);
        symbolTable.enterScope("constructor_" + name, 1, ctx);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (var parameter : ctx.parametros().parametro()) defineParameter(parameter);
        }
        for (var member : currentClass.miembroClase()) {
            if (member.atributoDef() != null) visitor.visit(member.atributoDef());
        }
        visitor.visit(ctx.bloque());
        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, name);
        symbolTable.exitScope();
        return null;
    }

    String emitMethod(ZetarianoParser.MetodoDefContext ctx) {
        String name = FunctionRuntime.name(currentClassName, ctx.ID().getText(),
                ctx.parametros() == null ? 0 : ctx.parametros().parametro().size());
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, name);
        symbolTable.enterScope("method_" + name, 1, ctx);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (var parameter : ctx.parametros().parametro()) defineParameter(parameter);
        }
        visitor.visit(ctx.bloque());
        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, name);
        symbolTable.exitScope();
        return null;
    }

    String emitField(ZetarianoParser.AtributoDefContext ctx) {
        if (ctx.expresion() == null) return null;
        Symbol field = symbolTable.resolve(ctx.ID().getText());
        if (field != null) store(field, visitor.visit(ctx.expresion()));
        return null;
    }

    String accessAddress(ZetarianoParser.AccesoMiembroContext access, int limit) {
        Symbol base = symbolTable.resolve(access.ID().getText());
        Type type = base.getType();
        String value = load(base);
        String address = value;
        for (int i = 0; i < limit; i++) {
            var step = access.miembroAcceso(i);
            if (step.DOT() != null) {
                Symbol owner = HeapRuntime.definition(symbolTable, type);
                address = ArrayRuntime.add(c3d, value, HeapRuntime.fieldOffset(owner, step.ID().getText()));
                type = owner.getMember(step.ID().getText()).getType();
                if (!step.expresion().isEmpty() || i + 1 < limit) value = HeapRuntime.read(c3d, address);
            }
            if (!step.expresion().isEmpty()) {
                List<String> indices = step.expresion().stream().map(visitor::visit).toList();
                address = ArrayRuntime.address(c3d, value, indices, type.getSizes());
                type = type.desindexar(indices.size());
                if (i + 1 < limit) value = type.isArray() ? address : HeapRuntime.read(c3d, address);
            }
        }
        return address;
    }
}
