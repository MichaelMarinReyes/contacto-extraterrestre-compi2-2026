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

/**
 * Barra de filtro para las tablas de la ventana de herramientas.
 *
 * <p>Se escribe lo que se busca y la tabla se va filtrando mientras se teclea.
 * A la derecha va la cuenta de lo que queda de lo que habia, porque si no
 * filtrar a "sin" y ver cuatro filas deja la sensacion de que solo hay cuatro
 * y el error se pierde de vista. El aspa quita el filtro de golpe.</p>
 *
 * <p>La busqueda no distingue mayusculas de minusculas: el filtro es para
 * encontrar, no para separar. Escribir "VARIABLE" tiene que encontrar lo mismo
 * que "variable".</p>
 */
public class TableFilterBar extends JPanel {

    private final JTextField field = new JTextField();
    private final JLabel countLabel = new JLabel();
    private final JButton clearButton = new JButton("×");

    /** Se avisa en cada tecleo, para ir filtrando mientras se escribe. */
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
                limpiar();
            }
        });

        // Escape quita el filtro, como en cualquier buscador.
        field.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "quitar-filtro");
        field.getActionMap().put("quitar-filtro", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
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

    /** Texto del filtro, ya recortado. */
    public String texto() {
        String texto = field.getText();
        return texto == null ? "" : texto.trim();
    }

    /**
     * Actualiza la cuenta de filas visibles.
     *
     * <p>Sin filtro se dice cuantas filas hay; con filtro se dice cuantas quedan
     * de cuantas habia, que es lo unico que permite saber que el filtro esta
     * escondiendo algo.</p>
     */
    public void setCounts(int visibles, int totales) {
        if (visibles == totales) {
            countLabel.setText(totales == 1 ? "1 fila" : totales + " filas");
        } else {
            countLabel.setText(visibles + " de " + totales);
        }
        clearButton.setEnabled(!texto().isEmpty());
    }

    public void setOnFilterChanged(Runnable onFilterChanged) {
        this.onFilterChanged = onFilterChanged;
    }

    /** Vacia el filtro. */
    public void limpiar() {
        if (!field.getText().isEmpty()) {
            field.setText("");
        }
    }

    /**
     * true si la fila debe verse con el filtro actual.
     *
     * <p>Se busca en todas las columnas y no solo en la primera: un error se
     * busca por su mensaje y un simbolo por su clase o su ambito, no por su
     * nombre.</p>
     *
     * @param celdas valores de la fila, ya en el idioma que se muestra
     */
    public boolean acepta(String... celdas) {
        String filtro = texto().toLowerCase();
        if (filtro.isEmpty()) {
            return true;
        }
        for (String celda : celdas) {
            if (celda != null && celda.toLowerCase().contains(filtro)) {
                return true;
            }
        }
        return false;
    }
}
