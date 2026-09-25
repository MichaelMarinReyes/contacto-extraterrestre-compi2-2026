package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Dock inferior de salidas del compilador.
 *
 * Contiene las seis vistas que produce el backend: consola de compilacion,
 * tripletes, cuartetas, codigo de tres direcciones, codigo C y el resumen.
 * Cada vista es un area de texto de solo lectura con fuente monoespaciada.
 */
public class ConsoleDock extends JPanel {

    public enum View {
        CONSOLA("Consola", IdeIcons.compile()),
        TRIPLETS("Tripletes", null),
        CUARTETAS("Cuartetas", null),
        C3D("Código 3D", null),
        CODIGO_C("Código C", null),
        RESUMEN("Resumen", null);

        private final String title;
        private final javax.swing.Icon icon;

        View(String title, javax.swing.Icon icon) {
            this.title = title;
            this.icon = icon;
        }

        public String title() {
            return title;
        }

        public javax.swing.Icon icon() {
            return icon;
        }
    }

    private final JTabbedPane tabs = new JTabbedPane();
    private final JTextArea consoleArea = area();
    private final JTextArea tripletsArea = area();
    private final JTextArea quadruplesArea = area();
    private final JTextArea c3dArea = area();
    private final JTextArea cCodeArea = area();
    private final JTextArea summaryArea = area();

    private final JLabel statusLabel = new JLabel(" ");

    public ConsoleDock() {
        setLayout(new BorderLayout());
        setBorder(UiTheme.hairlineTop());

        tabs.setFont(UiTheme.sans(12));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab(View.CONSOLA.title(), new Scroller(consoleArea));
        tabs.addTab(View.TRIPLETS.title(), new Scroller(tripletsArea));
        tabs.addTab(View.CUARTETAS.title(), new Scroller(quadruplesArea));
        tabs.addTab(View.C3D.title(), new Scroller(c3dArea));
        tabs.addTab(View.CODIGO_C.title(), new Scroller(cCodeArea));
        tabs.addTab(View.RESUMEN.title(), new Scroller(summaryArea));

        add(tabs, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new java.awt.BorderLayout());
        bar.setBorder(UiTheme.pad(2, 8, 2, 8));
        statusLabel.setFont(UiTheme.sans(11));
        statusLabel.setForeground(UiTheme.dim());
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    /** Area de texto de solo lectura con la tipografia del tema. */
    private static JTextArea area() {
        JTextArea a = new JTextArea();
        a.setEditable(false);
        a.setLineWrap(false);
        a.setTabSize(4);
        a.setFont(UiTheme.mono(13));
        a.setBackground(UiTheme.editorBg());
        a.setForeground(UiTheme.fg());
        a.setCaretColor(UiTheme.fg());
        a.setSelectionColor(UiTheme.accent());
        a.setSelectedTextColor(java.awt.Color.WHITE);
        a.setBorder(UiTheme.pad(4, 8, 4, 8));
        return a;
    }

    /** Envoltura simple para no repetir el JScrollPane en cada pestana. */
    private static class Scroller extends JScrollPane {
        Scroller(JTextArea area) {
            super(area);
            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setBackground(UiTheme.editorBg());
            getVerticalScrollBar().setUnitIncrement(18);
        }
    }

    // ====================== Contenido ======================

    public void setConsole(String text) {
        set(consoleArea, text);
    }

    public void appendConsole(String text) {
        consoleArea.append(text);
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    public void setTriplets(String text) {
        set(tripletsArea, text);
    }

    public void setQuadruples(String text) {
        set(quadruplesArea, text);
    }

    public void setC3D(String text) {
        set(c3dArea, text);
    }

    public void setCCode(String text) {
        set(cCodeArea, text);
    }

    public void setSummary(String text) {
        set(summaryArea, text);
    }

    public void setStatus(String text) {
        statusLabel.setText(text == null || text.isBlank() ? " " : text);
    }

    public String getStatus() {
        return statusLabel.getText();
    }

    /** Selecciona la vista indicada. */
    public void select(View view) {
        int idx = view.ordinal();
        if (idx >= 0 && idx < tabs.getTabCount()) {
            tabs.setSelectedIndex(idx);
        }
    }

    public View getSelectedView() {
        int idx = tabs.getSelectedIndex();
        return idx < 0 ? View.CONSOLA : View.values()[idx];
    }

    /** Limpia todas las vistas. */
    public void clearAll() {
        for (JTextArea a : all()) {
            a.setText("");
        }
        setStatus(" ");
    }

    public JTextArea getConsoleArea() {
        return consoleArea;
    }

    public JTextArea getTripletsArea() {
        return tripletsArea;
    }

    public JTextArea getQuadruplesArea() {
        return quadruplesArea;
    }

    public JTextArea getC3dArea() {
        return c3dArea;
    }

    public JTextArea getCCodeArea() {
        return cCodeArea;
    }

    public JTextArea getSummaryArea() {
        return summaryArea;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    private JTextArea[] all() {
        return new JTextArea[]{consoleArea, tripletsArea, quadruplesArea, c3dArea, cCodeArea, summaryArea};
    }

    private static void set(JTextArea area, String text) {
        area.setText(text == null ? "" : text);
        area.setCaretPosition(0);
    }

    /** Aplica los colores del tema a todas las areas. */
    public void applyTheme() {
        for (JTextArea a : all()) {
            a.setBackground(UiTheme.editorBg());
            a.setForeground(UiTheme.fg());
        }
    }

    /** Permite añadir pestañas extra desde MainWindow sin tocar la vista. */
    public void addView(String title, Component component) {
        tabs.addTab(title, component);
    }

    /** Cuantifica cuántas pestañas hay visibles. */
    public int getViewCount() {
        return tabs.getTabCount();
    }

    /** Altura preferida del dock. */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(120, d.height));
    }
}
