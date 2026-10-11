package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.TypeEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/** Coordina compilación, publicación de resultados y navegación a sus posiciones. */
final class CompilationActions {
    private static final String MAIN_FILE = "main.pig";
    private final MainWindow window;
    private final Compiler compiler = new Compiler();
    private Mode currentCMode = Mode.QUADRUPLES;
    private List<File> lastTargets = List.of();
    private File lastFocusFile;

    private record FileResult(File file, String language, boolean ok,
                              String astDot, List<Symbol> symbols, List<TypeEntry> types, List<ParseStep> steps,
                              List<CompilationError> errors, int quadruples, int triplets,
                              String tripletsCode, String quadruplesCode,
                              String c3dCode, String cCode, String translated, List<String> imports) { }

    CompilationActions(MainWindow window) {
        this.window = window;
    }

    void setMode(Mode mode) {
        currentCMode = mode;
        compiler.setCMode(mode);
    }

    void compile() {
        clearResults();
        List<File> targets = compileTargets();
        if (targets.isEmpty()) {
            window.getConsoleDock().setConsole("No hay nada que compilar.\n"
                    + "Abre una carpeta de proyecto o un archivo de código.\n");
            window.navText("Sin archivos que compilar");
            return;
        }
        File container = window.currentContainer();
        window.getConsoleDock().setConsole(compilationHeader(targets, container));
        compiler.setCMode(currentCMode);
        compiler.setWorkingDirectory(container);
        List<FileResult> results = new ArrayList<>(targets.size());
        for (File file : targets) results.add(compileOne(file));
        publishResults(results);
    }

    private String compilationHeader(List<File> targets, File container) {
        File main = targets.stream().filter(CompilationActions::isMainFile).findFirst().orElse(null);
        StringBuilder header = new StringBuilder();
        header.append("Compilando ").append(targets.size())
                .append(targets.size() == 1 ? " archivo" : " archivos")
                .append(container != null ? " de " + container.getName() : " abiertos").append("...\n");
        if (main != null) header.append("Punto de partida: ").append(main.getName())
                .append(" (se lee primero; sus imports se cargan antes)\n");
        else if (container != null) header.append("No hay ningún ").append(MAIN_FILE)
                .append(" en el proyecto: se compilan los archivos por orden de nombre\n");
        return header.toString();
    }

    private List<File> compileTargets() {
        List<File> targets = window.getFileTree().allSourceFiles();
        if (!targets.isEmpty()) return startingAtMain(targets);
        for (EditorPanel editor : window.getEditorTabs().getAllEditors()) {
            File file = editor.getFile();
            if (file != null && file.isFile()) targets.add(file);
        }
        return targets;
    }

    private static List<File> startingAtMain(List<File> targets) {
        File main = null;
        for (File file : targets) {
            if (!isMainFile(file)) continue;
            if (main == null || file.getAbsolutePath().length() < main.getAbsolutePath().length()) main = file;
        }
        if (main == null) return targets;
        List<File> ordered = new ArrayList<>(targets.size());
        ordered.add(main);
        for (File file : targets) if (!file.equals(main)) ordered.add(file);
        return ordered;
    }

    private static boolean isMainFile(File file) {
        return file != null && MAIN_FILE.equalsIgnoreCase(file.getName());
    }

    private FileResult compileOne(File file) {
        EditorPanel editor = window.editorOf(file);
        String language = editor != null ? editor.getLanguageId()
                : LanguageCompilerFactory.detectLanguageId(file.getName());
        String source;
        if (editor != null) source = editor.getCodeText();
        else {
            try {
                source = Files.readString(file.toPath());
            } catch (IOException e) {
                CompilationError error = new CompilationError(ErrorType.SEMANTIC,
                        "No se pudo leer el archivo: " + e.getMessage(), -1, -1).inFile(file.getName());
                return new FileResult(file, language, false, "", List.of(), List.of(), List.of(), List.of(error),
                        0, 0, "", "", "", "", "", List.of());
            }
        }
        boolean ok = compiler.compile(source, language, file.getAbsolutePath());
        List<CompilationError> errors = new ArrayList<>();
        for (CompilationError error : compiler.getErrors()) errors.add(error.inFile(file.getName()));
        List<Symbol> symbols = new ArrayList<>(compiler.getSymbols());
        for (Symbol symbol : symbols) if (symbol.getSourceFile() == null) symbol.inFile(file.getName());
        return new FileResult(file, language, ok, compiler.getAstDot(), symbols,
                compiler.getTypes(), new ArrayList<>(compiler.getParseSteps()), errors,
                compiler.getQuadrupleList().size(), compiler.getIcm().getTriplets().size(),
                compiler.getTriplets(), compiler.getQuadruples(), compiler.getC3DCode(),
                compiler.getCCode(), compiler.getTranslatedCode(), compiler.getImportedFiles());
    }

