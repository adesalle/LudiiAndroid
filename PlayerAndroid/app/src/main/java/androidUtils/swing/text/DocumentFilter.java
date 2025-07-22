package androidUtils.swing.text;

public abstract class DocumentFilter {
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        fb.insertString(offset, string, attr);
    }

    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        fb.remove(offset, length);
    }

    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        fb.replace(offset, length, text, attrs);
    }

    public interface FilterBypass {
        Document getDocument();
        void insertString(int offset, String string, AttributeSet attr) throws BadLocationException;
        void remove(int offset, int length) throws BadLocationException;
        void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException;
    }
}
