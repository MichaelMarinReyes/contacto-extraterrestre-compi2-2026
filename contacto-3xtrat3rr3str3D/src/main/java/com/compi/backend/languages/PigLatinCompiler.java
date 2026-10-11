package com.compi.backend.languages;

import com.compi.PigLatinLexer;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.piglatin.PigLatinC3DVisitor;
import com.compi.backend.languages.piglatin.PigLatinAstBuilder;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.ast.semantic.SemanticModel;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

public class PigLatinCompiler extends AbstractLanguageCompiler {
    private final Map<String, ProgramNodeAST> astImports = new LinkedHashMap<>();
    private final Map<String, SemanticModel> semanticImports = new LinkedHashMap<>();

    @Override
    public ProgramNodeAST buildAst(ParseTree tree, String sourceFile) {
        return constructAst(new PigLatinAstBuilder(), tree, sourceFile);
    }

    @Override
    public Map<String, ProgramNodeAST> importedAsts() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(astImports));
    }

    @Override
    public Map<String, SemanticModel> importedSemanticModels() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(semanticImports));
    }

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
                    errorCollector(lexer.getVocabulary(), errors, ErrorType.LEXICAL));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PigLatinParser parser = new PigLatinParser(tokens);
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
    public void generateIntermediate(ParseTree tree, SymbolTable symbolTable, C3DGenerator generator) {
        if (tree == null) {
            return;
        }
        try {
            symbolTable.beginCodeGeneration();
            PigLatinC3DVisitor c3d = new PigLatinC3DVisitor(symbolTable, generator);
            c3d.visit(tree);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo generar el código intermedio de PigLatin", exception);
        } finally {
            symbolTable.endCodeGeneration();
        }
    }

    @Override
    public List<String> loadImports(ParseTree tree, SymbolTable symbolTable,
                                    C3DGenerator generator, List<CompilationError> errors,
                                    List<String> cargados) {
        List<String> nuevos = new ArrayList<>();
        if (!(tree instanceof PigLatinParser.InitContext init)) {
            return nuevos;
        }
        astImports.clear();
        semanticImports.clear();
        for (PigLatinParser.Import_declaracionContext impCtx
                : init.pig_latin().import_declaracion()) {
            nuevos.addAll(loadImport(impCtx, symbolTable, generator, errors, cargados));
        }
        return nuevos;
    }

    private List<String> loadImport(PigLatinParser.Import_declaracionContext impCtx,
                                    SymbolTable symbolTable, C3DGenerator generator,
                                    List<CompilationError> errors, List<String> cargados) {
        List<String> nuevos = new ArrayList<>();
        String path = importPath(impCtx);

        File found = resolveImportFile(path);
        if (found == null) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "No se encontró el archivo importado \"" + path + "\" ("
                            + LanguageCompilerFactory.extensionPattern() + ")",
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return nuevos;
        }

        String key = found.toPath().toAbsolutePath().normalize().toString();
        if (cargados.contains(key)) {
            return nuevos;
        }
        cargados.add(key);
        nuevos.add(relativeToProject(found));
        compileImport(impCtx, found, key, symbolTable, generator, errors, cargados, nuevos);
        return nuevos;
    }

    private void compileImport(PigLatinParser.Import_declaracionContext impCtx, File found,
                               String key, SymbolTable symbolTable, C3DGenerator generator,
                               List<CompilationError> errors, List<String> cargados,
                               List<String> nuevos) {
        String content;
        try {
            content = Files.readString(found.toPath());
        } catch (IOException e) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "No se pudo leer el archivo importado \"" + key + "\": " + e.getMessage(),
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return;
        }

        LanguageCompiler target = LanguageCompilerFactory.getCompiler(found.getName());
        if (target == null) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "El archivo importado \"" + key + "\" no es de un lenguaje soportado",
                    impCtx.getStart().getLine(), impCtx.getStart().getCharPositionInLine()));
            return;
        }
        target.setWorkingDirectory(found.getAbsoluteFile().getParentFile());
        target.setProjectDirectory(getProjectDirectory());
        target.setSourceFileName(found.getName());
        List<CompilationError> sink = new ArrayList<>();
        ParseTree importedTree = target.parse(content, sink);
        ProgramNodeAST importedAst = sink.isEmpty() && importedTree != null ? target.buildAst(importedTree, key) : null;
        if (importedAst != null) astImports.put(key, importedAst);
        if (sink.isEmpty()) {
            nuevos.addAll(target.loadImports(importedTree, symbolTable, generator, sink, cargados));
            astImports.putAll(target.importedAsts());
            target.analyze(importedAst, symbolTable, sink);
            if (target.semanticModel() != null) semanticImports.put(key, target.semanticModel());
            semanticImports.putAll(target.importedSemanticModels());
        }
        if (sink.isEmpty()) {
            target.generateIntermediate(importedTree, symbolTable, generator);
        }
        for (CompilationError e : sink) {
            errors.add(e.inFile(found.getPath()));
        }
    }

    private String importPath(PigLatinParser.Import_declaracionContext impCtx) {
        List<String> partes = new ArrayList<>();
        for (TerminalNode v : impCtx.VARIABLE()) {
            partes.add(v.getText());
        }
        int last = partes.size() - 1;
        if (last >= 1
                && LanguageCompilerFactory.byExtensionLanguage(partes.get(last)) != null) {
            partes.set(last - 1, partes.get(last - 1) + "." + partes.get(last));
            partes.remove(last);
        }
        return String.join("/", partes);
    }

    private File resolveImportFile(String path) {
        File local = resolveInDirectory(getWorkingDirectory(), path);
        return local != null ? local : resolveInDirectory(getProjectDirectory(), path);
    }

    private File resolveInDirectory(File directory, String path) {
        if (LanguageCompilerFactory.hasAllowedExtension(path)) {
            File suchFile = new File(directory, path);
            if (suchFile.isFile()) {
                return suchFile;
            }
        }
        for (String ext : LanguageCompilerFactory.allowedExtensions()) {
            File conExtension = new File(directory, path + "." + ext);
            if (conExtension.isFile()) {
                return conExtension;
            }
        }

        return null;
    }

    private void findByPathSuffix(File dir, String path, List<File> out) {
        if (dir == null || !dir.isDirectory() || out.isEmpty()) {
            return;
        }
        File[] hijos = dir.listFiles();
        if (hijos == null) {
            return;
        }

        for (File child : hijos) {
            if (child.getName().startsWith(".")) {
                continue;
            }
            if (child.isDirectory()) {
                findByPathSuffix(child, path, out);
            } else if (matchesImportPath(child, path)) {
                out.add(child);
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
        String esperada = path.endsWith("." + ext) ? path : path + "." + ext;
        return relative.equals(esperada);
    }

    private String relativeToProject(File file) {
        String base = getProjectDirectory().getAbsolutePath();
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
