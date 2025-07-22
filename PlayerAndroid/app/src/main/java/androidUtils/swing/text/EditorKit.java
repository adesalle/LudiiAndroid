package androidUtils.swing.text;

public interface EditorKit {
    void install(JTextComponent c);
    void deinstall(JTextComponent c);
    void read(String html);
    String write();
    Document createDefaultDocument();
}
