package com.compi.backend;

import com.compi.*;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.CCodeEmitter;
import com.compi.backend.errors.AntlrErrorCollector;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.piglatin.PigLatinC3DVisitor;
import com.compi.backend.languages.piglatin.PigLatinSemanticVisitor;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.languages.y.YC3DVisitor;
import com.compi.backend.languages.y.YIndentLexer;
import com.compi.backend.languages.y.YSemanticVisitor;
import com.compi.backend.languages.zetariano.ZetarianoC3DVisitor;
import com.compi.backend.languages.zetariano.ZetarianoSemanticVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CompilerFacade {
    private final SymbolTable symbolTable = new SymbolTable();
    private final C3DGenerator c3dGenerator = new C3DGenerator();
    private final List<CompilationError> allErrors = new ArrayList<>();
    private File workingDirectory = new File(".");

    public CompilerFacade() {}

    public CompilerFacade(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setWorkingDirectory(File dir) {
        this.workingDirectory = dir;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<CompilationError> getErrors() {
        return allErrors;
    }

    public boolean hasErrors() {
        return !allErrors.isEmpty();
    }

    public C3DGenerator getC3DGenerator() {
        return c3dGenerator;
    }

    /**
     * Compila un código fuente en lenguaje 'Y'
     */
    public boolean compileY(String code) {
        try {
            AntlrErrorCollector lexerErrors = new AntlrErrorCollector(ErrorType.LEXICO);
            AntlrErrorCollector parserErrors = new AntlrErrorCollector(ErrorType.SINTACTICO);

            YIndentLexer lexer = new YIndentLexer(CharStreams.fromString(code));
            lexer.removeErrorListeners();
            lexer.addErrorListener(lexerErrors);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            YParser parser = new YParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(parserErrors);

            YParser.InitContext tree = parser.init();

            allErrors.addAll(lexerErrors.getErrors());
            allErrors.addAll(parserErrors.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Análisis semántico
            YSemanticVisitor semanticVisitor = new YSemanticVisitor(symbolTable);
            semanticVisitor.visit(tree);
            allErrors.addAll(semanticVisitor.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Generación de C3D
            YC3DVisitor c3dVisitor = new YC3DVisitor(symbolTable, c3dGenerator);
            c3dVisitor.visit(tree);
            return true;

        } catch (Exception e) {
            allErrors.add(new CompilationError(ErrorType.SEMANTICO, "Error interno al compilar Y: " + e.getMessage(), 1, 0));
            return false;
        }
    }

    /**
     * Compila un código fuente en lenguaje 'Zetariano'
     */
    public boolean compileZetariano(String code) {
        try {
            AntlrErrorCollector lexerErrors = new AntlrErrorCollector(ErrorType.LEXICO);
            AntlrErrorCollector parserErrors = new AntlrErrorCollector(ErrorType.SINTACTICO);

            ZetarianoLexer lexer = new ZetarianoLexer(CharStreams.fromString(code));
            lexer.removeErrorListeners();
            lexer.addErrorListener(lexerErrors);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ZetarianoParser parser = new ZetarianoParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(parserErrors);

            ZetarianoParser.ProgramaContext tree = parser.programa();

            allErrors.addAll(lexerErrors.getErrors());
            allErrors.addAll(parserErrors.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Semántica
            ZetarianoSemanticVisitor semanticVisitor = new ZetarianoSemanticVisitor(symbolTable);
            semanticVisitor.visit(tree);
            allErrors.addAll(semanticVisitor.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Generación C3D
            ZetarianoC3DVisitor c3dVisitor = new ZetarianoC3DVisitor(symbolTable, c3dGenerator);
            c3dVisitor.visit(tree);
            return true;

        } catch (Exception e) {
            allErrors.add(new CompilationError(ErrorType.SEMANTICO, "Error interno al compilar Zetariano: " + e.getMessage(), 1, 0));
            return false;
        }
    }

    /**
     * Compila un código fuente en lenguaje 'PigLatin' y resuelve sus imports dependientes (.y y .z)
     */
    public boolean compilePigLatin(String code) {
        try {
            AntlrErrorCollector lexerErrors = new AntlrErrorCollector(ErrorType.LEXICO);
            AntlrErrorCollector parserErrors = new AntlrErrorCollector(ErrorType.SINTACTICO);

            PigLatinLexer lexer = new PigLatinLexer(CharStreams.fromString(code));
            lexer.removeErrorListeners();
            lexer.addErrorListener(lexerErrors);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PigLatinParser parser = new PigLatinParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(parserErrors);

            PigLatinParser.InitContext tree = parser.init();

            allErrors.addAll(lexerErrors.getErrors());
            allErrors.addAll(parserErrors.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Cargar e importar archivos dependientes referenciados en 'import ...'
            for (PigLatinParser.Import_declaracionContext impCtx : tree.pig_latin().import_declaracion()) {
                resolveImport(impCtx);
            }

            // Semántica
            PigLatinSemanticVisitor semanticVisitor = new PigLatinSemanticVisitor(symbolTable);
            semanticVisitor.visit(tree);
            allErrors.addAll(semanticVisitor.getErrors());

            if (!allErrors.isEmpty()) return false;

            // Generación C3D
            PigLatinC3DVisitor c3dVisitor = new PigLatinC3DVisitor(symbolTable, c3dGenerator);
            c3dVisitor.visit(tree);
            return true;

        } catch (Exception e) {
            allErrors.add(new CompilationError(ErrorType.SEMANTICO, "Error interno al compilar PigLatin: " + e.getMessage(), 1, 0));
            return false;
        }
    }

    private void resolveImport(PigLatinParser.Import_declaracionContext impCtx) {
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < impCtx.VARIABLE().size(); i++) {
            if (i > 0) pathBuilder.append(File.separator);
            pathBuilder.append(impCtx.VARIABLE(i).getText());
        }

        String rawPath = pathBuilder.toString();
        // Buscar archivo con .y o .z
        File fileY = new File(workingDirectory, rawPath + ".y");
        File fileZ = new File(workingDirectory, rawPath + ".z");

        try {
            if (fileY.exists()) {
                String content = Files.readString(fileY.toPath());
                compileY(content);
            } else if (fileZ.exists()) {
                String content = Files.readString(fileZ.toPath());
                compileZetariano(content);
            }
        } catch (Exception e) {
            allErrors.add(new CompilationError(ErrorType.SEMANTICO, "No se pudo leer archivo importado: " + rawPath,
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
        }
    }

    /**
     * Genera el código final ejecutable en lenguaje C
     */
    public String emitCCode() {
        return CCodeEmitter.generateCCode(c3dGenerator.getQuadruples(), c3dGenerator.getDeclaredTemps());
    }

    /**
     * Retorna el C3D en formato legible
     */
    public String getC3DCode() {
        return c3dGenerator.toC3DString();
    }
}
