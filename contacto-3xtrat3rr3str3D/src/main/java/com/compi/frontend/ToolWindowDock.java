package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Dock derecho de herramientas del compilador.
 *
 * Agrupa las cuatro vistas de analisis en pestanas, al estilo de las tool
 * windows de IntelliJ: AST, tabla de simbolos, pila de procesos y errores.
 */
public class ToolWindowDock extends JPanel {

    public enum View {
        AST("Árbol AST", 0),
        SIMBOLOS("Símbolos", 1),
        PILA("Pila", 2),
        ERRORES("Errores", 3);

        private final String title;
        private final int index;

        View(String title, int index) {
            this.title = title;
            this.index = index;
        }

        public String title() {
            return title;
        }

        public int index() {
            return index;
        }
    }

    private final JTabbedPane tabs = new JTabbedPane();
    private final ParserTreePanel astPanel;
    private final SymbolTablePanel symbolPanel;
    private final StackVisualizerPanel stackPanel;
    private final ErrorTablePanel errorPanel;

    private int errorCount = 0;

    public ToolWindowDock(ParserTreePanel astPanel, SymbolTablePanel symbolPanel,
                          StackVisualizerPanel stackPanel, ErrorTablePanel errorPanel) {
        this.astPanel = astPanel;
        this.symbolPanel = symbolPanel;
        this.stackPanel = stackPanel;
        this.errorPanel = errorPanel;

        setLayout(new BorderLayout());
        setBorder(UiTheme.hairlineLeft());

        tabs.setFont(UiTheme.sans(12));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addTab(View.AST.title(), astPanel);
        tabs.addTab(View.SIMBOLOS.title(), symbolPanel);
        tabs.addTab(View.PILA.title(), stackPanel);
        tabs.addTab(View.ERRORES.title(), errorPanel);

        add(tabs, BorderLayout.CENTER);
    }

    // ====================== Navegacion ======================

    /** Selecciona la vista pedida. */
    public void select(View view) {
        int idx = view.index();
        if (idx >= 0 && idx < tabs.getTabCount()) {
            tabs.setSelectedIndex(idx);
        }
    }

    public View getSelected() {
        int idx = tabs.getSelectedIndex();
        if (idx < 0) {
            return View.AST;
        }
        for (View v : View.values()) {
            if (v.index == idx) {
                return v;
            }
        }
        return View.AST;
    }

    // ====================== Accesos a las vistas ======================

    public ParserTreePanel getAstPanel() {
        return astPanel;
    }

    public SymbolTablePanel getSymbolPanel() {
        return symbolPanel;
    }

    public StackVisualizerPanel getStackPanel() {
        return stackPanel;
    }

    public ErrorTablePanel getErrorPanel() {
        return errorPanel;
    }

    /**
     * Actualiza el contador de errores en el titulo de la pestaña.
     */
    public void setErrorCount(int count) {
        this.errorCount = count;
        refreshErrorTabTitle();
    }

    public int getErrorCount() {
        return errorCount;
    }

    private void refreshErrorTabTitle() {
        if (errorCount > 0) {
            tabs.setTitleAt(View.ERRORES.index(),
                    View.ERRORES.title() + "  (" + errorCount + ")");
        } else {
            tabs.setTitleAt(View.ERRORES.index(), View.ERRORES.title());
        }
    }

    /** Aplica el tema a las vistas contenidas. */
    public void applyTheme() {
        for (Component c : tabs.getComponents()) {
            if (c instanceof javax.swing.JComponent jc) {
                jc.putClientProperty("FlatLaf.updateUI", Boolean.TRUE);
                jc.updateUI();
            }
        }
    }
}
