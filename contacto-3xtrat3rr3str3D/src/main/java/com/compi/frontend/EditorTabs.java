package com.compi.frontend;

import com.compi.backend.languages2.LanguageCompilerFactory;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Area de trabajo multi-pestana.
 *
 * Cada archivo abierto vive en su propia {@link EditorPanel} y se representa con
 * un {@link TabHeader} que incluye el boton de cierre. Al estilo de IntelliJ las
 * pestanas se desplazan en horizontal cuando no caben.
 */
public class EditorTabs extends JTabbedPane {

    private final Map<File, EditorPanel> open = new LinkedHashMap<>();
    private final Map<EditorPanel, TabHeader> headers = new LinkedHashMap<>();
    /** Clave con la que cada editor entro al mapa (puede cambiar al guardar). */
    private final Map<EditorPanel, File> keys = new LinkedHashMap<>();
    private final List<File> untitled = new ArrayList<>();

    private int untitledCounter = 0;

    private final Consumer<EditorPanel> onActiveChanged;
    private final Consumer<File> onFileClosed;

    public EditorTabs(Consumer<EditorPanel> onActiveChanged, Consumer<File> onFileClosed) {
        this.onActiveChanged = onActiveChanged;
        this.onFileClosed = onFileClosed;

        setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
        setFont(UiTheme.sans(13));
        addChangeListener(e -> {
            EditorPanel active = getActiveEditor();
            if (active != null && onActiveChanged != null) {
                onActiveChanged.accept(active);
            }
        });
    }

    // ====================== Apertura ======================

