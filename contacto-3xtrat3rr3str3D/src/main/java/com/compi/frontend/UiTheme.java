package com.compi.frontend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Tokens visuales compartidos por todos los paneles.
 *
 * Delega en los valores de {@link UIManager} (que fija FlatLaf) para que el
 * tema se respete automaticamente y solo define lo que la distribucion por
 * defecto de IntelliJ no cubre.
 */
public final class UiTheme {

    /** Marca los ajustes ya aplicados, para que sean idempotentes. */
    private static final Set<String> APPLIED = new HashSet<>();

    private UiTheme() {
    }

    // ---------- Tipografías ----------

    public static final String MONO = "JetBrains Mono";
    public static final String SANS = "Inter";

    public static Font mono(int size) {
        return new Font(MONO, Font.PLAIN, size);
    }

    public static Font monoBold(int size) {
        return new Font(MONO, Font.BOLD, size);
    }

    public static Font sans(int size) {
        return new Font(SANS, Font.PLAIN, size);
    }

    public static Font sansBold(int size) {
        return new Font(SANS, Font.BOLD, size);
    }

    /** true si la fuente pedida existe en el sistema. */
    public static boolean isFontAvailable(String name) {
        try {
            for (String family : GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames()) {
                if (family.equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Colores derivados de FlatLaf ----------

    public static Color color(String key, Color fallback) {
        Color c = UIManager.getColor(key);
        return c != null ? c : fallback;
    }

    public static Color panelBg() {
        return color("Panel.background", new Color(0x2B2D30));
    }

    public static Color toolWindowBg() {
        return color("ToolWindow.background", new Color(0x2B2D30));
    }

    public static Color headerBg() {
        return color("ToolBar.background", new Color(0x2B2D30));
    }

    public static Color editorBg() {
        return color("EditorPane.background", new Color(0x1E1F22));
    }

    public static Color fg() {
        return color("Label.foreground", new Color(0xBCBEC4));
    }

    public static Color dim() {
        return color("Label.disabledForeground", new Color(0x6B6F76));
    }

    public static Color accent() {
        return color("Component.focusColor", new Color(0x548AF7));
    }

    public static Color error() {
        return color("Component.errorFocusColor", new Color(0xDB5860));
    }

    public static Color warning() {
        return color("Component.warningFocusColor", new Color(0xE2A03F));
    }

    public static Color success() {
        return color("Component.successFocusColor", new Color(0x57A64A));
    }

    public static Color separator() {
        return color("Separator.foreground", new Color(0x393B40));
    }

    // ---------- Bordes ----------

    /** Separador de 1px usando el color de divisor de FlatLaf. */
    public static Border hairline() {
        return BorderFactory.createMatteBorder(0, 0, 1, 0, separator());
    }

    public static Border hairlineRight() {
        return BorderFactory.createMatteBorder(0, 0, 0, 1, separator());
    }

    public static Border hairlineLeft() {
        return BorderFactory.createMatteBorder(0, 1, 0, 0, separator());
    }

    public static Border hairlineTop() {
        return BorderFactory.createMatteBorder(1, 0, 0, 0, separator());
    }

    /** Relleno uniforme de N px. */
    public static Border pad(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    // ---------- Comportamiento ----------

    /** Aplica el cursor de mano, usado por todos los botones de icono. */
    public static <T extends JComponent> T asButton(T c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return c;
    }

    /**
     * Ajustes globales de FlatLaf para aproximar la densidad y los radios de
     * IntelliJ. Es idempotente: puede llamarse mas de una vez.
     */
    public static void applyFlatLafTweaks() {
        if (!APPLIED.add("flatlaf-tweaks")) {
            return;
        }

        UIManager.put("Component.arc", 6);
        UIManager.put("Button.arc", 6);
        UIManager.put("TextComponent.arc", 4);
        UIManager.put("CheckBox.arc", 4);
        UIManager.put("ProgressBar.arc", 4);
        UIManager.put("ScrollBar.arc", 6);
        UIManager.put("ScrollBar.thumbArc", 8);
        UIManager.put("ScrollBar.width", 11);
        UIManager.put("TabbedPane.tabAreaAlignment", "leading");
        UIManager.put("ToolBar.border", BorderFactory.createMatteBorder(0, 0, 1, 0, separator()));
        UIManager.put("ToolBar.floatable", Boolean.FALSE);
        UIManager.put("ToolBar.background", headerBg());
        UIManager.put("Panel.background", panelBg());
        UIManager.put("TabbedPane.background", panelBg());
        UIManager.put("TabbedPane.tabsBackground", panelBg());
        UIManager.put("TabbedPane.contentAreaColor", editorBg());
        UIManager.put("TabbedPane.selectedTabBackground", editorBg());
        UIManager.put("TabbedPane.tabAreaBackground", headerBg());
        UIManager.put("SplitPane.dividerSize", 6);
        UIManager.put("SplitPane.oneTouchExpandable", Boolean.TRUE);
        UIManager.put("ScrollPane.verticalScrollBarWidth", 12);
        UIManager.put("Table.rowHeight", 22);
        UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
        UIManager.put("Table.showVerticalLines", Boolean.TRUE);
        UIManager.put("Table.gridColor", separator());
        UIManager.put("TableHeader.height", 26);
        UIManager.put("Tree.textBackground", "selectionBackground");
        UIManager.put("TreeRowHeight", 24);
        UIManager.put("ComboBox.background", panelBg());
        UIManager.put("TextField.background", color("TextField.background", new Color(0x1E1F22)));
    }

    /** Formatea un identificador de lenguaje para mostrarlo bonito. */
    public static String prettifyLanguage(String raw) {
        if (raw == null || raw.isBlank()) {
            return "-";
        }
        String s = raw.toLowerCase(Locale.ROOT);
        return switch (s) {
            case "pig", "piglatin" -> "PigLatin";
            case "y" -> "Y";
            case "zet", "zetariano" -> "Zetariano";
            default -> raw;
        };
    }
}
