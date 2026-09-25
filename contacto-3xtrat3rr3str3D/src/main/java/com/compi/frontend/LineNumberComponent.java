package com.compi.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Componente de encabezado que dibuja los numeros de linea de un editor.
 *
 * Se recalcula el ancho cada vez que el documento cambia para que las cifras
 * largas no se corten nunca.
 */
public class LineNumberComponent extends JComponent {

    private static final int MARGIN = 10;
    private static final int MIN_DIGITS = 3;

    private final JTextComponent textArea;

    public LineNumberComponent(JTextComponent textArea) {
        this.textArea = textArea;
        applyTheme();

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                revalidate();
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                revalidate();
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    /** Aplica los colores y la fuente del tema actual. */
    public void applyTheme() {
        setFont(UiTheme.mono(13));
        setForeground(UiTheme.dim());
        setBackground(UiTheme.editorBg());
        setOpaque(true);
    }

    private int lineCount() {
        return textArea.getDocument().getDefaultRootElement().getElementCount();
    }

    private int digitCount() {
        return Math.max(MIN_DIGITS, String.valueOf(Math.max(1, lineCount())).length());
    }

    private int preferredWidth() {
        FontMetrics fm = getFontMetrics(getFont());
        return MARGIN * 2 + Math.max(24, fm.charWidth('0') * digitCount());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(preferredWidth(), Math.max(16, textArea.getHeight()));
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(preferredWidth(), 16);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground() == null ? UiTheme.editorBg() : getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont());
        g2.setColor(getForeground());

        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();
        if (lineHeight <= 0) {
            g2.dispose();
            return;
        }

        int total = lineCount();
        Rectangle clip = g2.getClipBounds();
        int firstLine = 1;
        int lastLine = total;
        if (clip != null) {
            firstLine = Math.max(1, 1 + clip.y / lineHeight);
            lastLine = Math.min(total, 1 + (clip.y + clip.height) / lineHeight);
        }

        for (int i = firstLine; i <= lastLine; i++) {
            String number = String.valueOf(i);
            int x = getWidth() - MARGIN - fm.stringWidth(number);
            int y = i * lineHeight;
            g2.drawString(number, Math.max(2, x), y + fm.getAscent());
        }
        g2.dispose();
    }
}
