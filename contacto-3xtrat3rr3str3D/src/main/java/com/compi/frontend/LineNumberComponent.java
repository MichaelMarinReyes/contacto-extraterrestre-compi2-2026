package com.compi.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author michael
 */
public class LineNumberComponent extends JPanel {

    private final JTextPane textArea;

    public LineNumberComponent(JTextPane textArea) {
        this.textArea = textArea;
        setBackground(new Color(230, 230, 230));
        setForeground(new Color(120, 120, 120));

        updatePreferredSize();

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePreferredSize();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePreferredSize();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePreferredSize();
            }
        });

        textArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePreferredSize();
            }
        });
    }

    private void updatePreferredSize() {
        int height = Math.max(textArea.getHeight(), textArea.getPreferredSize().height);
        setPreferredSize(new Dimension(45, height));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(textArea.getFont());
        java.awt.FontMetrics fm = g.getFontMetrics();

        try {
            int height = fm.getHeight();
            int lines = textArea.getDocument().getDefaultRootElement().getElementCount();

            int y = fm.getAscent();
            for (int i = 0; i < lines; i++) {
                String lineNumStr = String.valueOf(i + 1);
                g.drawString(lineNumStr, getWidth() - fm.stringWidth(lineNumStr) - 10, y);
                y += height;
            }
        } catch (Exception e) {
            // Manejo de excepciones
        }
    }
}