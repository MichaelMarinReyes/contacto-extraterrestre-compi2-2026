package com.compi.frontend;

import com.compi.backend.c3d.Mode;
import com.compi.backend.languages2.LanguageCompiler;
import com.compi.backend.languages2.LanguageCompilerFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 * Barra de herramientas superior, al estilo de la de IntelliJ IDEA.
 *
 * Reutiliza los botones que genera NetBeans en {@code MainWindow.initComponents()}
 * (no se tocan esos metodos): aqui solo se les cambia el layout, se les fija
 * el icono y se les agrupan visualmente.
 */
public class TopToolbar extends JPanel {

    public static final int BAR_HEIGHT = 42;

    private final JPanel actionCluster = new JPanel();
    private final JPanel viewCluster = new JPanel();
    private final JLabel navLabel;
    private final JComboBox<String> cModeBox;
    private final JButton compileButton;
    private final JToggleButton sideToggle;
    private final JLabel projectLabel = new JLabel();

    /**
     * <p>No hay selector de lenguaje: el lenguaje de cada archivo lo decide su
     * extension, de modo que un mismo proyecto puede mezclar los tres.</p>
     *
     * @param navLabel      etiqueta de ruta (MainWindow.optionSelectedLabel)
     * @param cModeBox      selector de modo de generacion de C
     * @param compileButton boton de compilacion
     * @param sideToggle    boton que muestra/oculta el panel derecho
     */
    public TopToolbar(JLabel navLabel, JComboBox<String> cModeBox,
                      JButton compileButton, JToggleButton sideToggle) {
        this.navLabel = navLabel;
        this.cModeBox = cModeBox;
        this.compileButton = compileButton;
        this.sideToggle = sideToggle;

        setLayout(new BorderLayout());
        setOpaque(true);
        setBorder(UiTheme.pad(0, 10, 0, 8));
        setPreferredSize(new Dimension(100, BAR_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, BAR_HEIGHT));

        add(buildBrand(), BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildRight(), BorderLayout.EAST);
    }

    // ====================== Bandas ======================

    /** Logotipo + nombre de la aplicacion. */
    private JComponent buildBrand() {
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        brand.setOpaque(false);

        JLabel mark = new JLabel(IdeIcons.appMark());
        mark.setPreferredSize(new Dimension(24, 24));
        mark.setVerticalAlignment(SwingConstants.CENTER);

        JLabel name = new JLabel("Contacto 3xtrat3rr3str3D");
        name.setFont(UiTheme.sansBold(14));
        name.setBorder(UiTheme.pad(0, 0, 0, 8));

        brand.add(mark);
        brand.add(name);
        brand.add(verticalSeparator());
        return brand;
    }

    /** Acciones de archivo + vistas, pegadas a la izquierda. */
    private JComponent buildCenter() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        center.setOpaque(false);

        actionCluster.setOpaque(false);
        actionCluster.setLayout(new BoxLayout(actionCluster, BoxLayout.X_AXIS));
        actionCluster.setBorder(UiTheme.pad(5, 0, 5, 0));

        viewCluster.setOpaque(false);
        viewCluster.setLayout(new BoxLayout(viewCluster, BoxLayout.X_AXIS));
        viewCluster.setBorder(UiTheme.pad(5, 0, 5, 0));

        center.add(actionCluster);
        center.add(verticalSeparator());
        center.add(viewCluster);
        center.add(Box.createHorizontalStrut(10));

        projectLabel.setFont(UiTheme.sans(12));
        projectLabel.setForeground(UiTheme.dim());
        projectLabel.setBorder(UiTheme.pad(0, 4, 0, 0));
        center.add(projectLabel);

