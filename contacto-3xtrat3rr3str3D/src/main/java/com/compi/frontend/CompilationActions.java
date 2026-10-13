package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.riscv.RiscvRunner;
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
import javax.swing.SwingWorker;

/** Coordina compilación, publicación de resultados y navegación a sus posiciones. */
final class CompilationActions {
    private final MainWindow window;
    private final Compiler compiler = new Compiler();
    private Mode currentCMode = Mode.QUADRUPLES;
    private List<File> lastTargets = List.of();
    private File lastFocusFile;
    private String lastRiscvCode = "";
    private SwingWorker<RiscvRunner.Result, Void> riscvWorker;
    private volatile boolean stopRequested;

    private record FileResult(File file, String language, boolean ok,
                              String astDot, List<Symbol> symbols, List<TypeEntry> types, List<ParseStep> steps,
                              List<CompilationError> errors, int quadruples, int triplets,
                              String tripletsCode, String quadruplesCode,
                              String c3dCode, String cCode, String riscvCode,
                              String translated, List<String> imports) { }

    CompilationActions(MainWindow window) {
        this.window = window;
    }

    void setMode(Mode mode) {
        currentCMode = mode;
        compiler.setCMode(mode);
    }

    void compile() {
        List<File> targets = compileTargets();
        if (targets.isEmpty()) {
            clearResults();
            window.getConsoleDock().setConsole("No hay ningún archivo .pig para iniciar el programa.\n"
                    + "Abre o crea un archivo PigLatin con su sección MAIOR.\n");
            window.navText("Sin entrada .pig");
            return;
        }
        File entry = targets.size() == 1 ? targets.get(0) : window.promptEntryFile(targets);
        if (entry == null) return;
        clearResults();
        File container = window.currentContainer();
        window.getConsoleDock().setConsole(compilationHeader(entry, container));
        compiler.setCMode(currentCMode);
        compiler.setWorkingDirectory(container);
        publishResults(List.of(compileOne(entry)));
    }

    private String compilationHeader(File entry, File container) {
        return "Compilando " + entry.getName()
                + (container == null ? "" : " de " + container.getName()) + "...\n"
                + "Punto de partida: " + entry.getAbsolutePath() + "\n"
                + "Se cargan sus importaciones .y y .z antes de generar C.\n";
    }

