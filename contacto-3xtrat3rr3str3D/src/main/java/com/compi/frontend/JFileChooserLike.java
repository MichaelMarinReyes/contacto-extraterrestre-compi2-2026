package com.compi.frontend;

import java.io.File;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

final class JFileChooserLike {

    private JFileChooserLike() {
    }

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

    static void error(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(parent), message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
