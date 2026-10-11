package com.compi.backend.languages;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostic;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.ast.build.AbstractAstBuilder;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.ast.semantic.AstSemanticAnalyzer;
import com.compi.backend.ast.semantic.SemanticModel;
import com.compi.backend.ast.codegen.AstCodeGenerator;
import com.compi.backend.c3d.C3DGenerator;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.List;

public abstract class AbstractLanguageCompiler implements LanguageCompiler {
    private SemanticModel semanticModel;

    protected ProgramNodeAST constructAst(AbstractAstBuilder builder, ParseTree tree, String sourceFile) {
        var ast = builder.build(tree, sourceFile);
        semanticModel = null;
        return ast;
    }

    @Override
    public SemanticModel semanticModel() { return semanticModel; }

    @Override
    public void analyze(ProgramNodeAST ast, SymbolTable symbols, List<CompilationError> errors) {
        if (ast == null) return;
        semanticModel = new AstSemanticAnalyzer(symbols, id(), errors).analyze(ast);
    }

    @Override
    public void generateIntermediate(ProgramNodeAST ast, SymbolTable symbols, C3DGenerator generator) {
        if (ast != null) new AstCodeGenerator(symbols, semanticModel, generator, id()).generate(ast);
    }
    private File workingDirectory = new File(".");
    private File projectDirectory = new File(".");
    private Vocabulary vocabulary;
    private String[] ruleNames;

    protected File getWorkingDirectory() {
        return workingDirectory;
    }

    protected File getProjectDirectory() { return projectDirectory; }

    @Override
    public void setProjectDirectory(File directory) {
        projectDirectory = directory == null ? workingDirectory : directory;
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