    private void publishResults(List<FileResult> results) {
        List<CompilationError> errors = new ArrayList<>();
        for (FileResult result : results) errors.addAll(result.errors());
        FileResult focus = focusedResult(results);
        lastTargets = results.stream().map(FileResult::file).toList();
        lastFocusFile = focus == null ? null : focus.file();
        publishToolResults(focus, aggregatedSymbols(results), errors);
        publishCode(focus, errors);
        publishStatus(results, errors);
        String warning = errors.isEmpty() ? writeCFile(results) : "";
        window.getConsoleDock().appendConsole(buildReport(results, errors, focus));
        window.getConsoleDock().appendConsole(warning);
    }

    private void publishToolResults(FileResult focus, List<Symbol> symbols, List<CompilationError> errors) {
        window.getToolWindow().postResults(() -> {
            window.getAstPanel().renderGraph(focus == null ? "" : focus.astDot());
            window.getSymbolPanel().loadSymbols(symbols);
            window.getTypePanel().loadTypes(focus == null ? List.of() : focus.types(),
                    focus == null ? "" : focus.file().getName());
            window.getStackPanel().loadSteps(focus == null ? List.of() : focus.steps());
            window.getErrorPanel().loadErrors(errors);
            window.getToolWindowDock().setErrorCount(errors.size());
            if (window.getErrorPanel().getErrorCount() > 0) {
                window.getErrorPanel().getErrorTable().setRowSelectionInterval(0, 0);
            }
        });
    }

    private void publishCode(FileResult focus, List<CompilationError> errors) {
        ConsoleDock console = window.getConsoleDock();
        console.setTriplets(focus == null ? "" : focus.tripletsCode());
        console.setQuadruples(focus == null ? "" : focus.quadruplesCode());
        console.setC3D(focus == null ? "" : focus.c3dCode());
        console.setCCode(focus == null ? "" : focus.cCode());
        console.setSummary(focus == null ? "" : focus.translated());
        console.setStatus("Modo de generación de C: " + TopToolbar.modeLabel(currentCMode));
        window.getStatusBar().setResult(errors.isEmpty(), errors.size());
        window.getStatusBar().setCounts(focus == null ? 0 : focus.quadruples(), focus == null ? 0 : focus.triplets());
    }

    private void publishStatus(List<FileResult> results, List<CompilationError> errors) {
        if (errors.isEmpty()) {
            window.navText("Compilación correcta / " + results.size()
                    + (results.size() == 1 ? " archivo" : " archivos"));
            return;
        }
        window.showToolView(ToolWindowDock.View.ERRORS);
        CompilationError first = errors.get(0);
        if (first.getLine() > 0) {
            EditorPanel editor = window.editorOf(fileNamed(first.getFileName()));
            if (editor != null) editor.gotoLine(first.getLine());
        }
        window.navText(errors.size() + (errors.size() == 1 ? " error de compilación" : " errores de compilación"));
    }

    private String writeCFile(List<FileResult> results) {
        File container = window.currentContainer();
        if (container == null || results.isEmpty()) return "";
        FileResult principal = null;
        for (FileResult result : results) {
            if (isMainFile(result.file())) {
                principal = result;
                break;
            }
            if (principal == null && result.ok()) principal = result;
        }
        if (principal == null || principal.cCode() == null || principal.cCode().isBlank()) return "";
        String name = principal.file().getName();
        int point = name.lastIndexOf('.');
        File target = new File(container, (point > 0 ? name.substring(0, point) : name) + ".c");
        try {
            Files.writeString(target.toPath(), principal.cCode());
            return "\nC generado: " + target.getPath() + "\n";
        } catch (IOException e) {
            return "\nNo se pudo escribir el C en la raiz del proyecto: " + e.getMessage() + "\n";
        }
    }

    void clearResults() {
        lastTargets = List.of();
        lastFocusFile = null;
        window.clearToolViews();
        window.getStatusBar().setCounts(0, 0);
        window.getStatusBar().setIdle();
        window.navText("Compilando...");
    }

    private File fileNamed(String name) {
        if (name == null) return null;
        for (File file : lastTargets) if (file.getName().equals(name)) return file;
        return null;
    }

