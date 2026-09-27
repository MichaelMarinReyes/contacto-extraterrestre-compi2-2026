package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class YSemanticVisitor extends YBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();
    private Type currentFunctionReturnType = Type.VOID;

    public YSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private static int[] tamanosDe(YParser.Declaracion_variableContext ctx) {
        int[] sizes = new int[ctx.CORCHETE_IZQ().size()];
        List<TerminalNode> numeros = ctx.NUMERO_ENTERO();
        for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
            sizes[i] = Integer.parseInt(numeros.get(i).getText());
        }
        return sizes;
    }

    private static int widthOf(int[] shape, YParser.Elemento_listaContext row) {
        if (shape.length > 0) {
            return shape[0];
        }
        return row.LLAVE_IZQ() == null ? 1 : row.elemento_lista().size();
    }


    private static boolean hasIndices(YParser.Acceso_miembroContext ctx) {
        return !ctx.PUNTO().isEmpty();
    }


    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitEstructuras_seccion(YParser.Estructuras_seccionContext ctx) {
        for (YParser.Estructura_defContext structCtx : ctx.estructura_def()) {
            if (structCtx.ID() != null) {
                registerStruct(structCtx.ID().getText(), structCtx.campo_struct(), structCtx);
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion_estructura_local(YParser.Declaracion_estructura_localContext ctx) {
        registerStruct(ctx.ID().getText(), ctx.campo_struct(), ctx);
        return Type.VOID;
    }

    private void registerStruct(String name, List<YParser.Campo_structContext> campos,
                                     ParserRuleContext ctx) {
        if (symbolTable.getStruct(name) != null) {
            return;
        }
        Symbol structSymbol = new Symbol(name, Type.structType(name),
                SymbolCategory.STRUCT).at(ctx);
        symbolTable.addStruct(structSymbol);

        int offset = 0;
        for (YParser.Campo_structContext fieldCtx : campos) {
            if (fieldCtx.ID().size() >= 1) {
                String fieldTypeStr = fieldCtx.tipo_dato() != null ? fieldCtx.tipo_dato().getText() : fieldCtx.ID(0).getText();
                String fieldName = fieldCtx.tipo_dato() != null ? fieldCtx.ID(0).getText() : (fieldCtx.ID().size() > 1 ? fieldCtx.ID(1).getText() : "campo");
                Type fieldType = resolveType(fieldTypeStr);

                if (!isTypeDefined(fieldTypeStr)) {
                    errors.add(Diagnostic.undefinedType(fieldTypeStr, fieldCtx));
                }

                if (!fieldCtx.CORCHETE_IZQ().isEmpty()) {
                    int[] sizes = new int[fieldCtx.CORCHETE_IZQ().size()];
                    List<TerminalNode> numeros = fieldCtx.NUMERO_ENTERO();
                    for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
                        sizes[i] = Integer.parseInt(numeros.get(i).getText());
                    }
                    fieldType = Type.arrayOf(fieldType, sizes);
                }

                Symbol fieldSymbol = new Symbol(fieldName, fieldType, SymbolCategory.FIELD,
                        offset++, false).at(fieldCtx);
                structSymbol.addMember(fieldSymbol);
            }
        }
    }

    @Override
    public Type visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        Type returnType = Type.VOID;
        if (ctx.tipo_dato() != null) {
            returnType = resolveType(ctx.tipo_dato().getText());
        }
        currentFunctionReturnType = returnType;

        Symbol funcSymbol = new Symbol(funcName, returnType, SymbolCategory.FUNCTION).at(ctx);
        funcSymbol.setReturnType(returnType);

        symbolTable.enterScope("func_" + funcName, 0);

        if (ctx.parametros() != null) {
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                String paramName = paramCtx.ID().getText();
                Type paramType = resolveType(paramCtx.tipo_dato().getText());
                boolean isByRef = false;

                if (!isTypeDefined(paramCtx.tipo_dato().getText())) {
                    errors.add(Diagnostic.undefinedType(
                            paramCtx.tipo_dato().getText(), paramCtx));
                }

                if (paramCtx.CORCHETE_IZQ() != null) {
                    paramType = Type.array(paramType, 1);
                    isByRef = true;
                }
                if (paramCtx.LLAVE_IZQ() != null) {
                    paramType = Type.structType(paramCtx.tipo_dato().getText());
                    isByRef = true;
                }

                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol paramSymbol = new Symbol(paramName, paramType, SymbolCategory.PARAMETER,
                        offset, false).at(paramCtx);
                paramSymbol.setByReference(isByRef);
                funcSymbol.addParameter(paramSymbol);

                if (!symbolTable.define(paramSymbol)) {
                    errors.add(Diagnostic.duplicateParameter(paramName, paramCtx));
                }
            }
        }

        symbolTable.addFunction(funcSymbol);

        visit(ctx.bloque());

        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion_variable(YParser.Declaracion_variableContext ctx) {
        String typeName = ctx.tipo_dato() != null ? ctx.tipo_dato().getText() : "bool";
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "boolVar";
        if (ctx.tipo_dato() == null && ctx.ID().size() > 1) {
            typeName = ctx.ID(0).getText();
        }
        Type varType = resolveType(typeName);

        if (!isTypeDefined(typeName)) {
            errors.add(Diagnostic.undefinedType(typeName, ctx));
        }

        if (!ctx.CORCHETE_IZQ().isEmpty()) {
            varType = Type.arrayOf(varType, tamanosDe(ctx));
        }

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol varSymbol = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, false).at(ctx);

        if (!symbolTable.define(varSymbol)) {
            errors.add(Diagnostic.alreadyDeclaredVariable(varName, ctx));
        }

        if (ctx.expresion_inicializacion() != null) {
            if (ctx.expresion_inicializacion().expresion() != null) {
                Type initType = visit(ctx.expresion_inicializacion().expresion());
                if (Type.isKnown(initType) && Type.isKnown(varType)
                        && !initType.isAssignableTo(varType)) {
                    errors.add(Diagnostic.inicializacionIncompatible(
                            varName, varType, initType, ctx));
                }
            } else if (varType.isArray()) {
                Type obtenido = listType(varName, ctx.expresion_inicializacion().elemento_lista(),
                        ctx.expresion_inicializacion());
                CompilationError error = ArrayLiteral.validate(varName, varType, obtenido, ctx);
                if (error != null) {
                    errors.add(error);
                }
            }
        }

        return varType;
    }

    private Type listType(String name, List<YParser.Elemento_listaContext> elementos,
                             ParserRuleContext where) {
        int filas = elementos.size();
        Type rowType = Type.UNKNOWN;
        int[] rowShape = new int[0];
        for (int f = 0; f < filas; f++) {
            YParser.Elemento_listaContext element = elementos.get(f);
            Type type = element.LLAVE_IZQ() == null
                    ? visit(element.expresion())
                    : listType(name, element.elemento_lista(), element);
            int[] shape = type.isArray() ? type.getSizes() : new int[0];
            if (f == 0) {
                rowType = type.isArray() ? type.getElementType() : type;
                rowShape = shape;
            } else if (!java.util.Arrays.equals(shape, rowShape)) {
                errors.add(Diagnostic.filasDesiguales(name, f + 1,
                        widthOf(rowShape, elementos.get(0)),
                        widthOf(shape, element), element));
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = filas;
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        return Type.arrayOf(rowType, sizes);
    }

    @Override
    public Type visitAsignacion(YParser.AsignacionContext ctx) {
        Type targetType = Type.UNKNOWN;
        String name = "";

        if (ctx.ID() != null) {
            name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostic.undeclaredVariable(name, ctx));
            } else {
                targetType = s.getType();
                if (!ctx.CORCHETE_IZQ().isEmpty() && targetType != null) {
                    targetType = targetType.desindexar(ctx.CORCHETE_IZQ().size());
                }
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        Type exprType = ctx.expresion().isEmpty() ? Type.UNKNOWN : visit(ctx.expresion(ctx.expresion().size() - 1));
        if (Type.isKnown(targetType) && Type.isKnown(exprType)
                && !exprType.isAssignableTo(targetType)) {
            errors.add(Diagnostic.incompatibleAssignment(targetType, exprType, ctx));
        }

        return targetType;
    }

    @Override
    public Type visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        Type returnValType = Type.VOID;
        if (ctx.expresion() != null) {
            returnValType = visit(ctx.expresion());
        }

        if (Type.isKnown(currentFunctionReturnType) && Type.isKnown(returnValType)
                && !returnValType.isAssignableTo(currentFunctionReturnType)) {
            errors.add(Diagnostic.wrongReturnType(
                    currentFunctionReturnType, returnValType, ctx));
        }

        return returnValType;
    }

    @Override
    public Type visitExpresion(YParser.ExpresionContext ctx) {

        if (ctx.operador_negacion() != null) {
            Type t = visit(ctx.expresion(0));
            if (Type.isKnown(t) && t.getDataType() != DataType.BOOLEAN) {
                errors.add(Diagnostic.nonBooleanNegationOperator(ctx));
            }
            return Type.BOOLEAN;
        }

        if (ctx.operador_logico() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkLogical(t1, t2);
        }

        if (ctx.operador_relacional() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkRelational(t1, t2, ctx.operador_relacional().getText());
        }

        if (ctx.sumatoria() != null) {
            return visit(ctx.sumatoria());
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.leer_funcion() != null) {
            return Type.STRING;
        }

        return Type.UNKNOWN;
    }

    @Override
    public Type visitSumatoria(YParser.SumatoriaContext ctx) {
        Type acumulado = visit(ctx.productoria(0));
        for (int i = 1; i < ctx.productoria().size(); i++) {
            acumulado = TypeCompatibility.checkArithmetic(acumulado,
                    visit(ctx.productoria(i)), ctx.operador_aditivo(i - 1).getText());
        }
        return acumulado;
    }

    @Override
    public Type visitProductoria(YParser.ProductoriaContext ctx) {
        Type acumulado = visit(ctx.termino(0));
        for (int i = 1; i < ctx.termino().size(); i++) {
            acumulado = TypeCompatibility.checkArithmetic(acumulado,
                    visit(ctx.termino(i)), ctx.operador_multiplicativo(i - 1).getText());
        }
        return acumulado;
    }

    @Override
    public Type visitTermino(YParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return Type.INT;
        if (ctx.NUMERO_DECIMAL() != null) return Type.DOUBLE;
        if (ctx.CADENA_TEXTO() != null) return Type.STRING;
        if (ctx.CARACTER() != null) return Type.CHAR;
        if (ctx.VERDADERO() != null || ctx.FALSO() != null) return Type.BOOLEAN;

        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostic.unresolvedSymbol(name, ctx));
                return Type.UNKNOWN;
            }
            return s.getType();
        }

        if (ctx.acceso_miembro() != null) {
            return visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
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
    public Type visitLlamada_funcion(YParser.Llamada_funcionContext ctx) {
        String funcName = ctx.ID().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            errors.add(Diagnostic.undefinedFunction(funcName, ctx));
            return Type.UNKNOWN;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    @Override
    public Type visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String baseName = ctx.ID(0).getText();
        Symbol baseSymbol = symbolTable.resolve(baseName);
        if (baseSymbol == null) {
            errors.add(Diagnostic.undeclaredVariable(baseName, ctx));
            return Type.UNKNOWN;
        }

        Type currentType = baseSymbol.getType();
        if (currentType == null) {
            return Type.UNKNOWN;
        }

        int indices = ctx.CORCHETE_IZQ().size();
        if (indices > 0 && !hasIndices(ctx) && currentType.isArray()
                && indices > currentType.getDimensions()) {
            errors.add(Diagnostic.indexDimensions(baseName, indices,
                    currentType.getDimensions(), ctx));
            return Type.UNKNOWN;
        }

        for (int i = 1; i < ctx.getChildCount() && currentType != null; i++) {
            ParseTree child = ctx.getChild(i);
            if (child.getText().equals(".")) {
                String memberName = ctx.getChild(++i).getText();
                if (!currentType.isStruct()) {
                    return Type.UNKNOWN;
                }
                Symbol structDef = symbolTable.getStruct(currentType.getCustomTypeName());
                if (structDef == null) {
                    continue;
                }
                Symbol member = structDef.getMember(memberName);
                if (member == null) {
                    errors.add(Diagnostic.memberDoesNotExist(memberName,
                            currentType.getCustomTypeName(), ctx));
                    return Type.UNKNOWN;
                }
                currentType = member.getType();
            } else if (child instanceof YParser.ExpresionContext) {
                visit(child);
                if (!currentType.isArray()) {
                    errors.add(Diagnostic.notAnArray(baseName, currentType, ctx));
                    return Type.UNKNOWN;
                }
                checkIndex(child, currentType, baseName);
                currentType = currentType.desindexar(1);
            }
        }
        return currentType;
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
                    (YParser.ExpresionContext) index));
        }
    }

    private boolean isTypeDefined(String name) {
        if (name == null) {
            return true;
        }
        switch (name.toLowerCase()) {
            case "entero", "flotante", "cadena", "caracter", "bool":
                return true;
            default:
                return symbolTable.getStruct(name) != null
                        || symbolTable.getClass(name) != null;
        }
    }

    private Type resolveType(String name) {
        if (name == null) return Type.UNKNOWN;
        switch (name.toLowerCase()) {
            case "entero":
                return Type.INT;
            case "flotante":
                return Type.DOUBLE;
            case "cadena":
                return Type.STRING;
            case "caracter":
                return Type.CHAR;
            case "bool":
                return Type.BOOLEAN;
            default:
                if (symbolTable.getStruct(name) != null) {
                    return Type.structType(name);
                }
                if (symbolTable.getClass(name) != null) {
                    return Type.classType(name);
                }
                return new Type(DataType.STRUCT, name);
        }
    }
}
