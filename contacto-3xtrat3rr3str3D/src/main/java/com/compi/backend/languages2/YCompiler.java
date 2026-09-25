package com.compi.backend.languages2;

import com.compi.YParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.y.YC3DVisitor;
import com.compi.backend.languages.y.YIndentLexer;
import com.compi.backend.languages.y.YSemanticVisitor;
import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Pipeline de Y: usa {@link YIndentLexer} porque la gramatica depende de
 * INDENT / DEDENT.
 */
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
            List<CompilationError> sink = errors;

            BaseErrorListener lexListener = new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> r, Object o, int l, int c, String m,
                                        RecognitionException e) {
                    sink.add(new CompilationError(ErrorType.LEXICO, m, l, c));
                }
            };
            BaseErrorListener parListener = new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> r, Object o, int l, int c, String m,
                                        RecognitionException e) {
                    sink.add(new CompilationError(ErrorType.SINTACTICO, m, l, c));
                }
            };

            YIndentLexer lexer = new YIndentLexer(CharStreams.fromString(source));
            installErrorListener(lexer, lexListener);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            YParser parser = new YParser(tokens);
            installErrorListener(parser, parListener);

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
            errors.addAll(semantic.getErrors());
        } catch (Exception e) {
            errors.add(internalError(displayName(), e));
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
            // La generacion de C3D nunca aborta la compilacion.
        }
    }
}
