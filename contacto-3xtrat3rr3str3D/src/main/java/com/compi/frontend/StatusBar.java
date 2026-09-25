package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Barra de estado inferior, al estilo de la de IntelliJ.
 *
 * Muestra posicion del cursor, pestana activa, lenguaje, resultado de la
 * compilacion y contadores de instrucciones y errores.
 */
public class StatusBar extends JPanel {

    private final JLabel positionLabel = label("Ln 1, Col 1");
    private final JLabel fileLabel = label("Sin archivo");
    private final JLabel languageLabel = label("Lenguaje: -");
    private final JLabel resultLabel = label("Listo");
    private final JLabel countersLabel = label("Cuartetas: 0   Tripletes: 0");
    private final JLabel errorsLabel = label("0 errores");

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                UiTheme.hairlineTop(),
                UiTheme.pad(4, 10, 4, 10)));

        add(positionLabel, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(countersLabel);
        right.add(errorsLabel);
        right.add(languageLabel);
        right.add(resultLabel);
        add(right, BorderLayout.EAST);

        add(fileLabel, BorderLayout.CENTER);
    }

    private static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.sans(11));
        l.setForeground(UiTheme.dim());
        return l;
    }

    // ====================== Actualizaciones ======================

    public void setPosition(int line, int column) {
        positionLabel.setText("Ln " + line + ", Col " + column);
    }

    public void setFileName(String name) {
        fileLabel.setText(name == null || name.isBlank() ? "Sin archivo" : name);
    }

    public void setLanguage(String languageId) {
        languageLabel.setText("Lenguaje: " + UiTheme.prettifyLanguage(languageId));
    }

    /** Resultado de la ultima compilacion. */
    public void setResult(boolean success, int errorCount) {
        resultLabel.setText(success ? "Compilación correcta" : "Compilación con errores");
        resultLabel.setForeground(success ? UiTheme.success() : UiTheme.error());
        errorsLabel.setText(errorCount + (errorCount == 1 ? " error" : " errores"));
        errorsLabel.setForeground(errorCount == 0 ? UiTheme.dim() : UiTheme.error());
    }

    public void setCounts(int quadruples, int triplets) {
        countersLabel.setText("Cuartetas: " + quadruples + "   Tripletes: " + triplets);
    }

    public void setIdle() {
        resultLabel.setText("Listo");
        resultLabel.setForeground(UiTheme.dim());
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        return new Dimension(d.width, 28);
    }
}
