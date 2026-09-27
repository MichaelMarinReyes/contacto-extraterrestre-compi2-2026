package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TableFilterBar extends JPanel {

    private final JTextField field = new JTextField();
    private final JLabel countLabel = new JLabel();
    private final JButton clearButton = new JButton("×");

    private Runnable onFilterChanged;

    public TableFilterBar(String placeholder) {
        setLayout(new BorderLayout(6, 0));
        setOpaque(false);

        field.setFont(UiTheme.sans(12));
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                avisa();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                avisa();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                avisa();
            }
        });

        clearButton.setFont(UiTheme.sansBold(12));
        clearButton.setFocusable(false);
        clearButton.setToolTipText("Quitar el filtro");
        clearButton.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        clearButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        field.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "quitar-filtro");
        field.getActionMap().put("quitar-filtro", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        countLabel.setFont(UiTheme.sans(11));
        countLabel.setForeground(UiTheme.dim());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        right.add(countLabel);
        right.add(clearButton);

        add(field, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
    }

    private void avisa() {
        if (onFilterChanged != null) {
            onFilterChanged.run();
        }
    }

    public String text() {
        String text = field.getText();
        return text == null ? "" : text.trim();
    }

    public void setCounts(int visibles, int totales) {
        if (visibles == totales) {
            countLabel.setText(totales == 1 ? "1 fila" : totales + " filas");
        } else {
            countLabel.setText(visibles + " de " + totales);
        }
        clearButton.setEnabled(!text().isEmpty());
    }

    public void setOnFilterChanged(Runnable onFilterChanged) {
        this.onFilterChanged = onFilterChanged;
    }

    public void clear() {
        if (!field.getText().isEmpty()) {
            field.setText("");
        }
    }

    public boolean accepts(String... celdas) {
        String filter = text().toLowerCase();
        if (filter.isEmpty()) {
            return true;
        }
        for (String cell : celdas) {
            if (cell != null && cell.toLowerCase().contains(filter)) {
                return true;
            }
        }
        return false;
    }
}