    void jumpToError(int row) {
        CompilationError error = window.getErrorPanel().errorAt(row);
        if (error == null) return;
        File file = fileNamed(error.getFileName());
        if (file == null) {
            window.promptInfo("El archivo de este error ya no esta en el proyecto:\n" + error.getFileName(),
                    "Archivo no encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (file.isFile()) {
            EditorPanel editor = window.editorOf(file);
            if (editor != null && editor.isModified()) window.getEditorTabs().save(editor);
        }
        window.openInEditor(file);
        EditorPanel target = window.editorOf(file);
        if (target != null && error.getLine() > 0) target.gotoLine(error.getLine());
        window.navText(error.getFileName() + " / línea " + error.getLine());
    }

    void jumpToSymbol(int row) {
        Symbol symbol = window.getSymbolPanel().symbolAt(row);
        if (symbol == null) return;
        if (symbol.getLine() <= 0) {
            window.promptInfo("«" + symbol.getName() + "» lo genera el compilador, no aparece escrito en el fuente.\n"
                    + "No hay ninguna línea a la que saltar.", "Símbolo sin posición", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        File file = symbolFile(symbol);
        if (file == null) return;
        if (file.isFile()) window.openInEditor(file);
        EditorPanel target = window.editorOf(file);
        if (target != null) target.gotoLine(symbol.getLine());
        window.navText(file.getName() + " / línea " + symbol.getLine() + " / " + symbol.getName());
    }

    private File symbolFile(Symbol symbol) {
        String origin = symbol.getSourceFile();
        if (origin == null || origin.isBlank()) return lastFocusFile;
        File direct = new File(origin);
        if (direct.isAbsolute()) return direct.isFile() ? direct : lastFocusFile;
        File container = window.currentContainer();
        if (container != null) {
            File resolved = new File(container, origin);
            if (resolved.isFile()) return resolved;
        }
        return lastFocusFile;
    }

    private FileResult focusedResult(List<FileResult> results) {
        EditorPanel active = window.getEditorTabs().getActiveEditor();
        if (active != null && active.getFile() != null) {
            for (FileResult result : results) if (result.file().equals(active.getFile())) return result;
        }
        for (FileResult result : results) if (isMainFile(result.file())) return result;
        for (FileResult result : results) if (!result.astDot().isBlank()) return result;
        return results.isEmpty() ? null : results.get(0);
    }

    private List<Symbol> aggregatedSymbols(List<FileResult> results) {
        List<Symbol> symbols = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (FileResult result : results) {
            for (Symbol symbol : result.symbols()) if (seen.add(symbolKey(symbol))) symbols.add(symbol);
        }
        return symbols;
    }

    private static String symbolKey(Symbol symbol) {
        return (symbol.getLanguage() == null ? "?" : symbol.getLanguage()) + "|"
                + (symbol.getCategory() == null ? "?" : symbol.getCategory().name()) + "|"
                + (symbol.getScope() == null ? "" : symbol.getScope()) + "|" + symbol.getName() + "|"
                + (symbol.getType() == null ? "" : symbol.getType().label()) + "|"
                + symbol.getLine() + "|" + symbol.getColumn();
    }

    private String focusReason(FileResult focus) {
        EditorPanel active = window.getEditorTabs().getActiveEditor();
        if (active != null && active.getFile() != null && focus.file().equals(active.getFile())) return "pestaña activa";
        return isMainFile(focus.file()) ? MAIN_FILE + " (principal)" : "primer archivo del proyecto";
    }

    private String buildReport(List<FileResult> results, List<CompilationError> errors, FileResult focus) {
        StringBuilder text = new StringBuilder();
        for (FileResult result : results) appendFileReport(text, result);
        text.append('\n');
        if (errors.isEmpty()) text.append("Compilación correcta: ").append(results.size())
                .append(results.size() == 1 ? " archivo.\n" : " archivos.\n");
        else {
            text.append(errors.size()).append(errors.size() == 1 ? " error encontrado:\n" : " errores encontrados:\n");
            for (CompilationError error : errors) text.append("  ").append(error).append('\n');
        }
        if (focus != null) {
            text.append("\n--- Salidas de ").append(focus.file().getName()).append(" (")
                    .append(focusReason(focus)).append(") ---\n").append(focus.translated());
            text.append("\nAST generado: ").append(focus.astDot().isBlank() ? "no" : "sí")
                    .append("   Símbolos: ").append(focus.symbols().size())
                    .append("   Pila del parser: ").append(focus.steps().size()).append(" pasos\n");
        }
        return text.toString();
    }

    private void appendFileReport(StringBuilder text, FileResult result) {
        text.append(result.ok() ? "  OK    " : "  ERROR ").append(result.file().getName());
        if (isMainFile(result.file())) text.append("  <- principal");
        text.append("  (").append(UiTheme.prettifyLanguage(result.language())).append(')');
        if (!result.ok()) text.append("  ").append(result.errors().size())
                .append(result.errors().size() == 1 ? " error" : " errores");
        else text.append("  ").append(result.quadruples()).append(" cuartetas");
        if (!result.imports().isEmpty()) text.append("  importa: ").append(String.join(", ", result.imports()));
        text.append('\n');
    }
}
