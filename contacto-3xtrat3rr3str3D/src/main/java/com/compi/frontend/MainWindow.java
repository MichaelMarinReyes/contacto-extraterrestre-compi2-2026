package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.languages2.LanguageCompilerFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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

/**
 * Ventana principal con distribucion al estilo de IntelliJ IDEA.
 *
 * <pre>
 *  +-------------------------------------------------------------+
 *  |  Barra superior: acciones de archivo, vistas, compilar, C     |
 *  +-----------+---------------------------------+---------------+
 *  |  Proyecto |  Editor multi-pestana           |  Herramientas |
 *  |  (arbol)  +---------------------------------+  (AST,        |
 *  |           |  Consolas (tripletes, cuartetas, |  simbolos,    |
 *  |           |  C3D, C, resumen)               |  pila,        |
 *  |           +---------------------------------+  errores)     |
 *  +-----------+---------------------------------+---------------+
 *  |  Barra de estado                                            |
 *  +-------------------------------------------------------------+
 * </pre>
 *
 * Los metodos generados por NetBeans ({@code initComponents}) y los
 * manejadores de eventos se respetan tal cual; toda la logica nueva vive en
 * metodos adicionales.
 */
public class MainWindow extends JFrame {

    private static final String navText = "Contacto 3xtrat3rr3str3D";
    private static final String CARD_TABS = "tabs";
    private static final String CARD_WELCOME = "welcome";
    /** Nombre que se ofrece como carpeta contenedora cuando no hay proyecto. */
    private static final String DEFAULT_CONTAINER_NAME = "proyecto";
    /** Ancho inicial en pixeles del arbol de archivos. */
    private static final int TREE_DEFAULT_WIDTH = 280;
    /** A partir de cuantos ignorados el aviso se resume en vez de listarlos. */
    private static final int IGNORED_LIST_LIMIT = 12;

    // ---------- Componentes nuevos ----------
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
    /** true si el ancho inicial del arbol ya se fijo a mano. */
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

