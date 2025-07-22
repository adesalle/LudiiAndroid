package androidUtils.swing.text;

import androidUtils.swing.StyleSheet;
import androidUtils.swing.event.DocumentListener;

public interface Document {
    int getLength();
    String getText(int offset, int length) throws BadLocationException;
    void insertString(int offset, String str, AttributeSet attr) throws BadLocationException;
    void remove(int offset, int length) throws BadLocationException;
    void putProperty(Object key, Object value);
    Object getProperty(Object key);

    void setDocumentFilter(DocumentFilter documentFilter);

    // Implémentation basique de Document
    String getText();

    StyleSheet getStyleSheet();

    void addDocumentListener(DocumentListener listenerText1);

    void removeDocumentListener(DocumentListener listener);
}