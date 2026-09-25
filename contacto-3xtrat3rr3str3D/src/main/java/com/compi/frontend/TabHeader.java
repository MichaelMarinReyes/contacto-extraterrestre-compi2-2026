package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Encabezado de una pestana de editor: icono, nombre del archivo, punto de
 * "sin guardar" y boton de cierre, tal y como lo muestra IntelliJ.
 */
public class TabHeader extends JPanel {

    private final JLabel iconLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel dirtyLabel = new JLabel();
    private final JButton closeButton = new JButton();
    private final Runnable onClose;

    private boolean dirty;

    public TabHeader(EditorPanel editor, Runnable onClose) {
        this.onClose = onClose;

        setLayout(new BorderLayout(4, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));

        iconLabel.setIcon(fileIcon(editor.getLanguageId()));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        add(iconLabel, BorderLayout.WEST);

        nameLabel.setText(editor.getDisplayName());
        nameLabel.setFont(UiTheme.sans(13));
        nameLabel.setForeground(UiTheme.fg());
        nameLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(nameLabel, BorderLayout.CENTER);

        dirtyLabel.setPreferredSize(new Dimension(8, 8));
        dirtyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        dirtyLabel.setVisible(false);

        closeButton.setIcon(IdeIcons.close());
        closeButton.setPreferredSize(new Dimension(18, 18));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusable(false);
        closeButton.setOpaque(false);
        closeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        closeButton.setToolTipText("Cerrar");
        closeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireClose(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Sin accion: la apertura se dispara en mouseReleased.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    fireClose(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(UiTheme.error());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(UiTheme.dim());
            }
        });
        closeButton.setForeground(UiTheme.dim());

        JPanel right = new JPanel(new BorderLayout(2, 0));
        right.setOpaque(false);
        right.add(dirtyLabel, BorderLayout.WEST);
        right.add(closeButton, BorderLayout.EAST);
        add(right, BorderLayout.EAST);

        setDirty(editor.isModified());
    }

    private void fireClose(MouseEvent e) {
        // consumeClick se resuelve en el cierre real de la pestana
        if (onClose != null) {
            onClose.run();
        }
    }

    /** Cambia el nombre visible. */
    public void setName(String name) {
        nameLabel.setText(name);
    }

    public String getName() {
        return nameLabel.getText();
    }

    /** Muestra u oculta el punto de documento modificado. */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        dirtyLabel.setVisible(dirty);
        dirtyLabel.setIcon(dirty ? tintedDot() : null);
    }

    public boolean isDirty() {
        return dirty;
    }

    /** Cambia el icono segun el lenguaje del archivo. */
    public void setLanguageIcon(String languageId) {
        iconLabel.setIcon(fileIcon(languageId));
    }

    private Icon tintedDot() {
        return new Icon() {
            @Override
            public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(UiTheme.accent());
                g2.fillOval(x, y, 8, 8);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 8;
            }

            @Override
            public int getIconHeight() {
                return 8;
            }
        };
    }

    /** Devuelve el icono de archivo segun el lenguaje detectado. */
    static Icon fileIcon(String languageId) {
        return switch (languageId == null ? "pig" : languageId) {
            case "y" -> IdeIcons.fileWithDot(new Color(0x56A8F5));
            case "zet" -> IdeIcons.fileWithDot(new Color(0xC77DBB));
            default -> IdeIcons.fileWithDot(new Color(0x6AAB73));
        };
    }

    /** Permite al contenedor obtener el boton de cierre. */
    public JComponent getCloseButton() {
        return closeButton;
    }
}
