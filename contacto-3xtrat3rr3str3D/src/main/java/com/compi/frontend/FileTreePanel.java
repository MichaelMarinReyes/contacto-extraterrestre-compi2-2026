package com.compi.frontend;

import com.compi.backend.languages2.LanguageCompilerFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Panel izquierdo con el arbol de archivos del proyecto.
 *
 * Contiene una cabecera con el nombre del proyecto, un filtro de texto y el
 * arbol propiamente dicho. Al hacer doble clic sobre un archivo se notifica al
 * contenedor para que lo abra en una pestana de editor.
 *
 * <p>Solo se muestran los archivos cuya extension esta admitida (ver
 * {@link LanguageCompilerFactory#allowedExtensions()}). Los demas se ignoran y
 * se comunican con {@link #setOnFilesIgnored(Consumer)} para que la ventana
 * avise de cual ha quedado fuera. Una carpeta que se queda sin archivos
 * admitidos tampoco aparece.</p>
 */
public class FileTreePanel extends JPanel {

    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    private final DefaultTreeModel model;
    private final JLabel titleLabel = new JLabel("PROYECTO");
    private final JTextField filterField = new JTextField();
    private final JPanel emptyHint = new JPanel(new BorderLayout());
    private final JLabel emptyLabel = new JLabel();
    private CardLayout centerCards;
    private JPanel centerHolder;

    private File root;
    private Consumer<File> onFileSelected;
    private Consumer<File> onDirectorySelected;
    private Runnable onDirectoryChanged;
    private Consumer<List<File>> onFilesIgnored;
    /** Pide abrir una carpeta como proyecto; lo atiende la ventana principal. */
    private Runnable onOpenProjectRequest;

    /** Archivos rechazados en la ultima lectura, para no avisar dos veces. */
    private final Set<File> lastIgnored = new LinkedHashSet<>();

    private static final String CARD_TREE = "arbol";
    private static final String CARD_EMPTY = "vacio";

    public FileTreePanel() {
        setLayout(new BorderLayout());
        setBorder(UiTheme.hairlineRight());

        rootNode = new DefaultMutableTreeNode("Proyecto");
        model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(24);
        tree.setFont(UiTheme.sans(13));
        tree.setCellRenderer(new FileCellRenderer());
        tree.setBackground(UiTheme.toolWindowBg());
        tree.setForeground(UiTheme.fg());
        tree.setBorder(UiTheme.pad(4, 0, 4, 0));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ====================== Construccion ======================

    /** Cabecera con el nombre del proyecto y el filtro de texto. */
    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(UiTheme.pad(8, 10, 4, 6));

        titleLabel.setFont(UiTheme.sansBold(11));
        titleLabel.setForeground(UiTheme.dim());
        titleLabel.setText("PROYECTO");
        header.add(titleLabel, BorderLayout.WEST);

        JButton openDir = new JButton(IdeIcons.openFile());
        styleSmallButton(openDir, "Abrir carpeta como proyecto");
        openDir.addActionListener(e -> askForDirectory());

        JButton refresh = new JButton(IdeIcons.refresh());
        styleSmallButton(refresh, "Actualizar");
        refresh.addActionListener(e -> reload());

        JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        buttons.setOpaque(false);
        buttons.add(refresh);
        buttons.add(openDir);
        header.add(buttons, BorderLayout.EAST);

        top.add(header, BorderLayout.NORTH);
        top.add(buildFilter(), BorderLayout.SOUTH);
        return top;
    }

    private JPanel buildFilter() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(UiTheme.pad(0, 8, 6, 8));

        filterField.setFont(UiTheme.sans(12));
        filterField.putClientProperty("JTextField.placeholderText", "Buscar archivo");
        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
        });
        wrapper.add(filterField, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCenter() {
        JScrollPane scroll = new JScrollPane(tree);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UiTheme.toolWindowBg());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        emptyHint.setBackground(UiTheme.toolWindowBg());
        emptyLabel.setFont(UiTheme.sans(12));
        emptyLabel.setForeground(UiTheme.dim());
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyHint.add(emptyLabel, BorderLayout.CENTER);

        // Un CardLayout y no dos componentes en CENTER: superponer el aviso al
        // arbol dejaba el arbol Tapado y no se veia si habia archivos o no.
        centerCards = new CardLayout();
        centerHolder = new JPanel(centerCards);
        centerHolder.setBackground(UiTheme.toolWindowBg());
        centerHolder.add(scroll, CARD_TREE);
        centerHolder.add(emptyHint, CARD_EMPTY);
        centerCards.show(centerHolder, CARD_EMPTY);
        return centerHolder;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(UiTheme.pad(4, 10, 6, 10));
        JLabel hint = new JLabel("Doble clic para abrir");
        hint.setFont(UiTheme.sans(11));
        hint.setForeground(UiTheme.dim());
        footer.add(hint, BorderLayout.WEST);
        return footer;
    }

    private void styleSmallButton(JButton b, String tooltip) {
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusable(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setToolTipText(tooltip);
        b.setPreferredSize(new Dimension(24, 24));
    }

    // ====================== API ======================

    /** Callback invocado al seleccionar un archivo (doble clic). */
    public void setOnFileSelected(Consumer<File> consumer) {
        this.onFileSelected = consumer;
    }

    /** Callback invocado al seleccionar una carpeta. */
    public void setOnDirectorySelected(Consumer<File> consumer) {
        this.onDirectorySelected = consumer;
    }

    /** Callback invocado tras recargar el arbol. */
    public void setOnDirectoryChanged(Runnable consumer) {
        this.onDirectoryChanged = consumer;
    }

    /**
     * Callback invocado con los archivos que se han dejado fuera del arbol por
     * no tener una extension admitida. Solo se dispara cuando la lista cambia,
     * para no repetir el aviso en cada recarga.
     */
    public void setOnFilesIgnored(Consumer<List<File>> consumer) {
        this.onFilesIgnored = consumer;
    }

    /**
     * Callback del boton "Abrir carpeta" de la cabecera.
     *
     * <p>No se abre el selector aqui: lo atiende la ventana principal, que
     * ademas valida la ruta y avisa de lo encontrado o de por que fallo.</p>
     */
    public void setOnOpenProjectRequest(Runnable request) {
        this.onOpenProjectRequest = request;
    }

    /** Archivos ignorados en la ultima lectura del arbol. */
    public List<File> getIgnoredFiles() {
        return new ArrayList<>(lastIgnored);
    }

    public File getRoot() {
        return root;
    }

    public JTree getTree() {
        return tree;
    }

    /**
     * Carga una carpeta como raiz del proyecto.
     */
    public void loadDirectory(File dir) {
        this.root = dir;
        rootNode.setUserObject(dir == null ? "Proyecto" : dir.getName());
        rebuild();
    }

    /** Vuelve a leer el arbol desde disco. */
    public void reload() {
        if (root != null) {
            rebuild();
        }
    }

    /** Expande la rama raiz y selecciona su primer hijo. */
    public void revealFirstFile() {
        if (rootNode.getChildCount() > 0) {
            tree.expandPath(new TreePath(rootNode.getPath()));
            tree.setSelectionRow(1);
        }
    }

    /** Selecciona un archivo concreto si esta en el arbol. */
    public void selectFile(File file) {
        if (file == null) {
            return;
        }
        TreePath path = findPath(rootNode, file);
        if (path != null) {
            tree.setSelectionPath(path);
            tree.scrollPathToVisible(path);
        }
    }

    /** true si el archivo esta actualmente en el arbol. */
    public boolean contains(File file) {
        return file != null && findPath(rootNode, file) != null;
    }

    /**
     * Todos los archivos de codigo del proyecto, de las carpetas que tienen.
     *
     * <p>Recorre el disco en vez de usar el arbol, porque compilar el proyecto
     * tiene que abarcar todo lo que hay, no solo lo que se ve con el filtro de
     * busqueda puesto.</p>
     */
    public List<File> allSourceFiles() {
        List<File> found = new ArrayList<>();
        collectSources(root, found);
        // Orden estable: si no, el resultado cambiaria entre ejecuciones.
        found.sort(Comparator.comparing(File::getAbsolutePath));
        return found;
    }

    private static void collectSources(File dir, List<File> out) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                collectSources(child, out);
            } else if (isSourceFile(child)) {
                out.add(child);
            }
        }
    }

    /**
     * Cuenta los archivos de codigo que hay ahora mismo en el arbol.
     *
     * <p>Serve para informar de cuanto se ha cargado y para detectar que una
     * carpeta no contiene nada que el compilador pueda abrir.</p>
     */
    public int countSourceFiles() {
        int[] total = {0};
        countSources(rootNode, total);
        return total[0];
    }

    /**
     * Carpetas del proyecto donde tiene sentido crear un archivo nuevo.
     *
     * <p>Son la raiz y todas las carpetas intermedias que llevan a algun archivo de
     * codigo. No se ofrecen las carpetas vacias porque ahi no hay nada que
     * compilar todavia; si se quiere una de esas, se escoge con el selector de
     * carpetas.</p>
     */
    public List<File> codeFolders() {
        Set<File> folders = new LinkedHashSet<>();
        for (File file : allSourceFiles()) {
            File parent = file.getParentFile();
            while (parent != null && isWithinRoot(parent)) {
                folders.add(parent);
                if (parent.equals(root)) {
                    break;
                }
                parent = parent.getParentFile();
            }
        }
        // La raiz primero, que es donde se crea todo por defecto; el resto, por
        // nombre para que la lista no se mueva entre Builds.
        List<File> ordered = new ArrayList<>();
        if (root != null && root.isDirectory()) {
            ordered.add(root);
        }
        folders.stream()
                .filter(f -> !f.equals(root))
                .sorted(Comparator.comparing(File::getAbsolutePath))
                .forEach(ordered::add);
        return ordered;
    }

    /** true si la carpeta esta dentro de la raiz del proyecto. */
    private boolean isWithinRoot(File dir) {
        if (root == null) {
            return false;
        }
        try {
            String base = root.getCanonicalPath();
            String candidate = dir.getCanonicalPath();
            return candidate.equals(base) || candidate.startsWith(base + File.separator);
        } catch (IOException e) {
            return false;
        }
    }

    /** Carpeta del proyecto, o null si no hay ninguna abierta. */
    public File projectRoot() {
        return root;
    }

    private static void countSources(DefaultMutableTreeNode node, int[] total) {
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child.getUserObject() instanceof File f && !f.isDirectory()) {
                total[0]++;
            } else {
                countSources(child, total);
            }
        }
    }

    /**
     * Pide al usuario una carpeta y la carga como proyecto.
     *
     * <p>Si la ventana principal registro un gestor, se le delega para que
     * valide la ruta y avise del resultado. Sin gestor se usa el selector
     * directo.</p>
     */
    public void askForDirectory() {
        if (onOpenProjectRequest != null) {
            onOpenProjectRequest.run();
            return;
        }
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Seleccionar carpeta del proyecto");
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            loadDirectory(chooser.getSelectedFile());
        }
    }

    // ====================== Interno ======================

    /** Relee el arbol desde disco respetando el filtro activo. */
    private void rebuild() {
        String needle = currentFilter();
        List<File> ignored = new ArrayList<>();
        rootNode.removeAllChildren();
        if (root != null && root.isDirectory()) {
            for (File child : sortedChildren(root)) {
                if (isVisible(child, needle, ignored)) {
                    addNode(rootNode, child, needle, ignored);
                }
            }
        }
        model.reload();
        tree.expandPath(new TreePath(rootNode.getPath()));
        if (!needle.isEmpty()) {
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
        }
        refreshEmptyCard(needle, ignored.size());
        publishIgnored(ignored);
        if (onDirectoryChanged != null) {
            onDirectoryChanged.run();
        }
    }

    /**
     * Decide si se ve el arbol o el aviso, y redacta el aviso segun el motivo:
     * no hay proyecto, la carpeta esta vacia o el filtro no deja nada.
     */
    private void refreshEmptyCard(String needle, int ignored) {
        boolean vacio = rootNode.getChildCount() == 0;
        if (centerCards != null && centerHolder != null) {
            centerCards.show(centerHolder, vacio ? CARD_EMPTY : CARD_TREE);
        }
        emptyHint.setVisible(vacio);
        if (!vacio) {
            return;
        }
        emptyLabel.setText("<html><div style='text-align:center;padding:16px'>"
                + emptyMessage(needle, ignored) + "</div></html>");
    }

    private String emptyMessage(String needle, int ignored) {
        if (root == null) {
            return "Sin proyecto abierto.<br/>Usa <b>Abrir carpeta</b> en la cabecera"
                    + "<br/>o el botón <b>Abrir archivo</b> de la barra superior.";
        }
        if (!root.isDirectory()) {
            return "La carpeta del proyecto ya no existe:<br/>"
                    + escape(root.getAbsolutePath());
        }
        if (!needle.isEmpty()) {
            return "Ningún archivo coincide con el filtro<br/><b>" + escape(needle)
                    + "</b>.<br/>Borra el texto para verlos todos.";
        }
        if (ignored > 0) {
            return "Esta carpeta no tiene archivos de código.<br/>"
                    + ignored + (ignored == 1
                            ? " archivo fue ignorado"
                            : " archivos fueron ignorados")
                    + " por su extensión.<br/>Se admiten "
                    + LanguageCompilerFactory.extensionPattern() + ".";
        }
        return "Esta carpeta está vacía.<br/>Se admiten "
                + LanguageCompilerFactory.extensionPattern() + ".";
    }

    /** Escapa el texto para poder ponerlo dentro del HTML de una etiqueta. */
    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Avisa de los archivos descartados, pero solo si la lista ha cambiado: el
     * arbol se recarga al guardar o al filtrar y el aviso no debe repetirse.
     */
    private void publishIgnored(List<File> ignored) {
        if (ignored.equals(new ArrayList<>(lastIgnored))) {
            return;
        }
        lastIgnored.clear();
        lastIgnored.addAll(ignored);
        if (onFilesIgnored != null && !lastIgnored.isEmpty()) {
            List<File> copy = new ArrayList<>(lastIgnored);
            // El aviso es modal y el arbol se recarga desde eventos de Swing:
            // se encola para no dejar el arbol a medias si el usuario interactua.
            SwingUtilities.invokeLater(() -> onFilesIgnored.accept(copy));
        }
    }

    private void addNode(DefaultMutableTreeNode parent, File file, String needle,
                         List<File> ignored) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        parent.add(node);
        if (file.isDirectory()) {
            for (File child : sortedChildren(file)) {
                if (isVisible(child, needle, ignored)) {
                    addNode(node, child, needle, ignored);
                }
            }
        }
    }

    /**
     * Decide si una entrada entra en el arbol.
     *
     * <p>Un archivo entra si su extension esta admitida y ademas encaja con el
     * filtro de texto. Uno cuya extension no vale se anota en {@code ignored} y
     * se descarta. Una carpeta entra si conserva alguna entrada valida: una
     * carpeta vacia de codigo no aporta nada al arbol, pero sus archivos
     * rechazados se anotan igualmente para que el usuario sepa que existen.</p>
     */
    private boolean isVisible(File file, String needle, List<File> ignored) {
        if (file.isDirectory()) {
            return containsVisible(file, needle, ignored);
        }
        if (!isSourceFile(file)) {
            ignored.add(file);
            return false;
        }
        return matchesText(file, needle);
    }

    /** true si el archivo, o algun descendiente suyo, debe mostrarse. */
    private boolean containsVisible(File dir, String needle, List<File> ignored) {
        for (File child : sortedChildren(dir)) {
            if (isVisible(child, needle, ignored)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesText(File file, String needle) {
        return needle.isEmpty()
                || file.getName().toLowerCase(Locale.ROOT).contains(needle);
    }

    private static File[] sortedChildren(File dir) {
        File[] children = dir.listFiles();
        if (children == null) {
            return new File[0];
        }
        java.util.Arrays.sort(children, Comparator
                .comparing((File f) -> !f.isDirectory())
                .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));
        return children;
    }

    private TreePath findPath(DefaultMutableTreeNode node, File target) {
        if (node.getUserObject() instanceof File f && f.equals(target)) {
            return new TreePath(node.getPath());
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            TreePath found = findPath((DefaultMutableTreeNode) node.getChildAt(i), target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private String currentFilter() {
        String text = filterField.getText();
        return text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
    }

    private void applyFilter() {
        rebuild();
    }

    /** true si el nombre lleva una de las extensiones admitidas. */
    private static boolean isSourceFile(File file) {
        return LanguageCompilerFactory.hasAllowedExtension(file.getName());
    }

    // ====================== Renderer ======================

    /** Dibura nombre limpio, icono segun tipo y marca de archivo fuente. */
    private static class FileCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                      boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            setFont(UiTheme.sans(13));
            if (!(value instanceof DefaultMutableTreeNode node)) {
                return this;
            }
            Object userObject = node.getUserObject();
            if (userObject instanceof File file) {
                if (file.isDirectory()) {
                    setIcon(expanded ? IdeIcons.folderOpen() : IdeIcons.folder());
                    setText(file.getName().isEmpty() ? file.getPath() : file.getName());
                } else {
                    setIcon(TabHeader.fileIcon(
                            LanguageCompilerFactory.detectLanguageId(file.getName())));
                    setText(file.getName());
                }
                setToolTipText(file.getAbsolutePath());
            } else {
                setIcon(null);
                setText(String.valueOf(userObject));
            }
            if (!selected) {
                setBackground(UiTheme.toolWindowBg());
                setForeground(UiTheme.fg());
            }
            return this;
        }
    }

    // ====================== Eventos ======================

    /** Engancha seleccion por doble clic y seleccion simple de carpetas. */
    public void initSelectionBehavior() {
        tree.addTreeSelectionListener(e -> {
            Object last = e.getPath() == null ? null : e.getPath().getLastPathComponent();
            if (last instanceof DefaultMutableTreeNode node
                    && node.getUserObject() instanceof File file && file.isDirectory()
                    && onDirectorySelected != null) {
                onDirectorySelected.accept(file);
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2 || !javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {
                    return;
                }
                Object obj = path.getLastPathComponent();
                if (obj instanceof DefaultMutableTreeNode node
                        && node.getUserObject() instanceof File file && file.isFile()
                        && isSourceFile(file) && onFileSelected != null) {
                    onFileSelected.accept(file);
                }
            }
        });
    }
}
