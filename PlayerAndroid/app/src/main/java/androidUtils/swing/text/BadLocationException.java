package androidUtils.swing.text;

public class BadLocationException extends Exception {
    private int offset;

    public BadLocationException(String message, int offset) {
        super(message);
        this.offset = offset;
    }

    public int offset() {
        return offset;
    }
}