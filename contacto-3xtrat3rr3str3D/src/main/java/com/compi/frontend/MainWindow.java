package com.compi.frontend;

import com.compi.backend.Compiler;
import com.compi.backend.c3d.Mode;
import com.compi.backend.errors.CompilationError;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    // ---------- Componentes nuevos ----------
    private TopToolbar topToolbar;
    private EditorTabs editorTabs;
    private ConsoleDock consoleDock;
    private ToolWindowDock toolWindowDock;
    private StatusBar statusBar;
    private WelcomePanel welcomePanel;
    private JPanel editorCardHost;
    private CardLayout editorCards;
    private JSplitPane rootSplit;
    private JSplitPane centerSplit;
    private JSplitPane editorSplit;

    private final ParserTreePanel astPanel = new ParserTreePanel();
    private final SymbolTablePanel symbolPanel = new SymbolTablePanel();
    private final StackVisualizerPanel stackPanel = new StackVisualizerPanel();
    private final ErrorTablePanel errorPanel = new ErrorTablePanel();
    private final FileTreePanel fileTreePanelInstance = new FileTreePanel();

    private final JComboBox<String> languageCombo = new JComboBox<>();
    private final JComboBox<String> cModeCombo = new JComboBox<>();
    private final JToggleButton toolsToggle = new JToggleButton();
    private final JButton compileButton = new JButton();

    private final Compiler compiler = new Compiler();
    private Mode currentCMode = Mode.QUADRUPLES;
    private boolean sideDockVisible = true;

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
     * con el arbol de proyecto, el editor, las consolas, las herramientas y la
     * barra de estado. Todos los paneles quedan en {@link JSplitPane} para que
     * el usuario pueda redimensionarlos.
     */
    private void initContentLayout() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UiTheme.panelBg());

        consoleDock = new ConsoleDock();
        toolWindowDock = new ToolWindowDock(astPanel, symbolPanel, stackPanel, errorPanel);
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

        // Editor + dock de herramientas (redimensionable en horizontal)
        centerSplit = createSplit(JSplitPane.HORIZONTAL_SPLIT, editorSplit, toolWindowDock, 0.74, 0.75);

        // Arbol de proyecto + resto
        rootSplit = createSplit(JSplitPane.HORIZONTAL_SPLIT, fileTreePanelInstance, centerSplit, 0.17, 0.18);

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
                JOptionPane.showMessageDialog(this,
                        "No se pudo leer el archivo:\n" + file.getAbsolutePath(),
                        "Error de lectura", JOptionPane.ERROR_MESSAGE);
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
        fileTreePanelInstance.initSelectionBehavior();
    }

    // ====================== Barra superior ======================

    /**
     * Coloca los botones generados por NetBeans dentro de un {@link TopToolbar}
     * y los convierte en botones de icono plano.
     */
    private void initToolbar() {
        languageCombo.addItem("PigLatin");
        languageCombo.addItem("Y");
        languageCombo.addItem("Zetariano");
        languageCombo.setSelectedIndex(0);

        cModeCombo.addItem("Cuartetas");
        cModeCombo.addItem("Tripletes");
        cModeCombo.setSelectedIndex(0);

        topToolbar = new TopToolbar(optionSelectedLabel, languageCombo, cModeCombo,
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
        languageCombo.addActionListener(e -> onLanguageComboChanged());
        cModeCombo.addActionListener(e -> onCModeComboChanged());
        toolsToggle.setSelected(true);
        toolsToggle.addActionListener(e -> setSideDockVisible(toolsToggle.isSelected()));
        setSideDockVisible(true);

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

    /** Muestra u oculta el dock derecho moviendo su divisor. */
    private void setSideDockVisible(boolean visible) {
        sideDockVisible = visible;
        toolWindowDock.setVisible(visible);
        // El divisor se lleva al extremo para que el dock quede oculto.
        centerSplit.setDividerLocation(visible ? 0.74 : 1.0);
    }

    private void onLanguageComboChanged() {
        Object selected = languageCombo.getSelectedItem();
        if (selected == null) {
            return;
        }
        String languageId = switch (selected.toString()) {
            case "Y" -> "y";
            case "Zetariano" -> "zet";
            default -> "pig";
        };
        EditorPanel editor = editorTabs.getActiveEditor();
        if (editor != null && !editor.getLanguageId().equals(languageId)) {
            editor.setLanguageId(languageId);
            editorTabs.refreshHeaders();
        }
        statusBar.setLanguage(languageId);
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

    private void actionNewFile() {
        String languageId = "pig";
        EditorPanel editor = editorTabs.newFile(languageId);
        showTabs();
        navText("Nuevo archivo / " + editor.getDisplayName());
    }

    private void actionOpenFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo de código");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Código (*.pig, *.lat, *.y, *.z)", "pig", "lat", "y", "z"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        EditorPanel editor = editorTabs.openFile(file);
        if (editor == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer:\n" + file.getAbsolutePath(),
                    "Error de lectura", JOptionPane.ERROR_MESSAGE);
            return;
        }
        editorTabs.selectEditor(editor);
        showTabs();
        onActiveEditorChanged(editor);
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

    /** Muestra la vista pedida del dock derecho, asegurando que este visible. */
    private void showToolView(ToolWindowDock.View view) {
        if (!sideDockVisible) {
            toolsToggle.setSelected(true);
            setSideDockVisible(true);
        }
        toolWindowDock.select(view);
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

        astPanel.renderGraph(compiler.getAstDot());
        symbolPanel.loadSymbols(compiler.getSymbols());
        stackPanel.loadStates(compiler.getStackStates());
        errorPanel.loadErrors(errors);
        toolWindowDock.setErrorCount(errors.size());

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
            errorPanel.getErrorTable().setRowSelectionInterval(0, 0);
            CompilationError first = errors.get(0);
            if (first.getLine() > 0) {
                EditorPanel editor = editorTabs.getActiveEditor();
                if (editor != null) {
                    editor.gotoLine(first.getLine());
                }
            }
            showToolView(ToolWindowDock.View.ERRORES);
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

    /** Acceso a la barra superior, util para pruebas. */
    public TopToolbar getTopToolbar() {
        return topToolbar;
    }

    /** Acceso a la tabla de errores, util para pruebas. */
    public ErrorTablePanel getErrorPanel() {
        return errorPanel;
    }
}