        center.add(Box.createHorizontalGlue());
        if (navLabel != null) {
            navLabel.setFont(UiTheme.mono(12));
            navLabel.setForeground(UiTheme.dim());
            navLabel.setBorder(UiTheme.pad(0, 8, 0, 8));
            center.add(navLabel);
        }
        return center;
    }

    /** Modo de C, compilar y visibilidad del dock derecho. */
    private JComponent buildRight() {
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        styleCombo(cModeBox, 150);

        right.add(labelled("Generar C", cModeBox));

        if (sideToggle != null) {
            sideToggle.setText("Herramientas");
            sideToggle.setFocusable(false);
            sideToggle.setToolTipText("Mostrar u ocultar el panel de herramientas (AST, Símbolos, Pila, Errores)");
            sideToggle.setFont(UiTheme.sans(12));
        }
        right.add(sideToggle);

        if (compileButton != null) {
            compileButton.setText("Compilar");
            compileButton.setIcon(IdeIcons.compile());
            compileButton.setFocusable(false);
            compileButton.setFont(UiTheme.sansBold(12));
            compileButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UiTheme.accent().darker(), 1, true),
                    UiTheme.pad(6, 12, 6, 12)));
            compileButton.setContentAreaFilled(true);
        }
        right.add(compileButton);
        return right;
    }

    private JComponent labelled(String text, JComponent field) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(text + ":");
        l.setFont(UiTheme.sans(12));
        l.setForeground(UiTheme.dim());
        p.add(l);
        p.add(field);
        return p;
    }

    private JComponent verticalSeparator() {
        JPanel s = new JPanel();
        s.setBackground(UiTheme.separator());
        s.setPreferredSize(new Dimension(1, 22));
        s.setMaximumSize(new Dimension(1, 22));
        s.setBorder(UiTheme.pad(0, 6, 0, 6));
        return s;
    }

    private void styleCombo(JComboBox<String> box, int width) {
        box.setFocusable(false);
        box.setFont(UiTheme.sans(12));
        box.setPreferredSize(new Dimension(width, 26));
        box.setMaximumSize(new Dimension(width, 26));
    }

    // ====================== API de contenido ======================

    /**
     * Registra un boton de accion de archivo (nuevo / abrir / guardar).
     * Los botones pasan a ser iconos planos, como en IntelliJ.
     */
    public void addFileAction(JButton button, String tooltip) {
        styleActionButton(button, tooltip);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        actionCluster.add(button);
    }

    /**
     * Registra un boton de vista (AST / simbolos / pila / errores).
     */
    public void addViewAction(JButton button, String tooltip) {
        styleActionButton(button, tooltip);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        viewCluster.add(button);
    }

    /** Convierte un JButton de NetBeans en boton de icono plano. */
    public void styleActionButton(JButton button, String tooltip) {
        button.setText("");
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setMargin(new java.awt.Insets(4, 6, 4, 6));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(34, 32));
        button.setMinimumSize(new Dimension(34, 32));
        button.setMaximumSize(new Dimension(34, 32));
        UiTheme.asButton(button);
    }

    /** Asigna los iconos a los botones de NetBeans. */
    public void applyIcons(JButton newFile, JButton openFile, JButton saveFile,
                           JButton ast, JButton symbols, JButton stack, JButton errors) {
        if (newFile != null) newFile.setIcon(IdeIcons.newFile());
        if (openFile != null) openFile.setIcon(IdeIcons.openFile());
        if (saveFile != null) saveFile.setIcon(IdeIcons.saveFile());
        if (ast != null) ast.setIcon(IdeIcons.ast());
        if (symbols != null) symbols.setIcon(IdeIcons.symbols());
        if (stack != null) stack.setIcon(IdeIcons.stack());
        if (errors != null) errors.setIcon(IdeIcons.errors());
    }

    /** Muestra el nombre del proyecto cargado en el arbol. */
    public void setProjectName(String name) {
        projectLabel.setText(name == null || name.isBlank() ? "" : name);
    }

    /** Idiomas disponibles para el desplegable. */
    public static List<LanguageCompiler> supportedLanguages() {
        return LanguageCompilerFactory.all();
    }

    /** Convierte un modo de C a su etiqueta. */
    public static String modeLabel(Mode mode) {
        return mode == Mode.TRIPLETS ? "Tripletes" : "Cuartetas";
    }

    // ====================== Atajos de teclado ======================

    /** Registra un atajo global sobre el componente indicado. */
    public static void bindShortcut(JComponent root, String keyStroke, Action action) {
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(keyStroke), action);
        root.getActionMap().put(action, action);
    }

    /** Crea una accion sin icono a partir de un nombre. */
    public static Action action(String name, Runnable runnable) {
        return new AbstractAction(name) {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                runnable.run();
            }
        };
    }

    /** Agrupa botones excluyentes (usado por toggles de visibilidad). */
    public static ButtonGroup group(JToggleButton... buttons) {
        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : buttons) {
            g.add(b);
        }
        return g;
    }

    /** Permite a MainWindow inyectar consumidores de los desplegables. */
    public void onCModeSelected(Consumer<Mode> consumer) {
        cModeBox.addActionListener(e -> {
            Object v = cModeBox.getSelectedItem();
            if (v != null) {
                consumer.accept("Tripletes".equals(v.toString()) ? Mode.TRIPLETS : Mode.QUADRUPLES);
            }
        });
    }

    /** Modo de generacion de C que corresponde a una etiqueta del desplegable. */
    public static Mode modeOf(String label) {
        return "Tripletes".equals(label) ? Mode.TRIPLETS : Mode.QUADRUPLES;
    }

    /** Desplegable de generacion de C, para fijarlo desde fuera. */
    public JComboBox<String> getCModeBox() {
        return cModeBox;
    }
}
