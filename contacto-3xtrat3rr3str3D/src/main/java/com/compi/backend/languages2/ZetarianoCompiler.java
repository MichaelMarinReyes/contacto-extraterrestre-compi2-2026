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
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Pipeline de Zetariano (sintaxis tipo Java).
 */
public class ZetarianoCompiler extends AbstractLanguageCompiler {

    /**
     * Nombre del archivo que se esta compilando.
     *
     * <p>Zetariano exige que un archivo se llame como la clase que declara, asi
     * que el nombre hace falta en la fase semantica.</p>
     */
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
                    errorCollector(lexer.getVocabulary(), errors, ErrorType.LEXICO));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ZetarianoParser parser = new ZetarianoParser(tokens);
            installErrorListener(parser,
                    errorCollector(parser.getVocabulary(), errors, ErrorType.SINTACTICO));
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
            int antes = symbolTable.getAllSymbols().size();
            ZetarianoSemanticVisitor semantic = new ZetarianoSemanticVisitor(symbolTable);
            semantic.visit(tree);
            marcarLenguaje(symbolTable, antes, displayName());
            errors.addAll(semantic.getErrors());
            comprobarNombreDelArchivo(tree, errors);
        } catch (Exception e) {
            // Con el arbol a mano el error sale en la linea en la que esta el
            // fuente, y no sin posicion.
            errors.add(internalError(displayName(), e, tree));
        }
    }

    /**
     * El enunciado pide que un archivo de Zetariano se llame como la clase que
     * declara dentro: {@code Persona.z} para {@code class Persona}.
     *
     * <p>Se mira la clase publica si la hay, que es la que da nombre al archivo,
     * y si no la primera declarada.</p>
     */
    private void comprobarNombreDelArchivo(ParseTree tree, List<CompilationError> errors) {
        if (sourceFileName == null || !(tree instanceof ZetarianoParser.ProgramaContext programa)) {
            return;
        }
        String base = sourceFileName;
        int corte = Math.max(base.lastIndexOf('/'), base.lastIndexOf('\\'));
        if (corte >= 0) {
            base = base.substring(corte + 1);
        }
        int punto = base.lastIndexOf('.');
        String esperado = punto > 0 ? base.substring(0, punto) : base;

        ZetarianoParser.ClaseDefContext clase = null;
        for (ZetarianoParser.ClaseDefContext c : programa.claseDef()) {
            if (clase == null) {
                clase = c;
            }
            if (c.PUBLIC() != null) {
                clase = c;
                break;
            }
        }
        if (clase == null || clase.ID() == null) {
            return;
        }
        String declarado = clase.ID().getText();
        if (!declarado.equals(esperado)) {
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "El archivo se llama \"" + esperado + "\" pero declara la clase \""
                            + declarado + "\": en Zetariano el archivo debe llamarse "
                            + "como la clase que define",
                    clase.getStart().getLine(), clase.getStart().getCharPositionInLine()));
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
