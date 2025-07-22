package androidUtils.swing.event;

public interface DocumentListener {
    void insertUpdate(DocumentEvent e);
    void removeUpdate(DocumentEvent e);
    void changedUpdate(DocumentEvent e);
}