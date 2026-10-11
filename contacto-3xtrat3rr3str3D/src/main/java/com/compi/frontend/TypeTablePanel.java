package com.compi.frontend;

import com.compi.backend.symbols.Type;
import com.compi.backend.symbols.TypeEntry;
import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Presenta los tipos del archivo enfocado y sus referencias dentro de esa tabla. */
public class TypeTablePanel extends JPanel {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Tipo base", "Padre", "Dimensión", "Mínimo", "Máximo", "Ámbito"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableFilterBar filterBar = new TableFilterBar("Filtrar tipos");
    private final JLabel sourceLabel = new JLabel("Sin tipos de compilación");
    private List<TypeEntry> entries = List.of();
    private final Map<Integer, TypeEntry> byId = new LinkedHashMap<>();

    public TypeTablePanel() {
        setLayout(new BorderLayout());
        initTable();
        filterBar.setOnFilterChanged(this::applyFilter);
        filterBar.setBorder(UiTheme.pad(6, 8, 6, 8));
        sourceLabel.setFont(UiTheme.sans(12));
        sourceLabel.setForeground(UiTheme.dim());
        sourceLabel.setBorder(UiTheme.pad(6, 8, 6, 8));
        JPanel header = new JPanel(new BorderLayout());
        header.add(sourceLabel, BorderLayout.NORTH);
        header.add(filterBar, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    private void initTable() {
        table.setFont(UiTheme.mono(12));
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setBackground(UiTheme.toolWindowBg());
        table.setForeground(UiTheme.fg());
        table.setGridColor(UiTheme.separator());
        table.getTableHeader().setFont(UiTheme.sansBold(12));
        table.getTableHeader().setReorderingAllowed(false);
        int[] widths = {40, 150, 130, 100, 75, 90, 90, 180};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    public void loadTypes(List<TypeEntry> types, String fileName) {
        entries = types == null ? List.of() : List.copyOf(types);
        byId.clear();
        for (TypeEntry entry : entries) byId.put(entry.id(), entry);
        sourceLabel.setText(entries.isEmpty() ? "Sin tipos de compilación" : "Tipos de " + fileName);
        applyFilter();
    }

    private void applyFilter() {
        model.setRowCount(0);
        for (TypeEntry entry : entries) {
            Object[] row = rowOf(entry);
            String[] cells = java.util.Arrays.stream(row).map(String::valueOf).toArray(String[]::new);
            if (filterBar.accepts(cells)) model.addRow(row);
        }
        filterBar.setCounts(model.getRowCount(), entries.size());
    }

    private Object[] rowOf(TypeEntry entry) {
        return new Object[]{entry.id(), entry.name(), reference(entry.baseTypeId()), reference(entry.parentId()),
                entry.dimension(), bounds(entry.minimum()), bounds(entry.maximum()),
                entry.scopeName() + " (#" + entry.scopeId() + ")"};
    }

    private String reference(Integer id) {
        if (id == null) return "-";
        TypeEntry target = byId.get(id);
        return target == null ? String.valueOf(id) : id + " · " + target.name();
    }

    private String bounds(List<Integer> values) {
        if (values.isEmpty()) return "-";
        return values.stream().map(value -> value == Type.UNKNOWN_SIZE ? "?" : value.toString())
                .collect(java.util.stream.Collectors.joining(", "));
    }

    public JTable getTable() { return table; }
    public int getTypeCount() { return entries.size(); }

    public void clear() {
        loadTypes(List.of(), "");
        filterBar.clear();
    }
}
