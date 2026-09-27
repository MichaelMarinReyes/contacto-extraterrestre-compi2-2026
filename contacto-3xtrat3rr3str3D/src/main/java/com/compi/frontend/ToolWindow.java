package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class ToolWindow extends JDialog {

    private static final int DEFAULT_WIDTH = 1040;
    private static final int DEFAULT_HEIGHT = 700;
    private static final Dimension MIN_SIZE = new Dimension(560, 380);

    private final ToolWindowDock dock;
    private final AtomicBoolean open = new AtomicBoolean(false);

    private Runnable pending;
    private Rectangle lastBounds;

    private Runnable onClosed;

    public ToolWindow(Frame owner, ToolWindowDock dock) {
        super(owner, "Herramientas de análisis");
        this.dock = dock;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(MIN_SIZE);
        setLayout(new BorderLayout());
        add(dock, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        getRootPane().registerKeyboardAction(e -> close(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JPanel.WHEN_IN_FOCUSED_WINDOW);
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(UiTheme.pad(6, 10, 6, 10));

        JButton close = new JButton("Cerrar");
        close.setFont(UiTheme.sansBold(12));
        close.setFocusable(false);
        close.addActionListener(e -> close());

        footer.add(close, BorderLayout.EAST);
        return footer;
    }

    public boolean showView(ToolWindowDock.View view) {
        dock.select(view);
        return showWindow();
    }

    public boolean showWindow() {
        if (GraphicsEnvironment.isHeadless()) {
            return false;
        }
        if (!open.compareAndSet(false, true)) {

            toFront();
            requestFocus();
            return true;
        }
        applyPending();

        if (lastBounds != null) {
            setBounds(lastBounds);
        } else {
            centerOnOwner();
        }

        setAlwaysOnTop(true);
        setVisible(true);
        toFront();
        return true;
    }

    public void close() {
        if (!open.compareAndSet(true, false)) {
            return;
        }
        if (isVisible()) {
            lastBounds = getBounds();
        }
        setAlwaysOnTop(false);
        setVisible(false);
        ownerFocus();
        if (onClosed != null) {
            onClosed.run();
        }
    }

    public boolean isOpen() {
        return open.get();
    }

    public void setOnClosed(Runnable listener) {
        this.onClosed = listener;
    }

    public void postResults(Runnable apply) {
        pending = apply;
        if (open.get()) {
            apply.run();
            pending = null;
        }
    }

    public void clearPending() {
        pending = null;
    }

    private void applyPending() {
        if (pending != null) {
            Runnable r = pending;
            pending = null;
            r.run();
        }
    }

    private void ownerFocus() {
        java.awt.Window owner = getOwner();
        if (owner instanceof java.awt.Window w) {
            w.toFront();
            w.requestFocus();
        }
    }

    private void centerOnOwner() {
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        int w = Math.min(DEFAULT_WIDTH, Math.max(MIN_SIZE.width, screen.width - 120));
        int h = Math.min(DEFAULT_HEIGHT, Math.max(MIN_SIZE.height, screen.height - 120));

        int x = screen.x + (screen.width - w) / 2;
        int y = screen.y + (screen.height - h) / 2;

        java.awt.Window owner = getOwner();
        if (owner != null && owner.isShowing()) {
            Rectangle o = owner.getBounds();
            x = o.x + (o.width - w) / 2;
            y = o.y + (o.height - h) / 2;
        }
        setBounds(x, y, w, h);
    }

    public ToolWindowDock getDock() {
        return dock;
    }
}
