package com.compi.backend.languages2;

import com.compi.PigLatinLexer;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.piglatin.PigLatinC3DVisitor;
import com.compi.backend.languages.piglatin.PigLatinSemanticVisitor;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Pipeline de PigLatin: lexer, parser, semantico y C3D.
 */
public class PigLatinCompiler extends AbstractLanguageCompiler {

    @Override
    public String id() {
        return "pig";
    }

    @Override
    public String displayName() {
        return "PigLatin";
    }

    @Override
    public List<String> extensions() {
        return List.of(".pig");
    }

    @Override
    public ParseTree parse(String source, List<CompilationError> errors) {
        try {
            PigLatinLexer lexer = new PigLatinLexer(CharStreams.fromString(source));
            installErrorListener(lexer,
                    errorCollector(lexer.getVocabulary(), errors, ErrorType.LEXICO));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PigLatinParser parser = new PigLatinParser(tokens);
            installErrorListener(parser,
                    errorCollector(parser.getVocabulary(), errors, ErrorType.SINTACTICO));
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
            PigLatinSemanticVisitor semantic = new PigLatinSemanticVisitor(symbolTable);
            semantic.visit(tree);
            errors.addAll(semantic.getErrors());
        } catch (Exception e) {
            // Con el arbol a mano el error sale en la linea en la que esta el
            // fuente, y no sin posicion.
            errors.add(internalError(displayName(), e, tree));
        }
    }

    @Override
    public void generateIntermediate(ParseTree tree, SymbolTable symbolTable, C3DGenerator generator) {
        if (tree == null) {
            return;
        }
        try {
            PigLatinC3DVisitor c3d = new PigLatinC3DVisitor(symbolTable, generator);
            c3d.visit(tree);
        } catch (Exception ignored) {
            // La generacion de C3D nunca aborta la compilacion.
        }
    }

    // ====================== Imports ======================

    /**
     * Carga los archivos que el fuente importa, antes de analizarlo.
     *
     * <p>{@code import a.B;} se busca como {@code a/B} mas la extension que
     * toque. Primero se mira en la carpeta del proyecto y, si no esta, se busca en
     * el arbol del proyecto un archivo que termine en esa ruta: asi
     * {@code import util.Helper;} funciona tanto si {@code util} esta en la raiz
     * como si esta dentro de una subcarpeta.</p>
     *
     * <p>Cada archivo importado se compila entero (parseo, sus propios imports,
     * semantica y C3D) en la misma tabla y el mismo generador que el archivo que
     * lo importa, de modo que sus simbolos existen cuando se resuelve el fuente
     * que los usa y su codigo queda antes en el programa final. Los archivos ya
     * cargados no se vuelven a cargar, asi que dos imports del mismo archivo (o un
     * ciclo) no duplican nada.</p>
     */
    @Override
    public List<String> loadImports(ParseTree tree, SymbolTable symbolTable,
                                    C3DGenerator generator, List<CompilationError> errors,
                                    List<String> cargados) {
        List<String> nuevos = new ArrayList<>();
        if (!(tree instanceof PigLatinParser.InitContext init)) {
            return nuevos;
        }
        for (PigLatinParser.Import_declaracionContext impCtx
                : init.pig_latin().import_declaracion()) {
            nuevos.addAll(loadImport(impCtx, symbolTable, generator, errors, cargados));
        }
        return nuevos;
    }

