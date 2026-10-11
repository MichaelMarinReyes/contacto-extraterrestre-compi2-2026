package com.compi.backend.languages;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.languages.zetariano.ZetarianoAstBuilder;

import com.compi.ZetarianoLexer;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ZetarianoCompiler extends AbstractLanguageCompiler {
    @Override
    public ProgramNodeAST buildAst(ParseTree tree, String sourceFile) {
        return constructAst(new ZetarianoAstBuilder(), tree, sourceFile);
    }

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
    public void analyze(ProgramNodeAST tree, SymbolTable symbolTable, List<CompilationError> errors) {
        super.analyze(tree, symbolTable, errors);
        checkFileName(tree, errors);
    }

    private void checkFileName(ProgramNodeAST program, List<CompilationError> errors) {
        if (sourceFileName == null || program == null) {
            return;
        }
        String base = sourceFileName;
        int truncation = Math.max(base.lastIndexOf('/'), base.lastIndexOf('\\'));
        if (truncation >= 0) {
            base = base.substring(truncation + 1);
        }
        int point = base.lastIndexOf('.');
        String esperado = point > 0 ? base.substring(0, point) : base;

        com.compi.backend.ast.decl.ClassDefinitionNodeAST clazz = null;
        for (var definition : program.definitions()) {
            if (!(definition instanceof com.compi.backend.ast.decl.ClassDefinitionNodeAST c)) continue;
            if (clazz == null) {
                clazz = c;
            }
            if (c.publicClass()) {
                clazz = c;
                break;
            }
        }
        if (clazz == null) {
            return;
        }
        String declaredType = clazz.name();
        if (!declaredType.equals(esperado)) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "El archivo se llama \"" + esperado + "\" pero declara la clase \""
                            + declaredType + "\": en Zetariano el archivo debe llamarse "
                            + "como la clase que define",
                    clazz.line(), clazz.column()).inFile(program.sourceFile()));
        }
    }

}
