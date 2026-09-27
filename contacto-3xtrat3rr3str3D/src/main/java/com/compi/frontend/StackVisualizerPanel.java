package com.compi.frontend;

import com.compi.backend.StackAction;
import com.compi.backend.parser.ParseStep;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

public class StackVisualizerPanel extends JPanel {

    private static final int START_X = 20;
    private static final int COL_WIDTH = 180;
    private static final int CARD_WIDTH = COL_WIDTH - 10;
    private static final int STEP_NUM_Y = 15;
    private static final int STEP_ACTION_Y = 27;
    private static final int CARD_TOP = 32;
    private static final int CARD_PADDING = 8;
    private static final int BOX_HEIGHT = 22;
    private static final int BOX_GAP = 4;
    private static final int BOX_INSET = 6;
    private static final int BADGE_HEIGHT = 24;
    private static final int BADGE_GAP = 6;
    private static final int BOTTOM_MARGIN = 12;
    private static final int MIN_CARD_HEIGHT = 120;

    private static final int CLIP_THRESHOLD = 60;

    private static final Color CARD_BG = new Color(254, 243, 199);
    private static final Color CARD_BORDER = new Color(217, 119, 6);
    private static final Color TOP_BOX_BG = new Color(191, 219, 254);
    private static final Color BOX_BG = new Color(254, 202, 202);
    private static final Color BOX_BORDER = new Color(140, 140, 140);
    private static final Color BOX_FG = new Color(20, 20, 20);
    private static final Color BADGE_BG = new Color(187, 247, 208);
    private static final Color BADGE_BORDER = new Color(22, 101, 52);
    private static final Color BADGE_FG = new Color(20, 83, 45);
    private static final Color EMPTY_FG = new Color(120, 120, 120);
    private static final Color SHIFT_FG = new Color(134, 239, 172);
    private static final Color REDUCE_FG = new Color(253, 186, 116);

    private List<ParseStep> steps = new ArrayList<>();
    private JPanel stackDrawPanel;
    private JTextArea logTextArea;
    private JLabel countLabel;
    private JSplitPane splitPane;

