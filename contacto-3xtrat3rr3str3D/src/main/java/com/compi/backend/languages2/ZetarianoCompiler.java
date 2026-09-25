package com.compi.backend.languages2;

import com.compi.ZetarianoLexer;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.zetariano.ZetarianoC3DVisitor;
import com.compi.backend.languages.zetariano.ZetarianoSemanticVisitor;
import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Pipeline de Zetariano (sintaxis tipo Java).
 */
public class ZetarianoCompiler extends AbstractLanguageCompiler {

    @Override
    public String id() {
        return "zet";
    }

    @Override
    public String displayName() {
        return "Zetariano";
    }

    @Override
    public List<String> extensions() {
        return List.of(".z");
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

            ZetarianoLexer lexer = new ZetarianoLexer(CharStreams.fromString(source));
            installErrorListener(lexer, lexListener);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ZetarianoParser parser = new ZetarianoParser(tokens);
            installErrorListener(parser, parListener);

            return parser.programa();
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
            ZetarianoSemanticVisitor semantic = new ZetarianoSemanticVisitor(symbolTable);
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
            ZetarianoC3DVisitor c3d = new ZetarianoC3DVisitor(symbolTable, generator);
            c3d.visit(tree);
        } catch (Exception ignored) {
            // La generacion de C3D nunca aborta la compilacion.
        }
    }
}
