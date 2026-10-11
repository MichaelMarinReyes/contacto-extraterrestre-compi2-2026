package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.symbols.*;
import java.util.List;

/**
 * Validación semántica de las condiciones lógicas de PigLatin: condicion,
 * conjuncion, negación y primaria lógica con operadores relacionales.
 */
final class PigLatinConditionAnalyzer {

    private final SymbolTable symbolTable;
    private final List<CompilationError> errors;

    PigLatinConditionAnalyzer(SymbolTable symbolTable, List<CompilationError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    Type condicion(PigLatinBaseVisitor<Type> visitor, PigLatinParser.CondicionContext ctx) {
        if (ctx.condicion() != null) {
            visitor.visit(ctx.condicion());
        }
        Type type = visitor.visit(ctx.conjuncion());
        if (Type.isKnown(type) && !Type.BOOLEAN.equals(type)) {
            errors.add(Diagnostic.nonBooleanCondition("del bloque", ctx));
        }
        return Type.BOOLEAN;
    }

    Type conjuncion(PigLatinBaseVisitor<Type> visitor, PigLatinParser.ConjuncionContext ctx) {
        if (ctx.conjuncion() != null) {
            visitor.visit(ctx.conjuncion());
        }
        return visitor.visit(ctx.negacion_logica());
    }

    Type negacionLogica(PigLatinBaseVisitor<Type> visitor,
                        PigLatinParser.Negacion_logicaContext ctx) {
        Type type = ctx.primaria_logica() != null
                ? visitor.visit(ctx.primaria_logica())
                : visitor.visit(ctx.negacion_logica());
        if (ctx.NEGACION() != null && Type.isKnown(type) && !Type.BOOLEAN.equals(type)) {
            errors.add(Diagnostic.nonBooleanNegationOperator(ctx));
        }
        return type;
    }

    Type primariaLogica(PigLatinBaseVisitor<Type> visitor,
                        PigLatinParser.Primaria_logicaContext ctx) {
        if (ctx.condicion() != null) {
            return visitor.visit(ctx.condicion());
        }
        if (ctx.operador_relacional() != null) {
            Type left = visitor.visit(ctx.expresion(0));
            Type right = visitor.visit(ctx.expresion(1));
            String operator = ctx.operador_relacional().getText();
            Type result = "==".equals(operator) || "!=".equals(operator)
                    ? TypeCompatibility.checkEquality(left, right)
                    : TypeCompatibility.checkRelational(left, right, operator);
            if (Type.isKnown(left) && Type.isKnown(right) && !Type.isKnown(result)) {
                CallValidator.report("No se pueden comparar «" + left.label()
                        + "» y «" + right.label() + "»", ctx, errors);
            }
            return Type.BOOLEAN;
        }
        if (ctx.llamada_funcion() != null) {
            return visitor.visit(ctx.llamada_funcion());
        }
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
}