package androidUtils.swing.text;

public class DefaultEditorKit implements EditorKit {
    public static final String EndOfLineStringProperty = "\n";
    public static final String insertContentAction = "insert-content";
    public static final String insertBreakAction = "insert-break";
    public static final String insertTabAction = "insert-tab";
    public static final String deletePrevCharAction = "delete-previous";
    public static final String deleteNextCharAction = "delete-next";

    private HTMLDocument document;
    private JTextComponent textComponent;

    @Override
    public void install(JTextComponent c) {
        this.textComponent = c;
        this.document = c.getDocument();
    }

    @Override
    public void deinstall(JTextComponent c) {
        this.textComponent = null;
        this.document = null;
    }

    @Override
    public void read(String html) {
        if (document != null) {
            document.setHTML(html);
            if (textComponent != null) {
                textComponent.setText(document.getDocument());
            }
        }
    }

    @Override
    public String write() {
        return document != null ? document.getDocument().toString() : "";
    }

    @Override
    public Document createDefaultDocument() {
        return new HTMLDocument();
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (document != null) {
            document.insertString(offset, str, (Style) attr);
        }
    }

    public void remove(int offset, int len) throws BadLocationException {
        if (document != null) {
            document.remove(offset, len);
        }
    }
}