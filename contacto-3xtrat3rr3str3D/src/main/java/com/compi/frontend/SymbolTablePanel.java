package com.compi.frontend;

import com.compi.backend.symbols.Symbol;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class SymbolTablePanel extends javax.swing.JPanel {

    private static final String NO_POSITION = "-";

    private static final String CARD_TABLE = "tabla";
    private static final String CARD_EMPTY = "vacio";

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    private JLabel emptyMessageLabel;
    private CardLayout cards;
    private JPanel content;
    private IntConsumer onSymbolActivated;
    private TableFilterBar filterBar;

    private List<Symbol> allSymbols = List.of();

    private List<Symbol> shown = List.of();

    public SymbolTablePanel() {
        initComponents();
        initTableComponents();
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

    private void initTableComponents() {
        this.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"Línea", "Col", "Símbolo", "Tipo", "Categoría", "Ámbito",
                        "Lenguaje", "Offset"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(UiTheme.mono(12));
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(UiTheme.sansBold(12));
        table.getTableHeader().setReorderingAllowed(false);
        table.setBackground(UiTheme.toolWindowBg());
        table.setForeground(UiTheme.fg());
        table.setGridColor(UiTheme.separator());
        table.setSelectionBackground(UiTheme.accent());

        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setFillsViewportHeight(true);
        int[] widths = {50, 40, 120, 100, 90, 130, 90, 60};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        rightAlign(0);
        rightAlign(1);
        rightAlign(7);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showDetails();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && onSymbolActivated != null) {
                        onSymbolActivated.accept(row);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        emptyMessageLabel = new JLabel("Sin símbolos", SwingConstants.CENTER);
        emptyMessageLabel.setFont(UiTheme.sans(12));
        emptyMessageLabel.setForeground(UiTheme.dim());
        emptyMessageLabel.setBorder(UiTheme.pad(20, 0, 20, 0));

        cards = new CardLayout();
        content = new JPanel(cards);
        content.add(scroll, CARD_TABLE);
        content.add(emptyMessageLabel, CARD_EMPTY);
        cards.show(content, CARD_EMPTY);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(UiTheme.mono(12));
        detailsArea.setBackground(UiTheme.toolWindowBg());
        detailsArea.setForeground(UiTheme.fg());
        detailsArea.setBorder(UiTheme.pad(6, 8, 6, 8));
        detailsArea.setVisible(false);

        filterBar = new TableFilterBar("Filtrar símbolos");
        filterBar.setBorder(UiTheme.pad(6, 8, 6, 8));
        filterBar.setOnFilterChanged(this::applyFilter);

        filterBar.setVisible(false);

        add(filterBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(detailsArea, BorderLayout.SOUTH);
    }

    private void rightAlign(int index) {
        table.getColumnModel().getColumn(index).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(
                            JTable t, Object value, boolean selected,
                            boolean focused, int row, int column) {
                        super.getTableCellRendererComponent(t, value, selected, focused, row, column);
                        setHorizontalAlignment(SwingConstants.RIGHT);
                        return this;
                    }
                });
    }

    public void loadSymbols(List<Symbol> symbols) {
        allSymbols = symbols == null ? List.of() : List.copyOf(symbols);
        filterBar.setVisible(!allSymbols.isEmpty());
        applyFilter();
    }

    private void applyFilter() {
        tableModel.setRowCount(0);
        shown = new ArrayList<>(allSymbols.size());
        for (Symbol s : allSymbols) {
            if (accepts(s)) {
                shown.add(s);
            }
        }
        for (Symbol s : shown) {
            tableModel.addRow(row(s));
        }

        filterBar.setCounts(shown.size(), allSymbols.size());
        if (allSymbols.isEmpty()) {
            emptyMessageLabel.setText("Sin símbolos: compila un archivo para verlos");
        } else if (shown.isEmpty()) {
            emptyMessageLabel.setText("Ningún símbolo coincide con el filtro «"
                    + filterBar.text() + "»");
        } else {
            emptyMessageLabel.setText("");
        }
        cards.show(content, shown.isEmpty() ? CARD_EMPTY : CARD_TABLE);

        if (shown.isEmpty()) {
            detailsArea.setVisible(false);
        } else {
            showDetails();
        }
        revalidate();
        repaint();
    }

    private static Object[] row(Symbol s) {
        return new Object[]{
                s.getLine() > 0 ? String.valueOf(s.getLine()) : NO_POSITION,
                s.getColumn() > 0 ? String.valueOf(s.getColumn()) : NO_POSITION,
                s.getName(),
                typeOf(s),
                s.getCategory() == null ? "?" : s.getCategory().label(),
                scopeOf(s),
                s.getLanguage() == null ? "?" : s.getLanguage(),
                s.getOffset()
        };
    }

    private boolean accepts(Symbol s) {
        return filterBar.accepts(
                s.getName(),
                typeOf(s),
                s.getCategory() == null ? "" : s.getCategory().label(),
                scopeOf(s),
                s.getLanguage(),
                s.getSourceFile(),
                s.getLine() > 0 ? String.valueOf(s.getLine()) : NO_POSITION);
    }

    public void clearFilter() {
        filterBar.clear();
    }

    private static String typeOf(Symbol s) {
        return s.getType() == null ? "?" : s.getType().label();
    }

    private static String scopeOf(Symbol s) {
        String scope = s.getScope();
        if (scope != null && !scope.isBlank()) {
            return scope;
        }
        return s.isGlobal() ? "global" : "local";
    }

    public Symbol symbolAt(int row) {
        if (row < 0 || row >= shown.size()) {
            return null;
        }
        return shown.get(row);
    }

    private void showDetails() {
        int row = table.getSelectedRow();
        Symbol s = symbolAt(row);
        if (s == null) {
            detailsArea.setVisible(false);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            String value = String.valueOf(tableModel.getValueAt(row, col));
            if (value != null && !value.isBlank()) {
                sb.append(tableModel.getColumnName(col)).append(": ").append(value).append('\n');
            }
        }

        String extra = describe(s);
        if (!extra.isEmpty()) {
            sb.append("Detalle: ").append(extra).append('\n');
        }
        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
        detailsArea.setVisible(true);
        revalidate();
        repaint();
    }

    private static String describe(Symbol s) {
        StringBuilder sb = new StringBuilder();

        if (s.getSourceFile() != null && !s.getSourceFile().isBlank()) {
            sb.append("de ").append(s.getSourceFile());
        }
        if (s.getParameters() != null && !s.getParameters().isEmpty()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("params(");
            for (int i = 0; i < s.getParameters().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                Symbol p = s.getParameters().get(i);
                sb.append(p.getName()).append(":").append(p.getType() == null ? "?" : p.getType().label());
            }
            sb.append(')');
        }
        if (s.getMembers() != null && !s.getMembers().isEmpty()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("{");
            for (int i = 0; i < s.getMembers().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                Symbol m = s.getMembers().get(i);
                sb.append(m.getName()).append(":").append(m.getType() == null ? "?" : m.getType().label());
            }
            sb.append('}');
        }
        if (s.getReturnType() != null) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("-> ").append(s.getReturnType().label());
        }
        return sb.length() == 0 ? "" : sb.toString();
    }

    public void setOnSymbolActivated(IntConsumer consumer) {
        this.onSymbolActivated = consumer;
    }

    public JTable getTable() {
        return table;
    }

    public int getSymbolCount() {
        return shown.size();
    }

    public int getTotalSymbolCount() {
        return allSymbols.size();
    }

    public List<String> getColumnNames() {
        List<String> names = new ArrayList<>(tableModel.getColumnCount());
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            names.add(tableModel.getColumnName(i));
        }
        return names;
    }

    public void clear() {
        loadSymbols(null);
    }
}
