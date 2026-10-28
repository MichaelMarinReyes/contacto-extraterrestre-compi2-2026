package com.compi.frontend;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class FileTreePanelTest {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @TempDir Path directory;

    @Test
    void assemblyFileOffersRunBeforeDeleteAndInvokesItsCallback() throws Exception {
        File assembly = Files.writeString(directory.resolve("programa.s"), ".text\nmain:\n").toFile();
        FileTreePanel panel = new FileTreePanel();
        AtomicReference<File> selected = new AtomicReference<>();
        panel.setOnAssemblyRun(selected::set);

        JPopupMenu menu = panel.contextMenuFor(assembly);
        assertEquals("Ejecutar", ((JMenuItem) menu.getComponent(0)).getText());
        assertEquals("Eliminar", ((JMenuItem) menu.getComponent(2)).getText());
        ((JMenuItem) menu.getComponent(0)).doClick();
        assertEquals(assembly, selected.get());
    }

    @Test
    void nonAssemblyFileOnlyOffersDelete() throws Exception {
        File source = Files.writeString(directory.resolve("programa.pig"), "MAIOR> FINIS;").toFile();
        FileTreePanel panel = new FileTreePanel();

        JPopupMenu menu = panel.contextMenuFor(source);
        assertEquals(1, menu.getComponentCount());
        assertEquals("Eliminar", ((JMenuItem) menu.getComponent(0)).getText());
        assertFalse(FileTreePanel.isAssemblyFile(source));
    }
}
