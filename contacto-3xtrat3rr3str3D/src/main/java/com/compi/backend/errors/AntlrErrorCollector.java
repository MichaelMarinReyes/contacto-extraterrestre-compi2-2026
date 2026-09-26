package com.compi.backend.errors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class AntlrErrorCollector extends BaseErrorListener {
    private final List<CompilationError> errors = new ArrayList<>();
    private final ErrorType defaultType;

    public AntlrErrorCollector(ErrorType defaultType) {
        this.defaultType = defaultType;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // Sin vocabulario, porque este listener se puede instalar en cualquier
        // reconocedor y no guarda referencias: los nombres de token salen tal cual
        // los da ANTLR. El texto sigue siendo espanol, que es lo que importa.
        errors.add(new CompilationError(defaultType,
                Diagnostico.desdeAntlr(null, msg,
                        offendingSymbol instanceof Token token ? token : null),
                line, charPositionInLine));
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
