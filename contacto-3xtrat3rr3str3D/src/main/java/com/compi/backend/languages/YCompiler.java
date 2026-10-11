package com.compi.backend.languages;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.languages.y.YAstBuilder;

import com.compi.YParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.y.YIndentLexer;
import com.compi.backend.symbols.SymbolTable;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class YCompiler extends AbstractLanguageCompiler {
    @Override
    public ProgramNodeAST buildAst(ParseTree tree, String sourceFile) {
        return constructAst(new YAstBuilder(), tree, sourceFile);
    }

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

}
