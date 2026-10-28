package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class ConsoleDock extends JPanel {

    public enum View {
        CONSOLE("Consola", IdeIcons.compile()),
        TRIPLETS("Tripletes", null),
        QUADRUPLE("Cuartetas", null),
        C3D("Código 3D", null),
        C_CODE("Código C", null),
        ASSEMBLY("Ensamblador", null),
        RUN("Simulador", IdeIcons.run()),
        SUMMARY("Resumen", null);

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
    private final JTextArea assemblyArea = area();
    private final JTextArea runOutput = area();
    private final JTextArea runInput = inputArea();
    private final JTextArea summaryArea = area();

    private final JLabel statusLabel = new JLabel(" ");
    private final JButton runSendButton = new JButton("Enviar");
    private Runnable runInputSubmit;
    private boolean runInteractive;

    public ConsoleDock() {
        setLayout(new BorderLayout());
        setBorder(UiTheme.hairlineTop());

        tabs.setFont(UiTheme.sans(12));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab(View.CONSOLE.title(), new Scroller(consoleArea));
        tabs.addTab(View.TRIPLETS.title(), new Scroller(tripletsArea));
        tabs.addTab(View.QUADRUPLE.title(), new Scroller(quadruplesArea));
        tabs.addTab(View.C3D.title(), new Scroller(c3dArea));
        tabs.addTab(View.C_CODE.title(), new Scroller(cCodeArea));
        tabs.addTab(View.ASSEMBLY.title(), new Scroller(assemblyArea));
        tabs.addTab(View.RUN.title(), buildRunPanel());
        tabs.addTab(View.SUMMARY.title(), new Scroller(summaryArea));

        add(tabs, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
        installRunInputKeys();
        setRunInteractive(false);
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new java.awt.BorderLayout());
        bar.setBorder(UiTheme.pad(2, 8, 2, 8));
        statusLabel.setFont(UiTheme.sans(11));
        statusLabel.setForeground(UiTheme.dim());
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

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

    private static JTextArea inputArea() {
        JTextArea a = area();
        a.setEditable(true);
        a.setRows(3);
        return a;
    }

    /** Panel del simulador interno: salida de la ejecución y entrada interactiva. */
    private JPanel buildRunPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiTheme.editorBg());
        panel.add(new Scroller(runOutput), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 2));
        south.setBackground(UiTheme.editorBg());
        south.setBorder(UiTheme.pad(3, 8, 3, 8));
        south.add(buildRunInputHeader(), BorderLayout.NORTH);
        south.add(new Scroller(runInput), BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRunInputHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel label = new JLabel("Entrada opcional del programa (Enter envía una línea):");
        label.setFont(UiTheme.sans(11));
        label.setForeground(UiTheme.dim());
        header.add(label, BorderLayout.WEST);
        runSendButton.setFont(UiTheme.sans(11));
        runSendButton.setFocusable(false);
        runSendButton.setEnabled(false);
        UiTheme.asButton(runSendButton);
        runSendButton.addActionListener(e -> {
            if (runInputSubmit != null) {
                runInputSubmit.run();
            }
        });
        header.add(runSendButton, BorderLayout.EAST);
        return header;
    }

    private static class Scroller extends JScrollPane {
        Scroller(JTextArea area) {
            super(area);
            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setBackground(UiTheme.editorBg());
            getVerticalScrollBar().setUnitIncrement(18);
        }
    }

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

    public void setAssembly(String text) {
        set(assemblyArea, text);
    }

    public void setRunOutput(String text) {
        set(runOutput, text);
    }

    public void appendRunOutput(String text) {
        runOutput.append(text == null ? "" : text);
        runOutput.setCaretPosition(runOutput.getDocument().getLength());
    }

    public String getRunInput() {
        return runInput.getText();
    }

    public void setRunInput(String text) {
        runInput.setText(text == null ? "" : text);
    }

    /** Registra la acción que envía la entrada escrita al programa en ejecución. */
    public void setOnRunInputSubmit(Runnable action) {
        this.runInputSubmit = action;
    }

    /** Habilita el envío por Enter y el botón Enviar mientras hay una ejecución. */
    public void setRunInteractive(boolean running) {
        this.runInteractive = running;
        runSendButton.setEnabled(running);
        runInput.setToolTipText(running
                ? "Se usa solo si el programa ejecuta una instrucción de lectura."
                : "Entrada opcional para programas que contienen instrucciones de lectura.");
    }

    private void installRunInputKeys() {
        Action submit = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (runInteractive && runInputSubmit != null) {
                    runInputSubmit.run();
                } else {
                    runInput.replaceSelection("\n");
                }
            }
        };
        runInput.getActionMap().put("runInputEnter", submit);
        runInput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "runInputEnter");
        runInput.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK),
                DefaultEditorKit.insertBreakAction);
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

    public void select(View view) {
        int idx = view.ordinal();
        if (idx >= 0 && idx < tabs.getTabCount()) {
            tabs.setSelectedIndex(idx);
        }
    }

    public View getSelectedView() {
        int idx = tabs.getSelectedIndex();
        return idx < 0 ? View.CONSOLE : View.values()[idx];
    }

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

    public JTextArea getAssemblyArea() {
        return assemblyArea;
    }

    public JTextArea getSummaryArea() {
        return summaryArea;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    private JTextArea[] all() {
        return new JTextArea[]{consoleArea, tripletsArea, quadruplesArea, c3dArea, cCodeArea,
                assemblyArea, runOutput, summaryArea};
    }

    private static void set(JTextArea area, String text) {
        area.setText(text == null ? "" : text);
        area.setCaretPosition(0);
    }

    public void applyTheme() {
        for (JTextArea a : all()) {
            a.setBackground(UiTheme.editorBg());
            a.setForeground(UiTheme.fg());
        }
    }

    public void addView(String title, Component component) {
        tabs.addTab(title, component);
    }

    public int getViewCount() {
        return tabs.getTabCount();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(120, d.height));
    }
}
