package androidUtils.swing.event;

import androidUtils.swing.text.Document;

public class DocumentEvent {

    private final Document source;
    private final int offset;
    private final int length;
    private final int type;
    private String removedText;
    private String insertedText;

    public DocumentEvent(Document source, int offset, int length, int type) {
        this.source = source;
        this.offset = offset;
        this.length = length;
        this.type = type;
    }

    public void setChangedText(String removedText, String insertedText) {
        this.removedText = removedText;
        this.insertedText = insertedText;
    }

    // Getters for all fields
    public Document getDocument() { return source; }
    public int getOffset() { return offset; }
    public int getLength() { return length; }
    public int getType() { return type; }
    public String getRemovedText() { return removedText; }
    public String getInsertedText() { return insertedText; }
}