    private List<File> compileTargets() {
        List<File> targets = window.getFileTree().allSourceFiles();
        if (targets.isEmpty()) {
            for (EditorPanel editor : window.getEditorTabs().getAllEditors()) {
                File file = editor.getFile();
                if (file != null && file.isFile()) targets.add(file);
            }
        }
        return targets.stream().filter(file -> "pig".equals(LanguageCompilerFactory.detectLanguageId(file.getName())))
                .distinct().sorted(java.util.Comparator.comparing(File::getAbsolutePath)).toList();
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
                        0, 0, "", "", "", "", "", "", List.of());
            }
        }
        boolean ok = compiler.compile(source, language, file.getAbsolutePath());
        List<CompilationError> errors = new ArrayList<>();
        for (CompilationError error : compiler.getErrors()) {
            errors.add(error.getFileName() == null ? error.inFile(file.getAbsolutePath()) : error);
        }
        List<Symbol> symbols = new ArrayList<>(compiler.getSymbols());
        for (Symbol symbol : symbols) if (symbol.getSourceFile() == null) symbol.inFile(file.getName());
        return new FileResult(file, language, ok, compiler.getAstDot(), symbols,
                compiler.getTypes(), new ArrayList<>(compiler.getParseSteps()), errors,
                compiler.getQuadrupleList().size(), compiler.getIcm().getTriplets().size(),
                compiler.getTriplets(), compiler.getQuadruples(), compiler.getC3DCode(),
                compiler.getCCode(), compiler.getRiscvCode(), compiler.getTranslatedCode(),
                compiler.getImportedFiles());
    }

    private void publishResults(List<FileResult> results) {
        List<CompilationError> errors = new ArrayList<>();
        for (FileResult result : results) errors.addAll(result.errors());
        FileResult focus = results.get(0);
        lastTargets = new ArrayList<>();
        lastTargets.add(focus.file());
        for (String imported : focus.imports()) lastTargets.add(new File(imported));
        lastFocusFile = focus == null ? null : focus.file();
        lastRiscvCode = focus == null ? "" : focus.riscvCode();
        publishToolResults(focus, aggregatedSymbols(results), errors);
        publishCode(focus, errors);
        publishStatus(results, errors);
        String warning = errors.isEmpty() ? writeArtifacts(focus) : "";
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
        console.setAssembly(focus == null ? "" : focus.riscvCode());
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

    private String writeCFile(FileResult principal) {
        File container = window.currentContainer();
        if (container == null) container = principal.file().getAbsoluteFile().getParentFile();
        if (!principal.ok() || principal.cCode() == null || principal.cCode().isBlank()) return "";
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

    private String writeArtifacts(FileResult principal) {
        return writeCFile(principal) + writeAssemblyFile(principal);
    }

    private String writeAssemblyFile(FileResult principal) {
        File container = window.currentContainer();
        if (container == null) container = principal.file().getAbsoluteFile().getParentFile();
        if (!principal.ok() || principal.riscvCode() == null || principal.riscvCode().isBlank()) return "";
        String name = principal.file().getName();
        int point = name.lastIndexOf('.');
        File target = new File(container, (point > 0 ? name.substring(0, point) : name) + ".s");
        try {
            Files.writeString(target.toPath(), principal.riscvCode());
            return "\nEnsamblador RISC-V generado: " + target.getPath() + "\n";
        } catch (IOException e) {
            return "\nNo se pudo escribir el ensamblador en la raiz del proyecto: " + e.getMessage() + "\n";
        }
    }

    void setRiscvEnabled(boolean enabled) {
        compiler.setRiscvEnabled(enabled);
    }

    /**
     * Punto de entrada del botón Ejecutar/Detener y del atajo Ctrl+R. Si hay una
     * ejecución en curso solicita detenerla; en caso contrario arranca una nueva.
     */
    void runOrStopRiscv() {
        if (riscvWorker != null && !riscvWorker.isDone()) {
            if (stopRequested) return;
            stopRequested = true;
            window.getConsoleDock().appendRunOutput("\n[Deteniendo ejecución…]\n");
            window.navText("Deteniendo ejecución…");
            return;
        }
        runRiscv();
    }

    /**
     * Ejecuta el ensamblador RISC-V generado en el simulador interno (RARS
     * embebido) y publica el resultado en la vista Simulador. Corre en un hilo
     * de trabajo para no bloquear la interfaz y puede detenerse desde el botón.
     */
    void runRiscv() {
        ConsoleDock console = window.getConsoleDock();
        console.select(ConsoleDock.View.RUN);
        if (lastRiscvCode == null || lastRiscvCode.isBlank()) {
            console.setRunOutput("No hay ensamblador generado para ejecutar.\n"
                    + "Compila primero con la casilla RISC-V marcada (botón Compilar).\n");
            window.navText("Ejecutar: sin ensamblador");
            return;
        }
        String assembly = lastRiscvCode;
        String input = console.getRunInput();
        stopRequested = false;
        console.setRunOutput("Ejecutando el ensamblador en el simulador interno (RARS)…\n");
        window.navText("Ejecutando ensamblador…");
        window.setRunningState(true);
        riscvWorker = new SwingWorker<>() {
            @Override
            protected RiscvRunner.Result doInBackground() {
                return new RiscvRunner().run(assembly, input, () -> stopRequested);
            }

            @Override
            protected void done() {
                publishRunResult(this);
            }
        };
        riscvWorker.execute();
    }

    private void publishRunResult(SwingWorker<RiscvRunner.Result, Void> worker) {
        ConsoleDock console = window.getConsoleDock();
        try {
            RiscvRunner.Result result = worker.get();
            if (!result.ok()) {
                console.appendRunOutput("Error: " + result.message() + "\n");
                window.navText("Ejecución con errores");
                return;
            }
            console.appendRunOutput(result.stdout());
            if (result.stderr() != null && !result.stderr().isBlank()) {
                console.appendRunOutput(result.stderr());
            }
            String label = reasonLabel(result.reason());
            console.appendRunOutput("\n[Ejecución " + label + ", código "
                    + result.exitCode() + ", " + result.elapsedMillis() + " ms]\n");
            console.setStatus("Ejecución RISC-V: " + label
                    + " (código " + result.exitCode() + ")");
            window.navText("Ejecución: " + label);
        } catch (Exception e) {
            console.appendRunOutput("Error interno al ejecutar: " + e + "\n");
            window.navText("Ejecución con error");
        } finally {
            window.setRunningState(false);
        }
    }

    /** Traduce la razón de RARS a un texto breve para la consola y la barra. */
    private static String reasonLabel(String reason) {
        if (reason == null) return "sin razón";
        return switch (reason) {
            case "NORMAL_TERMINATION" -> "terminada normalmente";
            case "DETENIDO" -> "detenida por el usuario";
            case "MAX_STEPS" -> "límite de pasos alcanzado (posible lazo infinito)";
            case "BREAKPOINT" -> "detenida en un punto de interrupción";
            case "CLIFF_TERMINATION" -> "llegó al final del código";
            case "EXCEPTION" -> "terminó con excepción";
            default -> reason;
        };
    }

    void clearResults() {
        lastTargets = List.of();
        lastFocusFile = null;
        lastRiscvCode = "";
        window.clearToolViews();
        window.getStatusBar().setCounts(0, 0);
        window.getStatusBar().setIdle();
        window.navText("Compilando...");
    }

    private File fileNamed(String name) {
        if (name == null) return null;
        File direct = new File(name);
        if (direct.isAbsolute() && direct.isFile()) return direct;
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
                    .append("entrada PigLatin seleccionada").append(") ---\n").append(focus.translated());
            text.append("\nAST generado: ").append(focus.astDot().isBlank() ? "no" : "sí")
                    .append("   Símbolos: ").append(focus.symbols().size())
                    .append("   Pila del parser: ").append(focus.steps().size()).append(" pasos\n");
        }
        return text.toString();
    }

    private void appendFileReport(StringBuilder text, FileResult result) {
        text.append(result.ok() ? "  OK    " : "  ERROR ").append(result.file().getName());
        text.append("  <- entrada .pig");
        text.append("  (").append(UiTheme.prettifyLanguage(result.language())).append(')');
        if (!result.ok()) text.append("  ").append(result.errors().size())
                .append(result.errors().size() == 1 ? " error" : " errores");
        else text.append("  ").append(result.quadruples()).append(" cuartetas");
        if (!result.imports().isEmpty()) text.append("  importa: ").append(String.join(", ", result.imports()));
        text.append('\n');
    }
}
