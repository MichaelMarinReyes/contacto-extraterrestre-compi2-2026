package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.symbols.Symbol;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {

    private static final String navText = "Contacto 3xtrat3rr3str3D";
    private static final String CARD_TABS = "tabs";
    private static final String CARD_WELCOME = "welcome";

    private static final String DEFAULT_CONTAINER_NAME = "proyecto";

    private static final int TREE_DEFAULT_WIDTH = 280;

    private static final int IGNORED_LIST_LIMIT = 12;

    private TopToolbar topToolbar;
    private EditorTabs editorTabs;
    private ConsoleDock consoleDock;
    private ToolWindowDock toolWindowDock;
    private ToolWindow toolWindow;
    private StatusBar statusBar;
    private WelcomePanel welcomePanel;
    private JPanel editorCardHost;
    private CardLayout editorCards;
    private JSplitPane rootSplit;
    private JSplitPane editorSplit;

    private boolean treeDividerFixed = false;
    private final ParserTreePanel astPanel = new ParserTreePanel();
    private final SymbolTablePanel symbolPanel = new SymbolTablePanel();
    private final StackVisualizerPanel stackPanel = new StackVisualizerPanel();
    private final ErrorTablePanel errorPanel = new ErrorTablePanel();
    private final FileTreePanel fileTreePanelInstance = new FileTreePanel();

    private final JComboBox<String> cModeCombo = new JComboBox<>();
    private final JToggleButton toolsToggle = new JToggleButton();
    private final JButton compileButton = new JButton();

    private final Compiler compiler = new Compiler();
    private Mode currentCMode = Mode.QUADRUPLES;

    private List<CompilationError> lastErrors = List.of();

    private List<File> lastTargets = List.of();

    private File lastFocusFile;

    private static final String MAIN_FILE = "main.pig";

    private JPanel contentArea;
    private File currentFile;

    public MainWindow() {
        initComponents();
        setTitle("Contacto 3xtrat3rr3str3D");
        setMinimumSize(new Dimension(1024, 640));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        UiTheme.applyFlatLafTweaks();
        initContentLayout();
        initToolbar();
        initShortcuts();

        errorPanel.setOnErrorActivated(this::jumpToError);

        symbolPanel.setOnSymbolActivated(this::jumpToSymbol);
        initStyles();
        navText("");
        showWelcome();
    }

    private void initContentLayout() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UiTheme.panelBg());

        consoleDock = new ConsoleDock();
        toolWindowDock = new ToolWindowDock(astPanel, symbolPanel, stackPanel, errorPanel);
        toolWindow = new ToolWindow(this, toolWindowDock);

        toolWindow.setOnClosed(() -> toolsToggle.setSelected(false));
        statusBar = new StatusBar();
        editorTabs = new EditorTabs(this::onActiveEditorChanged, file -> {

            if (editorTabs.isEmpty()) {
                showWelcome();
            }
        });
        welcomePanel = new WelcomePanel(
                this::actionNewFile, this::actionOpenFile,
                () -> fileTreePanelInstance.askForDirectory(),
                this::actionCompile);

        editorCards = new CardLayout();
        editorCardHost = new JPanel(editorCards);
        editorCardHost.add(editorTabs, CARD_TABS);
        editorCardHost.add(welcomePanel, CARD_WELCOME);
        editorCards.show(editorCardHost, CARD_WELCOME);

        editorSplit = createSplit(JSplitPane.VERTICAL_SPLIT, editorCardHost, consoleDock, 0.68, 0.7);

        rootSplit = createSplit(JSplitPane.HORIZONTAL_SPLIT, fileTreePanelInstance, editorSplit, 0.17, 0.18);

        addHierarchyListener(e -> {
            boolean showing = (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0;
            if (showing && isShowing() && !treeDividerFixed) {
                treeDividerFixed = true;
                SwingUtilities.invokeLater(
                        () -> rootSplit.setDividerLocation(TREE_DEFAULT_WIDTH));
            }
        });

        JPanel body = new JPanel(new BorderLayout());
        body.add(rootSplit, BorderLayout.CENTER);
        body.add(statusBar, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().removeAll();
        getContentPane().add(sidebarPanel, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();

        wireFileTree();
    }

    private static JSplitPane createSplit(int orientation, java.awt.Component left,
                                          java.awt.Component right, double resizeWeight,
                                          double dividerRatio) {
        JSplitPane split = new JSplitPane(orientation, left, right);
        split.setResizeWeight(resizeWeight);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);
        split.setBorder(null);
        split.setDividerSize(6);

        split.setDividerLocation(dividerRatio);
        return split;
    }

    private void wireFileTree() {
        fileTreePanelInstance.setOnFileSelected(file -> {
            EditorPanel editor = editorTabs.openFile(file);
            if (editor == null) {

                if (!LanguageCompilerFactory.hasAllowedExtension(file.getName())) {
                    warnWrongExtension(file);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo leer el archivo:\n" + file.getAbsolutePath(),
                            "Error de lectura", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            editorTabs.selectEditor(editor);
            fileTreePanelInstance.selectFile(file);
            showTabs();
            onActiveEditorChanged(editor);
        });
        fileTreePanelInstance.setOnDirectoryChanged(() -> {
            File root = fileTreePanelInstance.getRoot();
            if (topToolbar != null) {
                topToolbar.setProjectName(root == null ? "" : root.getName());
            }
        });
        fileTreePanelInstance.setOnFilesIgnored(this::showIgnoredFiles);

        fileTreePanelInstance.setOnOpenProjectRequest(this::openProject);
        fileTreePanelInstance.initSelectionBehavior();
    }

    private void initToolbar() {
        cModeCombo.addItem("Cuartetas");
        cModeCombo.addItem("Tripletes");
        cModeCombo.setSelectedIndex(0);

        topToolbar = new TopToolbar(optionSelectedLabel, cModeCombo,
                compileButton, toolsToggle);

        topToolbar.applyIcons(newFileButton, openFileButton, saveFileButton,
                astButton, symbolTableButton, stackButton, lexerErrorButton);

        topToolbar.addFileAction(newFileButton, "Archivo nuevo (Ctrl+N)");
        topToolbar.addFileAction(openFileButton, "Abrir archivo (Ctrl+O)");
        topToolbar.addFileAction(saveFileButton, "Guardar (Ctrl+S)");

        topToolbar.addViewAction(astButton, "Ver árbol AST (Ctrl+1)");
        topToolbar.addViewAction(symbolTableButton, "Ver tabla de símbolos (Ctrl+2)");
        topToolbar.addViewAction(stackButton, "Ver pila de procesos (Ctrl+3)");
        topToolbar.addViewAction(lexerErrorButton, "Ver tabla de errores (Ctrl+4)");

        compileButton.addActionListener(e -> actionCompile());
        cModeCombo.addActionListener(e -> onCModeComboChanged());
        toolsToggle.setText("Herramientas");
        toolsToggle.setSelected(false);
        toolsToggle.setToolTipText("Abrir la ventana de herramientas (AST, Símbolos, "
                + "Pila, Errores). Ciérrala para volver al editor.");
        toolsToggle.addActionListener(e -> {
            if (toolsToggle.isSelected()) {
                openToolWindow(null);
            } else {
                toolWindow.close();
            }
        });

        sidebarPanel.removeAll();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMinimumSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, TopToolbar.BAR_HEIGHT));
        sidebarPanel.add(topToolbar, BorderLayout.CENTER);
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private void openToolWindow(ToolWindowDock.View view) {
        toolsToggle.setSelected(true);
        if (view == null) {
            toolWindow.showWindow();
        } else {
            toolWindow.showView(view);
        }
        if (!toolWindow.isOpen()) {

            toolsToggle.setSelected(false);
        }
    }

    private void onCModeComboChanged() {
        Object selected = cModeCombo.getSelectedItem();
        currentCMode = "Tripletes".equals(String.valueOf(selected)) ? Mode.TRIPLETS : Mode.QUADRUPLES;
        compiler.setCMode(currentCMode);
    }

    private void initShortcuts() {
        bind("control N", this::actionNewFile);
        bind("control O", this::actionOpenFile);
        bind("control S", () -> actionSave());
        bind("control B", this::actionCompile);
        bind("control W", () -> editorTabs.closeActive());
        bind("control shift S", () -> editorTabs.saveAll());
        bind("control 1", () -> showToolView(ToolWindowDock.View.AST));
        bind("control 2", () -> showToolView(ToolWindowDock.View.SYMBOLS));
        bind("control 3", () -> showToolView(ToolWindowDock.View.STACK));
        bind("control 4", () -> showToolView(ToolWindowDock.View.ERRORS));
    }

    private void bind(String keyStroke, Runnable action) {
        Action a = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        };
        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyStroke), a);
        getRootPane().getActionMap().put(a, a);
    }

    private void actionNewFile() {

        File container = currentContainer();
        if (container == null) {
            container = ensureContainer("archivo nuevo", null);
            if (container == null) {
                return;
            }
        }

        NewFileDialog.Choice choice = promptNewFile(container);
        if (choice == null) {
            return;
        }

        EditorPanel editor = editorTabs.newFile(choice.languageId(),
                choice.directory(), choice.baseName());
        if (editor == null) {
            promptInfo("No se pudo crear el archivo en:\n"
                            + choice.directory().getAbsolutePath(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return;
        }
        fileTreePanelInstance.reload();
        fileTreePanelInstance.selectFile(editor.getFile());
        showTabs();
        onActiveEditorChanged(editor);
        navText("Nuevo archivo / " + editor.getDisplayName());
    }

    NewFileDialog.Choice promptNewFile(File container) {
        return buildNewFileDialog(container).ask();
    }

    NewFileDialog buildNewFileDialog(File container) {
        List<File> folders = fileTreePanelInstance.codeFolders();
        if (folders.isEmpty()) {
            folders = List.of(container);
        }
        EditorPanel active = editorTabs.getActiveEditor();
        String languageId = active == null ? null : active.getLanguageId();

        File suggestedFolder = active != null && active.getFile() != null
                ? active.getFile().getParentFile() : container;
        int selected = folders.indexOf(suggestedFolder);
        List<File> ordered = new ArrayList<>(folders);
        if (selected > 0) {

            ordered.add(0, ordered.remove(selected));
        }

        String suggestedName = suggestFileName(languageId, container);
        return new NewFileDialog(this, container, ordered, languageId, suggestedName);
    }

    private String suggestFileName(String languageId, File container) {
        String ext = LanguageCompilerFactory.extensionOfLanguage(languageId);
        if (ext == null) {
            ext = "pig";
        }
        for (int i = 1; i < 10_000; i++) {
            String candidate = "archivo" + i;
            boolean taken = new File(container, candidate + "." + ext).exists();
            if (!taken) {
                for (EditorPanel editor : editorTabs.getAllEditors()) {
                    if (editor.getDisplayName().equals(candidate + "." + ext)) {
                        taken = true;
                        break;
                    }
                }
            }
            if (!taken) {
                return candidate;
            }
        }
        return "archivo";
    }

    private void actionOpenFile() {
        File file = promptSourceFile();
        if (file == null) {
            return;
        }
        if (!LanguageCompilerFactory.hasAllowedExtension(file.getName())) {
            warnWrongExtension(file);
            return;
        }

        File placed = placeInContainer(file);
        if (placed == null) {
            return;
        }
        openInEditor(placed);
    }

    File promptSourceFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo de código");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Código (" + LanguageCompilerFactory.extensionPattern() + ")",
                LanguageCompilerFactory.allowedExtensions().toArray(new String[0])));
        if (lastContainer() != null) {
            chooser.setCurrentDirectory(lastContainer());
        }
        return approve(chooser) ? chooser.getSelectedFile() : null;
    }

    File promptDirectory(String title, File startDir, File preselected,
                         String approveText, boolean allowCreate) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText(approveText);
        chooser.setCurrentDirectory(startDir != null && startDir.isDirectory()
                ? startDir : new File(System.getProperty("user.home", ".")));
        if (preselected != null) {
            chooser.setSelectedFile(preselected);
        }
        if (!approve(chooser)) {
            return null;
        }
        File dir = chooser.getSelectedFile();
        if (dir == null) {
            return null;
        }
        if (!dir.exists() && allowCreate && !dir.mkdirs()) {
            promptInfo("No se pudo crear la carpeta:\n" + dir.getAbsolutePath(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return dir;
    }

    int promptConfirm(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    void promptInfo(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private static boolean approve(JFileChooser chooser) {
        return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
    }

    private File ensureContainer(String purpose, File suggested) {
        File current = currentContainer();
        if (current != null) {
            return current;
        }

        boolean fromSuggestion = suggested != null && suggested.isDirectory();
        File start = fromSuggestion ? suggested
                : new File(System.getProperty("user.home", "."));

        File preselected = fromSuggestion
                ? start : new File(start, DEFAULT_CONTAINER_NAME);

        File dir = promptDirectory("Carpeta contenedora (" + purpose + ")",
                start, preselected, "Usar esta carpeta", true);
        if (dir == null) {
            return null;
        }
        if (!loadProject(dir, false)) {
            return null;
        }
        return dir;
    }

    boolean loadProject(File dir) {
        return loadProject(dir, true);
    }

    boolean loadProject(File dir, boolean announce) {
        if (dir == null) {
            return false;
        }
        File abs;
        try {
            abs = dir.getCanonicalFile();
        } catch (IOException e) {
            promptInfo("No se pudo resolver la ruta:\n" + dir + "\n\n" + e.getMessage(),
                    "Ruta no válida", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!abs.exists()) {
            promptInfo("La carpeta no existe:\n\n" + abs.getAbsolutePath()
                    + "\n\nRevisa la ruta e inténtalo de nuevo.",
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!abs.isDirectory()) {
            promptInfo("Eso no es una carpeta, es un archivo:\n\n"
                    + abs.getAbsolutePath()
                    + "\n\nElige una carpeta que contenga tus archivos de código.",
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!abs.canRead()) {
            promptInfo("No hay permiso para leer la carpeta:\n\n"
                    + abs.getAbsolutePath(),
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        fileTreePanelInstance.loadDirectory(abs);
        lastContainerDir = abs;
        if (!announce) {
            return true;
        }
        announceProject(abs);
        return true;
    }

    private void announceProject(File abs) {
        int fuentes = fileTreePanelInstance.countSourceFiles();
        List<File> ignorados = fileTreePanelInstance.getIgnoredFiles();
        if (fuentes == 0) {

            promptInfo("La carpeta se cargó, pero no contiene archivos de código.\n\n"
                            + abs.getAbsolutePath() + "\n\n"
                            + "Extensiones admitidas: "
                            + LanguageCompilerFactory.extensionPattern()
                            + (ignorados.isEmpty() ? "\n\nLa carpeta está vacía."
                                    : "\n\nSe ignoraron " + ignorados.size()
                                        + (ignorados.size() == 1
                                                ? " archivo por su extensión."
                                                : " archivos por su extensión.")),
                    "Proyecto sin archivos de código",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String summary = "Proyecto cargado: " + fuentes
                + (fuentes == 1 ? " archivo de código" : " archivos de código");
        if (!ignorados.isEmpty()) {
            summary += "\n" + ignorados.size()
                    + (ignorados.size() == 1
                            ? " archivo ignorado por su extensión."
                            : " archivos ignorados por su extensión.");
        }
        promptInfo(summary + "\n\n" + abs.getAbsolutePath(),
                "Proyecto abierto", JOptionPane.INFORMATION_MESSAGE);
    }

    void openProject() {
        File start = currentContainer() != null ? currentContainer()
                : lastContainer() != null ? lastContainer()
                : new File(System.getProperty("user.home", "."));
        File dir = promptDirectory("Abrir carpeta del proyecto", start, null,
                "Abrir proyecto", false);
        if (dir == null) {
            return;
        }
        loadProject(dir, true);
    }

    private File lastContainerDir;

    private File lastContainer() {
        return lastContainerDir;
    }

    private File placeInContainer(File file) {
        File project = currentContainer();
        if (project == null || isInside(project, file)) {
            File dir = ensureContainer("archivo abierto", file.getParentFile());
            if (dir == null) {
                return null;
            }
            if (isInside(dir, file)) {
                return file;
            }
            return moveToContainer(file, dir);
        }
        return file;
    }

    private File moveToContainer(File file, File dir) {
        int answer = promptConfirm(
                "\"" + file.getName() + "\" está fuera del proyecto.\n\n"
                        + "¿Moverlo a la carpeta elegida?\n"
                        + dir.getAbsolutePath(),
                "Mover archivo al proyecto");
        if (answer != JOptionPane.YES_OPTION) {

            File parent = file.getParentFile();
            if (parent != null) {
                loadProject(parent, false);
                navText("El archivo se mantiene en " + parent.getName()
                        + " porque no se movió al proyecto");
            }
            return file;
        }
        File target = new File(dir, file.getName());
        try {
            Files.move(file.toPath(), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            loadProject(dir, false);
            return target;
        } catch (IOException e) {
            promptInfo("No se pudo mover el archivo:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static boolean isInside(File dir, File file) {
        try {
            String base = dir.getCanonicalPath();
            String child = file.getCanonicalPath();
            return !child.equals(base) && child.startsWith(base + File.separator);
        } catch (IOException e) {
            return false;
        }
    }

    private File currentContainer() {
        File root = fileTreePanelInstance.getRoot();
        return root != null && root.isDirectory() ? root : null;
    }

    private void warnWrongExtension(File file) {
        String name = file == null ? "(sin nombre)" : file.getName();
        promptInfo("No se abrió \"" + name + "\".\n\n"
                        + "Extensiones admitidas: "
                        + LanguageCompilerFactory.extensionPattern()
                        + "\nEl lenguaje de cada archivo lo decide su extensión.",
                "Extensión no admitida", JOptionPane.WARNING_MESSAGE);
    }

    private void showIgnoredFiles(List<File> ignored) {
        if (ignored == null || ignored.isEmpty()) {
            return;
        }
        int total = ignored.size();

        int maxListed = total > IGNORED_LIST_LIMIT ? 6 : total;

        StringBuilder sb = new StringBuilder();
        sb.append("Estos archivos del proyecto no se abren porque su extensión no está")
                .append(" admitida (").append(LanguageCompilerFactory.extensionPattern())
                .append("):\n\n");
        for (int i = 0; i < maxListed; i++) {
            File f = ignored.get(i);
            sb.append("  • ").append(f.getName()).append("   [")
                    .append(describeExtension(f.getName())).append("]\n");
        }
        if (total > maxListed) {
            sb.append("\n… y ").append(total - maxListed)
                    .append(" más. Si la carpeta es muy amplia, conviene elegir")
                    .append(" una carpeta que contenga solo tu proyecto.");
        }
        promptInfo(sb.toString(), total + (total == 1
                ? " archivo ignorado" : " archivos ignorados"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static String describeExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 || dot == fileName.length() - 1
                ? "sin extensión" : "extensión ." + fileName.substring(dot + 1);
    }

    private void actionSave() {
        EditorPanel editor = editorTabs.getActiveEditor();
        if (editor == null) {
            return;
        }
        if (editorTabs.save(editor)) {
            fileTreePanelInstance.reload();
            statusBar.setFileName(editor.getFile().getName());
            consoleDock.setStatus("Guardado: " + editor.getFile().getAbsolutePath());
        }
    }

    private void showToolView(ToolWindowDock.View view) {
        openToolWindow(view);
    }

    private record FileResult(File file, String language, boolean ok,
                              String astDot, List<Symbol> symbols, List<ParseStep> steps,
                              List<CompilationError> errors, int quadruples, int triplets,
                              String tripletsCode, String quadruplesCode,
                              String c3dCode, String cCode, String translated,
                              List<String> imports) {
    }

    private void actionCompile() {

        clearResults();

        List<File> targets = compileTargets();
        if (targets.isEmpty()) {
            consoleDock.setConsole("No hay nada que compilar.\n"
                    + "Abre una carpeta de proyecto o un archivo de código.\n");
            navText("Sin archivos que compilar");
            return;
        }

        File container = currentContainer();
        File main = targets.stream().filter(MainWindow::isMainFile).findFirst().orElse(null);
        StringBuilder header = new StringBuilder();
        header.append("Compilando ").append(targets.size())
                .append(targets.size() == 1 ? " archivo" : " archivos")
                .append(container != null ? " de " + container.getName() : " abiertos")
                .append("...\n");
        if (main != null) {
            header.append("Punto de partida: ").append(main.getName())
                    .append(" (se lee primero; sus imports se cargan antes)\n");
        } else if (container != null) {
            header.append("No hay ningún ").append(MAIN_FILE)
                    .append(" en el proyecto: se compilan los archivos por orden de nombre\n");
        }
        consoleDock.setConsole(header.toString());

        compiler.setCMode(currentCMode);

        compiler.setWorkingDirectory(container);

        List<FileResult> results = new ArrayList<>(targets.size());
        for (File file : targets) {
            results.add(compileOne(file));
        }

        publishResults(results);
    }

    private List<File> compileTargets() {
        List<File> targets = fileTreePanelInstance.allSourceFiles();
        if (!targets.isEmpty()) {
            return startingAtMain(targets);
        }

        for (EditorPanel editor : editorTabs.getAllEditors()) {
            File file = editor.getFile();
            if (file != null && file.isFile()) {
                targets.add(file);
            }
        }
        return targets;
    }

    private static List<File> startingAtMain(List<File> targets) {
        File main = null;
        for (File file : targets) {
            if (!MAIN_FILE.equalsIgnoreCase(file.getName())) {
                continue;
            }
            if (main == null || file.getAbsolutePath().length() < main.getAbsolutePath().length()) {
                main = file;
            }
        }
        if (main == null) {
            return targets;
        }
        List<File> ordered = new ArrayList<>(targets.size());
        ordered.add(main);
        for (File file : targets) {
            if (!file.equals(main)) {
                ordered.add(file);
            }
        }
        return ordered;
    }

    private static boolean isMainFile(File file) {
        return file != null && MAIN_FILE.equalsIgnoreCase(file.getName());
    }

    private FileResult compileOne(File file) {
        EditorPanel editor = editorOf(file);
        String language = editor != null
                ? editor.getLanguageId()
                : LanguageCompilerFactory.detectLanguageId(file.getName());
        String source;
        if (editor != null) {
            source = editor.getCodeText();
        } else {
            try {
                source = Files.readString(file.toPath());
            } catch (IOException e) {
                CompilationError io = new CompilationError(ErrorType.SEMANTIC,
                        "No se pudo leer el archivo: " + e.getMessage(),
                        -1, -1)
                        .inFile(file.getName());
                return new FileResult(file, language, false, "", List.of(), List.of(),
                        List.of(io), 0, 0, "", "", "", "", "", List.of());
            }
        }

        boolean ok = compiler.compile(source, language, file.getName());
        List<CompilationError> errors = new ArrayList<>();
        for (CompilationError e : compiler.getErrors()) {
            errors.add(e.inFile(file.getName()));
        }
        List<Symbol> symbols = new ArrayList<>(compiler.getSymbols());

        for (Symbol s : symbols) {
            if (s.getSourceFile() == null) {
                s.inFile(file.getName());
            }
        }
        return new FileResult(file, language, ok,
                compiler.getAstDot(), symbols,
                new ArrayList<>(compiler.getParseSteps()), errors,
                compiler.getQuadrupleList().size(), compiler.getIcm().getTriplets().size(),
                compiler.getTriplets(), compiler.getQuadruples(), compiler.getC3DCode(),
                compiler.getCCode(), compiler.getTranslatedCode(),
                compiler.getImportedFiles());
    }

    private EditorPanel editorOf(File file) {
        for (EditorPanel editor : editorTabs.getAllEditors()) {
            File candidate = editor.getFile();
            if (candidate != null && candidate.equals(file)) {
                return editor;
            }
        }
        return null;
    }

    private void publishResults(List<FileResult> results) {
        List<CompilationError> allErrors = new ArrayList<>();
        for (FileResult r : results) {
            allErrors.addAll(r.errors());
        }
        FileResult focus = focusedResult(results);
        lastErrors = List.copyOf(allErrors);
        lastTargets = results.stream().map(FileResult::file).toList();
        lastFocusFile = focus == null ? null : focus.file();

        toolWindow.postResults(() -> {
            astPanel.renderGraph(focus == null ? "" : focus.astDot());
            symbolPanel.loadSymbols(focus == null ? List.of() : focus.symbols());
            stackPanel.loadSteps(focus == null ? List.of() : focus.steps());
            errorPanel.loadErrors(allErrors);
            toolWindowDock.setErrorCount(allErrors.size());
            if (errorPanel.getErrorCount() > 0) {
                errorPanel.getErrorTable().setRowSelectionInterval(0, 0);
            }
        });

        consoleDock.setTriplets(focus == null ? "" : focus.tripletsCode());
        consoleDock.setQuadruples(focus == null ? "" : focus.quadruplesCode());
        consoleDock.setC3D(focus == null ? "" : focus.c3dCode());
        consoleDock.setCCode(focus == null ? "" : focus.cCode());
        consoleDock.setSummary(focus == null ? "" : focus.translated());
        consoleDock.setStatus("Modo de generación de C: " + TopToolbar.modeLabel(currentCMode));

        statusBar.setResult(allErrors.isEmpty(), allErrors.size());
        statusBar.setCounts(focus == null ? 0 : focus.quadruples(),
                focus == null ? 0 : focus.triplets());

        if (!allErrors.isEmpty()) {

            showToolView(ToolWindowDock.View.ERRORS);
            CompilationError first = allErrors.get(0);
            if (first.getLine() > 0) {
                EditorPanel editor = editorOf(fileNamed(first.getFileName()));
                if (editor != null) {
                    editor.gotoLine(first.getLine());
                }
            }
            navText(allErrors.size() + (allErrors.size() == 1
                    ? " error de compilación" : " errores de compilación"));
        } else {
            navText("Compilación correcta / " + results.size()
                    + (results.size() == 1 ? " archivo" : " archivos"));
        }
        StringBuilder warning = new StringBuilder();
        if (allErrors.isEmpty()) {
            warning.append(writeCFile(results));
        }
        consoleDock.appendConsole(buildReport(results, allErrors, focus));
        consoleDock.appendConsole(warning.toString());
    }

    private String writeCFile(List<FileResult> results) {
        File container = currentContainer();
        if (container == null || results.isEmpty()) {
            return "";
        }

        FileResult principal = null;
        for (FileResult r : results) {
            if (isMainFile(r.file())) {
                principal = r;
                break;
            }
            if (principal == null && r.ok()) {
                principal = r;
            }
        }
        if (principal == null || principal.cCode() == null || principal.cCode().isBlank()) {
            return "";
        }
        String name = principal.file().getName();
        int point = name.lastIndexOf('.');
        String base = point > 0 ? name.substring(0, point) : name;
        File target = new File(container, base + ".c");
        try {
            Files.writeString(target.toPath(), principal.cCode());
            return "\nC generado: " + target.getPath() + "\n";
        } catch (IOException e) {
            return "\nNo se pudo escribir el C en la raiz del proyecto: "
                    + e.getMessage() + "\n";
        }
    }

    private void clearResults() {
        lastErrors = List.of();
        lastTargets = List.of();
        lastFocusFile = null;

        astPanel.clear();
        symbolPanel.clear();
        errorPanel.clear();
        stackPanel.clear();
        toolWindowDock.setErrorCount(0);
        consoleDock.clearAll();

        statusBar.setCounts(0, 0);
        statusBar.setIdle();
        navText("Compilando...");
    }

    private File fileNamed(String name) {
        if (name == null) {
            return null;
        }
        for (File file : lastTargets) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    private void jumpToError(int row) {

        CompilationError error = errorPanel.errorAt(row);
        if (error == null) {
            return;
        }
        File file = fileNamed(error.getFileName());
        if (file == null) {
            promptInfo("El archivo de este error ya no esta en el proyecto:\n"
                            + error.getFileName(),
                    "Archivo no encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (file.isFile()) {

            EditorPanel editor = editorOf(file);
            if (editor != null && editor.isModified()) {
                editorTabs.save(editor);
            }
        }
        openInEditor(file);
        EditorPanel target = editorOf(file);
        if (target != null && error.getLine() > 0) {
            target.gotoLine(error.getLine());
        }
        navText(error.getFileName() + " / línea " + error.getLine());
    }

    private void jumpToSymbol(int row) {
        Symbol symbol = symbolPanel.symbolAt(row);
        if (symbol == null) {
            return;
        }
        if (symbol.getLine() <= 0) {
            promptInfo("«" + symbol.getName() + "» lo genera el compilador, "
                            + "no aparece escrito en el fuente.\n"
                            + "No hay ninguna línea a la que saltar.",
                    "Símbolo sin posición", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        File symbolPath = symbolFile(symbol);
        if (symbolPath == null) {
            return;
        }
        if (symbolPath.isFile()) {
            openInEditor(symbolPath);
        }
        EditorPanel target = editorOf(symbolPath);
        if (target != null) {
            target.gotoLine(symbol.getLine());
        }
        navText(symbolPath.getName() + " / línea " + symbol.getLine()
                + " / " + symbol.getName());
    }

    private File symbolFile(Symbol symbol) {
        String origin = symbol.getSourceFile();
        File container = currentContainer();
        if (origin == null || lastFocusFile == null
                || origin.equals(lastFocusFile.getName())) {
            return lastFocusFile;
        }
        if (container == null) {
            return lastFocusFile;
        }
        File importado = new File(container, origin);
        return importado.isFile() ? importado : lastFocusFile;
    }

    private FileResult focusedResult(List<FileResult> results) {
        EditorPanel active = editorTabs.getActiveEditor();
        if (active != null && active.getFile() != null) {
            for (FileResult r : results) {
                if (r.file().equals(active.getFile())) {
                    return r;
                }
            }
        }
        for (FileResult r : results) {
            if (isMainFile(r.file())) {
                return r;
            }
        }
        for (FileResult r : results) {
            if (!r.astDot().isBlank()) {
                return r;
            }
        }
        return results.isEmpty() ? null : results.get(0);
    }

    private String focusReason(FileResult focus) {
        if (focus == null) {
            return "";
        }
        EditorPanel active = editorTabs.getActiveEditor();
        if (active != null && active.getFile() != null && focus.file().equals(active.getFile())) {
            return "pestaña activa";
        }
        if (isMainFile(focus.file())) {
            return MAIN_FILE + " (principal)";
        }
        return "primer archivo del proyecto";
    }

    private String buildReport(List<FileResult> results, List<CompilationError> allErrors,
                               FileResult focus) {
        StringBuilder sb = new StringBuilder();
        for (FileResult r : results) {
            sb.append(r.ok() ? "  OK    " : "  ERROR ")
                    .append(r.file().getName());
            if (isMainFile(r.file())) {
                sb.append("  <- principal");
            }
            sb.append("  (").append(UiTheme.prettifyLanguage(r.language())).append(')');
            if (!r.ok()) {
                sb.append("  ").append(r.errors().size())
                        .append(r.errors().size() == 1 ? " error" : " errores");
            } else {
                sb.append("  ").append(r.quadruples()).append(" cuartetas");
            }
            if (!r.imports().isEmpty()) {
                sb.append("  importa: ").append(String.join(", ", r.imports()));
            }
            sb.append('\n');
        }

        sb.append('\n');
        if (allErrors.isEmpty()) {
            sb.append("Compilación correcta: ").append(results.size())
                    .append(results.size() == 1 ? " archivo." : " archivos.")
                    .append('\n');
        } else {
            sb.append(allErrors.size())
                    .append(allErrors.size() == 1 ? " error encontrado:\n"
                            : " errores encontrados:\n");
            for (CompilationError e : allErrors) {
                sb.append("  ").append(e).append('\n');
            }
        }

        if (focus != null) {
            sb.append("\n--- Salidas de ").append(focus.file().getName())
                    .append(" (").append(focusReason(focus)).append(") ---\n");
            sb.append(focus.translated());
            sb.append("\nAST generado: ").append(focus.astDot().isBlank() ? "no" : "sí")
                    .append("   Símbolos: ").append(focus.symbols().size())
                    .append("   Pila del parser: ").append(focus.steps().size())
                    .append(" pasos\n");
        }
        return sb.toString();
    }

    private void onActiveEditorChanged(EditorPanel editor) {
        if (editor == null) {
            return;
        }
        currentFile = editor.getFile();
        statusBar.setFileName(editor.getDisplayName());
        statusBar.setLanguage(editor.getLanguageId());
        updateCaret(editor);
    }

    private void updateCaret(EditorPanel editor) {
        statusBar.setPosition(editor.getCaretLine(), editor.getCaretColumn());
    }

    private void showTabs() {
        editorCards.show(editorCardHost, CARD_TABS);
    }

    private void showWelcome() {
        editorCards.show(editorCardHost, CARD_WELCOME);
        statusBar.setFileName("Sin archivo");
        statusBar.setIdle();
        statusBar.setCounts(0, 0);
    }

    private void initStyles() {
        optionSelectedLabel.setOpaque(false);
        optionSelectedLabel.setForeground(UiTheme.dim());
        optionSelectedLabel.setFont(UiTheme.mono(12));
        optionSelectedLabel.setBorder(UiTheme.pad(2, 6, 2, 6));

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void navText(String optionMenu) {
        if (optionMenu == null || optionMenu.isBlank()) {
            optionSelectedLabel.setText(navText);
        } else {
            optionSelectedLabel.setText(navText + "  ›  " + optionMenu);
        }
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        sidebarPanel = new javax.swing.JPanel();
        openFileButton = new javax.swing.JButton();
        saveFileButton = new javax.swing.JButton();
        astButton = new javax.swing.JButton();
        symbolTableButton = new javax.swing.JButton();
        stackButton = new javax.swing.JButton();
        newFileButton = new javax.swing.JButton();
        lexerErrorButton = new javax.swing.JButton();
        optionSelectedLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        contentPane = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        openFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open-file.png")));
        openFileButton.setBorder(new javax.swing.border.MatteBorder(null));
        openFileButton.setBorderPainted(false);
        openFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openFileButtonMouseEntered(evt);
            }
        });
        openFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(openFileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 30, 30));

        saveFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save-file.png")));
        saveFileButton.setBorder(new javax.swing.border.MatteBorder(null));
        saveFileButton.setBorderPainted(false);
        saveFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveFileButtonMouseEntered(evt);
            }
        });
        saveFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(saveFileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 30, 30));

        astButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ast.png")));
        astButton.setBorder(new javax.swing.border.MatteBorder(null));
        astButton.setBorderPainted(false);
        astButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        astButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                astButtonMouseEntered(evt);
            }
        });
        astButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                astButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(astButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 30, 30));

        symbolTableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/symbol-table.png")));
        symbolTableButton.setBorder(new javax.swing.border.MatteBorder(null));
        symbolTableButton.setBorderPainted(false);
        symbolTableButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        symbolTableButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symbolTableButtonMouseEntered(evt);
            }
        });
        symbolTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symbolTableButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(symbolTableButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 30, 30));

        stackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/stack.png")));
        stackButton.setBorder(new javax.swing.border.MatteBorder(null));
        stackButton.setBorderPainted(false);
        stackButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stackButtonMouseEntered(evt);
            }
        });
        stackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stackButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(stackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 30, 30));

        newFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/new-file.png")));
        newFileButton.setBorder(new javax.swing.border.MatteBorder(null));
        newFileButton.setBorderPainted(false);
        newFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newFileButtonMouseEntered(evt);
            }
        });
        newFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(newFileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 30));

        lexerErrorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error-table.png")));
        lexerErrorButton.setBorder(new javax.swing.border.MatteBorder(null));
        lexerErrorButton.setBorderPainted(false);
        lexerErrorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lexerErrorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lexerErrorButtonMouseEntered(evt);
            }
        });
        lexerErrorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexerErrorButtonActionPerformed(evt);
            }
        });
        sidebarPanel.add(lexerErrorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 30, 30));

        optionSelectedLabel.setText("jLabel1");
        sidebarPanel.add(optionSelectedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 710, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sidebarPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 10, 30));

        contentPane.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
        );

        pack();
    }

    private void newFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        actionNewFile();
    }

    private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        actionOpenFile();
    }

    private void astButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showToolView(ToolWindowDock.View.AST);
        navText("Árbol AST");
    }

    private void symbolTableButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showToolView(ToolWindowDock.View.SYMBOLS);
        navText("Tabla de símbolos (" + symbolCount() + ")");
    }

    private String symbolCount() {
        if (symbolPanel.getSymbolCount() == symbolPanel.getTotalSymbolCount()) {
            return String.valueOf(symbolPanel.getTotalSymbolCount());
        }
        return symbolPanel.getSymbolCount() + " de " + symbolPanel.getTotalSymbolCount();
    }

    private void lexerErrorButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showToolView(ToolWindowDock.View.ERRORS);
        navText("Tabla de errores (" + errorCount() + ")");
    }

    private String errorCount() {
        if (errorPanel.getErrorCount() == errorPanel.getTotalErrorCount()) {
            return String.valueOf(errorPanel.getTotalErrorCount());
        }
        return errorPanel.getErrorCount() + " de " + errorPanel.getTotalErrorCount();
    }

    private void stackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showToolView(ToolWindowDock.View.STACK);
        navText("Pila de procesos (" + stackPanel.getStateCount() + " pasos)");
    }

    private void saveFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        actionSave();
    }

    private void newFileButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Nuevo archivo");
    }

    private void openFileButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Abrir archivo");
    }

    private void saveFileButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Guardar archivo");
    }

    private void symbolTableButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Tabla de Símbolos");
    }

    private void astButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("AST");
    }

    private void stackButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Pila de Procesos");
    }

    private void lexerErrorButtonMouseEntered(java.awt.event.MouseEvent evt) {
        this.optionSelectedLabel.setText("Tabla de Errores");
    }

    private javax.swing.JButton astButton;
    private javax.swing.JPanel contentPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton lexerErrorButton;
    private javax.swing.JButton newFileButton;
    private javax.swing.JButton openFileButton;
    private javax.swing.JLabel optionSelectedLabel;
    private javax.swing.JButton saveFileButton;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JButton stackButton;
    private javax.swing.JButton symbolTableButton;

    public void openProject(File directory) {
        fileTreePanelInstance.loadDirectory(directory);
        fileTreePanelInstance.revealFirstFile();
    }

    public ToolWindowDock getToolWindowDock() {
        return toolWindowDock;
    }

    public EditorTabs getEditorTabs() {
        return editorTabs;
    }

    public ConsoleDock getConsoleDock() {
        return consoleDock;
    }

    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    public TopToolbar getTopToolbar() {
        return topToolbar;
    }

    public ErrorTablePanel getErrorPanel() {
        return errorPanel;
    }

    public FileTreePanel getFileTree() {
        return fileTreePanelInstance;
    }

    public ParserTreePanel getAstPanel() {
        return astPanel;
    }

    public SymbolTablePanel getSymbolPanel() {
        return symbolPanel;
    }

    public StackVisualizerPanel getStackPanel() {
        return stackPanel;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void compileActive() {
        actionCompile();
    }

    public void openInEditor(File file) {
        EditorPanel editor = editorTabs.openFile(file);
        if (editor == null) {
            if (!LanguageCompilerFactory.hasAllowedExtension(file.getName())) {
                warnWrongExtension(file);
            } else {
                promptInfo("No se pudo leer el archivo:\n" + file.getAbsolutePath(),
                        "Error de lectura", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        revealInTree(file);
        editorTabs.selectEditor(editor);
        showTabs();
        onActiveEditorChanged(editor);
    }

    private void revealInTree(File file) {
        if (!fileTreePanelInstance.contains(file)) {
            File parent = file.getAbsoluteFile().getParentFile();
            if (parent != null && parent.isDirectory()) {
                fileTreePanelInstance.loadDirectory(parent);
            }
        }
        fileTreePanelInstance.selectFile(file);
    }

    public void newFileIn(String languageId) {
        File dir = currentContainer();
        if (dir == null) {
            dir = ensureContainer("archivo nuevo", null);
            if (dir == null) {
                return;
            }
        }
        EditorPanel editor = editorTabs.newFile(languageId, dir);
        if (editor == null) {
            return;
        }
        fileTreePanelInstance.reload();
        fileTreePanelInstance.selectFile(editor.getFile());
        showTabs();
        onActiveEditorChanged(editor);
    }
}