    /**
     * Abre un archivo existente, reutilizando la pestana si ya estaba abierto.
     *
     * <p>El lenguaje se deduce siempre de la extension; un archivo sin
     * extension admitida se rechaza para que el arbol y el selector de archivos
     * sean la unica puerta de entrada.</p>
     *
     * @return el editor del archivo, o null si no se pudo leer o la extension
     *         no esta admitida
     */
    public EditorPanel openFile(File file) {
        String lang = LanguageCompilerFactory.detectLanguageId(file.getName());
        if (lang == null) {
            return null;
        }
        EditorPanel existing = open.get(file);
        if (existing != null) {
            setSelectedComponent(existing);
            return existing;
        }
        String content;
        try {
            content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
        EditorPanel editor = new EditorPanel(this::onCaretMoved, () -> onEditorModified());
        editor.setFile(file);
        editor.setLanguageId(lang);
        editor.setCodeText(content);
        addTab(editor);
        return editor;
    }

    /**
     * Crea una pestana vacia cuyo nombre ya lleva la extension del lenguaje
     * ("archivoN.pig", "archivoN.y", "archivoN.z").
     */
    public EditorPanel newFile(String languageId) {
        return newFile(languageId, null);
    }

    /**
     * Crea un archivo de verdad dentro de la carpeta contenedora y lo abre.
     *
     * <p>El archivo se escribe vacio en disco para que aparezca en el arbol de
     * proyectos y no quede como una pestana sin ruta. Si el nombre ya existe se
     * añade un sufijo numerico.</p>
     *
     * @param dir carpeta donde crear el archivo, o null para dejarlo sin ruta
     * @return el editor del archivo nuevo, o null si no se pudo crear
     */
    public EditorPanel newFile(String languageId, File dir) {
        return newFile(languageId, dir, null);
    }

    /**
     * Crea el archivo con el nombre que el usuario escribio.
     *
     * <p>Si el nombre ya lleva la extension no se repite. Si el archivo existe se
     * avisa con un sufijo numerico, para no pisar el trabajo anterior.</p>
     *
     * @param baseName nombre sin extension; null o vacio para el "archivoN" automatico
     * @return el editor del archivo nuevo, o null si no se pudo crear
     */
    public EditorPanel newFile(String languageId, File dir, String baseName) {
        String lang = languageId == null ? "pig" : languageId;
        String ext = LanguageCompilerFactory.extensionOfLanguage(lang);
        if (ext == null) {
            ext = "pig";
            lang = "pig";
        }

        String name = sanitizeName(baseName);
        File file;
        if (name == null) {
            file = dir == null
                    ? new File("archivo" + (untitledCounter + 1) + "." + ext)
                    : uniqueName(dir, ext);
        } else {
            file = dir == null ? new File(name + "." + ext) : numbered(new File(dir, name + "." + ext));
        }
        untitledCounter++;

        if (dir != null && !file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

        EditorPanel editor = new EditorPanel(this::onCaretMoved, () -> onEditorModified());
        editor.setFile(file);
        editor.setLanguageId(lang);
        editor.setCodeText("");
        if (dir == null) {
            untitled.add(file);
        }
        addTab(editor);
        return editor;
    }

    /**
     * Limpia el nombre escrito por el usuario.
     *
     * @return el nombre sin extension ni caracteres prohibidos, o null si no queda
     *         nada utilizable
     */
    private static String sanitizeName(String raw) {
        if (raw == null) {
            return null;
        }
        String name = raw.trim();
        if (name.isEmpty()) {
            return null;
        }
        // Se quita la extension si el usuario ya la escribio, para no acabar en
        // "saludo.pig.pig".
        String ext = LanguageCompilerFactory.extensionOf(name);
        if (ext != null) {
            name = name.substring(0, name.length() - ext.length() - 1).trim();
        }
        // Caracteres que no valen en un nombre de archivo. Se cambian por guion en
        // vez de rechazarlos, para no fallarle el nombre al usuario por un caracter.
        name = name.replaceAll("[\\\\/:*?\"<>|]", "-").trim();
        // Un nombre que sea solo puntos subiria de directorio o se perderia.
        while (name.startsWith(".")) {
            name = name.substring(1).trim();
        }
        return name.isEmpty() ? null : name;
    }

    /** El mismo nombre con un sufijo numerico, si ya hay un archivo con el. */
    private static File numbered(File wanted) {
        if (!wanted.exists()) {
            return wanted;
        }
        String base = wanted.getName();
        int dot = base.lastIndexOf('.');
        String stem = dot > 0 ? base.substring(0, dot) : base;
        String ext = dot > 0 ? base.substring(dot) : "";
        for (int i = 2; i < 10_000; i++) {
            File candidate = new File(wanted.getParentFile(), stem + "-" + i + ext);
            if (!candidate.exists()) {
                return candidate;
            }
        }
        return new File(wanted.getParentFile(), stem + "-" + System.nanoTime() + ext);
    }

    /** Primer "archivoN.<ext>" libre dentro de la carpeta. */
    private static File uniqueName(File dir, String ext) {
        for (int i = 1; i < 10_000; i++) {
            File candidate = new File(dir, "archivo" + i + "." + ext);
            if (!candidate.exists()) {
                return candidate;
            }
        }
        return new File(dir, "archivo." + ext);
    }

    private void addTab(EditorPanel editor) {
        TabHeader header = new TabHeader(editor, () -> close(editor));
        open.put(editor.getFile(), editor);
        keys.put(editor, editor.getFile());
        headers.put(editor, header);
        addTab(header.getName(), editor);
        setTabComponentAt(getTabCount() - 1, header);
        setSelectedComponent(editor);
        // Notifica al contenedor para que actualice el arbol y el estado.
        firePropertyChange("tabOpened", null, editor);
    }

    // ====================== Cierre ======================

    /**
     * Cierra la pestana del editor. Intenta guardar antes si hay cambios.
     *
     * @return true si la pestana se cerro
     */
    public boolean close(EditorPanel editor) {
        if (editor == null) {
            return false;
        }
        if (editor.isModified()) {
            int answer = javax.swing.JOptionPane.showConfirmDialog(this,
                    "\"" + editor.getDisplayName() + "\" tiene cambios sin guardar. ¿Guardar?",
                    "Guardar cambios", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
            if (answer == javax.swing.JOptionPane.CANCEL_OPTION
                    || answer == javax.swing.JOptionPane.CLOSED_OPTION) {
                return false;
            }
            if (answer == javax.swing.JOptionPane.YES_OPTION && !save(editor)) {
                return false;
            }
        }
        doClose(editor);
        return true;
    }

    /** Cierra sin preguntar (usado tras guardar). */
    public void doClose(EditorPanel editor) {
        if (editor == null) {
            return;
        }
        int index = indexOfComponent(editor);
        File key = keys.remove(editor);
        if (key != null) {
            open.remove(key);
            untitled.remove(key);
        }
        headers.remove(editor);
        if (index >= 0) {
            removeTabAt(index);
        }
        if (onFileClosed != null) {
            onFileClosed.accept(editor.getFile());
        }
        firePropertyChange("tabClosed", null, editor);
    }

    /** Cierra la pestana actualmente seleccionada. */
    public boolean closeActive() {
        return close(getActiveEditor());
    }

    /**
     * Guarda el editor. Si es un archivo sin ruta, pregunta por la ubicacion.
     *
     * @return true si se guardo
     */
    public boolean save(EditorPanel editor) {
        if (editor == null) {
            return false;
        }
        File file = editor.getFile();
        boolean isReal = file != null && file.getParentFile() != null;
        File oldKey = keys.get(editor);
        if (!isReal) {
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Guardar como");
            String ext = extensionFor(editor.getLanguageId());
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    UiTheme.prettifyLanguage(editor.getLanguageId()) + " (*" + ext + ")", ext));
            if (chooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
                return false;
            }
            File chosen = chooser.getSelectedFile();
            if (!chosen.getName().toLowerCase().endsWith(ext)) {
                chosen = new File(chosen.getParentFile(), chosen.getName() + ext);
            }
            editor.setFile(chosen);
            file = chosen;
        }
        try {
            Files.writeString(file.toPath(), editor.getCodeText(), StandardCharsets.UTF_8);
            editor.setModified(false);
            // Reindexa con la ruta definitiva tras un "guardar como".
            if (!file.equals(oldKey)) {
                if (oldKey != null) {
                    open.remove(oldKey);
                    untitled.remove(oldKey);
                }
                open.put(file, editor);
                keys.put(editor, file);
            }
            TabHeader header = headers.get(editor);
            if (header != null) {
                header.setName(file.getName());
                header.setDirty(false);
            }
            firePropertyChange("tabSaved", null, editor);
            return true;
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se pudo guardar:\n" + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ====================== Consultas ======================

    public EditorPanel getActiveEditor() {
        return getSelectedComponent() instanceof EditorPanel e ? e : null;
    }

    public EditorPanel getEditorFor(File file) {
        return open.get(file);
    }

    public List<EditorPanel> getAllEditors() {
        return new ArrayList<>(open.values());
    }

    public List<File> getOpenFiles() {
        return new ArrayList<>(open.keySet());
    }

    public int getOpenCount() {
        return open.size();
    }

    public boolean isEmpty() {
        return open.isEmpty();
    }

    public void selectEditor(EditorPanel editor) {
        if (editor != null) {
            setSelectedComponent(editor);
        }
    }

    /** Guarda todos los editors modificados. Devuelve false si alguno falla. */
    public boolean saveAll() {
        boolean ok = true;
        for (EditorPanel editor : getAllEditors()) {
            if (editor.isModified() && !save(editor)) {
                ok = false;
            }
        }
        return ok;
    }

    /** Cierra todos los editors modificados. */
    public boolean closeAll() {
        List<EditorPanel> snapshot = getAllEditors();
        for (EditorPanel editor : snapshot) {
            if (!close(editor)) {
                return false;
            }
        }
        return true;
    }

    // ====================== Interno ======================

    private void onCaretMoved(EditorPanel editor) {
        firePropertyChange("caretMoved", null, editor);
    }

    private void onEditorModified() {
        EditorPanel editor = getActiveEditor();
        if (editor == null) {
            return;
        }
        TabHeader header = headers.get(editor);
        if (header != null) {
            header.setDirty(true);
        }
        firePropertyChange("tabModified", null, editor);
    }

    /** Extension de archivo que corresponde al lenguaje de un editor. */
    private static String extensionFor(String languageId) {
        String ext = LanguageCompilerFactory.extensionOfLanguage(languageId);
        return ext == null ? "pig" : ext;
    }

    /** Refresca los encabezados (por ejemplo, tras cambiar de lenguaje). */
    public void refreshHeaders() {
        for (Map.Entry<EditorPanel, TabHeader> entry : headers.entrySet()) {
            entry.getValue().setLanguageIcon(entry.getKey().getLanguageId());
            entry.getValue().setDirty(entry.getKey().isModified());
        }
        SwingUtilities.invokeLater(() -> {
        });
    }
}
