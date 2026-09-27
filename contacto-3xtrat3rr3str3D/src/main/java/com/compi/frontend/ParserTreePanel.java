package com.compi.frontend;

import com.compi.frontend.dot.TreeCanvas;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ParserTreePanel extends JPanel {

    private TreeCanvas canvas;
    private JScrollPane scrollPane;
    private JLabel infoLabel;

    public ParserTreePanel() {
        initComponents();
        initCustomComponents();
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }

    private void initCustomComponents() {
        setLayout(new BorderLayout());

        canvas = new TreeCanvas();
        canvas.setBackground(UiTheme.toolWindowBg());

        scrollPane = new JScrollPane(canvas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UiTheme.toolWindowBg());
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(24);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(240);

        add(buildToolbar(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buildInfoBar(), BorderLayout.SOUTH);
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 6));
        bar.setBorder(UiTheme.pad(0, 4, 0, 4));
        bar.add(toolButton(IdeIcons.zoomIn(), "Acercar", () -> canvas.zoomIn()));
        bar.add(toolButton(IdeIcons.zoomOut(), "Alejar", () -> canvas.zoomOut()));
        bar.add(toolButton(IdeIcons.fit(), "Ajustar al panel", () -> fitGraph()));
        bar.add(toolButton(IdeIcons.export(), "Exportar PNG", () -> exportPng()));
        return bar;
    }

    private JPanel buildInfoBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(UiTheme.pad(3, 10, 4, 10));
        infoLabel = new JLabel("Sin árbol generado", SwingConstants.LEFT);
        infoLabel.setFont(UiTheme.sans(11));
        infoLabel.setForeground(UiTheme.dim());
        bar.add(infoLabel, BorderLayout.WEST);
        return bar;
    }

    private JButton toolButton(javax.swing.Icon icon, String tooltip, Runnable action) {
        JButton b = new JButton(icon);
        b.setToolTipText(tooltip);
        b.setFocusable(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(26, 26));
        b.addActionListener(e -> action.run());
        return b;
    }

    public void renderGraph(String dotCode) {
        canvas.setDot(dotCode);
        if (!canvas.hasGraph()) {
            infoLabel.setText("No se generó el árbol. Revisa la tabla de errores.");
            return;
        }

        javax.swing.SwingUtilities.invokeLater(this::fitGraph);
    }

    public void setDotText(String dotCode) {
        renderGraph(dotCode);
    }

    private void fitGraph() {
        canvas.zoomToFit();
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
        infoLabel.setText("Nodos: " + canvas.getNodeCount()
                + "   Profundidad: " + canvas.getTreeDepth()
                + "   Zoom: " + Math.round(canvas.getZoom() * 100) + "%");
    }

    public TreeCanvas getCanvas() {
        return canvas;
    }

    public boolean hasGraph() {
        return canvas.hasGraph();
    }

    public void exportPng() {
        if (!canvas.hasGraph()) {
            JOptionPane.showMessageDialog(this, "Primero compila un archivo para tener un árbol.",
                    "Exportar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFileChooserLike.choosePng(this, "ast.png", path -> {
            try {
                Dimension size = canvas.getPreferredSize();
                BufferedImage image = new BufferedImage(Math.max(1, size.width), Math.max(1, size.height),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                g2.setColor(UiTheme.toolWindowBg());
                g2.fillRect(0, 0, image.getWidth(), image.getHeight());
                g2.translate(-scrollPane.getViewport().getViewPosition().x,
                        -scrollPane.getViewport().getViewPosition().y);
                canvas.paint(g2);
                g2.dispose();
                ImageIO.write(image, "png", path.toFile());
                JOptionPane.showMessageDialog(this, "Imagen guardada en:\n" + path,
                        "Exportar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo exportar:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void clear() {
        canvas.setDot(null);
        infoLabel.setText("Sin árbol generado");
    }
}
