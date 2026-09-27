package com.compi.backend.languages;

import com.compi.YParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.y.YC3DVisitor;
import com.compi.backend.languages.y.YIndentLexer;
import com.compi.backend.languages.y.YSemanticVisitor;
import com.compi.backend.symbols.SymbolTable;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class YCompiler extends AbstractLanguageCompiler {

    @Override
    public String id() {
        return "y";
    }

    @Override
    public String displayName() {
        return "Y";
    }

    @Override
    public List<String> extensions() {
        return List.of(".y");
    }

    @Override
    public ParseTree parse(String source, List<CompilationError> errors) {
        try {
            YIndentLexer lexer = new YIndentLexer(CharStreams.fromString(source));
            installErrorListener(lexer,
                    errorCollector(lexer.getVocabulary(), errors, ErrorType.LEXICAL));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            YParser parser = new YParser(tokens);
            installErrorListener(parser,
                    errorCollector(parser.getVocabulary(), errors, ErrorType.SYNTACTIC));
            rememberParser(parser);

            return parser.init();
        } catch (Exception e) {
            errors.add(internalError(displayName(), e));
            return null;
        }
    }

    @Override
    public void analyze(ParseTree tree, SymbolTable symbolTable, List<CompilationError> errors) {
        if (tree == null) {
            return;
        }
        try {
            YSemanticVisitor semantic = new YSemanticVisitor(symbolTable);
            semantic.visit(tree);
            markLanguage(symbolTable, displayName());
            errors.addAll(semantic.getErrors());
        } catch (Exception e) {
            errors.add(internalError(displayName(), e, tree));
        }
    }

    @Override
    public void generateIntermediate(ParseTree tree, SymbolTable symbolTable, C3DGenerator generator) {
        if (tree == null) {
            return;
        }
        try {
            YC3DVisitor c3d = new YC3DVisitor(symbolTable, generator);
            c3d.visit(tree);
        } catch (Exception ignored) {

        }
    }
}
