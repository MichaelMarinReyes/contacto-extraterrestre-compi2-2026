package com.compi.backend.languages2;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Base comun para los compiladores de lenguaje.
 *
 * Centraliza la creacion del listener de ANTLR que traduce los errores del
 * reconocedor en {@link CompilationError} con el {@link ErrorType} correcto
 * (LEXICO para el lexer, SINTACTICO para el parser).
 */
public abstract class AbstractLanguageCompiler implements LanguageCompiler {

    /**
     * Crea un listener que acumula errores en la lista indicada.
     *
     * @param errors destino de los errores
     * @param type   tipo a asignar a cada error capturado
     */
    protected BaseErrorListener errorCollector(List<CompilationError> errors, ErrorType type) {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                errors.add(new CompilationError(type, msg, line, charPositionInLine));
            }
        };
    }

    /** Desconecta los listeners por defecto e instala el nuestro. */
    protected void installErrorListener(Lexer lexer, BaseErrorListener listener) {
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
    }

    /** Desconecta los listeners por defecto e instala el nuestro. */
    protected void installErrorListener(Parser parser, BaseErrorListener listener) {
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
    }

    /** Traduce una excepcion inesperada en error semantico. */
    protected CompilationError internalError(String language, Exception e) {
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        return new CompilationError(ErrorType.SEMANTICO,
                "Error interno en " + language + ": " + msg, 0, 0);
    }
}
