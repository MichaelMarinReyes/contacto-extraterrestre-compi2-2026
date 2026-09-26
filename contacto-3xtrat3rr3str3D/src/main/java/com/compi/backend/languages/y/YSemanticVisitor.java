package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.symbols.*;

import java.util.ArrayList;
import java.util.List;

public class YSemanticVisitor extends YBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();
    private Type currentFunctionReturnType = Type.VOID;

    public YSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitEstructuras_seccion(YParser.Estructuras_seccionContext ctx) {
        // Registrar estructuras primero para permitir tipos referenciados
        for (YParser.Estructura_defContext structCtx : ctx.estructura_def()) {
            if (structCtx.ID() != null) {
                String structName = structCtx.ID().getText();
                Symbol structSymbol = new Symbol(structName, Type.structType(structName),
                        SymbolCategory.STRUCT).at(structCtx);
                symbolTable.addStruct(structSymbol);

                int offset = 0;
                for (YParser.Campo_structContext fieldCtx : structCtx.campo_struct()) {
                    if (fieldCtx.ID().size() >= 1) {
                        String fieldTypeStr = fieldCtx.tipo_dato() != null ? fieldCtx.tipo_dato().getText() : fieldCtx.ID(0).getText();
                        String fieldName = fieldCtx.tipo_dato() != null ? fieldCtx.ID(0).getText() : (fieldCtx.ID().size() > 1 ? fieldCtx.ID(1).getText() : "campo");
                        Type fieldType = resolveType(fieldTypeStr);

                        if (!fieldCtx.CORCHETE_IZQ().isEmpty()) {
                            fieldType = Type.array(fieldType, fieldCtx.CORCHETE_IZQ().size());
                        }

                        Symbol fieldSymbol = new Symbol(fieldName, fieldType, SymbolCategory.FIELD,
                                offset++, false).at(fieldCtx);
                        structSymbol.addMember(fieldSymbol);
                    }
                }
            }
        }
        return Type.VOID;
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

                // [] entero miArray -> Arreglos pasan exclusivamente por referencia (PDF pág. 287)
                if (paramCtx.CORCHETE_IZQ() != null) {
                    paramType = Type.array(paramType, 1);
                    isByRef = true;
                }
                // {} MiEstructura miEstructura -> Estructuras pasan exclusivamente por referencia (PDF pág. 295)
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
                    errors.add(Diagnostico.parametroDuplicado(paramName, paramCtx));
                }
            }
        }

        symbolTable.addFunction(funcSymbol);

        // Analizar cuerpo
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

        if (!ctx.CORCHETE_IZQ().isEmpty()) {
            varType = Type.array(varType, ctx.CORCHETE_IZQ().size());
        }

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol varSymbol = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, false).at(ctx);

        if (!symbolTable.define(varSymbol)) {
            errors.add(Diagnostico.variableYaDeclarada(varName, ctx));
        }

        if (ctx.expresion_inicializacion() != null) {
            // Validar compatibilidad de tipo si hay expresión
            if (ctx.expresion_inicializacion().expresion() != null) {
                Type initType = visit(ctx.expresion_inicializacion().expresion());
                if (esConocido(initType) && esConocido(varType)
                        && !initType.isAssignableTo(varType)) {
                    errors.add(Diagnostico.inicializacionIncompatible(
                            varName, varType, initType, ctx));
                }
            }
        }

        return varType;
    }

    @Override
    public Type visitAsignacion(YParser.AsignacionContext ctx) {
        Type targetType = Type.UNKNOWN;
        String name = "";

        if (ctx.ID() != null) {
            name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostico.variableNoDeclarada(name, ctx));
            } else {
                targetType = s.getType();
                if (ctx.CORCHETE_IZQ() != null && targetType.isArray()) {
                    targetType = targetType.getElementType();
                }
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        Type exprType = ctx.expresion().isEmpty() ? Type.UNKNOWN : visit(ctx.expresion(ctx.expresion().size() - 1));
        if (esConocido(targetType) && esConocido(exprType)
                && !exprType.isAssignableTo(targetType)) {
            errors.add(Diagnostico.asignacionIncompatible(targetType, exprType, ctx));
        }

        return targetType;
    }

    @Override
    public Type visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        Type returnValType = Type.VOID;
        if (ctx.expresion() != null) {
            returnValType = visit(ctx.expresion());
        }

        if (esConocido(currentFunctionReturnType) && esConocido(returnValType)
                && !returnValType.isAssignableTo(currentFunctionReturnType)) {
            errors.add(Diagnostico.tipoDeRetornoErroneo(
                    currentFunctionReturnType, returnValType, ctx));
        }

        return returnValType;
    }

    @Override
    public Type visitExpresion(YParser.ExpresionContext ctx) {
        if (!ctx.termino().isEmpty()) {
            Type left = visit(ctx.termino(0));
            return left != null ? left : Type.UNKNOWN;
        }

        if (!ctx.operador_aritmetico().isEmpty()) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkArithmetic(t1, t2, ctx.operador_aritmetico(0).getText());
        }

        if (ctx.operador_relacional() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkRelational(t1, t2, ctx.operador_relacional().getText());
        }

        if (ctx.operador_logico() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkLogical(t1, t2);
        }

        if (ctx.operador_negacion() != null) {
            Type t = visit(ctx.expresion(0));
            // Con una expresion que ya fallo el tipo puede venir nulo: entonces el
            // error ya esta reportado y aqui solo habria un NullPointerException.
            if (esConocido(t) && t.getDataType() != DataType.BOOLEAN) {
                errors.add(Diagnostico.operadorNegacionNoBooleano(ctx));
            }
            return Type.BOOLEAN;
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
                errors.add(Diagnostico.simboloNoResuelto(name, ctx));
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

        return Type.UNKNOWN;
    }

    @Override
    public Type visitLlamada_funcion(YParser.Llamada_funcionContext ctx) {
        String funcName = ctx.ID().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            errors.add(Diagnostico.funcionNoDefinida(funcName, ctx));
            return Type.UNKNOWN;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    @Override
    public Type visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String baseName = ctx.ID(0).getText();
        Symbol baseSymbol = symbolTable.resolve(baseName);
        if (baseSymbol == null) {
            errors.add(Diagnostico.variableNoDeclarada(baseName, ctx));
            return Type.UNKNOWN;
        }

        Type currentType = baseSymbol.getType();
        for (int i = 1; i < ctx.ID().size(); i++) {
            String memberName = ctx.ID(i).getText();
            if (currentType.isStruct()) {
                Symbol structDef = symbolTable.getStruct(currentType.getCustomTypeName());
                if (structDef != null) {
                    Symbol member = structDef.getMember(memberName);
                    if (member != null) {
                        currentType = member.getType();
                    } else {
                        errors.add(Diagnostico.miembroNoExiste(memberName,
                                currentType.getCustomTypeName(), ctx));
                        return Type.UNKNOWN;
                    }
                }
            }
        }
        return currentType;
    }

    private Type resolveType(String name) {
        if (name == null) return Type.UNKNOWN;
        switch (name.toLowerCase()) {
            case "entero": return Type.INT;
            case "flotante": return Type.DOUBLE;
            case "cadena": return Type.STRING;
            case "caracter": return Type.CHAR;
            case "bool": return Type.BOOLEAN;
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
