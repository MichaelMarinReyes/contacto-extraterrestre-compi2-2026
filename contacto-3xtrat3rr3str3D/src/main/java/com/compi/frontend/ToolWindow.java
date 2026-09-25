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

/**
 * Ventana flotante con las herramientas de analisis (AST, simbolos, pila y
 * errores).
 *
 * <p>Antes estas cuatro vistas vivian en un dock pegado al borde derecho, donde
 * quedaban estrechas y ademas ocupaban ancho aunque no se estuvieran usando.
 * Ahora viven en una ventana aparte, grande, redimensionable y centrada sobre el
 * editor, y se queda por encima de la ventana principal mientras esta abierta:
 * hay que cerrarla para volver a escribir codigo o abrir mas archivos.</p>
 *
 * <p>No es modal a proposito. Una ventana modal bloquearia el hilo de eventos de
 * Swing y las actualizaciones de la barra de estado y de las consolas se
 * quedarian en cola hasta cerrarla; dejarla siempre encima da el mismo efecto
 * para el usuario sin bloquear nada.</p>
 *
 * <p>Los resultados de la ultima compilacion se guardan pendientes: si la
 * ventana esta cerrada se aplazan y se pintan al volver a abrirla, para que al
 * cerrarla no quede trabajo a medias hecho.</p>
 */
public class ToolWindow extends JDialog {

    /** Tamano por defecto: ancho de sobra para leer un grafo con zoom. */
    private static final int DEFAULT_WIDTH = 1040;
    private static final int DEFAULT_HEIGHT = 700;
    private static final Dimension MIN_SIZE = new Dimension(560, 380);

    private final ToolWindowDock dock;
    private final AtomicBoolean open = new AtomicBoolean(false);

    /** Resultados de la compilacion que aun no se han pintado. */
    private Runnable pending;
    private Rectangle lastBounds;

    /** Se avisa al contenedor para que actualice el boton de la barra. */
    private Runnable onClosed;

    public ToolWindow(Frame owner, ToolWindowDock dock) {
        super(owner, "Herramientas de análisis");
        this.dock = dock;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(MIN_SIZE);
        setLayout(new BorderLayout());
        add(dock, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        // Cerrar la ventana la oculta en vez de destruirla: asi el mismo
        // objeto sirve para volver a mostrarla sin reconstruir las vistas.
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

    /** Barra inferior con el contador de errores y el boton de cerrar. */
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

    // ====================== Mostrar y ocultar ======================

    /**
     * Abre la ventana mostrando la vista pedida.
     *
     * @return true si se abrio, false si el sistema no tiene pantalla
     */
    public boolean showView(ToolWindowDock.View view) {
        dock.select(view);
        return showWindow();
    }

    /** Abre la ventana en la vista que tenga seleccionada. */
    public boolean showWindow() {
        if (GraphicsEnvironment.isHeadless()) {
            return false;
        }
        if (!open.compareAndSet(false, true)) {
            // Ya estaba abierta: solo hay que traerla delante.
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
        // Queda por encima del editor: hay que cerrarla para escribir.
        setAlwaysOnTop(true);
        setVisible(true);
        toFront();
        return true;
    }

    /**
     * Cierra la ventana y devuelve el foco al editor.
     *
     * <p>No se destruye nada: las vistas conservan lo que ya se habia pintado y
     * {@link #postResults(Runnable)} se encarga de lo que llegue despues.</p>
     *
     * <p>El metodo se llama {@code close} y no {@code hide} a proposito:
     * {@link java.awt.Window#hide()} lo usa Swing por dentro para propagar el
     * cierre a las ventanas hijas, y sobrescribirlo dejaba la ventana
     * pegada en pantalla sin poder ocultarla.</p>
     */
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

    /** Se invoca cada vez que la ventana se cierra, para sincronizar la barra. */
    public void setOnClosed(Runnable listener) {
        this.onClosed = listener;
    }

    /**
     * Encola los resultados de una compilacion.
     *
     * <p>Si la ventana esta abierta se pintan de inmediato; si esta cerrada
     * quedan pendientes para el momento en que se vuelva a abrir.</p>
     */
    public void postResults(Runnable apply) {
        pending = apply;
        if (open.get()) {
            apply.run();
            pending = null;
        }
    }

    /** Descarta resultados pendientes sin aplicarlos. */
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

    /** Coloca la ventana centrada respecto de la principal, o de la pantalla. */
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

    /** El dock contenido, para las consultas de la ventana principal. */
    public ToolWindowDock getDock() {
        return dock;
    }
}