    /**
     * Carga un archivo importado y devuelve las rutas que cargo, el suyo y los de
     * sus propios imports.
     *
     * @param cargados archivos ya cargados en esta compilacion, para no cargar dos
     *                 veces el mismo (ni entrar en un ciclo)
     */
    private List<String> loadImport(PigLatinParser.Import_declaracionContext impCtx,
                                    SymbolTable symbolTable, C3DGenerator generator,
                                    List<CompilationError> errors, List<String> cargados) {
        List<String> nuevos = new ArrayList<>();
        StringBuilder rawPath = new StringBuilder();
        for (int i = 0; i < impCtx.VARIABLE().size(); i++) {
            if (i > 0) {
                rawPath.append('/');
            }
            rawPath.append(impCtx.VARIABLE(i).getText());
        }
        String path = rawPath.toString();

        File found = resolveImportFile(path);
        if (found == null) {
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "No se encontró el archivo importado \"" + path + "\" ("
                            + LanguageCompilerFactory.extensionPattern() + ")",
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return nuevos;
        }

        String key = relativeToProject(found);
        if (cargados.contains(key)) {
            return nuevos;
        }
        cargados.add(key);
        nuevos.add(key);

        String content;
        try {
            content = Files.readString(found.toPath());
        } catch (IOException e) {
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "No se pudo leer el archivo importado \"" + key + "\": " + e.getMessage(),
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return nuevos;
        }

        // El archivo importado se compila con el compilador de su propia extension,
        // que es lo unico que decide como se lee.
        LanguageCompiler target = LanguageCompilerFactory
                .getCompiler(LanguageCompilerFactory.extensionOf(found.getName()));
        if (target == null) {
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "El archivo importado \"" + key + "\" no es de un lenguaje soportado",
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return nuevos;
        }
        target.setWorkingDirectory(getWorkingDirectory());

        // Los errores del archivo importado se anotan con su nombre, para que en
        // la tabla se sepa de que archivo son y no del que lo importa.
        List<CompilationError> sink = new ArrayList<>();
        ParseTree importedTree = target.parse(content, sink);
        if (sink.isEmpty()) {
            nuevos.addAll(target.loadImports(importedTree, symbolTable, generator, sink, cargados));
            List<Symbol> antes = new ArrayList<>(symbolTable.getAllSymbols());
            target.analyze(importedTree, symbolTable, sink);
            marcarOrigen(symbolTable, antes, key);
        }
        target.generateIntermediate(importedTree, symbolTable, generator);
        for (CompilationError e : sink) {
            errors.add(e.inFile(key));
        }
        return nuevos;
    }

    /**
     * Anota con que archivo se declararon los simbolos que acaba de añadir el
     * archivo importado.
     *
     * <p>Sin esto la tabla de simbolos de {@code main.pig} mostraria los simbolos
     * de un import con la linea que tienen en <em>sus</em> archivo, y al hacer
     * doble clic se saltaria a una linea que no es la de ese simbolo.</p>
     */
    private void marcarOrigen(SymbolTable symbolTable, List<Symbol> antes, String key) {
        List<Symbol> despues = symbolTable.getAllSymbols();
        for (int i = antes.size(); i < despues.size(); i++) {
            despues.get(i).inFile(key);
        }
    }

    /** Busca el archivo de un import, con cualquiera de las extensiones admitidas. */
    private File resolveImportFile(String path) {
        for (String ext : LanguageCompilerFactory.allowedExtensions()) {
            File directo = new File(getWorkingDirectory(), path + "." + ext);
            if (directo.isFile()) {
                return directo;
            }
        }
        // El import puede escrito desde la raiz mientras el archivo esta en una
        // subcarpeta: se busca por final de ruta dentro del proyecto.
        List<File> porFinalDeRuta = new ArrayList<>();
        buscarPorFinalDeRuta(getWorkingDirectory(), path, porFinalDeRuta);
        return porFinalDeRuta.isEmpty() ? null : porFinalDeRuta.get(0);
    }

    private void buscarPorFinalDeRuta(File dir, String path, List<File> out) {
        if (dir == null || !dir.isDirectory() || out.isEmpty()) {
            return;
        }
        File[] hijos = dir.listFiles();
        if (hijos == null) {
            return;
        }
        // Las carpetas ocultas y las de compilacion no tienen codigo fuente que
        // importar, y bajarlas solo haria la busqueda mas lenta.
        for (File hijo : hijos) {
            if (hijo.getName().startsWith(".")) {
                continue;
            }
            if (hijo.isDirectory()) {
                buscarPorFinalDeRuta(hijo, path, out);
            } else if (matchesImportPath(hijo, path)) {
                out.add(hijo);
                if (!out.isEmpty()) {
                    return;
                }
            }
        }
    }

    private boolean matchesImportPath(File file, String path) {
        String ext = LanguageCompilerFactory.extensionOf(file.getName());
        if (ext == null) {
            return false;
        }
        String relative = relativeToProject(file).replace(File.separatorChar, '/');
        return relative.equals(path + "." + ext);
    }

    /** Ruta del archivo respecto a la carpeta del proyecto, para el informe. */
    private String relativeToProject(File file) {
        String base = getWorkingDirectory().getAbsolutePath();
        String full = file.getAbsolutePath();
        if (full.startsWith(base)) {
            String relative = full.substring(base.length());
            while (relative.startsWith(File.separator)) {
                relative = relative.substring(File.separator.length());
            }
            return relative.isEmpty() ? file.getName() : relative;
        }
        return file.getName();
    }
}
