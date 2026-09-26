package com.compi.frontend;

import com.compi.backend.languages2.LanguageCompilerFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Dialogo de archivo nuevo: nombre, lenguaje y carpeta de destino.
 *
 * <p>Antes se preguntaba la extension en un desplegable y la carpeta en otro
 * dialogo aparte, y el nombre lo ponia el programa: no habia forma de llamarlo
 * como uno quiere ni de decidir en que carpeta del proyecto nace. Aqui se
 * preguntan las tres cosas a la vez, con las carpetas del proyecto ya cargadas en
 * el desplegable y una vista previa del nombre completo.</p>
 */
public class NewFileDialog extends javax.swing.JDialog {

    /** Lo que el usuario eligio, o null si se cancelo. */
    public record Choice(String baseName, String languageId, File directory) {
    }

    /** Marca el desplegable de carpetas para abrir el selector del sistema. */
    private static final String OTHER_FOLDER = "… otra carpeta…";

    private final JTextField nameField = new JTextField(18);
    private final JComboBox<String> extensionBox = new JComboBox<>();
    private final JComboBox<String> folderBox = new JComboBox<>();
    private final List<File> folders = new ArrayList<>();
    private final List<String> extensions = new ArrayList<>();
    private final JLabel preview = new JLabel();
    private final Component owner;
    private final File projectRoot;
    private Choice choice;

    /**
     * Monta el dialogo.
     *
     * @param owner         componente padre
     * @param projectRoot   raiz del proyecto, o null si no hay ninguna
     * @param folderChoices carpetas a ofrecer (la raiz ya incluida)
     * @param languageId    lenguaje que se propone, por ser el de la pestana activa
     * @param suggestedName nombre que se propone
     */
    public NewFileDialog(Component owner, File projectRoot, List<File> folderChoices,
                          String languageId, String suggestedName) {
        super(padreDe(owner), "Archivo nuevo", ModalityType.APPLICATION_MODAL);
        this.owner = owner;
        this.projectRoot = projectRoot;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        folders.addAll(folderChoices);
        folderBox.setModel(new DefaultComboBoxModel<>(folderLabels()));
        folderBox.setSelectedIndex(0);

        extensions.addAll(LanguageCompilerFactory.allowedExtensions());
        List<String> extLabels = new ArrayList<>(extensions.size());
        for (String ext : extensions) {
            extLabels.add("*." + ext + "  (" + UiTheme.prettifyLanguage(
                    LanguageCompilerFactory.byExtensionLanguage(ext)) + ")");
        }
        extensionBox.setModel(new DefaultComboBoxModel<>(extLabels.toArray(new String[0])));
        extensionBox.setSelectedIndex(Math.max(0, indexOfLanguage(languageId)));

        nameField.setText(suggestedName);
        nameField.selectAll();
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updatePreview();
            }

            public void removeUpdate(DocumentEvent e) {
                updatePreview();
            }