    public StackVisualizerPanel() {
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

        stackDrawPanel = new StackCanvas();
        stackDrawPanel.setBackground(UiTheme.toolWindowBg());
        stackDrawPanel.setPreferredSize(new Dimension(900, 400));

        JScrollPane stackScroll = new JScrollPane(stackDrawPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        stackScroll.setBorder(BorderFactory.createEmptyBorder());
        stackScroll.getViewport().setBackground(UiTheme.toolWindowBg());

        countLabel = new JLabel("0 pasos", SwingConstants.RIGHT);
        countLabel.setFont(UiTheme.sans(11));
        countLabel.setForeground(UiTheme.dim());
        countLabel.setBorder(UiTheme.pad(0, 8, 0, 10));

        JPanel stackBar = new JPanel(new BorderLayout());
        stackBar.setOpaque(false);
        stackBar.add(title("Secuencia de Estados de la Pila (Historial Completo)", 12),
                BorderLayout.WEST);
        stackBar.add(countLabel, BorderLayout.EAST);
        stackBar.setBorder(UiTheme.pad(6, 2, 4, 0));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(stackBar, BorderLayout.NORTH);
        topContainer.add(stackScroll, BorderLayout.CENTER);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(false);
        logTextArea.setFont(UiTheme.mono(11));
        logTextArea.setBackground(UiTheme.toolWindowBg());
        logTextArea.setForeground(UiTheme.fg());
        logTextArea.setBorder(UiTheme.pad(2, 4, 2, 4));

        JScrollPane logScroll = new JScrollPane(logTextArea);
        logScroll.setBorder(BorderFactory.createEmptyBorder());
        logScroll.getViewport().setBackground(UiTheme.toolWindowBg());

        JPanel logBar = new JPanel(new BorderLayout());
        logBar.setOpaque(false);
        logBar.add(title("Consola de Logs / Operaciones (shift - reduce):", 11),
                BorderLayout.WEST);
        logBar.setBorder(UiTheme.pad(4, 2, 2, 0));

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.add(logBar, BorderLayout.NORTH);
        bottomContainer.add(logScroll, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topContainer, bottomContainer);
        splitPane.setResizeWeight(0.75);
        splitPane.setDividerLocation(360);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }

    private JLabel title(String text, int size) {
        JLabel label = new JLabel("  " + text);
        label.setFont(UiTheme.sansBold(size));
        label.setForeground(UiTheme.fg());
        return label;
    }

    public void loadSteps(List<ParseStep> nuevos) {
        steps = nuevos == null ? new ArrayList<>() : new ArrayList<>(nuevos);

        int maxDepth = 1;
        for (ParseStep st : steps) {
            maxDepth = Math.max(maxDepth, st.getStack().size());
        }

        int alto = CARD_TOP + cardHeight(maxDepth) + BADGE_GAP + BADGE_HEIGHT + 2 * BOTTOM_MARGIN;
        stackDrawPanel.setPreferredSize(new Dimension(
                Math.max(900, steps.size() * COL_WIDTH + 2 * START_X), alto));

        countLabel.setText(steps.size() + (steps.size() == 1 ? " paso" : " pasos"));
        updateLogs();
        stackDrawPanel.revalidate();
        stackDrawPanel.repaint();
    }

    public int getStateCount() {
        return steps.size();
    }

    public int getStepCount() {
        return steps.size();
    }

    public void clear() {
        loadSteps(null);
    }

    private int cardHeight(int maxDepth) {
        return Math.max(MIN_CARD_HEIGHT, maxDepth * (BOX_HEIGHT + BOX_GAP) + 2 * CARD_PADDING);
    }

    private void updateLogs() {
        StringBuilder logs = new StringBuilder();
        for (ParseStep st : steps) {
            logs.append(st).append('\n');
        }
        logTextArea.setText(logs.toString());
        logTextArea.setCaretPosition(0);
    }

    private class StackCanvas extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawAllSteps(g);
        }
    }

    private void drawAllSteps(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (steps.isEmpty()) {
            g2.setColor(UiTheme.dim());
            g2.setFont(UiTheme.sans(13));
            g2.drawString("No hay pila que mostrar. Compile un archivo para ver el trazo "
                    + "de shift y reduce.", 24, 44);
            g2.dispose();
            return;
        }

        int maxDepth = 1;
        for (ParseStep st : steps) {
            maxDepth = Math.max(maxDepth, st.getStack().size());
        }
        int cardH = cardHeight(maxDepth);
        int badgeY = CARD_TOP + cardH + BADGE_GAP;

        for (int i = firstVisibleColumn(); i <= lastVisibleColumn(); i++) {
            drawStep(g2, steps.get(i), i, cardH, badgeY);
        }
        g2.dispose();
    }

    private void drawStep(Graphics2D g2, ParseStep state, int i, int cardH, int badgeY) {
        int x = START_X + i * COL_WIDTH;
        List<String> elementos = state.getStack();

        g2.setFont(UiTheme.sansBold(12));
        g2.setColor(colorDe(state.getType()));
        String number = String.valueOf(state.getNumber());
        g2.drawString(number, x + (CARD_WIDTH - g2.getFontMetrics().stringWidth(number)) / 2,
                STEP_NUM_Y);

        g2.setFont(UiTheme.sans(9));
        String label = state.getType().getTag();
        g2.drawString(label, x + (CARD_WIDTH - g2.getFontMetrics().stringWidth(label)) / 2,
                STEP_ACTION_Y);

        g2.setColor(CARD_BG);
        g2.fillRoundRect(x, CARD_TOP, CARD_WIDTH, cardH, 10, 10);
        g2.setColor(CARD_BORDER);
        g2.drawRoundRect(x, CARD_TOP, CARD_WIDTH, cardH, 10, 10);

        int boxW = CARD_WIDTH - 2 * BOX_INSET;

        if (elementos.isEmpty()) {
            g2.setColor(EMPTY_FG);
            g2.setFont(UiTheme.sans(11));
            FontMetrics fm = g2.getFontMetrics();
            String vacia = "Pila vacía";
            g2.drawString(vacia, x + (CARD_WIDTH - fm.stringWidth(vacia)) / 2,
                    CARD_TOP + CARD_PADDING + fm.getAscent());
        } else {

            g2.setFont(UiTheme.sansBold(9));
            FontMetrics fm = g2.getFontMetrics();
            int y = CARD_TOP + CARD_PADDING;
            for (int j = elementos.size() - 1; j >= 0; j--) {
                boolean isTop = j == elementos.size() - 1;
                g2.setColor(isTop ? TOP_BOX_BG : BOX_BG);
                g2.fillRoundRect(x + BOX_INSET, y, boxW, BOX_HEIGHT, 5, 5);
                g2.setColor(BOX_BORDER);
                g2.drawRoundRect(x + BOX_INSET, y, boxW, BOX_HEIGHT, 5, 5);

                g2.setColor(BOX_FG);
                String text = trim(elementos.get(j), fm, boxW - 6);
                g2.drawString(text, x + BOX_INSET + (boxW - fm.stringWidth(text)) / 2,
                        y + (BOX_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
                y += BOX_HEIGHT + BOX_GAP;
            }
        }

        g2.setFont(UiTheme.sans(9));
        FontMetrics fmBadge = g2.getFontMetrics();
        String detail = trim(badge(state), fmBadge, CARD_WIDTH - 12);
        g2.setColor(BADGE_BG);
        g2.fillRoundRect(x + 2, badgeY, CARD_WIDTH - 4, BADGE_HEIGHT, 6, 6);
        g2.setColor(BADGE_BORDER);
        g2.drawRoundRect(x + 2, badgeY, CARD_WIDTH - 4, BADGE_HEIGHT, 6, 6);
        g2.setColor(BADGE_FG);
        g2.drawString(detail, x + 2 + (CARD_WIDTH - 4 - fmBadge.stringWidth(detail)) / 2,
                badgeY + (BADGE_HEIGHT - fmBadge.getHeight()) / 2 + fmBadge.getAscent());
    }

    private static String badge(ParseStep state) {
        if (state.getType() == StackAction.SHIFT) {
            String terminal = state.getTerminal();
            if (terminal == null || terminal.equals(state.getSymbol())) {
                return state.getSymbol();
            }
            return terminal + ": " + state.getSymbol();
        }
        return "retira " + state.getConsumed().size();
    }

    private static Color colorDe(StackAction type) {
        return type == StackAction.SHIFT ? SHIFT_FG : REDUCE_FG;
    }

    private static String trim(String text, FontMetrics fm, int maxWidth) {
        if (text == null) {
            return "";
        }
        String t = text;
        while (t.length() > 3 && fm.stringWidth(t) > maxWidth) {
            t = t.substring(0, t.length() - 1);
        }
        return t.equals(text) ? t : t + "..";
    }

    private int firstVisibleColumn() {
        return visibleRange()[0];
    }

    private int lastVisibleColumn() {
        return visibleRange()[1];
    }

    private int[] visibleRange() {
        int last = steps.size() - 1;
        Container padre = stackDrawPanel.getParent();
        if (steps.size() <= CLIP_THRESHOLD || !(padre instanceof JViewport)) {
            return new int[]{0, last};
        }
        Point position = ((JViewport) padre).getViewPosition();
        int width = ((JViewport) padre).getExtentSize().width;
        int first = Math.max(0, (position.x - START_X) / COL_WIDTH);
        int fin = Math.max(0, Math.min(last, (position.x + width - START_X) / COL_WIDTH));
        return new int[]{first, Math.max(first, fin)};
    }
}
