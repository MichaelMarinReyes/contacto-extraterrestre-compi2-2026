package com.compi.frontend.dot;

import com.compi.frontend.UiTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * Lienzo que dibuja un grafo DOT como arbol.
 *
 * Permite zoom con la rueda del raton, arrastre con el boton central y Exportar
 * la vista a PNG. Las etiquetas se recortan si el nodo es mas estrecho que el
 * texto completo.
 */
public class TreeCanvas extends JComponent {

    private static final double ZOOM_MIN = 0.25;
    private static final double ZOOM_MAX = 2.5;

    private DotModel model;
    private TreeLayout layout = new TreeLayout();

    private double zoom = 1.0;
    private String hovered;
    private String selected;

    private final Map<String, Rectangle2D> hitAreas = new HashMap<>();

    public TreeCanvas() {
        setFont(UiTheme.mono(12));
        setOpaque(true);
        installMouse();
    }

    // ====================== Datos ======================

    /**
     * Carga un grafo DOT y recalcula el layout.
     */
    public void setDot(String dot) {
        this.model = DotModel.parse(dot);
        this.selected = null;
        this.hovered = null;
        relayout();
    }

    /** Recalcula posiciones con el tamano de fuente actual. */
    public void relayout() {
        if (model != null) {
            layout.compute(model, getFont());
        }
        revalidate();
        repaint();
    }

    public boolean hasGraph() {
        return model != null && !model.isEmpty();
    }

    public int getNodeCount() {
        return model == null ? 0 : model.nodeCount();
    }

    public int getTreeDepth() {
        return model == null ? 0 : model.depth();
    }

    // ====================== Vista ======================

    @Override
    public Dimension getPreferredSize() {
        Dimension b = layout.bounds();
        return new Dimension((int) (b.width * zoom), (int) (b.height * zoom));
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(120, 120);
    }

    public double getZoom() {
        return zoom;
    }

    /** Fija el zoom dentro de los limites permitidos. */
    public void setZoom(double value) {
        double clamped = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, value));
        if (Math.abs(clamped - zoom) > 1e-6) {
            zoom = clamped;
            revalidate();
            repaint();
        }
    }

    public void zoomIn() {
        setZoom(zoom * 1.2);
    }

    public void zoomOut() {
        setZoom(zoom / 1.2);
    }

    /** Ajusta el zoom para que el arbol completo entre en el area visible. */
    public void zoomToFit() {
        if (!hasGraph()) {
            return;
        }
        Dimension b = layout.bounds();
        if (b.width <= 0 || b.height <= 0) {
            return;
        }
        double sx = getWidth() / (double) b.width;
        double sy = getHeight() / (double) b.height;
        setZoom(Math.min(sx, sy));
    }

    public void resetZoom() {
        setZoom(1.0);
    }

    // ====================== Dibujo ======================

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(getBackground() == null ? UiTheme.toolWindowBg() : getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (!hasGraph()) {
            g2.setColor(UiTheme.dim());
            g2.setFont(UiTheme.sans(12));
            String msg = "Compila un archivo para ver su árbol de sintaxis";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            g2.dispose();
            return;
        }

        g2.scale(zoom, zoom);
        hitAreas.clear();

        // 1. Aristas
        g2.setColor(UiTheme.separator().brighter());
        g2.setStroke(new BasicStroke(1.4f));
        for (String[] edge : model.edges()) {
            int[] from = layout.positionOf(edge[0]);
            int[] to = layout.positionOf(edge[1]);
            Dimension fs = layout.sizeOf(edge[0]);
            Dimension ts = layout.sizeOf(edge[1]);
            if (from == null || to == null || fs == null || ts == null) {
                continue;
            }
            int x1 = from[0] + fs.width / 2;
            int y1 = from[1] + fs.height;
            int x2 = to[0] + ts.width / 2;
            int y2 = to[1];
            int midY = (y1 + y2) / 2;
            g2.draw(new Line2D.Double(x1, y1, x1, midY));
            g2.draw(new Line2D.Double(x1, midY, x2, midY));
            g2.draw(new Line2D.Double(x2, midY, x2, y2));
        }

        // 2. Nodos
        Font font = getFont();
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics(font);
        for (String id : model.nodes().keySet()) {
            int[] pos = layout.positionOf(id);
            Dimension size = layout.sizeOf(id);
            if (pos == null || size == null) {
                continue;
            }
            String label = model.node(id).label();
            boolean isRoot = id.equals(model.root());
            boolean isToken = label.startsWith("'");
            boolean isError = label.contains("(!)") || label.equals("<error>");

            Color fill = isError ? UiTheme.error().darker()
                    : isRoot ? UiTheme.accent().darker()
                    : isToken ? UiTheme.color("TableHeader.background", new Color(0x3A3D41))
                    : UiTheme.color("TextField.background", new Color(0x313335));

            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Double(pos[0], pos[1], size.width, size.height, 8, 8));

            g2.setColor(isError ? UiTheme.error()
                    : hovered != null && hovered.equals(id) ? UiTheme.accent()
                    : UiTheme.separator().brighter());
            g2.setStroke(new BasicStroke(hovered != null && hovered.equals(id) ? 2f : 1f));
            g2.draw(new RoundRectangle2D.Double(pos[0], pos[1], size.width, size.height, 8, 8));

            g2.setColor(Color.WHITE);
            g2.drawString(fit(label, fm, size.width - 12),
                    pos[0] + 6, pos[1] + fm.getAscent() + 5);

            hitAreas.put(id, new Rectangle2D.Double(pos[0], pos[1], size.width, size.height));
        }
        g2.dispose();
    }

    private static String fit(String text, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(text) <= maxWidth) {
            return text;
        }
        String ellipsis = "...";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i));
            if (fm.stringWidth(sb + ellipsis) > maxWidth) {
                sb.setLength(sb.length() - 1);
                break;
            }
        }
        return sb + ellipsis;
    }

    // ====================== Interaccion ======================

    private void installMouse() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String id = idAt(e);
                if (id == null ? hovered != null : !id.equals(hovered)) {
                    hovered = id;
                    setToolTipText(id == null ? null : model.node(id).label());
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = null;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String id = idAt(e);
                if (id != null && SwingUtilities.isLeftMouseButton(e)) {
                    selected = id;
                    repaint();
                }
            }

            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                if (e.isControlDown() || e.isMetaDown() || e.isShiftDown()) {
                    if (e.getWheelRotation() < 0) {
                        zoomIn();
                    } else {
                        zoomOut();
                    }
                } else {
                    getParent().dispatchEvent(e);
                }
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    private String idAt(MouseEvent e) {
        double x = e.getX() / zoom;
        double y = e.getY() / zoom;
        for (Map.Entry<String, Rectangle2D> entry : hitAreas.entrySet()) {
            if (entry.getValue().contains(x, y)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getSelectedNode() {
        return selected;
    }
}
