package androidUtils.swing.text;

public class DefaultEditorKit implements EditorKit {
    public static final String EndOfLineStringProperty = "\n";
    public static final String insertContentAction = "insert-content";
    public static final String insertBreakAction = "insert-break";
    public static final String insertTabAction = "insert-tab";
    public static final String deletePrevCharAction = "delete-previous";
    public static final String deleteNextCharAction = "delete-next";


    @Override
    public void install(JTextComponent c) {

    }

    @Override
    public void deinstall(JTextComponent c) {

    }

    @Override
    public void read(String html) {

    }

    @Override
    public String write() {
        return "";
    }

    @Override
    public Document createDefaultDocument() {
        return null;
    }
}