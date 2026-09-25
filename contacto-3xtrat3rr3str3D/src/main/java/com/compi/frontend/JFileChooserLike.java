package com.compi.frontend;

import java.io.File;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Utilidades de dialogo para elegir archivos.
 *
 * Se extraen para que los paneles no repitan el mismo bloque de JFileChooser.
 */
final class JFileChooserLike {

    private JFileChooserLike() {
    }

    /**
     * Pide un destino PNG y entrega la ruta elegida al consumidor.
     */
    static void choosePng(java.awt.Component parent, String defaultName, Consumer<java.nio.file.Path> onChosen) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar imagen");
        chooser.setFileFilter(new FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        chooser.setSelectedFile(new File(defaultName));
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }
            onChosen.accept(file.toPath());
        }
    }

    /** Pide un destino de texto y entrega la ruta elegida. */
    static void chooseText(java.awt.Component parent, String defaultName, Consumer<java.nio.file.Path> onChosen) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar texto");
        chooser.setFileFilter(new FileNameExtensionFilter("Texto (*.txt)", "txt"));
        chooser.setSelectedFile(new File(defaultName));
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            onChosen.accept(file.toPath());
        }
    }

    /** Muestra un error de forma uniforme. */
    static void error(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(parent), message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
