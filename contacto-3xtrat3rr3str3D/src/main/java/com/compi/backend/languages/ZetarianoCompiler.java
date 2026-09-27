package com.compi.backend.languages;

import com.compi.ZetarianoLexer;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.zetariano.ZetarianoC3DVisitor;
import com.compi.backend.languages.zetariano.ZetarianoSemanticVisitor;
import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ZetarianoCompiler extends AbstractLanguageCompiler {

    private String sourceFileName;

    @Override
    public String id() {
        return "zet";
    }

    @Override
    public void setSourceFileName(String fileName) {
        this.sourceFileName = fileName;
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
            ZetarianoLexer lexer = new ZetarianoLexer(CharStreams.fromString(source));
            installErrorListener(lexer,
                    errorCollector(lexer.getVocabulary(), errors, ErrorType.LEXICAL));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ZetarianoParser parser = new ZetarianoParser(tokens);
            installErrorListener(parser,
                    errorCollector(parser.getVocabulary(), errors, ErrorType.SYNTACTIC));
            rememberParser(parser);

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
            markLanguage(symbolTable, displayName());
            errors.addAll(semantic.getErrors());
            checkFileName(tree, errors);
        } catch (Exception e) {

            errors.add(internalError(displayName(), e, tree));
        }
    }

    private void checkFileName(ParseTree tree, List<CompilationError> errors) {
        if (sourceFileName == null || !(tree instanceof ZetarianoParser.ProgramaContext program)) {
            return;
        }
        String base = sourceFileName;
        int truncation = Math.max(base.lastIndexOf('/'), base.lastIndexOf('\\'));
        if (truncation >= 0) {
            base = base.substring(truncation + 1);
        }
        int point = base.lastIndexOf('.');
        String esperado = point > 0 ? base.substring(0, point) : base;

        ZetarianoParser.ClaseDefContext clazz = null;
        for (ZetarianoParser.ClaseDefContext c : program.claseDef()) {
            if (clazz == null) {
                clazz = c;
            }
            if (c.PUBLIC() != null) {
                clazz = c;
                break;
            }
        }
        if (clazz == null || clazz.ID() == null) {
            return;
        }
        String declaredType = clazz.ID().getText();
        if (!declaredType.equals(esperado)) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "El archivo se llama \"" + esperado + "\" pero declara la clase \""
                            + declaredType + "\": en Zetariano el archivo debe llamarse "
                            + "como la clase que define",
                    clazz.getStart().getLine(), clazz.getStart().getCharPositionInLine()));
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

        }
    }
}
