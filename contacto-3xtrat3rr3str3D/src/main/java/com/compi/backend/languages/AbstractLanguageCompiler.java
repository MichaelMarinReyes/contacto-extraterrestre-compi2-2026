package com.compi.backend.languages;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.List;

public abstract class AbstractLanguageCompiler implements LanguageCompiler {
    private File workingDirectory = new File(".");
    private Vocabulary vocabulary;
    private String[] ruleNames;

    protected File getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public void setWorkingDirectory(File directory) {
        this.workingDirectory = directory == null ? new File(".") : directory;
    }

    @Override
    public Vocabulary vocabulary() {
        return vocabulary;
    }

    @Override
    public String[] ruleNames() {
        return ruleNames;
    }

    protected void rememberParser(Parser parser) {
        this.vocabulary = parser.getVocabulary();
        this.ruleNames = parser.getRuleNames();
    }

    protected BaseErrorListener errorCollector(Vocabulary vocabulary,
                                               List<CompilationError> errors,
                                               ErrorType type) {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                errors.add(new CompilationError(type,
                        Diagnostic.fromAntlr(vocabulary, msg,
                                offendingSymbol instanceof Token token ? token : null),
                        line, charPositionInLine));
            }
        };
    }

    protected void installErrorListener(Lexer lexer, BaseErrorListener listener) {
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
    }

    protected void installErrorListener(Parser parser, BaseErrorListener listener) {
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
    }

    protected CompilationError internalError(String language, Exception e) {
        return internalError(language, e, null);
    }

    protected void markLanguage(SymbolTable symbolTable, String language) {
        for (Symbol symbol : symbolTable.getAllSymbols()) {
            if (symbol.getLanguage() == null) {
                symbol.inLanguage(language);
            }
        }
    }

    protected CompilationError internalError(String language, Exception e, ParseTree tree) {
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        int line = 0;
        int column = 0;
        if (tree instanceof ParserRuleContext ctx && ctx.getStart() != null) {
            line = ctx.getStart().getLine();
            column = ctx.getStart().getCharPositionInLine();
        }
        return new CompilationError(ErrorType.SEMANTIC,
                "Error interno en " + language + ": " + msg, line, column);
    }
}
