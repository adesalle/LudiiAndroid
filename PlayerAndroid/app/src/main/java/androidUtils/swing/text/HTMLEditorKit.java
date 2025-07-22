package androidUtils.swing.text;

import java.io.IOException;
import java.io.Writer;

public class HTMLEditorKit {
    public void insertHTML(HTMLDocument doc, int offset, String html,
                           int popDepth, int pushDepth, String tag) throws IOException {
        // Implémentation simplifiée pour Android
        String text = android.text.Html.fromHtml(html).toString();
        try {
            doc.insertString(offset, text, null);
        } catch (BadLocationException e) {
            throw new IOException(e);
        }
    }

    public void write(Writer out, HTMLDocument doc, int pos, int len) throws IOException, BadLocationException {
        String text = doc.getText(pos, len);
        out.write(text);
    }
}