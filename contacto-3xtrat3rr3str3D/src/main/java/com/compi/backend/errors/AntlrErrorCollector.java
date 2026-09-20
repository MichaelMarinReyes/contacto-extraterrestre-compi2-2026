package com.compi.backend.errors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

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
        errors.add(new CompilationError(defaultType, msg, line, charPositionInLine));
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
