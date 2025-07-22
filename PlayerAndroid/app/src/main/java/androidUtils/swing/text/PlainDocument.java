package androidUtils.swing.text;

import static androidUtils.swing.event.EventType.CHANGE;
import static androidUtils.swing.event.EventType.INSERT;
import static androidUtils.swing.event.EventType.REMOVE;

import android.text.SpannableStringBuilder;
import androidUtils.swing.StyleSheet;
import androidUtils.swing.event.DocumentEvent;
import androidUtils.swing.event.DocumentListener;
import androidUtils.swing.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlainDocument implements Document, DocumentFilter.FilterBypass {
    protected SpannableStringBuilder content;
    private Map<Object, Object> properties;
    private DocumentFilter documentFilter;
    private List<DocumentListener> documentListeners;

    public PlainDocument() {
        content = new SpannableStringBuilder();
        properties = new HashMap<>();
        documentListeners = new ArrayList<>();
    }

    @Override
    public int getLength() {
        return content.length();
    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        if (offset < 0 || offset + length > getLength()) {
            throw new BadLocationException("Invalid range", offset);
        }
        char[] result = new char[length];
        content.getChars(offset, offset + length, result, 0);
        return new String(result);
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (offset < 0 || offset > getLength()) {
            throw new BadLocationException("Invalid position", offset);
        }

        if (documentFilter != null) {
            documentFilter.insertString(this, offset, str, attr);
        } else {
            doInsertString(offset, str, attr);
        }
    }


    @Override
    public void remove(int offset, int length) throws BadLocationException {
        if (offset < 0 || offset + length > getLength()) {
            throw new BadLocationException("Invalid range", offset);
        }

        if (documentFilter != null) {
            documentFilter.remove(this, offset, length);
        } else {
            doRemove(offset, length);
        }
    }


    @Override
    public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (documentFilter != null) {
            documentFilter.replace(this, offset, length, text, attrs);
        } else {
            doReplace(offset, length, text, attrs);
        }
    }
    protected void doInsertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        content.insert(offset, str);
        if (attr != null) {
            // Apply attributes if needed
        }
        fireDocumentListeners(offset, str.length(), INSERT);
    }

    protected void doRemove(int offset, int length) throws BadLocationException {
        String removedText = getText(offset, length);
        content.delete(offset, offset + length);
        fireDocumentListeners(offset, length, REMOVE, removedText);
    }

    protected void doReplace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String removedText = length > 0 ? getText(offset, length) : "";
        content.replace(offset, offset + length, text);
        if (attrs != null) {
            // Apply attributes if needed
        }
        fireDocumentListeners(offset, length, CHANGE, removedText, text);
    }

    @Override
    public void putProperty(Object key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object getProperty(Object key) {
        return properties.get(key);
    }

    @Override
    public void setDocumentFilter(DocumentFilter documentFilter) {
        this.documentFilter = documentFilter;
    }

    @Override
    public String getText() {
        return content.toString();
    }

    @Override
    public StyleSheet getStyleSheet() {
        return new StyleSheet();
    }


    @Override
    public void addDocumentListener(DocumentListener listener) {
        if (listener != null && !documentListeners.contains(listener)) {
            documentListeners.add(listener);
        }
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        documentListeners.remove(listener);
    }

    protected void fireDocumentListeners(int offset, int length, int type) {
        fireDocumentListeners(offset, length, type, null, null);
    }

    protected void fireDocumentListeners(int offset, int length, int type, String changedText) {
        fireDocumentListeners(offset, length, type, changedText, null);
    }

    protected void fireDocumentListeners(int offset, int length, int type, String removedText, String insertedText) {
        if (documentListeners.isEmpty()) {
            return;
        }

        DocumentEvent event = new DocumentEvent(this, offset, length, type);
        if (removedText != null || insertedText != null) {
            event.setChangedText(removedText, insertedText);
        }

        // Notify listeners
        for (DocumentListener listener : documentListeners) {
            if(type == INSERT) listener.insertUpdate(event);
            else if (type == REMOVE) listener.removeUpdate(event);
            else if (type == CHANGE) listener.changedUpdate(event);
        }
    }
    // DocumentFilter.FilterBypass implementation
    @Override
    public Document getDocument() {
        return this;
    }


    public SpannableStringBuilder getContent() {
        return content;
    }
}