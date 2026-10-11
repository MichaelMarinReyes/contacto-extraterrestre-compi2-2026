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

            // Tipo anotado explícitamente: tipo_dato (numerus, textum,
            // decimalis, littera, bool), texto/caracter literal, «novus Clase»
            // o un nombre de estructura/clase definido en un archivo importado.
            boolean hasDeclaredType = true;
            Type declaredType;
            if (ctx.tipo_dato() != null) {
                declaredType = resolveType(ctx.tipo_dato().getText());
            } else if (ctx.TEXTUM() != null) {
                declaredType = Type.STRING;
            } else if (ctx.LITTERA() != null) {
                declaredType = Type.CHAR;
            } else if (ctx.NOVUS() != null && ctx.VARIABLE().size() > 1) {
                declaredType = Type.classType(ctx.VARIABLE(1).getText());
            } else if (ctx.VARIABLE().size() > 1) {
                declaredType = resolveType(ctx.VARIABLE(1).getText());
            } else {
                declaredType = Type.UNKNOWN;
                hasDeclaredType = false;
            }

            // Tipo del valor inicializador. Una condición lógica (por ejemplo
            // «verum || 1 == 1») también produce un booleano.
            Type valueType = null;
            if (ctx.expresion() != null) {
                valueType = visit(ctx.expresion());
            } else if (ctx.condicion() != null) {
                visit(ctx.condicion());
                valueType = Type.BOOLEAN;
            }

            // «esto x : falsus» / «esto x : verum»: sin tipo anotado (o con un
            // tipo no reconocido) se infiere el tipo a partir del valor.
            Type varType = hasDeclaredType && Type.isKnown(declaredType)
                    ? declaredType
                    : (valueType != null && Type.isKnown(valueType)
                            ? valueType
                            : (hasDeclaredType ? declaredType : Type.UNKNOWN));

            int offset = symbolTable.getGlobalScope().allocateOffset(1);
            Symbol sym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, true)
                    .at(ctx);

            if (!symbolTable.defineGlobal(sym)) {
                errors.add(Diagnostic.globalAlreadyDeclared(varName, ctx));
            }

            if (hasDeclaredType && valueType != null && Type.isKnown(declaredType)
                    && Type.isKnown(valueType) && !valueType.isAssignableTo(declaredType)) {
                errors.add(Diagnostic.inicializacionIncompatible(
                        varName, declaredType, valueType, ctx));
            }
            if (ctx.NOVUS() != null) validateConstructor(ctx.VARIABLE(1).getText(), ctx.argumentos(), ctx);
            if (ctx.structura_instanciacion() != null) validateStruct(varType, ctx.structura_instanciacion());
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
            elemType = resolveType(ctx.VARIABLE(1).getText());
            if (!Type.isKnown(elemType)) errors.add(Diagnostic.undefinedType(ctx.VARIABLE(1).getText(), ctx));
        }

        int[] sizes = new int[ctx.NUMERO_ENTERO().size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = Integer.parseInt(ctx.NUMERO_ENTERO(i).getText());
            if (sizes[i] <= 0) CallValidator.report("Las dimensiones del arreglo deben ser positivas", ctx, errors);
        }
        Type arrayType = Type.arrayOf(elemType, sizes);

        if (ctx.elemento_arreglo() != null) {
            Type obtenido = arrayLiteralType(arrayName, ctx.elemento_arreglo());
            CompilationError error = ArrayLiteral.validate(arrayName, arrayType, obtenido, ctx);
            if (error != null) {
                errors.add(error);
            }
        }
        if (ctx.elemento_arreglo_struct() != null) {
            var elements = ctx.elemento_arreglo_struct().structura_instanciacion();
            if (elements.size() != arrayType.totalSize())
                CallValidator.report("La cantidad de objetos no coincide con el tamaño del arreglo «" + arrayName + "»", ctx, errors);
            for (var element : elements) validateStruct(elemType, element);
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
            Type element = type.isArray() ? type.getElementType() : type;
            if (f > 0 && Type.isKnown(rowType) && Type.isKnown(element)) {
                if (rowType.isNumeric() && element.isNumeric()) {
                    if (Type.DOUBLE.equals(element)) rowType = Type.DOUBLE;
                } else if (!element.equals(rowType)) {
                    CallValidator.report("El arreglo «" + name + "» contiene elementos de tipos incompatibles", valores.get(f), errors);
                }
            }
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
        symbolTable.enterScope("maior", 0, ctx);

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
        if (ctx.NOVUS() != null) validateConstructor(ctx.VARIABLE(1).getText(), ctx.argumentos(), ctx);
        if (ctx.structura_instanciacion() != null) validateStruct(targetType, ctx.structura_instanciacion());
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
        if (ctx.NOVUS() != null) {
            validateConstructor(ctx.VARIABLE().getText(), ctx.argumentos(), ctx);
            return Type.classType(ctx.VARIABLE().getText());
        }
        if (ctx.expresion() != null) return visit(ctx.expresion());
        if (ctx.acceso_miembro() != null) return visit(ctx.acceso_miembro());
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
                Type indexType = visit(child);
                if (Type.isKnown(indexType) && !Type.INT.equals(indexType))
                    CallValidator.report("El índice de «" + baseName + "» debe ser entero", (PigLatinParser.ExpresionContext) child, errors);
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
        if (sizes.length == 0 || sizes[0] <= 0 || !text.matches("-?\\d+")) {
            return;
        }
        int valor = Integer.parseInt(text);
        if (valor < 0 || valor >= sizes[0]) {
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
        List<Type> arguments = ctx.argumentos() == null ? List.of()
                : ctx.argumentos().expresion().stream().map(this::visit).toList();
        Symbol function;
        if (ctx.acceso_miembro() != null) {
            var access = ctx.acceso_miembro();
            Symbol base = symbolTable.resolve(access.VARIABLE(0).getText());
            if (base == null) {
                errors.add(Diagnostic.undeclaredVariable(access.VARIABLE(0).getText(), ctx));
                return Type.UNKNOWN;
            }
            Type type = base.getType();
            for (int i = 1; i < access.getChildCount() - 2;) {
                if (".".equals(access.getChild(i).getText())) {
                    Symbol member = findMember(type, access.getChild(i + 1).getText());
                    if (member == null) return Type.UNKNOWN;
                    type = member.getType();
                    i += 2;
                } else {
                    visit(access.getChild(i + 1));
                    if (!type.isArray()) return Type.UNKNOWN;
                    type = type.desindexar(1);
                    i += 3;
                }
            }
            Symbol owner = declaredType(type);
            String method = access.VARIABLE(access.VARIABLE().size() - 1).getText();
            function = owner == null ? null : owner.getCallable(method, arguments.size());
        } else {
            function = symbolTable.getFunction(ctx.VARIABLE().getText());
        }
        if (function == null) {
            errors.add(Diagnostic.undefinedFunction(ctx.getChild(0).getText(), ctx));
            return Type.UNKNOWN;
        }
        CallValidator.validate(function, arguments, ctx, errors);
        return function.getReturnType() != null ? function.getReturnType() : function.getType();
    }

    private void validateConstructor(String name, PigLatinParser.ArgumentosContext arguments,
                                     org.antlr.v4.runtime.ParserRuleContext context) {
        Symbol owner = symbolTable.getClass(name);
        List<Type> types = arguments == null ? List.of() : arguments.expresion().stream().map(this::visit).toList();
        if (owner == null) {
            errors.add(Diagnostic.undefinedType(name, context));
            return;
        }
        Symbol constructor = owner.getCallable(name, types.size());
        if (constructor == null) CallValidator.report("No existe un constructor de «" + name + "» con " + types.size() + " argumentos", context, errors);
        else CallValidator.validate(constructor, types, context, errors);
    }

    private void validateStruct(Type type, PigLatinParser.Structura_instanciacionContext initializer) {
        Symbol definition = declaredType(type);
        if (definition == null) {
            CallValidator.report("No se encontró la estructura «" + type + "»", initializer, errors);
            return;
        }
        if (!initializer.expresion().isEmpty()) {
            if (initializer.expresion().size() != definition.getMembers().size()) {
                CallValidator.report("La estructura «" + definition.getName() + "» requiere "
                        + definition.getMembers().size() + " campos", initializer, errors);
            }
            for (int i = 0; i < Math.min(initializer.expresion().size(), definition.getMembers().size()); i++) {
                Type actual = visit(initializer.expresion(i));
                Type expected = definition.getMembers().get(i).getType();
                if (Type.isKnown(actual) && !actual.isAssignableTo(expected))
                    errors.add(Diagnostic.incompatibleAssignment(expected, actual, initializer.expresion(i)));
            }
        } else {
            java.util.Set<String> assigned = new java.util.HashSet<>();
            for (var field : initializer.atributo_asignacion()) {
                String name = field.VARIABLE(0).getText();
                Symbol member = definition.getMember(name);
                if (member == null || !assigned.add(name)) {
                    CallValidator.report("Campo desconocido o repetido «" + name + "» en «" + definition.getName() + "»", field, errors);
                } else if (field.expresion() != null) {
                    Type actual = visit(field.expresion());
                    if (Type.isKnown(actual) && !actual.isAssignableTo(member.getType()))
                        errors.add(Diagnostic.incompatibleAssignment(member.getType(), actual, field));
                } else if (field.structura_instanciacion() != null) validateStruct(member.getType(), field.structura_instanciacion());
            }
        }
    }

    @Override
    public Type visitInicializacion_per(PigLatinParser.Inicializacion_perContext ctx) {
        if (ctx.ESTO() != null && ctx.VARIABLE() != null) {
            String name = ctx.VARIABLE().getText();
            Type type = ctx.tipo_dato() != null ? resolveType(ctx.tipo_dato().getText()) : Type.INT;
            {
                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                if (!symbolTable.define(
                        new Symbol(name, type, SymbolCategory.VARIABLE, offset, false)
                                 .at(ctx))) errors.add(Diagnostic.alreadyDeclaredVariable(name, ctx));
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
        symbolTable.enterScope("for_" + ctx.getStart().getStartIndex(), 0, ctx);
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
            symbolTable.exitScope();
        }
        return Type.VOID;
    }

    @Override
    public Type visitCondicion(PigLatinParser.CondicionContext ctx) {
        if (ctx.condicion() != null) visit(ctx.condicion());
        Type type = visit(ctx.conjuncion());
        if (Type.isKnown(type) && !Type.BOOLEAN.equals(type)) errors.add(Diagnostic.nonBooleanCondition("del bloque", ctx));
        return Type.BOOLEAN;
    }

    @Override
    public Type visitConjuncion(PigLatinParser.ConjuncionContext ctx) {
        if (ctx.conjuncion() != null) visit(ctx.conjuncion());
        return visit(ctx.negacion_logica());
    }

    @Override
    public Type visitNegacion_logica(PigLatinParser.Negacion_logicaContext ctx) {
        Type type = ctx.primaria_logica() != null ? visit(ctx.primaria_logica()) : visit(ctx.negacion_logica());
        if (ctx.NEGACION() != null && Type.isKnown(type) && !Type.BOOLEAN.equals(type))
            errors.add(Diagnostic.nonBooleanNegationOperator(ctx));
        return type;
    }

    @Override
    public Type visitPrimaria_logica(PigLatinParser.Primaria_logicaContext ctx) {
        if (ctx.condicion() != null) return visit(ctx.condicion());
        if (ctx.operador_relacional() != null) {
            Type left = visit(ctx.expresion(0)), right = visit(ctx.expresion(1));
            String operator = ctx.operador_relacional().getText();
            Type result = "==".equals(operator) || "!=".equals(operator)
                    ? TypeCompatibility.checkEquality(left, right) : TypeCompatibility.checkRelational(left, right, operator);
            if (Type.isKnown(left) && Type.isKnown(right) && !Type.isKnown(result))
                CallValidator.report("No se pueden comparar «" + left.label() + "» y «" + right.label() + "»", ctx, errors);
            return Type.BOOLEAN;
        }
        if (ctx.llamada_funcion() != null) return visit(ctx.llamada_funcion());
        if (ctx.VARIABLE() != null) {
            Symbol symbol = symbolTable.resolve(ctx.VARIABLE().getText());
            if (symbol == null) {
                errors.add(Diagnostic.undeclaredVariable(ctx.VARIABLE().getText(), ctx));
                return Type.UNKNOWN;
            }
            return symbol.getType();
        }
        return Type.BOOLEAN;
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
            case "bool": return Type.BOOLEAN;
            case "verum":
            case "falsus": return Type.BOOLEAN;
            default:
                if (symbolTable.getStruct(text) != null) return Type.structType(text);
                if (symbolTable.getClass(text) != null) return Type.classType(text);
                return Type.UNKNOWN;
        }
    }

}
