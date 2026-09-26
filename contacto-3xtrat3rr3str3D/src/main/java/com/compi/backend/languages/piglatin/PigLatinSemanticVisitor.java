package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.symbols.*;

import java.util.ArrayList;
import java.util.List;

public class PigLatinSemanticVisitor extends PigLatinBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();

    public PigLatinSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitVariables(PigLatinParser.VariablesContext ctx) {
        // Declaraciones globales
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
                String className = ctx.VARIABLE(1).getText();
                varType = Type.classType(className);
            }

            int offset = symbolTable.getGlobalScope().allocateOffset(1);
            Symbol sym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, true)
                    .at(ctx);

            if (!symbolTable.defineGlobal(sym)) {
                errors.add(Diagnostico.variableGlobalYaDeclarada(varName, ctx));
            }

            if (ctx.expresion() != null) {
                Type exprType = visit(ctx.expresion());
                if (esConocido(exprType) && esConocido(varType)
                        && !exprType.isAssignableTo(varType)) {
                    errors.add(Diagnostico.inicializacionIncompatible(
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

        Type arrayType = Type.array(elemType, 1);
        int offset = symbolTable.getGlobalScope().allocateOffset(1);
        Symbol sym = new Symbol(arrayName, arrayType, SymbolCategory.VARIABLE, offset, true)
                .at(ctx);
        symbolTable.defineGlobal(sym);
        return Type.VOID;
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
                errors.add(Diagnostico.variableNoDeclarada(name, ctx));
            } else {
                targetType = s.getType();
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            Type exprType = visit(ctx.expresion());
            if (esConocido(targetType) && esConocido(exprType)
                    && !exprType.isAssignableTo(targetType)) {
                errors.add(Diagnostico.asignacionIncompatible(targetType, exprType, ctx));
            }
        }
        return targetType;
    }

    @Override
    public Type visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.termino() != null && ctx.termino().size() == 1) {
            return visit(ctx.termino(0));
        }

        if (!ctx.operacion_aritmetica().isEmpty()) {
            Type t1 = visit(ctx.termino(0));
            for (int i = 1; i < ctx.termino().size(); i++) {
                Type t2 = visit(ctx.termino(i));
                t1 = TypeCompatibility.checkArithmetic(t1, t2, ctx.operacion_aritmetica(i - 1).getText());
            }
            return t1;
        }

        return Type.UNKNOWN;
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
                errors.add(Diagnostico.simboloNoResuelto(varName, ctx));
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

        return Type.UNKNOWN;
    }

    @Override
    public Type visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        String funcName = ctx.VARIABLE().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            // PigLatin no declara funciones, solo las llama. Y la que se llama
            // puede venir de un archivo importado de otro lenguaje, asi que un
            // nombre desconocido aqui no es un error: lo que no se sabe es el
            // tipo que devuelve, y se supone que es un entero.
            return Type.INT;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    /**
     * El ciclo "per" declara su variable de control en la inicializacion.
     * Sin este paso la condicion y el incremento del bucle fallarian al
     * resolver el identificador.
     *
     * <p>La variable se registra en el ambito global aunque el ciclo este dentro
     * de un bloque, para que siga viva cuando ese bloque se cierre.</p>
     */
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
        // Caso: variable ya declarada a la que se le asigna el valor inicial
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
        for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
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
