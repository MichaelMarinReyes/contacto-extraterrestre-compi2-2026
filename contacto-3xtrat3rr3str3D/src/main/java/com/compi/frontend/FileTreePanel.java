package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
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
 */
public class FileTreePanel extends JPanel {

    private static final String[] SOURCE_EXTENSIONS = {"pig", "lat", "y", "z"};

    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    private final DefaultTreeModel model;
    private final JLabel titleLabel = new JLabel("PROYECTO");
    private final JTextField filterField = new JTextField();
    private final JPanel emptyHint = new JPanel(new BorderLayout());

    private File root;
    private Consumer<File> onFileSelected;
    private Consumer<File> onDirectorySelected;
    private Runnable onDirectoryChanged;

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
        JLabel hint = new JLabel(
                "<html><div style='text-align:center;padding:16px'>"
                        + "Selecciona una carpeta con el boton<br/>de la cabecera para ver<br/>sus archivos fuente</div></html>",
                JLabel.CENTER);
        hint.setFont(UiTheme.sans(12));
        hint.setForeground(UiTheme.dim());
        emptyHint.add(hint, BorderLayout.CENTER);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(UiTheme.toolWindowBg());
        holder.add(scroll, BorderLayout.CENTER);
        holder.add(emptyHint, BorderLayout.CENTER);
        return holder;
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

    /** Pide al usuario una carpeta y la carga como proyecto. */
    public void askForDirectory() {
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
        rootNode.removeAllChildren();
        if (root != null && root.isDirectory()) {
            for (File child : sortedChildren(root)) {
                if (keep(child, needle)) {
                    addNode(rootNode, child, needle);
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
        emptyHint.setVisible(rootNode.getChildCount() == 0);
        if (onDirectoryChanged != null) {
            onDirectoryChanged.run();
        }
    }

    private void addNode(DefaultMutableTreeNode parent, File file, String needle) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        parent.add(node);
        if (file.isDirectory()) {
            for (File child : sortedChildren(file)) {
                if (keep(child, needle)) {
                    addNode(node, child, needle);
                }
            }
        }
    }

    /** true si el archivo (o algun descendiente suyo) encaja con el filtro. */
    private boolean keep(File file, String needle) {
        if (needle.isEmpty()) {
            return true;
        }
        if (file.getName().toLowerCase(Locale.ROOT).contains(needle)) {
            return true;
        }
        if (file.isDirectory()) {
            for (File child : sortedChildren(file)) {
                if (keep(child, needle)) {
                    return true;
                }
            }
        }
        return false;
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

    private static boolean isSourceFile(File file) {
        String name = file.getName().toLowerCase(Locale.ROOT);
        for (String ext : SOURCE_EXTENSIONS) {
            if (name.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
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
                    setIcon(TabHeader.fileIcon(languageOf(file.getName())));
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

        private static String languageOf(String name) {
            String lower = name.toLowerCase();
            if (lower.endsWith(".y")) {
                return "y";
            }
            if (lower.endsWith(".z")) {
                return "zet";
            }
            return "pig";
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
