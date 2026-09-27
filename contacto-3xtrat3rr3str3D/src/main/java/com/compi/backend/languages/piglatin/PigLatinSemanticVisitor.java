package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class PigLatinSemanticVisitor extends PigLatinBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();

    private int inLoop;

    public PigLatinSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitCiclo_dum(PigLatinParser.Ciclo_dumContext ctx) {
        return visitBody(ctx.condicion(), ctx.sentencia());
    }

    @Override
    public Type visitCiclo_facere(PigLatinParser.Ciclo_facereContext ctx) {
        return visitBody(ctx.condicion(), ctx.sentencia());
    }

    private Type visitBody(PigLatinParser.CondicionContext condition,
                              List<PigLatinParser.SentenciaContext> sentencias) {
        if (condition != null) {
            visit(condition);
        }
        inLoop++;
        try {
            for (PigLatinParser.SentenciaContext s : sentencias) {
                visit(s);
            }
        } finally {
            inLoop--;
        }
        return Type.VOID;
    }

    @Override
    public Type visitVariables(PigLatinParser.VariablesContext ctx) {
        for (PigLatinParser.DeclaracionContext dCtx : ctx.declaracion()) {
            visit(dCtx);
        }
        for (PigLatinParser.Arreglo_declaracionContext aCtx : ctx.arreglo_declaracion()) {
            visit(aCtx);
        }
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion(PigLatinParser.DeclaracionContext ctx) {
        if (ctx.arreglo_declaracion() != null) {
            return visit(ctx.arreglo_declaracion());
        }
        if (!ctx.VARIABLE().isEmpty()) {
            String varName = ctx.VARIABLE(0).getText();
            Type varType = Type.UNKNOWN;

            if (ctx.tipo_dato() != null) {
                varType = resolveType(ctx.tipo_dato().getText());
            } else if (ctx.TEXTUM() != null) {
                varType = Type.STRING;
            } else if (ctx.LITTERA() != null) {
                varType = Type.CHAR;
            } else if (ctx.NOVUS() != null && ctx.VARIABLE().size() > 1) {
                varType = Type.classType(ctx.VARIABLE(1).getText());
            } else if (ctx.VARIABLE().size() > 1) {
                varType = resolveType(ctx.VARIABLE(1).getText());
            }

            int offset = symbolTable.getGlobalScope().allocateOffset(1);
            Symbol sym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, true)
                    .at(ctx);

            if (!symbolTable.defineGlobal(sym)) {
                errors.add(Diagnostic.globalAlreadyDeclared(varName, ctx));
            }

            if (ctx.expresion() != null) {
                Type exprType = visit(ctx.expresion());
                if (Type.isKnown(exprType) && Type.isKnown(varType)
                        && !exprType.isAssignableTo(varType)) {
                    errors.add(Diagnostic.inicializacionIncompatible(
                            varName, varType, exprType, ctx));
                }
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visitArreglo_declaracion(PigLatinParser.Arreglo_declaracionContext ctx) {
        String arrayName = ctx.VARIABLE(0).getText();
        Type elemType = Type.INT;
        if (ctx.tipo_dato() != null) {
            elemType = resolveType(ctx.tipo_dato().getText());
        } else if (ctx.VARIABLE().size() > 1) {
            elemType = Type.structType(ctx.VARIABLE(1).getText());
        }

        int[] sizes = new int[ctx.NUMERO_ENTERO().size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = Integer.parseInt(ctx.NUMERO_ENTERO(i).getText());
        }
        Type arrayType = Type.arrayOf(elemType, sizes);

        if (ctx.elemento_arreglo() != null) {
            Type obtenido = arrayLiteralType(arrayName, ctx.elemento_arreglo());
            CompilationError error = ArrayLiteral.validate(arrayName, arrayType, obtenido, ctx);
            if (error != null) {
                errors.add(error);
            }
        }

        int offset = symbolTable.getGlobalScope().allocateOffset(1);
        Symbol sym = new Symbol(arrayName, arrayType, SymbolCategory.VARIABLE, offset, true)
                .at(ctx);
        symbolTable.defineGlobal(sym);
        return Type.VOID;
    }

    private Type arrayLiteralType(String name,
                                   PigLatinParser.Elemento_arregloContext ctx) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        int filas = valores.size();
        Type rowType = Type.UNKNOWN;
        int[] rowShape = new int[0];
        for (int f = 0; f < filas; f++) {
            Type type = arrayValueType(name, valores.get(f));
            int[] shape = type.isArray() ? type.getSizes() : new int[0];
            if (f == 0) {
                rowType = type.isArray() ? type.getElementType() : type;
                rowShape = shape;
            } else if (!java.util.Arrays.equals(shape, rowShape)) {
                errors.add(Diagnostic.filasDesiguales(name, f + 1,
                        widthOf(rowShape, valores.get(0)), widthOf(shape, valores.get(f)),
                        valores.get(f)));
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = filas;
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return Type.arrayOf(rowType, sizes);
    }

    private static int widthOf(int[] shape, PigLatinParser.Elemento_arreglo_valorContext row) {
        if (shape.length > 0) {
            return shape[0];
        }
        if (row.LLAVE_IZQ() == null) {
            return 1;
        }
        return row.elemento_arreglo() == null ? 0 : row.elemento_arreglo().elemento_arreglo_valor().size();
    }

    private Type arrayValueType(String name,
                                    PigLatinParser.Elemento_arreglo_valorContext ctx) {
        if (ctx.LLAVE_IZQ() == null) {
            return visit(ctx.expresion());
        }
        if (ctx.elemento_arreglo() == null) {
            return Type.arrayOf(Type.UNKNOWN, new int[]{0});
        }
        return arrayLiteralType(name, ctx.elemento_arreglo());
    }

    @Override
    public Type visitMaior(PigLatinParser.MaiorContext ctx) {
        symbolTable.enterScope("maior", 0);

        for (PigLatinParser.SentenciaContext sCtx : ctx.sentencia()) {
            visit(sCtx);
        }

        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx) {
        Type targetType = Type.UNKNOWN;
        if (!ctx.VARIABLE().isEmpty()) {
            String name = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostic.undeclaredVariable(name, ctx));
            } else {
                targetType = s.getType();
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            Type exprType = visit(ctx.expresion());
            if (Type.isKnown(targetType) && Type.isKnown(exprType)
                    && !exprType.isAssignableTo(targetType)) {
                errors.add(Diagnostic.incompatibleAssignment(targetType, exprType, ctx));
            }
        }
        return targetType;
    }

    @Override
    public Type visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.suma_resta().isEmpty()) {
            return visit(ctx.producto(0));
        }

        Type t1 = visit(ctx.producto(0));
        for (int i = 1; i < ctx.producto().size(); i++) {
            Type t2 = visit(ctx.producto(i));
            t1 = TypeCompatibility.checkArithmetic(t1, t2, ctx.suma_resta(i - 1).getText());
        }
        return t1;
    }

    @Override
    public Type visitProducto(PigLatinParser.ProductoContext ctx) {
        if (ctx.multiplicacion().isEmpty()) {
            return visit(ctx.termino(0));
        }

        Type t1 = visit(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            Type t2 = visit(ctx.termino(i));
            t1 = TypeCompatibility.checkArithmetic(t1, t2, ctx.multiplicacion(i - 1).getText());
        }
        return t1;
    }

    @Override
    public Type visitTermino(PigLatinParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return Type.INT;
        if (ctx.NUMERO_DECIMAL() != null) return Type.DOUBLE;
        if (ctx.CADENA_TEXTO() != null) return Type.STRING;
        if (ctx.CARACTER() != null) return Type.CHAR;
        if (ctx.VERUM() != null || ctx.FALSUS() != null) return Type.BOOLEAN;

        if (ctx.VARIABLE() != null) {
            String varName = ctx.VARIABLE().getText();
            Symbol s = symbolTable.resolve(varName);
            if (s == null) {
                errors.add(Diagnostic.unresolvedSymbol(varName, ctx));
                return Type.UNKNOWN;
            }
            return s.getType();
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.NOVUS() != null) {
            String className = ctx.VARIABLE().getText();
            return Type.classType(className);
        }

        if (ctx.MENOS() != null) {
            Type t = visit(ctx.termino());
            if (t != null && t != Type.UNKNOWN && !t.isNumeric()) {
                errors.add(Diagnostic.nonNumericMinusOperator(ctx));
                return Type.UNKNOWN;
            }
            return t == null ? Type.UNKNOWN : t;
        }

        return Type.UNKNOWN;
    }

    @Override
    public Type visitAcceso_miembro(PigLatinParser.Acceso_miembroContext ctx) {
        String baseName = ctx.VARIABLE(0).getText();
        Symbol base = symbolTable.resolve(baseName);
        if (base == null) {
            errors.add(Diagnostic.undeclaredVariable(baseName, ctx));
            return Type.UNKNOWN;
        }
        Type actual = base.getType();
        if (actual == null) {
            return Type.UNKNOWN;
        }

        int indices = ctx.CORCHETE_IZQ().size();
        if (indices > 0 && actual.isArray() && indices > actual.getDimensions()) {
            errors.add(Diagnostic.indexDimensions(baseName, indices,
                    actual.getDimensions(), ctx));
            return Type.UNKNOWN;
        }

        for (int i = 1; i < ctx.getChildCount() && actual != null; i++) {
            ParseTree child = ctx.getChild(i);

            if (child.getText().equals(".")) {
                String memberName = ctx.getChild(++i).getText();
                Symbol member = findMember(actual, memberName);
                if (member == null) {
                    if (hasKnownMembers(actual)) {
                        errors.add(Diagnostic.memberDoesNotExist(memberName, actual.label(), ctx));
                    }
                    return Type.UNKNOWN;
                }
                actual = member.getType();
            } else if (child instanceof PigLatinParser.ExpresionContext) {
                visit(child);
                if (!actual.isArray()) {
                    errors.add(Diagnostic.notAnArray(baseName, actual, ctx));
                    return Type.UNKNOWN;
                }
                checkIndex(child, actual, baseName);
                actual = actual.desindexar(1);
            }
        }
        return actual == null ? Type.UNKNOWN : actual;
    }


    private void checkIndex(ParseTree index, Type array, String name) {
        int[] sizes = array.getSizes();
        String text = index.getText();
        if (sizes.length == 0 || sizes[0] <= 0 || !text.matches("\\d+")) {
            return;
        }
        int valor = Integer.parseInt(text);
        if (valor >= sizes[0]) {
            errors.add(Diagnostic.indexOutOfRange(name, valor, sizes[0],
                    (PigLatinParser.ExpresionContext) index));
        }
    }

    private Symbol findMember(Type container, String memberName) {
        Symbol type = declaredType(container);
        return type == null ? null : type.getMember(memberName);
    }

    private boolean hasKnownMembers(Type container) {
        Symbol symbol = declaredType(container);
        return symbol != null && !symbol.getMembers().isEmpty();
    }

    private Symbol declaredType(Type type) {
        if (type == null || type.getCustomTypeName() == null) {
            return null;
        }
        Symbol symbol = symbolTable.getClass(type.getCustomTypeName());
        return symbol != null ? symbol : symbolTable.getStruct(type.getCustomTypeName());
    }

    @Override
    public Type visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        if (ctx.acceso_miembro() != null) {
            visit(ctx.acceso_miembro());
            return Type.INT;
        }
        String funcName = ctx.VARIABLE().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            return Type.INT;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    @Override
    public Type visitInicializacion_per(PigLatinParser.Inicializacion_perContext ctx) {
        if (ctx.ESTO() != null && ctx.VARIABLE() != null) {
            String name = ctx.VARIABLE().getText();
            Type type = ctx.tipo_dato() != null ? resolveType(ctx.tipo_dato().getText()) : Type.INT;
            if (symbolTable.resolve(name) == null) {
                int offset = symbolTable.getGlobalScope().allocateOffset(1);
                symbolTable.defineGlobal(
                        new Symbol(name, type, SymbolCategory.VARIABLE, offset, true)
                                .at(ctx));
            }
            if (ctx.expresion() != null) {
                visit(ctx.expresion());
            }
            return type;
        }
        return visit(ctx.expresion());
    }

    @Override
    public Type visitCondiciones_per(PigLatinParser.Condiciones_perContext ctx) {
        if (ctx.condicion() != null) {
            return visit(ctx.condicion());
        }
        return Type.BOOLEAN;
    }

    @Override
    public Type visitIncremento_per(PigLatinParser.Incremento_perContext ctx) {
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
        }
        return Type.INT;
    }

    @Override
    public Type visitCiclo_per(PigLatinParser.Ciclo_perContext ctx) {
        visit(ctx.inicializacion_per());
        visit(ctx.condiciones_per());
        visit(ctx.incremento_per());
        inLoop++;
        try {
            for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
                visit(s);
            }
        } finally {
            inLoop--;
        }
        return Type.VOID;
    }

    @Override
    public Type visitSalto_sentencia(PigLatinParser.Salto_sentenciaContext ctx) {
        if (inLoop == 0) {
            String word = ctx.PERGE() != null ? "perge" : "interrumpe";
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "«" + word + "» solo se puede usar dentro de un ciclo"
                            + " (dum, facere o per)",
                    ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        }
        return Type.VOID;
    }

    private Type resolveType(String text) {
        if (text == null) return Type.UNKNOWN;
        switch (text.toLowerCase()) {
            case "numerus": return Type.INT;
            case "decimalis": return Type.DOUBLE;
            case "textum": return Type.STRING;
            case "littera": return Type.CHAR;
            case "verum":
            case "falsus": return Type.BOOLEAN;
            default:
                if (symbolTable.getStruct(text) != null) return Type.structType(text);
                if (symbolTable.getClass(text) != null) return Type.classType(text);
                return Type.UNKNOWN;
        }
    }

}
