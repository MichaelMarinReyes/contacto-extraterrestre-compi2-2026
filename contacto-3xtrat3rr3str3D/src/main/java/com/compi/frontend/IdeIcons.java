package com.compi.frontend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

/**
 * Iconos vectoriales dibujados en tiempo de ejecucion.
 *
 * Se generan con Graphics2D en lugar de PNG para que hereden el color de
 * primer plano del tema y se vean nitidos a cualquier escala.
 */
public final class IdeIcons {

    public static final int SIZE = 18;

    private IdeIcons() {
    }

    // ---------- Acciones de la barra superior ----------

    public static Icon newFile() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.7f, s));
            g.draw(new RoundRectangle2D.Double(s * .22, s * .12, s * .56, s * .76, s * .1, s * .1));
            g.setColor(c);
            g.draw(new Line2D.Double(s * .35, s * .52, s * .65, s * .52));
            g.draw(new Line2D.Double(s * .5, s * .37, s * .5, s * .67));
        });
    }

    public static Icon openFile() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.7f, s));
            Path2D folder = new Path2D.Double();
            folder.moveTo(s * .12, s * .78);
            folder.lineTo(s * .12, s * .24);
            folder.lineTo(s * .42, s * .24);
            folder.lineTo(s * .52, s * .38);
            folder.lineTo(s * .88, s * .38);
            folder.lineTo(s * .88, s * .78);
            folder.closePath();
            g.draw(folder);
        });
    }

    public static Icon saveFile() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.7f, s));
            g.draw(new RoundRectangle2D.Double(s * .16, s * .16, s * .68, s * .68, s * .08, s * .08));
            g.draw(new Rectangle2D.Double(s * .34, s * .16, s * .32, s * .22));
            g.draw(new Rectangle2D.Double(s * .28, s * .52, s * .44, s * .32));
        });
    }

    public static Icon compile() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            Path2D play = new Path2D.Double();
            play.moveTo(s * .3, s * .2);
            play.lineTo(s * .8, s * .5);
            play.lineTo(s * .3, s * .8);
            play.closePath();
            g.fill(play);
        });
    }

    public static Icon ast() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.5f, s));
            g.draw(new Ellipse2D.Double(s * .40, s * .10, s * .20, s * .14));
            g.draw(new Ellipse2D.Double(s * .12, s * .70, s * .18, s * .13));
            g.draw(new Ellipse2D.Double(s * .70, s * .70, s * .18, s * .13));
            g.draw(new Line2D.Double(s * .48, s * .24, s * .22, s * .70));
            g.draw(new Line2D.Double(s * .52, s * .24, s * .78, s * .70));
        });
    }

    public static Icon symbols() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.5f, s));
            g.draw(new RoundRectangle2D.Double(s * .14, s * .18, s * .72, s * .64, s * .08, s * .08));
            g.draw(new Line2D.Double(s * .14, s * .42, s * .86, s * .42));
            g.draw(new Line2D.Double(s * .14, s * .62, s * .86, s * .62));
            g.draw(new Line2D.Double(s * .44, s * .18, s * .44, s * .82));
        });
    }

    public static Icon stack() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            for (int i = 0; i < 3; i++) {
                double y = s * (.30 + i * .20);
                g.draw(new Line2D.Double(s * .16, y, s * .84, y));
                g.draw(new Ellipse2D.Double(s * .11, y - s * .05, s * .10, s * .10));
            }
        });
    }

    public static Icon errors() {
        return glyph(SIZE, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.7f, s));
            Path2D tri = new Path2D.Double();
            tri.moveTo(s * .5, s * .12);
            tri.lineTo(s * .92, s * .84);
            tri.lineTo(s * .08, s * .84);
            tri.closePath();
            g.draw(tri);
            g.draw(new Line2D.Double(s * .5, s * .40, s * .5, s * .62));
            g.fill(new Ellipse2D.Double(s * .455, s * .69, s * .09, s * .09));
        });
    }

    public static Icon refresh() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new Arc2D.Double(s * .22, s * .22, s * .56, s * .56, 40, 280, Arc2D.OPEN));
            Path2D head = new Path2D.Double();
            head.moveTo(s * .70, s * .10);
            head.lineTo(s * .78, s * .38);
            head.lineTo(s * .52, s * .30);
            head.closePath();
            g.fill(head);
        });
    }

    public static Icon zoomIn() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new Ellipse2D.Double(s * .18, s * .18, s * .44, s * .44));
            g.draw(new Line2D.Double(s * .56, s * .56, s * .84, s * .84));
            g.draw(new Line2D.Double(s * .32, s * .40, s * .48, s * .40));
            g.draw(new Line2D.Double(s * .40, s * .32, s * .40, s * .48));
        });
    }

    public static Icon zoomOut() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new Ellipse2D.Double(s * .18, s * .18, s * .44, s * .44));
            g.draw(new Line2D.Double(s * .56, s * .56, s * .84, s * .84));
            g.draw(new Line2D.Double(s * .32, s * .40, s * .48, s * .40));
        });
    }

    public static Icon fit() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new RoundRectangle2D.Double(s * .14, s * .24, s * .72, s * .52, s * .06, s * .06));
            g.draw(new Line2D.Double(s * .28, s * .38, s * .72, s * .38));
            g.draw(new Line2D.Double(s * .28, s * .52, s * .72, s * .52));
            g.draw(new Line2D.Double(s * .28, s * .66, s * .56, s * .66));
        });
    }

    public static Icon export() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            Path2D tray = new Path2D.Double();
            tray.moveTo(s * .20, s * .46);
            tray.lineTo(s * .20, s * .84);
            tray.lineTo(s * .80, s * .84);
            tray.lineTo(s * .80, s * .46);
            g.draw(tray);
            g.draw(new Line2D.Double(s * .5, s * .70, s * .5, s * .16));
            Path2D head = new Path2D.Double();
            head.moveTo(s * .34, s * .32);
            head.lineTo(s * .5, s * .16);
            head.lineTo(s * .66, s * .32);
            g.draw(head);
        });
    }

    // ---------- Arbol de archivos ----------

    public static Icon folder() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            Path2D p = new Path2D.Double();
            p.moveTo(s * .10, s * .76);
            p.lineTo(s * .10, s * .24);
            p.lineTo(s * .40, s * .24);
            p.lineTo(s * .50, s * .40);
            p.lineTo(s * .90, s * .40);
            p.lineTo(s * .90, s * .76);
            p.closePath();
            g.draw(p);
        });
    }

    public static Icon folderOpen() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.5f, s));
            Path2D p = new Path2D.Double();
            p.moveTo(s * .10, s * .76);
            p.lineTo(s * .10, s * .24);
            p.lineTo(s * .40, s * .24);
            p.lineTo(s * .50, s * .40);
            p.lineTo(s * .82, s * .40);
            p.lineTo(s * .90, s * .52);
            p.closePath();
            g.draw(p);
            g.draw(new Line2D.Double(s * .16, s * .84, s * .90, s * .84));
        });
    }

    public static Icon file() {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.4f, s));
            Path2D p = new Path2D.Double();
            p.moveTo(s * .24, s * .10);
            p.lineTo(s * .58, s * .10);
            p.lineTo(s * .78, s * .32);
            p.lineTo(s * .78, s * .90);
            p.lineTo(s * .24, s * .90);
            p.closePath();
            g.draw(p);
            g.draw(new Line2D.Double(s * .58, s * .10, s * .58, s * .32));
            g.draw(new Line2D.Double(s * .58, s * .32, s * .78, s * .32));
        });
    }

    public static Icon fileWithDot(Color dot) {
        return glyph(16, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.4f, s));
            Path2D p = new Path2D.Double();
            p.moveTo(s * .20, s * .08);
            p.lineTo(s * .56, s * .08);
            p.lineTo(s * .76, s * .30);
            p.lineTo(s * .76, s * .88);
            p.lineTo(s * .20, s * .88);
            p.closePath();
            g.draw(p);
            g.draw(new Line2D.Double(s * .56, s * .08, s * .56, s * .30));
            g.draw(new Line2D.Double(s * .56, s * .30, s * .76, s * .30));
            g.setColor(dot);
            g.fill(new Ellipse2D.Double(s * .58, s * .56, s * .24, s * .24));
        });
    }

    public static Icon close() {
        return glyph(14, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new Line2D.Double(s * .26, s * .26, s * .74, s * .74));
            g.draw(new Line2D.Double(s * .74, s * .26, s * .26, s * .74));
        });
    }

    public static Icon chevronDown() {
        return glyph(14, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.6f, s));
            g.draw(new Line2D.Double(s * .24, s * .42, s * .5, s * .68));
            g.draw(new Line2D.Double(s * .5, s * .68, s * .76, s * .42));
        });
    }

    public static Icon dirtyDot() {
        return glyph(10, (g, s, c) -> {
            g.setColor(c);
            g.fill(new Ellipse2D.Double(s * .22, s * .22, s * .56, s * .56));
        });
    }

    public static Icon appMark() {
        return glyph(22, (g, s, c) -> {
            g.setColor(c);
            g.setStroke(sf(1.8f, s));
            g.draw(new RoundRectangle2D.Double(s * .10, s * .10, s * .80, s * .80, s * .18, s * .18));
            g.draw(new Ellipse2D.Double(s * .36, s * .36, s * .28, s * .28));
        });
    }

    // ---------- Infraestructura ----------

    @FunctionalInterface
    private interface Painter {
        void paint(Graphics2D g, double s, Color c);
    }

    /** Construye un icono que se pinta con el color de primer plano del tema. */
    public static Icon glyph(int size, Painter painter) {
        return new GlyphIcon(size, painter);
    }

    private static BasicStroke sf(float width, double s) {
        return new BasicStroke((float) (width * s / 18.0), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    private static final class GlyphIcon implements Icon {
        private final int size;
        private final Painter painter;

        GlyphIcon(int size, Painter painter) {
            this.size = size;
            this.painter = painter;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(x, y);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            Color fg = c != null ? c.getForeground() : java.awt.Color.LIGHT_GRAY;
            if (fg == null) {
                fg = java.awt.Color.LIGHT_GRAY;
            }
            painter.paint(g2, size, fg);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
