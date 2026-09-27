package com.compi.frontend;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ErrorTablePanel extends javax.swing.JPanel {

    private JTable errorTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel emptyMessageLabel;
    private CardLayout cards;
    private JPanel content;
    private MouseAdapter doubleClick;
    private TableFilterBar filterBar;

    private List<CompilationError> allErrors = List.of();

    private List<CompilationError> shown = List.of();

    private static final String CARD_TABLE = "tabla";
    private static final String CARD_EMPTY = "vacio";

    private static final String NO_POSITION = "-";

    public ErrorTablePanel() {
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

        tableModel = new DefaultTableModel(
                new Object[]{"#", "Archivo", "Tipo", "Línea", "Col.", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        errorTable = new JTable(tableModel);
        errorTable.setFont(UiTheme.mono(12));
        errorTable.setRowHeight(24);
        errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        errorTable.setFillsViewportHeight(true);
        errorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        errorTable.setBackground(UiTheme.toolWindowBg());
        errorTable.setForeground(UiTheme.fg());
        errorTable.setGridColor(UiTheme.separator());
        errorTable.getTableHeader().setFont(UiTheme.sansBold(12));
        errorTable.getTableHeader().setReorderingAllowed(false);

        int[] widths = {34, 150, 100, 52, 52, 240};
        for (int i = 0; i < widths.length; i++) {
            errorTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        DefaultTableCellRenderer typeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    comp.setForeground(colorForType(value instanceof ErrorType t ? t : null));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        };
        errorTable.getColumnModel().getColumn(2).setCellRenderer(typeRenderer);

        rightAlign(0);
        rightAlign(3);
        rightAlign(4);

        emptyMessageLabel = new JLabel("Sin errores de compilación", SwingConstants.CENTER);
        emptyMessageLabel.setFont(UiTheme.sans(12));
        emptyMessageLabel.setForeground(UiTheme.dim());
        emptyMessageLabel.setBorder(UiTheme.pad(20, 0, 20, 0));

        scrollPane = new JScrollPane(errorTable);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);

        filterBar = new TableFilterBar("Filtrar errores");
        filterBar.setBorder(UiTheme.pad(6, 8, 6, 8));
        filterBar.setOnFilterChanged(this::applyFilter);

        filterBar.setVisible(false);

        cards = new CardLayout();
        content = new JPanel(cards);
        content.add(scrollPane, CARD_TABLE);
        content.add(emptyMessageLabel, CARD_EMPTY);
        cards.show(content, CARD_EMPTY);

        setLayout(new BorderLayout());
        add(filterBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private Color colorForType(ErrorType type) {
        if (type == null) {
            return UiTheme.dim();
        }
        return switch (type) {
            case LEXICAL -> UiTheme.warning();
            case SYNTACTIC -> UiTheme.error();
            case SEMANTIC -> new Color(0xDB5860).brighter();
        };
    }

    private void rightAlign(int index) {
        errorTable.getColumnModel().getColumn(index).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable t, Object value, boolean selected,
                            boolean focused, int row, int column) {
                        super.getTableCellRendererComponent(t, value, selected, focused, row, column);
                        setHorizontalAlignment(SwingConstants.RIGHT);
                        return this;
                    }
                });
    }

    public void loadErrors(List<CompilationError> errors) {
        allErrors = errors == null ? List.of() : List.copyOf(errors);
        filterBar.setVisible(!allErrors.isEmpty());
        applyFilter();
    }

    private void applyFilter() {
        tableModel.setRowCount(0);
        shown = new ArrayList<>(allErrors.size());
        for (int i = 0; i < allErrors.size(); i++) {
            CompilationError e = allErrors.get(i);
            if (accepts(e)) {

                tableModel.addRow(row(e, i + 1));
                shown.add(e);
            }
        }

        filterBar.setCounts(shown.size(), allErrors.size());
        if (allErrors.isEmpty()) {
            emptyMessageLabel.setText("Sin errores de compilación");
        } else if (shown.isEmpty()) {
            emptyMessageLabel.setText("Ningún error coincide con el filtro «"
                    + filterBar.text() + "»");
        } else {
            emptyMessageLabel.setText("");
        }
        cards.show(content, shown.isEmpty() ? CARD_EMPTY : CARD_TABLE);
        revalidate();
        repaint();
    }

    private static Object[] row(CompilationError e, int number) {
        return new Object[]{
                number,
                e.getFileName() == null ? "" : e.getFileName(),
                e.getErrorType(),
                e.getLine() > 0 ? String.valueOf(e.getLine()) : NO_POSITION,
                e.getColumn() > 0 ? String.valueOf(e.getColumn()) : NO_POSITION,
                e.getMessage()
        };
    }

    private boolean accepts(CompilationError e) {
        return filterBar.accepts(
                e.getFileName(),
                e.getType(),
                e.getLine() > 0 ? String.valueOf(e.getLine()) : NO_POSITION,
                e.getMessage());
    }

    public void clearFilter() {
        filterBar.clear();
    }

    public JTable getErrorTable() {
        return errorTable;
    }

    public int getErrorCount() {
        return shown.size();
    }

    public int getTotalErrorCount() {
        return allErrors.size();
    }

    public CompilationError errorAt(int row) {
        if (row < 0 || row >= shown.size()) {
            return null;
        }
        return shown.get(row);
    }

    public CompilationError getSelectedError() {
        return errorAt(errorTable.getSelectedRow());
    }

    public void setOnErrorActivated(java.util.function.IntConsumer onErrorActivated) {
        errorTable.removeMouseListener(doubleClick);
        if (onErrorActivated == null) {
            return;
        }
        java.util.function.IntConsumer listener = onErrorActivated;
        doubleClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                int row = errorTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    listener.accept(row);
                }
            }
        };
        errorTable.addMouseListener(doubleClick);
    }

    public void clear() {
        loadErrors(null);
    }
}
