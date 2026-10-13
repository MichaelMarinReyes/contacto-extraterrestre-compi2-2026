package com.compi.frontend;

import com.compi.backend.c3d.Mode;
import com.compi.backend.languages.LanguageCompilerFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Ventana del IDE; delega archivos y compilación a sus colaboradores. */
public class MainWindow extends JFrame {
    private static final String NAV_TEXT = "Contacto 3xtrat3rr3str3D";
    private static final String CARD_TABS = "tabs";
    private static final String CARD_WELCOME = "welcome";
    private final ParserTreePanel astPanel = new ParserTreePanel();
    private final SymbolTablePanel symbolPanel = new SymbolTablePanel();
    private final StackVisualizerPanel stackPanel = new StackVisualizerPanel();
    private final ErrorTablePanel errorPanel = new ErrorTablePanel();
    private final TypeTablePanel typePanel = new TypeTablePanel();
    private final FileTreePanel fileTreePanelInstance = new FileTreePanel();
    private final JComboBox<String> cModeCombo = new JComboBox<>();
    private final JCheckBox riscvToggle = new JCheckBox();
    private final JToggleButton toolsToggle = new JToggleButton();
    private final JButton runButton = new JButton();
    private final JButton compileButton = new JButton();
    private final JButton newProjectButton = new JButton();
    private final JButton newFileButton = new JButton();
    private final JButton openFileButton = new JButton();
    private final JButton saveFileButton = new JButton();
    private final JButton astButton = new JButton();
    private final JButton symbolTableButton = new JButton();
    private final JButton stackButton = new JButton();
    private final JButton lexerErrorButton = new JButton();
    private final JButton typesButton = new JButton();
    private final JLabel optionSelectedLabel = new JLabel();
    private final JPanel sidebarPanel = new JPanel(new BorderLayout());
    private final WorkspaceActions workspace = new WorkspaceActions(this);
    private final CompilationActions compilation = new CompilationActions(this);
    private final CardLayout editorCards = new CardLayout();
    private final JPanel editorCardHost = new JPanel(editorCards);
    private TopToolbar topToolbar;
    private EditorTabs editorTabs;
    private ConsoleDock consoleDock;
    private ToolWindowDock toolWindowDock;
    private ToolWindow toolWindow;
    private StatusBar statusBar;
    private JSplitPane rootSplit;
    private boolean treeDividerFixed;

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(NAV_TEXT);
        setMinimumSize(new Dimension(1024, 640));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        UiTheme.applyFlatLafTweaks();
        initContentLayout();
        initToolbar();
        initShortcuts();
        errorPanel.setOnErrorActivated(compilation::jumpToError);
        symbolPanel.setOnSymbolActivated(compilation::jumpToSymbol);
        initStyles();
        navText("");
        showWelcome();
        pack();
    }

    private void initContentLayout() {
        consoleDock = new ConsoleDock();
        toolWindowDock = new ToolWindowDock(astPanel, symbolPanel, stackPanel, errorPanel, typePanel);
        toolWindow = new ToolWindow(this, toolWindowDock);
        toolWindow.setOnClosed(() -> toolsToggle.setSelected(false));
        statusBar = new StatusBar();
        editorTabs = new EditorTabs(this::onActiveEditorChanged, file -> {
            if (editorTabs.isEmpty()) showWelcome();
        });
        WelcomePanel welcome = new WelcomePanel(workspace::newFile, workspace::openFile,
                () -> fileTreePanelInstance.askForDirectory(), compilation::compile);
        editorCardHost.add(editorTabs, CARD_TABS);
        editorCardHost.add(welcome, CARD_WELCOME);
        editorCards.show(editorCardHost, CARD_WELCOME);
        JSplitPane editorSplit = createSplit(JSplitPane.VERTICAL_SPLIT, editorCardHost, consoleDock, 0.68, 0.7);
        rootSplit = createSplit(JSplitPane.HORIZONTAL_SPLIT, fileTreePanelInstance, editorSplit, 0.17, 0.18);
        installTreeDivider();
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(UiTheme.panelBg());
        body.add(rootSplit, BorderLayout.CENTER);
        body.add(statusBar, BorderLayout.SOUTH);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebarPanel, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        wireFileTree();
    }

    private void installTreeDivider() {
        addHierarchyListener(event -> {
            boolean showing = (event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0;
            if (showing && isShowing() && !treeDividerFixed) {
                treeDividerFixed = true;
                SwingUtilities.invokeLater(() -> rootSplit.setDividerLocation(280));
            }
        });
    }

    private static JSplitPane createSplit(int orientation, java.awt.Component left,
                                          java.awt.Component right, double weight, double ratio) {
        JSplitPane split = new JSplitPane(orientation, left, right);
        split.setResizeWeight(weight);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);
        split.setBorder(null);
        split.setDividerSize(6);
        split.setDividerLocation(ratio);
        return split;
    }

    private void wireFileTree() {
        fileTreePanelInstance.setOnFileSelected(this::openInEditor);
        fileTreePanelInstance.setOnDirectoryChanged(() -> {
            File root = fileTreePanelInstance.getRoot();
            if (topToolbar != null) topToolbar.setProjectName(root == null ? "" : root.getName());
        });
        fileTreePanelInstance.setOnFilesIgnored(workspace::showIgnoredFiles);
        fileTreePanelInstance.setOnOpenProjectRequest(this::openProject);
        fileTreePanelInstance.initSelectionBehavior();
    }

    private void initToolbar() {
        cModeCombo.addItem("Cuartetas");
        cModeCombo.addItem("Tripletes");
        cModeCombo.setSelectedIndex(0);
        topToolbar = new TopToolbar(optionSelectedLabel, cModeCombo, riscvToggle, runButton, compileButton, toolsToggle);
        topToolbar.applyIcons(newFileButton, openFileButton, saveFileButton,
                astButton, symbolTableButton, stackButton, lexerErrorButton);
        initFileActions();
        initViewActions();
        initToolToggle();
        compileButton.addActionListener(event -> compilation.compile());
        runButton.addActionListener(event -> compilation.runOrStopRiscv());
        cModeCombo.addActionListener(event -> compilation.setMode(
                "Tripletes".equals(String.valueOf(cModeCombo.getSelectedItem())) ? Mode.TRIPLETS : Mode.QUADRUPLES));
        topToolbar.onRiscvToggled(compilation::setRiscvEnabled);
        compilation.setRiscvEnabled(riscvToggle.isSelected());
        sidebarPanel.setPreferredSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMinimumSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, TopToolbar.BAR_HEIGHT));
        sidebarPanel.add(topToolbar, BorderLayout.CENTER);
    }

    /**
     * Ajusta el botón Ejecutar/Detener según haya o no una simulación en curso.
     * Lo invoca {@link CompilationActions} al arrancar y al terminar.
     */
    void setRunningState(boolean running) {
        runButton.setText(running ? "Detener" : "Ejecutar");
        runButton.setIcon(running ? IdeIcons.stop() : IdeIcons.run());
        runButton.setToolTipText(running
                ? "Detener la ejecución en curso"
                : "Ejecutar el ensamblador en el simulador interno (RARS)");
    }

    private void initFileActions() {
        newProjectButton.setIcon(IdeIcons.newProject());
        configureAction(newFileButton, "Nuevo archivo", workspace::newFile);
        configureAction(newProjectButton, "Abrir proyecto", this::openProject);
        configureAction(openFileButton, "Abrir archivo", workspace::openFile);
        configureAction(saveFileButton, "Guardar archivo", workspace::save);
        topToolbar.addFileAction(newFileButton, "Archivo nuevo (Ctrl+N)");
        topToolbar.addFileAction(newProjectButton, "Abrir proyecto (Ctrl+Shift+N)");
        topToolbar.addFileAction(openFileButton, "Abrir archivo (Ctrl+O)");
        topToolbar.addFileAction(saveFileButton, "Guardar (Ctrl+S)");
    }

    private void initViewActions() {
        configureAction(astButton, "AST", () -> {
            showToolView(ToolWindowDock.View.AST);
            navText("Árbol AST");
        });
        configureAction(symbolTableButton, "Tabla de Símbolos", () -> {
            showToolView(ToolWindowDock.View.SYMBOLS);
            navText("Tabla de símbolos (" + symbolCount() + ")");
        });
        configureAction(stackButton, "Pila de Procesos", () -> {
            showToolView(ToolWindowDock.View.STACK);
            navText("Pila de procesos (" + stackPanel.getStateCount() + " pasos)");
        });
        configureAction(lexerErrorButton, "Tabla de Errores", () -> {
            showToolView(ToolWindowDock.View.ERRORS);
            navText("Tabla de errores (" + errorCount() + ")");
        });
        topToolbar.addViewAction(astButton, "Ver árbol AST (Ctrl+1)");
        topToolbar.addViewAction(symbolTableButton, "Ver tabla de símbolos (Ctrl+2)");
        topToolbar.addViewAction(stackButton, "Ver pila de procesos (Ctrl+3)");
        topToolbar.addViewAction(lexerErrorButton, "Ver tabla de errores (Ctrl+4)");
        typesButton.setIcon(IdeIcons.symbols());
        configureAction(typesButton, "Tabla de tipos", () -> {
            showToolView(ToolWindowDock.View.TYPES);
            navText("Tabla de tipos (" + typePanel.getTypeCount() + ")");
        });
        topToolbar.addViewAction(typesButton, "Ver tabla de tipos (Ctrl+5)");
    }

    private void configureAction(JButton button, String label, Runnable action) {
        button.setBorderPainted(false);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.addActionListener(event -> action.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                optionSelectedLabel.setText(label);
            }
        });
    }

    private void initToolToggle() {
        toolsToggle.setText("Herramientas");
        toolsToggle.setSelected(false);
        toolsToggle.setToolTipText("Abrir la ventana de herramientas (AST, Símbolos, Pila, Errores, Tipos). "
                + "Ciérrala para volver al editor.");
        toolsToggle.addActionListener(event -> {
            if (toolsToggle.isSelected()) openToolWindow(null);
            else toolWindow.close();
        });
    }

    private void openToolWindow(ToolWindowDock.View view) {
        toolsToggle.setSelected(true);
        if (view == null) toolWindow.showWindow();
        else toolWindow.showView(view);
        if (!toolWindow.isOpen()) toolsToggle.setSelected(false);
    }

    void showToolView(ToolWindowDock.View view) {
        openToolWindow(view);
    }

    private void initShortcuts() {
        bind("control N", workspace::newFile);
        bind("control shift N", this::openProject);
        bind("control O", workspace::openFile);
        bind("control S", workspace::save);
        bind("control B", compilation::compile);
        bind("control R", compilation::runOrStopRiscv);
        bind("control W", () -> editorTabs.closeActive());
        bind("control shift S", () -> editorTabs.saveAll());
        bind("control 1", () -> showToolView(ToolWindowDock.View.AST));
        bind("control 2", () -> showToolView(ToolWindowDock.View.SYMBOLS));
        bind("control 3", () -> showToolView(ToolWindowDock.View.STACK));
        bind("control 4", () -> showToolView(ToolWindowDock.View.ERRORS));
        bind("control 5", () -> showToolView(ToolWindowDock.View.TYPES));
    }

    private void bind(String keyStroke, Runnable action) {
        AbstractAction binding = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        };
        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), binding);
        getRootPane().getActionMap().put(binding, binding);
    }

    NewFileDialog.Choice promptNewFile(File container) {
        return buildNewFileDialog(container).ask();
    }

    NewFileDialog buildNewFileDialog(File container) {
        return workspace.buildNewFileDialog(container);
    }

    File promptSourceFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo de código");
        chooser.setFileFilter(new FileNameExtensionFilter("Código (" + LanguageCompilerFactory.extensionPattern() + ")",
                LanguageCompilerFactory.allowedExtensions().toArray(new String[0])));
        if (workspace.lastContainer() != null) chooser.setCurrentDirectory(workspace.lastContainer());
        return approve(chooser) ? chooser.getSelectedFile() : null;
    }

    /** Elige una entrada PigLatin mostrando rutas para distinguir nombres repetidos. */
    File promptEntryFile(List<File> candidates) {
        File container = currentContainer();
        List<String> labels = candidates.stream().map(file -> container == null
                ? file.getAbsolutePath() : container.toPath().toAbsolutePath().normalize()
                        .relativize(file.toPath().toAbsolutePath().normalize()).toString()).toList();
        EditorPanel active = editorTabs.getActiveEditor();
        int suggested = active == null ? -1 : candidates.indexOf(active.getFile());
        Object choice = JOptionPane.showInputDialog(this,
                "Hay varios archivos .pig. Selecciona el punto de partida del programa:",
                "Seleccionar entrada PigLatin", JOptionPane.QUESTION_MESSAGE, null,
                labels.toArray(), labels.get(suggested < 0 ? 0 : suggested));
        return choice == null ? null : candidates.get(labels.indexOf(choice.toString()));
    }

    File promptDirectory(String title, File start, File selected, String approveText, boolean allowCreate) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText(approveText);
        chooser.setCurrentDirectory(start != null && start.isDirectory()
                ? start : new File(System.getProperty("user.home", ".")));
        if (selected != null) chooser.setSelectedFile(selected);
        if (!approve(chooser)) return null;
        File directory = chooser.getSelectedFile();
        if (directory == null) return null;
        if (!directory.exists() && allowCreate && !directory.mkdirs()) {
            promptInfo("No se pudo crear la carpeta:\n" + directory.getAbsolutePath(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return directory;
    }

    int promptConfirm(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    void promptInfo(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private static boolean approve(JFileChooser chooser) {
        return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
    }

    boolean loadProject(File directory) {
        return loadProject(directory, true);
    }

    boolean loadProject(File directory, boolean announce) {
        return workspace.loadProject(directory, announce);
    }

    void openProject() {
        workspace.openProject();
    }

    File currentContainer() {
        return workspace.currentContainer();
    }

    void resetWorkspace() {
        for (EditorPanel editor : new ArrayList<>(editorTabs.getAllEditors())) editorTabs.doClose(editor);
        compilation.clearResults();
        symbolPanel.clearFilter();
        showWelcome();
        navText("");
    }

    void clearToolViews() {
        astPanel.clear();
        symbolPanel.clear();
        errorPanel.clear();
        stackPanel.clear();
        typePanel.clear();
        toolWindowDock.setErrorCount(0);
        consoleDock.clearAll();
    }

    EditorPanel editorOf(File file) {
        for (EditorPanel editor : editorTabs.getAllEditors()) {
            File candidate = editor.getFile();
            if (candidate != null && candidate.equals(file)) return editor;
        }
        return null;
    }

    void onActiveEditorChanged(EditorPanel editor) {
        if (editor == null) return;
        statusBar.setFileName(editor.getDisplayName());
        statusBar.setLanguage(editor.getLanguageId());
        statusBar.setPosition(editor.getCaretLine(), editor.getCaretColumn());
    }

    void showTabs() {
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

    void navText(String text) {
        optionSelectedLabel.setText(text == null || text.isBlank() ? NAV_TEXT : NAV_TEXT + "  ›  " + text);
    }

    private String symbolCount() {
        int shown = symbolPanel.getSymbolCount(), total = symbolPanel.getTotalSymbolCount();
        return shown == total ? String.valueOf(total) : shown + " de " + total;
    }

    private String errorCount() {
        int shown = errorPanel.getErrorCount(), total = errorPanel.getTotalErrorCount();
        return shown == total ? String.valueOf(total) : shown + " de " + total;
    }

    public void openProject(File directory) {
        fileTreePanelInstance.loadDirectory(directory);
        fileTreePanelInstance.revealFirstFile();
    }

    public ToolWindowDock getToolWindowDock() { return toolWindowDock; }
    public EditorTabs getEditorTabs() { return editorTabs; }
    public ConsoleDock getConsoleDock() { return consoleDock; }
    public ToolWindow getToolWindow() { return toolWindow; }
    public TopToolbar getTopToolbar() { return topToolbar; }
    public ErrorTablePanel getErrorPanel() { return errorPanel; }
    public FileTreePanel getFileTree() { return fileTreePanelInstance; }
    public ParserTreePanel getAstPanel() { return astPanel; }
    public SymbolTablePanel getSymbolPanel() { return symbolPanel; }
    public StackVisualizerPanel getStackPanel() { return stackPanel; }
    public TypeTablePanel getTypePanel() { return typePanel; }
    public StatusBar getStatusBar() { return statusBar; }

    public void compileActive() {
        compilation.compile();
    }

    public void openInEditor(File file) {
        EditorPanel editor = editorTabs.openFile(file);
        if (editor == null) {
            if (!LanguageCompilerFactory.hasAllowedExtension(file.getName())) workspace.warnWrongExtension(file);
            else promptInfo("No se pudo leer el archivo:\n" + file.getAbsolutePath(), "Error de lectura", JOptionPane.ERROR_MESSAGE);
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
            if (parent != null && parent.isDirectory()) fileTreePanelInstance.loadDirectory(parent);
        }
        fileTreePanelInstance.selectFile(file);
    }

    public void newFileIn(String languageId) {
        workspace.newFileIn(languageId);
    }
}