    private JPanel contentArea;
    private File currentFile;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        setTitle("Contacto 3xtrat3rr3str3D");
        setMinimumSize(new Dimension(1024, 640));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        UiTheme.applyFlatLafTweaks();
        initContentLayout();
        initToolbar();
        initShortcuts();
        initStyles();
        navText("");
        showWelcome();
    }

    // ====================== Distribucion ======================

    /**
     * Reorganiza {@code sidebarPanel} como barra superior y {@code contentPane}
     * con el arbol de proyecto, el editor, las consolas y la barra de estado.
     * Los paneles quedan en {@link JSplitPane} para que el usuario pueda
     * redimensionarlos.
     *
     * <p>Las herramientas ya no ocupan sitio aqui: viven en una ventana
     * flotante ({@link ToolWindow}) que hay que cerrar para volver al editor,
     * de modo que el ancho completo queda para el codigo.</p>
     */
    private void initContentLayout() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UiTheme.panelBg());

        consoleDock = new ConsoleDock();
        toolWindowDock = new ToolWindowDock(astPanel, symbolPanel, stackPanel, errorPanel);
        toolWindow = new ToolWindow(this, toolWindowDock);
        // Al cerrar la ventana, el boton de la barra debe quedar sin marcar.
        toolWindow.setOnClosed(() -> toolsToggle.setSelected(false));
        statusBar = new StatusBar();
        editorTabs = new EditorTabs(this::onActiveEditorChanged, file -> {
            // Al cerrar la ultima pestana se muestra la bienvenida.
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

        // Editor + consolas (redimensionable en vertical)
        editorSplit = createSplit(JSplitPane.VERTICAL_SPLIT, editorCardHost, consoleDock, 0.68, 0.7);

        // Arbol de proyecto + editor (redimensionable en horizontal)
        rootSplit = createSplit(JSplitPane.HORIZONTAL_SPLIT, fileTreePanelInstance, editorSplit, 0.17, 0.18);

        // Swing reparte el ancho inicial segun el ancho preferido del arbol, que
        // llega a 368 px por su encabezado de botones, y el 18% no llega a
        // cumplirse. El divisor se fija a mano en cuanto aparece la ventana, y
        // solo una vez, para no pelearse con el usuario si lo mueve luego.
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

        // Sustituye el GroupLayout de NetBeans: sidebarPanel pasa a ser la barra
        // superior y el resto ocupa todo el ancho disponible.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().removeAll();
        getContentPane().add(sidebarPanel, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();

        wireFileTree();
    }

    /**
     * Crea un divisor con desplazamiento continuo, boton de colapso y una
     * posicion proporcional inicial.
     */
    private static JSplitPane createSplit(int orientation, java.awt.Component left,
                                          java.awt.Component right, double resizeWeight,
                                          double dividerRatio) {
        JSplitPane split = new JSplitPane(orientation, left, right);
        split.setResizeWeight(resizeWeight);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);
        split.setBorder(null);
        split.setDividerSize(6);
        // Ratio aplicado cuando Swing dimensiona por primera vez el divisor.
        split.setDividerLocation(dividerRatio);
        return split;
    }

    private void wireFileTree() {
        fileTreePanelInstance.setOnFileSelected(file -> {
            EditorPanel editor = editorTabs.openFile(file);
            if (editor == null) {
                // El arbol ya filtra por extension, asi que aqui solo hay error
                // de lectura; se distingue igualmente por el mensaje.
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
        // El boton "Abrir carpeta" pasa por la ventana principal, que valida la
        // ruta y avisa del resultado en vez de dejar el arbol en silencio.
        fileTreePanelInstance.setOnOpenProjectRequest(this::openProject);
        fileTreePanelInstance.initSelectionBehavior();
    }

    // ====================== Barra superior ======================

    /**
     * Coloca los botones generados por NetBeans dentro de un {@link TopToolbar}
     * y los convierte en botones de icono plano.
     */
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

        // Se vacia el sidebarPanel que NetBeans poblo con layout absoluto.
        sidebarPanel.removeAll();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMinimumSize(new Dimension(100, TopToolbar.BAR_HEIGHT));
        sidebarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, TopToolbar.BAR_HEIGHT));
        sidebarPanel.add(topToolbar, BorderLayout.CENTER);
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    /**
     * Abre (o trae al frente) la ventana flotante de herramientas.
     *
     * @param view vista a mostrar, o null para la que ya estuviera seleccionada
     */
    private void openToolWindow(ToolWindowDock.View view) {
        toolsToggle.setSelected(true);
        if (view == null) {
            toolWindow.showWindow();
        } else {
            toolWindow.showView(view);
        }
        if (!toolWindow.isOpen()) {
            // Sin pantalla no se puede abrir la ventana: el boton no debe
            // quedarse marcado como si lo estuviera.
            toolsToggle.setSelected(false);
        }
    }

    private void onCModeComboChanged() {
        Object selected = cModeCombo.getSelectedItem();
        currentCMode = "Tripletes".equals(String.valueOf(selected)) ? Mode.TRIPLETS : Mode.QUADRUPLES;
        compiler.setCMode(currentCMode);
    }

    // ====================== Atajos de teclado ======================

    private void initShortcuts() {
        bind("control N", this::actionNewFile);
        bind("control O", this::actionOpenFile);
        bind("control S", () -> actionSave());
        bind("control B", this::actionCompile);
        bind("control W", () -> editorTabs.closeActive());
        bind("control shift S", () -> editorTabs.saveAll());
        bind("control 1", () -> showToolView(ToolWindowDock.View.AST));
        bind("control 2", () -> showToolView(ToolWindowDock.View.SIMBOLOS));
        bind("control 3", () -> showToolView(ToolWindowDock.View.PILA));
        bind("control 4", () -> showToolView(ToolWindowDock.View.ERRORES));
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

    // ====================== Acciones ======================

    /**
     * Crea un archivo vacio. Como el lenguaje ya no se elige con un desplegable,
     * se pregunta por la extension, que es la que determina el lenguaje.
     *
     * <p>Si todavia no hay proyecto abierto se pide tambien la carpeta
     * contenedora, para que el archivo nazca dentro del arbol y no quede como
     * una pestana suelta.</p>
     */
    private void actionNewFile() {
        String languageId = askForLanguage();
        if (languageId == null) {
            return;
        }
        File dir = ensureContainer("archivo nuevo", null);
        if (dir == null) {
            return;
        }
        EditorPanel editor = editorTabs.newFile(languageId, dir);
        if (editor == null) {
            promptInfo("No se pudo crear el archivo en:\n" + dir.getAbsolutePath(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return;
        }
        fileTreePanelInstance.reload();
        fileTreePanelInstance.selectFile(editor.getFile());
        showTabs();
        onActiveEditorChanged(editor);
        navText("Nuevo archivo / " + editor.getDisplayName());
    }

    /**
     * Pregunta con que extension crear el archivo.
     *
     * <p>Se propone primero el lenguaje de la pestana activa, que es lo que
     * esta escribiendo el usuario, y en su defecto PigLatin.</p>
     */
    private String askForLanguage() {
        return promptLanguage();
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
        // Un archivo suelto tambien necesita su carpeta en el arbol: si no esta
        // dentro de la carpeta del proyecto se le pregunta donde guardarlo.
        File placed = placeInContainer(file);
        if (placed == null) {
            return;
        }
        openInEditor(placed);
    }

    // ====================== Dialogos ======================
    //
    // Los selectores y avisos estan en metodos aparte y no private para que las
    // pruebas puedan responderlos sin montar una ventana modal de verdad.

    /** Selector de un archivo de codigo, o null si se cancela. */
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

    /**
     * Selector de una carpeta.
     *
     * @param preselected nombre a dejar escrito; null para ninguno
     * @return la carpeta elegida, o null si se cancela
     */
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

    /** Selector de la extension del archivo nuevo, o null si se cancela. */
    String promptLanguage() {
        List<String> extensions = LanguageCompilerFactory.allowedExtensions();
        List<String> options = new ArrayList<>(extensions.size());
        for (String ext : extensions) {
            options.add("*." + ext + "  (" + UiTheme.prettifyLanguage(
                    LanguageCompilerFactory.byExtensionLanguage(ext)) + ")");
        }

        EditorPanel active = editorTabs.getActiveEditor();
        String currentExt = LanguageCompilerFactory.extensionOfLanguage(
                active == null ? null : active.getLanguageId());
        Object preselected = currentExt == null ? options.get(0)
                : options.get(extensions.indexOf(currentExt));

        Object choice = JOptionPane.showInputDialog(this,
                "Extensión del archivo nuevo (define el lenguaje):",
                "Archivo nuevo", JOptionPane.QUESTION_MESSAGE, null,
                options.toArray(), preselected);
        if (choice == null) {
            return null;
        }
        int index = options.indexOf(String.valueOf(choice));
        return LanguageCompilerFactory.byExtensionLanguage(extensions.get(index));
    }

    /** Confirmacion si/no. */
    int promptConfirm(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /** Aviso modal. */
    void promptInfo(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private static boolean approve(JFileChooser chooser) {
        return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
    }

    /**
     * Asegura que exista una carpeta contenedora y la deja como raiz del arbol.
     *
     * <p>Si ya hay un proyecto abierto se devuelve su carpeta sin preguntar nada.
     * Si no, se pide una: el nombre por defecto es {@code proyecto} dentro de la
     * carpeta de inicio, o la carpeta del propio archivo si se sugiere una.</p>
     *
     * @param purpose  texto que explica para que se necesita la carpeta
     * @param suggested carpeta de la que partir (puede ser null)
     * @return la carpeta contenedora, o null si el usuario cancelo
     */
    private File ensureContainer(String purpose, File suggested) {
        File current = currentContainer();
        if (current != null) {
            return current;
        }

        boolean fromSuggestion = suggested != null && suggested.isDirectory();
        File start = fromSuggestion ? suggested
                : new File(System.getProperty("user.home", "."));
        // Si el archivo ya esta en alguna carpeta se propone esa misma, que es
        // lo habitual; si no se ofrece una carpeta nueva llamada "proyecto".
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

    // ====================== Proyecto ======================

    /**
     * Abre una carpeta como proyecto, validandola y avisando del resultado.
     *
     * <p>Es el unico camino para poner una raiz en el arbol, de modo que siempre
     * se sepa si funciono: si la ruta no existe, no es una carpeta o no se puede
     * leer, se explica por que y el proyecto anterior se deja intacto.</p>
     *
     * @return true si la carpeta se cargo en el arbol
     */
    boolean loadProject(File dir) {
        return loadProject(dir, true);
    }

    /**
     * Igual que {@link #loadProject(File)} pero decide siBuiltin avisar del exito.
     *
     * <p>Los fallos se avisan siempre. El exito solo se anuncia cuando la carpeta
     * la eligio el usuario a proposito ({@code announce}); si se carga como paso
     * intermedio para crear o abrir un archivo suelto, el dialogo de la accion
     * principal ya informa y uno aqui solo estorbaría. En ese modo una carpeta
     * sin codigo es normal todavia y no se avisa.</p>
     */
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

    /** Informa de lo que se ha encontrado al abrir el proyecto a proposito. */
    private void announceProject(File abs) {
        int fuentes = fileTreePanelInstance.countSourceFiles();
        List<File> ignorados = fileTreePanelInstance.getIgnoredFiles();
        if (fuentes == 0) {
            // Se avisa en vez de dejar un arbol mudo que parece un fallo.
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
        String resumen = "Proyecto cargado: " + fuentes
                + (fuentes == 1 ? " archivo de código" : " archivos de código");
        if (!ignorados.isEmpty()) {
            resumen += "\n" + ignorados.size()
                    + (ignorados.size() == 1
                            ? " archivo ignorado por su extensión."
                            : " archivos ignorados por su extensión.");
        }
        promptInfo(resumen + "\n\n" + abs.getAbsolutePath(),
                "Proyecto abierto", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Abre una carpeta como proyecto, preguntando cual. */
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

    /** Ultima carpeta con la que se abrio un proyecto, para retomar desde ahi. */
    private File lastContainerDir;

    private File lastContainer() {
        return lastContainerDir;
    }

    /**
     * Coloca un archivo recien abierto dentro de la carpeta del proyecto.
     *
     * <p>Si el archivo ya esta dentro, no hay nada que hacer. Si esta fuera y no
     * hay proyecto abierto se pregunta si se mueve a la carpeta elegida; si el
     * usuario no lo mueve, el arbol se queda con la carpeta del propio archivo
     * para que siempre se vea lo que se esta editando.</p>
     *
     * @return la ruta definitiva del archivo, o null si se cancelo
     */
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

    /** Mueve el archivo a la carpeta del proyecto, previa confirmacion. */
    private File moveToContainer(File file, File dir) {
        int answer = promptConfirm(
                "\"" + file.getName() + "\" está fuera del proyecto.\n\n"
                        + "¿Moverlo a la carpeta elegida?\n"
                        + dir.getAbsolutePath(),
                "Mover archivo al proyecto");
        if (answer != JOptionPane.YES_OPTION) {
            // No se mueve: el arbol se queda con la carpeta del propio archivo.
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

    /** true si {@code file} esta dentro de la carpeta {@code dir}. */
    private static boolean isInside(File dir, File file) {
        try {
            String base = dir.getCanonicalPath();
            String child = file.getCanonicalPath();
            return !child.equals(base) && child.startsWith(base + File.separator);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Carpeta del proyecto en uso, o null si no hay ninguna valida.
     *
     * <p>Una raiz que ya no existe en disco (se borro la carpeta) no cuenta:
     * entonces se vuelve a preguntar.</p>
     */
    private File currentContainer() {
        File root = fileTreePanelInstance.getRoot();
        return root != null && root.isDirectory() ? root : null;
    }

    /**
     * Informa de que un archivo se ha dejado fuera por su extension.
     *
     * <p>El selector de archivos deja escribir cualquier nombre ("Todos los
     * archivos"), asi que el caso se comprueba igual al abrir.</p>
     */
    private void warnWrongExtension(File file) {
        String name = file == null ? "(sin nombre)" : file.getName();
        promptInfo("No se abrió \"" + name + "\".\n\n"
                        + "Extensiones admitidas: "
                        + LanguageCompilerFactory.extensionPattern()
                        + "\nEl lenguaje de cada archivo lo decide su extensión.",
                "Extensión no admitida", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Avisa de los archivos del proyecto que se han ignorado por no tener una
     * extension admitida, listando el motivo y la extension de cada uno.
     */
    private void showIgnoredFiles(List<File> ignored) {
        if (ignored == null || ignored.isEmpty()) {
            return;
        }
        int total = ignored.size();
        // Abrir una carpeta amplia (~, /tmp) puede dejar fuera cientos de
        // archivos: listarlos todos convierte el aviso en un muro ilegible, asi
        // que se enseñan unos pocos y el resto se resume.
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

    /** "extensión .txt" a partir del nombre de un archivo sin extension valida. */
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

    /**
     * Abre la ventana de herramientas en la vista pedida.
     *
     * <p>Los botones de la barra superior ya no encienden y apagan un dock:
     * cada uno abre directamente su vista.</p>
     */
    private void showToolView(ToolWindowDock.View view) {
        openToolWindow(view);
    }

    // ====================== Compilacion ======================

    /**
     * Compila el contenido de la pestana activa y reparte el resultado entre
     * todos los paneles.
     */
    private void actionCompile() {
        EditorPanel editor = editorTabs.getActiveEditor();
        if (editor == null) {
            consoleDock.setConsole("Abre un archivo antes de compilar.\n");
            navText("Sin archivo activo");
            return;
        }

        consoleDock.setConsole("Compilando " + editor.getDisplayName()
                + " (" + UiTheme.prettifyLanguage(editor.getLanguageId()) + ")...\n");

        compiler.setCMode(currentCMode);
        boolean ok = compiler.compile(editor.getCodeText(), editor.getLanguageId());

        publishResults(ok);
    }

    private void publishResults(boolean ok) {
        List<CompilationError> errors = compiler.getErrors();

        // Las cuatro vistas viven en la ventana flotante. Si esta cerrada solo se
        // encolan los resultados y se pintan al volver a abrirla, para no gastar
        // en dibujar un grafo que nadie esta mirando.
        toolWindow.postResults(() -> {
            astPanel.renderGraph(compiler.getAstDot());
            symbolPanel.loadSymbols(compiler.getSymbols());
            stackPanel.loadStates(compiler.getStackStates());
            errorPanel.loadErrors(errors);
            toolWindowDock.setErrorCount(errors.size());
            if (!errors.isEmpty() && errorPanel.getErrorTable().getRowCount() > 0) {
                errorPanel.getErrorTable().setRowSelectionInterval(0, 0);
            }
        });

        consoleDock.setTriplets(compiler.getTriplets());
        consoleDock.setQuadruples(compiler.getQuadruples());
        consoleDock.setC3D(compiler.getC3DCode());
        consoleDock.setCCode(compiler.getCCode());
        consoleDock.setSummary(compiler.getTranslatedCode());
        consoleDock.setStatus("Modo de generación de C: " + TopToolbar.modeLabel(currentCMode));

        statusBar.setResult(ok, errors.size());
        statusBar.setCounts(compiler.getQuadrupleList().size(),
                compiler.getIcm().getTriplets().size());

        if (!errors.isEmpty()) {
            // Los errores siempre se enseñan: si la ventana estaba cerrada se
            // abre, y al abrir se pintan los resultados que quedaban encolados.
            showToolView(ToolWindowDock.View.ERRORES);
            CompilationError first = errors.get(0);
            if (first.getLine() > 0) {
                EditorPanel editor = editorTabs.getActiveEditor();
                if (editor != null) {
                    editor.gotoLine(first.getLine());
                }
            }
            navText("Errores de compilación (" + errors.size() + ")");
        } else {
            navText("Compilación correcta / " + compiler.getLanguage());
        }
        consoleDock.appendConsole(buildReport(errors));
    }

    private String buildReport(List<CompilationError> errors) {
        StringBuilder sb = new StringBuilder();
        if (errors.isEmpty()) {
            sb.append("Compilación correcta.\n");
        } else {
            sb.append(errors.size()).append(errors.size() == 1
                    ? " error encontrado:\n" : " errores encontrados:\n");
            for (CompilationError e : errors) {
                sb.append("  ").append(e).append('\n');
            }
        }
        sb.append("\n").append(compiler.getTranslatedCode());
        sb.append("\nAST generado: ").append(compiler.getAstDot().isBlank() ? "no" : "si");
        sb.append("   Símbolos: ").append(compiler.getSymbols().size());
        sb.append("   Pila: ").append(compiler.getStackStates().size()).append(" estados\n");
        return sb.toString();
    }

    // ====================== Navegacion entre pestanas ======================

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

    // ====================== Estilos ======================

    /**
     * Genera los estilos de los componentes.
     */
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

    // ====================== This method is not part of NetBeans ======================

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        openFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open-file.png"))); // NOI18N
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

        saveFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save-file.png"))); // NOI18N
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

        astButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ast.png"))); // NOI18N
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

        symbolTableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/symbol-table.png"))); // NOI18N
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

        stackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/stack.png"))); // NOI18N
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

        newFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/new-file.png"))); // NOI18N
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

        lexerErrorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error-table.png"))); // NOI18N
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
    }// </editor-fold>//GEN-END:initComponents

    // ====================== Manejadores generados por NetBeans ======================

    private void newFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileButtonActionPerformed
        actionNewFile();
    }//GEN-LAST:event_newFileButtonActionPerformed

    private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileButtonActionPerformed
        actionOpenFile();
    }//GEN-LAST:event_openFileButtonActionPerformed

    private void astButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_astButtonActionPerformed
        showToolView(ToolWindowDock.View.AST);
        navText("Árbol AST");
    }//GEN-LAST:event_astButtonActionPerformed

    private void symbolTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symbolTableButtonActionPerformed
        showToolView(ToolWindowDock.View.SIMBOLOS);
        navText("Tabla de símbolos (" + symbolPanel.getSymbolCount() + ")");
    }//GEN-LAST:event_symbolTableButtonActionPerformed

    private void lexerErrorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexerErrorButtonActionPerformed
        showToolView(ToolWindowDock.View.ERRORES);
        navText("Tabla de errores (" + errorPanel.getErrorCount() + ")");
    }//GEN-LAST:event_lexerErrorButtonActionPerformed

    private void stackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stackButtonActionPerformed
        showToolView(ToolWindowDock.View.PILA);
        navText("Pila de procesos (" + stackPanel.getStateCount() + " pasos)");
    }//GEN-LAST:event_stackButtonActionPerformed

    private void saveFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileButtonActionPerformed
        actionSave();
    }//GEN-LAST:event_saveFileButtonActionPerformed

    private void newFileButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFileButtonMouseEntered
        this.optionSelectedLabel.setText("Nuevo archivo");
    }//GEN-LAST:event_newFileButtonMouseEntered

    private void openFileButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openFileButtonMouseEntered
        this.optionSelectedLabel.setText("Abrir archivo");
    }//GEN-LAST:event_openFileButtonMouseEntered

    private void saveFileButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveFileButtonMouseEntered
        this.optionSelectedLabel.setText("Guardar archivo");
    }//GEN-LAST:event_saveFileButtonMouseEntered

    private void symbolTableButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symbolTableButtonMouseEntered
        this.optionSelectedLabel.setText("Tabla de Símbolos");
    }//GEN-LAST:event_symbolTableButtonMouseEntered

    private void astButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_astButtonMouseEntered
        this.optionSelectedLabel.setText("AST");
    }//GEN-LAST:event_astButtonMouseEntered

    private void stackButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stackButtonMouseEntered
        this.optionSelectedLabel.setText("Pila de Procesos");
    }//GEN-LAST:event_stackButtonMouseEntered

    private void lexerErrorButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lexerErrorButtonMouseEntered
        this.optionSelectedLabel.setText("Tabla de Errores");
    }//GEN-LAST:event_lexerErrorButtonMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables

    // ====================== Utilidades ======================

    /**
     * Carga un archivo en una pestana nueva (usado por el arranque y por
     * {@link FileTreePanel}).
     */
    private void loadFileFromTree(File file) {
        try {
            String contenido = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            currentFile = file;
            EditorPanel editor = editorTabs.openFile(file);
            if (editor == null) {
                throw new IOException("No se pudo crear la pestaña");
            }
            showTabs();
            onActiveEditorChanged(editor);
            navText("Editor de código  ›  " + file.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al leer el archivo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre una carpeta como proyecto en el arbol de archivos.
     */
    public void openProject(File directory) {
        fileTreePanelInstance.loadDirectory(directory);
        fileTreePanelInstance.revealFirstFile();
    }

    /** Acceso al dock de herramientas, util para pruebas. */
    public ToolWindowDock getToolWindowDock() {
        return toolWindowDock;
    }

    /** Acceso al gestor de pestanas, util para pruebas. */
    public EditorTabs getEditorTabs() {
        return editorTabs;
    }

    /** Acceso al dock de salidas, util para pruebas. */
    public ConsoleDock getConsoleDock() {
        return consoleDock;
    }

    /** Acceso a la ventana flotante de herramientas, util para pruebas. */
    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    /** Acceso a la barra superior, util para pruebas. */
    public TopToolbar getTopToolbar() {
        return topToolbar;
    }

    /** Acceso a la tabla de errores, util para pruebas. */
    public ErrorTablePanel getErrorPanel() {
        return errorPanel;
    }

    /** Acceso al arbol de archivos, util para pruebas. */
    public FileTreePanel getFileTree() {
        return fileTreePanelInstance;
    }

    /** Acceso a la barra de estado, util para pruebas. */
    public StatusBar getStatusBar() {
        return statusBar;
    }

    /** Compila la pestana activa. */
    public void compileActive() {
        actionCompile();
    }

    /**
     * Abre un archivo en una pestana, como si se hiciera doble clic en el arbol.
     *
     * <p>La extension debe estar admitida; si el archivo no esta dentro de la
     * carpeta del proyecto se avisa en vez de dejarlo fuera del arbol.</p>
     */
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

    /**
     * Se asegura de que un archivo se vea en el arbol y lo selecciona.
     *
     * <p>Si no hay proyecto, o el archivo ha quedado fuera de la carpeta
     * cargada, se carga su carpeta para que el editor y el arbol nunca
     * discrepen: abrir un archivo siempre lo hace aparecer en el arbol.</p>
     */
    private void revealInTree(File file) {
        if (!fileTreePanelInstance.contains(file)) {
            File parent = file.getAbsoluteFile().getParentFile();
            if (parent != null && parent.isDirectory()) {
                fileTreePanelInstance.loadDirectory(parent);
            }
        }
        fileTreePanelInstance.selectFile(file);
    }

    /** Crea un archivo dentro de la carpeta del proyecto y lo abre. */
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