            public void changedUpdate(DocumentEvent e) {
                updatePreview();
            }
        });
        folderBox.addActionListener(e -> onFolderPicked());
        extensionBox.addActionListener(e -> updatePreview());

        setContentPane(buildContent());
        JButton crear = findButton(getContentPane(), "Crear");
        if (crear != null) {
            getRootPane().setDefaultButton(crear);
        }
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        pack();
        setMinimumSize(new Dimension(500, getPreferredSize().height));
        setLocationRelativeTo(owner);
        updatePreview();
    }

    /**
     * Ventana a la que colgarse.
     *
     * <p>{@code getWindowAncestor} sube por los contenedores, y un JFrame no es
     * ancestro de si mismo: si el que se pasa ya es una ventana, se usa
     * directamente. Sin esto el dialogo salia sin padre y sin icono, y con el
     * icono de la aplicacion al lado.</p>
     */
    private static java.awt.Window padreDe(Component owner) {
        if (owner instanceof java.awt.Window window) {
            return window;
        }
        return SwingUtilities.getWindowAncestor(owner);
    }

    private String[] folderLabels() {        List<String> labels = new ArrayList<>(folders.size() + 1);
        for (File dir : folders) {
            labels.add(relativeLabel(dir));
        }
        labels.add(OTHER_FOLDER);
        return labels.toArray(new String[0]);
    }

    private JPanel buildContent() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 4, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;

        gbc.gridy = 0;
        form.add(caption("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(caption("Lenguaje:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(extensionBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(caption("Carpeta:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(folderBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 0, 0);
        preview.setFont(UiTheme.sans(11));
        preview.setForeground(UiTheme.dim());
        form.add(preview, gbc);

        JButton crear = new JButton("Crear");
        crear.addActionListener(e -> accept());
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 16, 14, 16));
        buttons.add(crear, BorderLayout.WEST);
        buttons.add(cancelar, BorderLayout.EAST);

        JPanel root = new JPanel(new BorderLayout());
        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        return root;
    }

    /**
     * Al elegir "otra carpeta" se abre el selector y la carpeta encontrada se
     * añade al desplegable, para no perder lo ya escrito en el nombre.
     */
    private void onFolderPicked() {
        if (!OTHER_FOLDER.equals(folderBox.getSelectedItem())) {
            return;
        }
        File chosen = pickFolder(selectedFolder() != null ? selectedFolder() : projectRoot);
        if (chosen == null) {
            // Volver a la anterior: sin carpeta nueva no se puede seguir.
            folderBox.setSelectedIndex(0);
            return;
        }
        folders.add(chosen);
        folderBox.setModel(new DefaultComboBoxModel<>(folderLabels()));
        folderBox.setSelectedIndex(folders.size() - 1);
        updatePreview();
    }

    private void accept() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            preview.setText("Escribe un nombre para el archivo.");
            nameField.requestFocusInWindow();
            return;
        }
        File dir = selectedFolder();
        if (dir == null) {
            return;
        }
        choice = new Choice(name,
                LanguageCompilerFactory.byExtensionLanguage(selectedExtension()), dir);
        dispose();
    }

    /**
     * Muestra el dialogo y espera a que se cierre.
     *
     * @return la eleccion, o null si se cancelo
     */
    public Choice ask() {
        setVisible(true);
        return choice;
    }

    // ====================== Detalles de la vista ======================

    private static JLabel caption(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.sans(12));
        return l;
    }

    private static JButton findButton(Component parent, String text) {
        if (parent instanceof JButton b && text.equals(b.getText())) {
            return b;
        }
        if (parent instanceof Container c) {
            for (Component kid : c.getComponents()) {
                JButton found = findButton(kid, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /** Ruta de la carpeta relativa a la raiz del proyecto, para que se lea corta. */
    private String relativeLabel(File dir) {
        if (projectRoot == null || dir == null) {
            return dir == null ? "" : dir.getAbsolutePath();
        }
        String base = projectRoot.getAbsolutePath();
        String path = dir.getAbsolutePath();
        if (path.equals(base)) {
            return "." + File.separator;
        }
        return path.startsWith(base + File.separator)
                ? path.substring(base.length() + 1) : path;
    }

    private int indexOfLanguage(String languageId) {
        if (languageId != null) {
            String ext = LanguageCompilerFactory.extensionOfLanguage(languageId);
            int index = extensions.indexOf(ext);
            if (index >= 0) {
                return index;
            }
        }
        return 0;
    }

    private String selectedExtension() {
        int index = extensionBox.getSelectedIndex();
        return extensions.get(Math.max(0, Math.min(index, extensions.size() - 1)));
    }

    private File selectedFolder() {
        int index = folderBox.getSelectedIndex();
        return index < 0 || index >= folders.size() ? null : folders.get(index);
    }

    /** Muestra como se va a llamar el archivo, para que no haya sorpresas. */
    private void updatePreview() {
        String base = nameField.getText().trim();
        File dir = selectedFolder();
        if (base.isEmpty()) {
            preview.setText("Escribe un nombre para el archivo.");
            return;
        }
        String ext = selectedExtension();
        String full = LanguageCompilerFactory.extensionOf(base) != null ? base : base + "." + ext;
        preview.setText("Se creará:  "
                + (dir == null ? full : dir.getAbsolutePath() + File.separator + full));
    }

    private File pickFolder(File startDir) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Carpeta del archivo nuevo");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Usar esta carpeta");
        chooser.setCurrentDirectory(startDir != null && startDir.isDirectory()
                ? startDir : new File(System.getProperty("user.home", ".")));
        if (chooser.showOpenDialog(owner) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File dir = chooser.getSelectedFile();
        if (dir == null) {
            return null;
        }
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        return dir;
    }
}
