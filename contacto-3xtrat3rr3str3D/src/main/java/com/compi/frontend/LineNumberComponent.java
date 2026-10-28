package com.compi.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

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

        Rectangle clip = g2.getClipBounds();
        Element root = textArea.getDocument().getDefaultRootElement();
        int first = 0;
        int last = root.getElementCount() - 1;
        if (clip != null && textArea.getWidth() > 0 && textArea.getHeight() > 0) {
            first = root.getElementIndex(textArea.viewToModel2D(new Point(0, Math.max(0, clip.y))));
            last = root.getElementIndex(textArea.viewToModel2D(
                    new Point(0, Math.min(textArea.getHeight(), clip.y + clip.height))));
        }

        FontMetrics fm = g2.getFontMetrics();
        for (int index = Math.max(0, first); index <= Math.min(last, root.getElementCount() - 1); index++) {
            try {
                Rectangle2D row = textArea.modelToView2D(root.getElement(index).getStartOffset());
                if (row == null) continue;
                String number = String.valueOf(index + 1);
                int x = getWidth() - MARGIN - fm.stringWidth(number);
                float baseline = (float) (row.getY()
                        + Math.max(0, (row.getHeight() - fm.getHeight()) / 2.0)
                        + fm.getAscent());
                g2.drawString(number, Math.max(2, x), baseline);
            } catch (BadLocationException ignored) {
                // El documento pudo cambiar entre el cálculo del rango y el pintado.
            }
        }
        g2.dispose();
    }
}
