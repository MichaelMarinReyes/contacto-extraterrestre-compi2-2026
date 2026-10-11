package com.compi.backend.symbols;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/** Comprueba la firma común de llamadas de los tres lenguajes. */
public final class CallValidator {
    private CallValidator() { }
    public static void validate(Symbol callable, List<Type> arguments, ParserRuleContext context,
                                List<CompilationError> errors) {
        if (callable.getParameters().size() != arguments.size()) {
            report("«" + callable.getName() + "» espera " + callable.getParameters().size()
                    + " argumentos y recibió " + arguments.size(), context, errors);
            return;
        }
        for (int i = 0; i < arguments.size(); i++) {
            Type actual = arguments.get(i);
            Type expected = callable.getParameters().get(i).getType();
            if (Type.isKnown(actual) && Type.isKnown(expected) && !actual.isAssignableTo(expected)) {
                report("El argumento " + (i + 1) + " de «" + callable.getName() + "» debe ser «"
                        + expected.label() + "», pero recibió «" + actual.label() + "»", context, errors);
            }
        }
    }

    public static void report(String message, ParserRuleContext context, List<CompilationError> errors) {
        errors.add(new CompilationError(ErrorType.SEMANTIC, message,
                context.getStart().getLine(), context.getStart().getCharPositionInLine()));
    }
}
