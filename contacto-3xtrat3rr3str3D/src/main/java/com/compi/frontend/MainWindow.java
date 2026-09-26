package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages2.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.symbols.Symbol;
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

    /**
     * Errores de la ultima compilacion, en el mismo orden que las filas de la
     * tabla. Se guardan aparte porque la tabla guarda solo el texto de cada celda
     * y al saltar a un error hace falta saber de que archivo era.
     */
    private List<CompilationError> lastErrors = List.of();

    /**
     * Archivos de la ultima compilacion, para resolver el nombre de un error sin
     * tener que volver a recorrer el disco cada vez que se salta a uno.
     */
    private List<File> lastTargets = List.of();

    /**
     * Archivo al que pertenecen los simbolos de la tabla.
     *
     * <p>La tabla de simbolos, a diferencia de la de errores, no es del proyecto
     * entero sino del archivo de la pestana activa, asi que el salto a la
     * declaracion va siempre ahi.</p>
     */
    private File lastFocusFile;

    /**
     * Nombre del archivo que es el punto de partida de un proyecto.
     *
     * <p>{@code main.pig} se lee el primero, es el que se enseña por defecto y el
     * que carga los demas a traves de sus imports.</p>
     */
    private static final String MAIN_FILE = "main.pig";

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
        // Doble clic en un error: abrir su archivo y quedarse en la linea.
        errorPanel.setOnErrorActivated(this::jumpToError);
        // Doble clic en un simbolo: quedarse en su declaracion.
        symbolPanel.setOnSymbolActivated(this::jumpToSymbol);
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
        // El proyecto manda: si ya hay carpeta abierta se pregunta solo el nombre,
        // el lenguaje y en que carpeta de esa nace. Sin proyecto hay que elegir
        // primero la carpeta contenedora, que si no el archivo no tendria donde
        // aparecer en el arbol.
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

    /**
     * Pregunta como se llama el archivo nuevo, en que lenguaje y en que carpeta.
     *
     * <p>Se propone el lenguaje de la pestana activa, que es lo que esta
     * escribiendo el usuario, y la carpeta donde esta el archivo abierto: es
     * donde suele querer seguir escribiendo.</p>
     */
    NewFileDialog.Choice promptNewFile(File container) {
        return buildNewFileDialog(container).ask();
    }

    /**
     * Monta el dialogo del archivo nuevo, sin mostrarlo.
     *
     * <p>Va aparte del {@link #promptNewFile} para poder inspeccionarlo en las
     * pruebas sin abrir una ventana modal que las bloquearia.</p>
     */
    NewFileDialog buildNewFileDialog(File container) {
        List<File> folders = fileTreePanelInstance.codeFolders();
        if (folders.isEmpty()) {
            folders = List.of(container);
        }
        EditorPanel active = editorTabs.getActiveEditor();
        String languageId = active == null ? null : active.getLanguageId();

        // Si hay un archivo abierto se propone la carpeta en la que esta; si no, la
        // raiz del proyecto.
        File suggestedFolder = active != null && active.getFile() != null
                ? active.getFile().getParentFile() : container;
        int selected = folders.indexOf(suggestedFolder);
        List<File> ordered = new ArrayList<>(folders);
        if (selected > 0) {
            // NewFileDialog marca la primera carpeta del desplegable, asi que se
            // deja la deseada en ese puesto.
            ordered.add(0, ordered.remove(selected));
        }

        String suggestedName = suggestFileName(languageId, container);
        return new NewFileDialog(this, container, ordered, languageId, suggestedName);
    }

    /** Nombre libre del estilo "archivoN", para proponerlo como punto de partida. */
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
     * Resultado de compilar un archivo, para poder juntar en una sola pasada los
     * de todo el proyecto.
     *
     * <p>Son copias y no referencias vivas: {@link Compiler} reutiliza las mismas
     * estructuras en cada {@code compile}, asi que si se guardara el estado
     * compartida, al terminar el bucle todas las copias apuntarian al ultimo
     * archivo compilado.</p>
     */
    private record FileResult(File file, String language, boolean ok,
                              String astDot, List<Symbol> symbols, List<ParseStep> steps,
                              List<CompilationError> errors, int quadruples, int triplets,
                              String tripletsCode, String quadruplesCode,
                              String c3dCode, String cCode, String translated,
                              List<String> imports) {
    }

    /**
     * Compila el proyecto entero, como haria un compilador de Java con todas sus
     * clases: no solo el archivo de la pestana activa.
     *
     * <p>Con carpeta abierta se compilan todos los archivos de codigo que haya
     * dentro, cada uno con el lenguaje que le toca por extension. Sin proyecto se
     * compilan las pestanas abiertas, que es lo unico que hay.</p>
     */
    private void actionCompile() {
        // Cada compilacion arranca de cero: nada de lo que salio de la anterior
        // puede quedarse colgando en ninguna de las vistas.
        limpiarResultados();

        List<File> targets = compileTargets();
        if (targets.isEmpty()) {
            consoleDock.setConsole("No hay nada que compilar.\n"
                    + "Abre una carpeta de proyecto o un archivo de código.\n");
            navText("Sin archivos que compilar");
            return;
        }

        File container = currentContainer();
        File main = targets.stream().filter(MainWindow::isMainFile).findFirst().orElse(null);
        StringBuilder cabecera = new StringBuilder();
        cabecera.append("Compilando ").append(targets.size())
                .append(targets.size() == 1 ? " archivo" : " archivos")
                .append(container != null ? " de " + container.getName() : " abiertos")
                .append("...\n");
        if (main != null) {
            cabecera.append("Punto de partida: ").append(main.getName())
                    .append(" (se lee primero; sus imports se cargan antes)\n");
        } else if (container != null) {
            cabecera.append("No hay ningún ").append(MAIN_FILE)
                    .append(" en el proyecto: se compilan los archivos por orden de nombre\n");
        }
        consoleDock.setConsole(cabecera.toString());

        compiler.setCMode(currentCMode);
        // Los imports se resuelven contra la raiz del proyecto, que es donde estan
        // los archivos que se importan.
        compiler.setWorkingDirectory(container);

        List<FileResult> results = new ArrayList<>(targets.size());
        for (File file : targets) {
            results.add(compileOne(file));
        }

        publishResults(results);
    }

    /**
     * Archivos a compilar: los del proyecto entero, o las pestanas abiertas si no
     * hay proyecto.
     *
     * <p>Empieza siempre por {@code main.pig}, que es el punto de partida del
     * proyecto: es el que se lee primero y el que manda. Al compilarlo se cargan
     * los archivos que sus imports mencionan, y el resto del proyecto se compila
     * despues, tambien para no dejar ningun archivo fuera.</p>
     */
    private List<File> compileTargets() {
        List<File> targets = fileTreePanelInstance.allSourceFiles();
        if (!targets.isEmpty()) {
            return startingAtMain(targets);
        }
        // Sin carpeta de proyecto se compila lo que este abierto, para no dejar al
        // usuario sin poder compilar por no haber abierto un proyecto.
        for (EditorPanel editor : editorTabs.getAllEditors()) {
            File file = editor.getFile();
            if (file != null && file.isFile()) {
                targets.add(file);
            }
        }
        return targets;
    }

    /**
     * Pone {@code main.pig} el primero, dejando el resto en el mismo orden.
     *
     * <p>Si hay varios (main.pig en distintas carpetas) gana el de la ruta mas
     * corta, que es el de la raiz del proyecto.</p>
     */
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

    /** true si ese archivo es el punto de partida del proyecto. */
    private static boolean isMainFile(File file) {
        return file != null && MAIN_FILE.equalsIgnoreCase(file.getName());
    }

    /**
     * Compila un archivo y guarda una copia de todo lo que produce.
     *
     * <p>Si el archivo esta abierto en una pestana se compila lo que hay escrito
     * en ella, no lo que hay en disco, para que no haga falta guardar antes de
     * comprobar si lo escrito compila.</p>
     */
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
                CompilationError io = new CompilationError(ErrorType.SEMANTICO,
                        "No se pudo leer el archivo: " + e.getMessage(),
                        -1, -1) // sin posicion: no hay linea a la que saltar
                        .inFile(file.getName());
                return new FileResult(file, language, false, "", List.of(), List.of(),
                        List.of(io), 0, 0, "", "", "", "", "", List.of());
            }
        }

        boolean ok = compiler.compile(source, language);
        List<CompilationError> errors = new ArrayList<>();
        for (CompilationError e : compiler.getErrors()) {
            errors.add(e.inFile(file.getName()));
        }
        List<Symbol> symbols = new ArrayList<>(compiler.getSymbols());
        // Los simbolos que salieron de un import ya saben de que archivo
        // vienen; estos son los del propio archivo, que se completan aqui
        // porque el backend no sabe como se llama.
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

    /** Editor abierto de un archivo, o null si no esta en ninguna pestana. */
    private EditorPanel editorOf(File file) {
        for (EditorPanel editor : editorTabs.getAllEditors()) {
            File candidate = editor.getFile();
            if (candidate != null && candidate.equals(file)) {
                return editor;
            }
        }
        return null;
    }

    /**
     * Reparte el resultado de la compilacion del proyecto entre los paneles.
     *
     * <p>Los errores se juntan todos: la tabla de errores es del proyecto, que es
     * lo que interesa saber. El AST, los simbolos, la pila y las consolas si son
     * de un solo archivo, el de la pestana activa, porque en una ventana solo cabe
     * un grafo y tiene que ser el que se esta editando.</p>
     */
    private void publishResults(List<FileResult> results) {
        List<CompilationError> allErrors = new ArrayList<>();
        for (FileResult r : results) {
            allErrors.addAll(r.errors());
        }
        FileResult focus = focusedResult(results);
        lastErrors = List.copyOf(allErrors);
        lastTargets = results.stream().map(FileResult::file).toList();
        lastFocusFile = focus == null ? null : focus.file();

        // Las cuatro vistas viven en la ventana flotante. Si esta cerrada solo se
        // encolan los resultados y se pintan al volver a abrirla, para no gastar
        // en dibujar un grafo que nadie esta mirando.
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
            // Los errores siempre se enseñan: si la ventana estaba cerrada se
            // abre, y al abrir se pintan los resultados que quedaban encolados.
            showToolView(ToolWindowDock.View.ERRORES);
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
        consoleDock.appendConsole(buildReport(results, allErrors, focus));
    }

    /**
     * Vacía todo lo que dejó la compilación anterior: simbolos, errores, pila,
     * AST, las cuatro consolas y los contadores.
     *
     * <p>Se llama al empezar a compilar, no al terminar, para que una compilación
     * se pinte siempre desde cero. Si una tabla, el grafo o una consola se
     * quedaron con datos de la vuelta anterior, esa fila, ese nodo o esa linea no
     * puede sobrevivir a la nueva: si el archivo ya no da error, su error
     * anterior tiene que desaparecer, y si el grafo no se puede construir, el
     * grafo viejo tampoco puede quedarse ahi.</p>
     */
    private void limpiarResultados() {
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

    /**
     * Busca un archivo de la compilacion actual por su nombre.
     *
     * <p>Los errores guardan el nombre y no la ruta, asi que se localiza cual de
     * los archivos compilados responde a ese nombre.</p>
     */
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

    /**
     * Abre el archivo de un error de la tabla y se para en su linea.
     *
     * <p>La tabla es del proyecto entero, asi que un error puede estar en un archivo
     * distinto del que se esta editando. Al abrirlo, el editor activo pasa a ser
     * ese y la vista de herramientas queda con lo suyo al volver a compilar.</p>
     */
    private void jumpToError(int row) {
        // La tabla puede tener un filtro puesto, asi que el numero de fila que
        // llega no es el indice en la lista de errores: lo resuelve la propia
        // tabla, que es quien sabe que hay en cada fila.
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
            // Si esta abierto y con cambios se guarda antes de saltar, para no
            // dejar el archivo a medias entre dos pestanas.
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

    /**
     * Abre la declaracion del simbolo de la fila indicada.
     *
     * <p>La tabla de simbolos es del archivo de la pestana activa, asi que el salto
     * va ahi, salvo que el simbolo venga de un import: en ese caso se abre el
     * archivo del que venia, que es donde esta su linea. Los simbolos que el
     * compilador genera sin escribir (el {@code this} implicito se anota con el
     * metodo al que pertenece, asi que casi nunca pasa) no tienen linea a la que
     * saltar y se avisa en vez de hacer nada en silencio.</p>
     */
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
        File destino = archivoDelSimbolo(symbol);
        if (destino == null) {
            return;
        }
        if (destino.isFile()) {
            openInEditor(destino);
        }
        EditorPanel target = editorOf(destino);
        if (target != null) {
            target.gotoLine(symbol.getLine());
        }
        navText(destino.getName() + " / línea " + symbol.getLine()
                + " / " + symbol.getName());
    }

    /**
     * Archivo donde se declaro un simbolo de la tabla.
     *
     * <p>Si el simbolo vino de un import se devuelve ese archivo; si no, el de la
     * pestana activa, que es al que pertenece la tabla.</p>
     */
    private File archivoDelSimbolo(Symbol symbol) {
        String origen = symbol.getSourceFile();
        File contenedor = currentContainer();
        if (origen == null || lastFocusFile == null
                || origen.equals(lastFocusFile.getName())) {
            return lastFocusFile;
        }
        if (contenedor == null) {
            return lastFocusFile;
        }
        File importado = new File(contenedor, origen);
        return importado.isFile() ? importado : lastFocusFile;
    }

    /**
     * Resultado que se enseña en el AST, los simbolos y la pila.
     *
     * <p>Se prefiere el de la pestana activa y, si no esta, el de
     * {@code main.pig}, que es el principal del proyecto. Solo si tampoco hay
     * {@code main.pig} se recurre al primero que haya producido un arbol, para
     * que la ventana no se quede con las vistas vacias sin motivo.</p>
     */
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

    /** Por que se enseña el resultado de este archivo y no el de otro. */
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

    /** Informe por consola, archivo a archivo, al estilo de un build. */
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
        navText("Tabla de símbolos (" + cuentaDeSimbolos() + ")");
    }//GEN-LAST:event_symbolTableButtonActionPerformed

    /**
     * Cuenta de simbolos que se enseña: con el filtro puesto se dice cuantas hay
     * de cuantas son, para que quede claro que el filtro esta escondiendo.
     */
    private String cuentaDeSimbolos() {
        if (symbolPanel.getSymbolCount() == symbolPanel.getTotalSymbolCount()) {
            return String.valueOf(symbolPanel.getTotalSymbolCount());
        }
        return symbolPanel.getSymbolCount() + " de " + symbolPanel.getTotalSymbolCount();
    }

    private void lexerErrorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexerErrorButtonActionPerformed
        showToolView(ToolWindowDock.View.ERRORES);
        navText("Tabla de errores (" + cuentaDeErrores() + ")");
    }//GEN-LAST:event_lexerErrorButtonActionPerformed

    /** Cuenta de errores que se enseña, con la misma regla que la de simbolos. */
    private String cuentaDeErrores() {
        if (errorPanel.getErrorCount() == errorPanel.getTotalErrorCount()) {
            return String.valueOf(errorPanel.getTotalErrorCount());
        }
        return errorPanel.getErrorCount() + " de " + errorPanel.getTotalErrorCount();
    }

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

    /** Acceso a la vista de arbol AST, util para pruebas. */
    public ParserTreePanel getAstPanel() {
        return astPanel;
    }

    /** Acceso a la vista de tabla de simbolos, util para pruebas. */
    public SymbolTablePanel getSymbolPanel() {
        return symbolPanel;
    }

    /** Acceso a la vista de pila de procesos, util para pruebas. */
    public StackVisualizerPanel getStackPanel() {
        return stackPanel;
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
