package com.compi.frontend;

import com.compi.backend.languages.LanguageCompilerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;

/** Gestiona archivos y cambios de proyecto del área de trabajo. */
final class WorkspaceActions {
    private final MainWindow window;
    private File lastContainer;

    WorkspaceActions(MainWindow window) {
        this.window = window;
    }

    File currentContainer() {
        File root = window.getFileTree().getRoot();
        return root != null && root.isDirectory() ? root : null;
    }

    File lastContainer() {
        return lastContainer;
    }

    void newFile() {
        File container = ensureContainer("archivo nuevo", null);
        if (container == null) return;
        NewFileDialog.Choice choice = window.promptNewFile(container);
        if (choice == null) return;
        EditorPanel editor = window.getEditorTabs().newFile(choice.languageId(),
                choice.directory(), choice.baseName());
        if (editor == null) {
            window.promptInfo("No se pudo crear el archivo en:\n" + choice.directory().getAbsolutePath(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return;
        }
        revealNewEditor(editor);
        window.navText("Nuevo archivo / " + editor.getDisplayName());
    }

    void newFileIn(String languageId) {
        File container = ensureContainer("archivo nuevo", null);
        if (container == null) return;
        EditorPanel editor = window.getEditorTabs().newFile(languageId, container);
        if (editor != null) revealNewEditor(editor);
    }

    private void revealNewEditor(EditorPanel editor) {
        window.getFileTree().reload();
        window.getFileTree().selectFile(editor.getFile());
        window.showTabs();
        window.onActiveEditorChanged(editor);
    }

    NewFileDialog buildNewFileDialog(File container) {
        List<File> folders = window.getFileTree().codeFolders();
        if (folders.isEmpty()) folders = List.of(container);
        EditorPanel active = window.getEditorTabs().getActiveEditor();
        String languageId = active == null ? null : active.getLanguageId();
        File suggested = active != null && active.getFile() != null
                ? active.getFile().getParentFile() : container;
        int selected = folders.indexOf(suggested);
        List<File> ordered = new ArrayList<>(folders);
        if (selected > 0) ordered.add(0, ordered.remove(selected));
        return new NewFileDialog(window, container, ordered, languageId,
                suggestFileName(languageId, container));
    }

    private String suggestFileName(String languageId, File container) {
        String extension = LanguageCompilerFactory.extensionOfLanguage(languageId);
        if (extension == null) extension = "pig";
        for (int i = 1; i < 10_000; i++) {
            String candidate = "archivo" + i;
            boolean taken = new File(container, candidate + "." + extension).exists();
            if (!taken) {
                for (EditorPanel editor : window.getEditorTabs().getAllEditors()) {
                    if (editor.getDisplayName().equals(candidate + "." + extension)) {
                        taken = true;
                        break;
                    }
                }
            }
            if (!taken) return candidate;
        }
        return "archivo";
    }

    void openFile() {
        File file = window.promptSourceFile();
        if (file == null) return;
        String lower = file.getName().toLowerCase(Locale.ROOT);
        if (!LanguageCompilerFactory.hasAllowedExtension(file.getName())
                && !lower.endsWith(".c") && !lower.endsWith(".s") && !lower.endsWith(".asm")) {
            warnWrongExtension(file);
            return;
        }
        File placed = placeInContainer(file);
        if (placed != null) window.openInEditor(placed);
    }

    File ensureContainer(String purpose, File suggested) {
        File current = currentContainer();
        if (current != null) return current;
        boolean fromSuggestion = suggested != null && suggested.isDirectory();
        File start = fromSuggestion ? suggested : new File(System.getProperty("user.home", "."));
        File selected = fromSuggestion ? start : new File(start, "proyecto");
        File directory = window.promptDirectory("Carpeta contenedora (" + purpose + ")",
                start, selected, "Usar esta carpeta", true);
        return directory != null && window.loadProject(directory, false) ? directory : null;
    }

    boolean loadProject(File directory, boolean announce) {
        File resolved = resolveProjectDirectory(directory);
        if (resolved == null) return false;
        window.getFileTree().loadDirectory(resolved);
        lastContainer = resolved;
        if (announce) announceProject(resolved);
        return true;
    }

    private File resolveProjectDirectory(File directory) {
        if (directory == null) return null;
        File resolved;
        try {
            resolved = directory.getCanonicalFile();
        } catch (IOException e) {
            window.promptInfo("No se pudo resolver la ruta:\n" + directory + "\n\n" + e.getMessage(),
                    "Ruta no válida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!resolved.exists()) {
            window.promptInfo("La carpeta no existe:\n\n" + resolved.getAbsolutePath()
                    + "\n\nRevisa la ruta e inténtalo de nuevo.",
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!resolved.isDirectory()) {
            window.promptInfo("Eso no es una carpeta, es un archivo:\n\n" + resolved.getAbsolutePath()
                    + "\n\nElige una carpeta que contenga tus archivos de código.",
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!resolved.canRead()) {
            window.promptInfo("No hay permiso para leer la carpeta:\n\n" + resolved.getAbsolutePath(),
                    "No se pudo abrir el proyecto", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return resolved;
    }

    private void announceProject(File directory) {
        int sources = window.getFileTree().countSourceFiles();
        List<File> ignored = window.getFileTree().getIgnoredFiles();
        if (sources == 0) {
            window.promptInfo("La carpeta se cargó, pero no contiene archivos de código.\n\n"
                    + directory.getAbsolutePath() + "\n\nExtensiones admitidas: "
                    + LanguageCompilerFactory.extensionPattern()
                    + (ignored.isEmpty() ? "\n\nLa carpeta está vacía."
                    : "\n\nSe ignoraron " + ignored.size() + (ignored.size() == 1
                    ? " archivo por su extensión." : " archivos por su extensión.")),
                    "Proyecto sin archivos de código", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String summary = "Proyecto cargado: " + sources
                + (sources == 1 ? " archivo de código" : " archivos de código");
        if (!ignored.isEmpty()) summary += "\n" + ignored.size() + (ignored.size() == 1
                ? " archivo ignorado por su extensión." : " archivos ignorados por su extensión.");
        window.promptInfo(summary + "\n\n" + directory.getAbsolutePath(),
                "Proyecto abierto", JOptionPane.INFORMATION_MESSAGE);
    }

    void openProject() {
        File start = currentContainer() != null ? currentContainer()
                : lastContainer != null ? lastContainer : new File(System.getProperty("user.home", "."));
        File directory = window.promptDirectory("Abrir carpeta del proyecto", start, null,
                "Abrir proyecto", false);
        if (directory == null) return;
        File resolved = resolveProjectDirectory(directory);
        if (resolved == null || !confirmReplaceWorkspace()) return;
        window.resetWorkspace();
        window.loadProject(resolved, true);
    }

    private boolean confirmReplaceWorkspace() {
        List<EditorPanel> modified = new ArrayList<>();
        for (EditorPanel editor : window.getEditorTabs().getAllEditors()) {
            if (editor.isModified()) modified.add(editor);
        }
        if (modified.isEmpty()) return true;
        String message = modified.size() == 1
                ? "El archivo \"" + modified.get(0).getDisplayName() + "\" tiene cambios sin guardar.\n\n"
                : "Hay " + modified.size() + " archivos con cambios sin guardar.\n\n";
        message += "¿Quieres guardarlos antes de abrir el nuevo proyecto?\n"
                + "Si eliges «No», los cambios se descartarán.";
        int answer = JOptionPane.showConfirmDialog(window, message, "Cambios sin guardar",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.CLOSED_OPTION) return false;
        return answer != JOptionPane.YES_OPTION || window.getEditorTabs().saveAll();
    }

    private File placeInContainer(File file) {
        File project = currentContainer();
        if (project == null || isInside(project, file)) {
            File directory = ensureContainer("archivo abierto", file.getParentFile());
            if (directory == null) return null;
            return isInside(directory, file) ? file : moveToContainer(file, directory);
        }
        return file;
    }

    private File moveToContainer(File file, File directory) {
        int answer = window.promptConfirm("\"" + file.getName() + "\" está fuera del proyecto.\n\n"
                + "¿Moverlo a la carpeta elegida?\n" + directory.getAbsolutePath(), "Mover archivo al proyecto");
        if (answer != JOptionPane.YES_OPTION) {
            File parent = file.getParentFile();
            if (parent != null) {
                window.loadProject(parent, false);
                window.navText("El archivo se mantiene en " + parent.getName() + " porque no se movió al proyecto");
            }
            return file;
        }
        File target = new File(directory, file.getName());
        try {
            Files.move(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            window.loadProject(directory, false);
            return target;
        } catch (IOException e) {
            window.promptInfo("No se pudo mover el archivo:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static boolean isInside(File directory, File file) {
        try {
            String base = directory.getCanonicalPath();
            String child = file.getCanonicalPath();
            return !child.equals(base) && child.startsWith(base + File.separator);
        } catch (IOException e) {
            return false;
        }
    }

    void warnWrongExtension(File file) {
        String name = file == null ? "(sin nombre)" : file.getName();
        window.promptInfo("No se abrió \"" + name + "\".\n\nExtensiones admitidas: "
                + LanguageCompilerFactory.extensionPattern()
                + "\nEl lenguaje de cada archivo lo decide su extensión.",
                "Extensión no admitida", JOptionPane.WARNING_MESSAGE);
    }

    void showIgnoredFiles(List<File> ignored) {
        if (ignored == null || ignored.isEmpty()) return;
        int total = ignored.size();
        int listed = total > 12 ? 6 : total;
        StringBuilder text = new StringBuilder();
        text.append("Estos archivos del proyecto no se abren porque su extensión no está admitida (")
                .append(LanguageCompilerFactory.extensionPattern()).append("):\n\n");
        for (int i = 0; i < listed; i++) {
            File file = ignored.get(i);
            text.append("  • ").append(file.getName()).append("   [")
                    .append(describeExtension(file.getName())).append("]\n");
        }
        if (total > listed) text.append("\n… y ").append(total - listed)
                .append(" más. Si la carpeta es muy amplia, conviene elegir una carpeta que contenga solo tu proyecto.");
        window.promptInfo(text.toString(), total + (total == 1 ? " archivo ignorado" : " archivos ignorados"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static String describeExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot < 0 || dot == name.length() - 1 ? "sin extensión" : "extensión ." + name.substring(dot + 1);
    }

    void save() {
        EditorPanel editor = window.getEditorTabs().getActiveEditor();
        if (editor != null && window.getEditorTabs().save(editor)) {
            window.getFileTree().reload();
            window.getStatusBar().setFileName(editor.getFile().getName());
            window.getConsoleDock().setStatus("Guardado: " + editor.getFile().getAbsolutePath());
        }
    }
}
