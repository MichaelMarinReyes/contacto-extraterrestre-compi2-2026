package com.compi.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class EditorPanel extends JPanel {

    private JTextPane codeTextArea;
    private LineNumberComponent lineNumberComponent;
    private SyntaxHighlighter highlighter;

    private File file;
    private String languageId = "pig";
    private boolean modified;
    private boolean suppressChangeEvents;

    private final Consumer<EditorPanel> onCaretMove;
    private final Runnable onModified;

    public EditorPanel(Consumer<EditorPanel> onCaretMove, Runnable onModified) {
        this.onCaretMove = onCaretMove;
        this.onModified = onModified;
        initComponents();
        initCustomEditor();
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 704, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 387, Short.MAX_VALUE)
        );
    }

    private void initCustomEditor() {
        setLayout(new BorderLayout());
        setBorder(UiTheme.hairlineTop());

        codeTextArea = new JTextPane();
        codeTextArea.setFont(UiTheme.mono(14));
        codeTextArea.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        codeTextArea.setBackground(UiTheme.editorBg());
        codeTextArea.setForeground(UiTheme.fg());
        codeTextArea.setCaretColor(UiTheme.fg());
        codeTextArea.setSelectionColor(UiTheme.accent());
        codeTextArea.setSelectedTextColor(Color.WHITE);
        codeTextArea.setBorder(UiTheme.pad(4, 8, 4, 8));

        highlighter = new SyntaxHighlighter(codeTextArea);
        lineNumberComponent = new LineNumberComponent(codeTextArea);

        JScrollPane scroll = new JScrollPane(codeTextArea);
        scroll.setRowHeaderView(lineNumberComponent);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UiTheme.editorBg());
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);

        attachListeners();
    }

    private void attachListeners() {
        codeTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        codeTextArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (onCaretMove != null) {
                    onCaretMove.accept(EditorPanel.this);
                }
            }
        });
    }

    private void documentChanged() {
        highlighter.rehighlight();
        if (suppressChangeEvents) {
            return;
        }
        if (!modified) {
            modified = true;
            if (onModified != null) {
                onModified.run();
            }
        }
    }

    public String getCodeText() {
        return codeTextArea.getText();
    }

    public void setCodeText(String text) {
        suppressChangeEvents = true;
        try {
            codeTextArea.setText(text == null ? "" : text);
            codeTextArea.setCaretPosition(0);
            modified = false;
        } finally {
            suppressChangeEvents = false;
        }
        highlighter.rehighlight();
    }

    public void insertAtCaret(String text) {
        try {
            codeTextArea.getDocument().insertString(codeTextArea.getCaretPosition(), text, null);
        } catch (BadLocationException ignored) {

        }
    }

    public JTextPane getTextPane() {
        return codeTextArea;
    }

    public LineNumberComponent getLineNumberComponent() {
        return lineNumberComponent;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDisplayName() {
        return file != null ? file.getName() : "Sin titulo";
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
        highlighter.setLanguage(languageId);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public int getCaretLine() {
        return rootElement().getElementIndex(codeTextArea.getCaretPosition()) + 1;
    }

    public int getCaretColumn() {
        var root = rootElement();
        int index = root.getElementIndex(codeTextArea.getCaretPosition());
        return codeTextArea.getCaretPosition() - root.getElement(index).getStartOffset() + 1;
    }

    public int getLineCount() {
        return rootElement().getElementCount();
    }

    public void gotoLine(int line) {
        var root = rootElement();
        int idx = Math.max(0, Math.min(root.getElementCount() - 1, line - 1));
        int pos = root.getElement(idx).getStartOffset();
        codeTextArea.setCaretPosition(Math.min(pos, codeTextArea.getDocument().getLength()));
    }

    private javax.swing.text.Element rootElement() {
        return codeTextArea.getDocument().getDefaultRootElement();
    }

    public void refreshHighlight() {
        SwingUtilities.invokeLater(highlighter::rehighlight);
    }

    public void applyTheme() {
        codeTextArea.setBackground(UiTheme.editorBg());
        codeTextArea.setForeground(UiTheme.fg());
        codeTextArea.setCaretColor(UiTheme.fg());
        if (lineNumberComponent != null) {
            lineNumberComponent.applyTheme();
        }
        refreshHighlight();
    }

    static void setForeground(javax.swing.text.Style style, Color color) {
        StyleConstants.setForeground(style, color);
    }
}
