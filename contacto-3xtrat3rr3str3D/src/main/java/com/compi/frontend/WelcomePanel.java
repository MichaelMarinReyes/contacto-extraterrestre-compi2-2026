package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pantalla de bienvenida que se muestra cuando no hay ningun archivo abierto.
 *
 * Mantiene la zona del editor util en vez de dejar un hueco vacio, y ofrece
 * accesos directos a las acciones principales.
 */
public class WelcomePanel extends JPanel {

    private final Runnable onNewFile;
    private final Runnable onOpenFile;
    private final Runnable onOpenProject;
    private final Runnable onCompile;

    public WelcomePanel(Runnable onNewFile, Runnable onOpenFile,
                        Runnable onOpenProject, Runnable onCompile) {
        this.onNewFile = onNewFile;
        this.onOpenFile = onOpenFile;
        this.onOpenProject = onOpenProject;
        this.onCompile = onCompile;

        setLayout(new java.awt.GridBagLayout());
        setBackground(UiTheme.editorBg());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Contacto 3xtrat3rr3str3D");
        title.setFont(UiTheme.sansBold(26));
        title.setForeground(UiTheme.fg());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Compilador de PigLatin, Y y Zetariano");
        subtitle.setFont(UiTheme.sans(14));
        subtitle.setForeground(UiTheme.dim());
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.CENTER_ALIGNMENT);
        actions.add(actionButton("Archivo nuevo", IdeIcons.newFile(), onNewFile));
        actions.add(actionButton("Abrir archivo", IdeIcons.openFile(), onOpenFile));
        actions.add(actionButton("Abrir proyecto", IdeIcons.folder(), onOpenProject));
        actions.add(actionButton("Compilar", IdeIcons.compile(), onCompile));

        JLabel shortcuts = new JLabel("<html><div style='text-align:center'>"
                + "<b>Atajos</b><br/>"
                + "Ctrl+N nuevo &middot; Ctrl+O abrir &middot; Ctrl+S guardar &middot; Ctrl+B compilar<br/>"
                + "Ctrl+W cerrar pestana &middot; Ctrl+1 AST &middot; Ctrl+2 S&iacute;mbolos "
                + "&middot; Ctrl+3 Pila &middot; Ctrl+4 Errores"
                + "</div></html>", JLabel.CENTER);
        shortcuts.setFont(UiTheme.sans(12));
        shortcuts.setForeground(UiTheme.dim());
        shortcuts.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel hint = new JPanel();
        hint.setOpaque(false);
        hint.setLayout(new BoxLayout(hint, BoxLayout.Y_AXIS));
        hint.add(shortcuts);

        box.add(title);
        box.add(Box.createVerticalStrut(6));
        box.add(subtitle);
        box.add(Box.createVerticalStrut(26));
        box.add(actions);
        box.add(Box.createVerticalStrut(34));
        box.add(hint);

        add(box);
    }

    private JButton actionButton(String text, javax.swing.Icon icon, Runnable action) {
        JButton b = new JButton(text, icon);
        b.setFont(UiTheme.sans(13));
        b.setFocusable(false);
        b.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        b.setIconTextGap(8);
        b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());
        return b;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(520, 340);
    }
}
