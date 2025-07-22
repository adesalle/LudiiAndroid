package androidUtils.swing.text;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidUtils.swing.StyleSheet;
import androidUtils.swing.event.DocumentListener;

public class HTMLDocument implements StyledDocument {
    private SpannableStringBuilder document;
    private Map<Object, Object> properties;
    private StyleContext styleContext;

    private StyleSheet styleSheet = new StyleSheet();

    private List<DocumentListener> listeners = new ArrayList<>();

    public HTMLDocument() {
        document = new SpannableStringBuilder();
        properties = new HashMap<>();
        styleContext = new StyleContext();
    }

    @Override
    public void setHTML(String htmlText) {
        Spanned spannedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY);
        document.clear();
        document.append(spannedText);
    }

    @Override
    public SpannableStringBuilder getDocument() {
        return document;
    }

    @Override
    public String getText(int start, int end) throws BadLocationException {
        if (start < 0 || end > getLength() || start > end) {
            throw new BadLocationException("Invalid range", start);
        }
        char[] result = new char[end - start];
        document.getChars(start, end, result, 0);
        return new String(result);
    }

    @Override
    public String getText(){
        char[] result = new char[document.length()];
        document.getChars(0, document.length(), result, 0);
        return new String(result);
    }

    @Override
    public StyleSheet getStyleSheet() {
        // Parse le contenu HTML pour extraire les styles
        String html = getText();
        if (html != null && !html.isEmpty()) {
            // Extraire les styles des balises HTML
            extractStylesFromHTML(html);
        }
        return styleSheet;
    }

    @Override
    public void addDocumentListener(DocumentListener listenerText1) {
        listeners.add(listenerText1);
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        listeners.remove(listener);

    }

    private void extractStylesFromHTML(String html) {
        // Réinitialise la feuille de style
        styleSheet = new StyleSheet();

        // Exemple: extraire les styles des balises communes
        addRuleFromTag("h1", "font-size: 24px; font-weight: bold;");
        addRuleFromTag("h2", "font-size: 20px; font-weight: bold;");
        addRuleFromTag("h3", "font-size: 18px; font-weight: bold;");
        addRuleFromTag("p", "font-size: 16px; margin: 4px 0;");
        addRuleFromTag("b", "font-weight: bold;");
        addRuleFromTag("i", "font-style: italic;");
        addRuleFromTag("u", "text-decoration: underline;");

        // Extraire les styles inline et des balises style
        extractInlineStyles(html);
    }

    private void addRuleFromTag(String tag, String rule) {
        styleSheet.addRule(tag, rule);
    }

    private void extractInlineStyles(String html) {
        // Implémentation simplifiée pour extraire les styles inline
        // Ceci est un exemple basique - une vraie implémentation nécessiterait un parser HTML complet

        // Exemple: extraire les styles des attributs style="..."
        int styleStart = html.indexOf("style=\"");
        while (styleStart >= 0) {
            int styleEnd = html.indexOf("\"", styleStart + 7);
            if (styleEnd > styleStart) {
                String styleDecl = html.substring(styleStart + 7, styleEnd);
                // Ajouter comme règle pour une classe générée
                String className = "inline_" + System.currentTimeMillis();
                styleSheet.addRule("." + className, styleDecl);
            }
            styleStart = html.indexOf("style=\"", styleEnd);
        }

        // Extraire les balises <style>...</style>
        int styleTagStart = html.indexOf("<style");
        while (styleTagStart >= 0) {
            int styleContentStart = html.indexOf(">", styleTagStart) + 1;
            int styleTagEnd = html.indexOf("</style>", styleContentStart);
            if (styleTagEnd > styleContentStart) {
                String cssContent = html.substring(styleContentStart, styleTagEnd);
                parseCSSRules(cssContent);
            }
            styleTagStart = html.indexOf("<style", styleTagEnd);
        }
    }

    private void parseCSSRules(String css) {
        // Implémentation très simplifiée d'un parser CSS
        String[] rules = css.split("}");
        for (String rule : rules) {
            int bracePos = rule.indexOf("{");
            if (bracePos > 0) {
                String selector = rule.substring(0, bracePos).trim();
                String declaration = rule.substring(bracePos + 1).trim();
                styleSheet.addRule(selector, declaration);
            }
        }
    }
    @Override
    public int getLength() {
        return document.length();
    }

    @Override
    public void insertString(int position, String str, AttributeSet attr) throws BadLocationException {
        if (position < 0 || position > getLength()) {
            throw new BadLocationException("Invalid position", position);
        }
        document.insert(position, str);
        if (attr != null) {
            Style span = styleContext.getStyle(attr);
            document.setSpan(span, position, position + str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public void remove(int offset, int length) throws BadLocationException {
        if (offset < 0 || offset + length > getLength()) {
            throw new BadLocationException("Invalid range", offset);
        }
        document.delete(offset, offset + length);
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

    }

    @Override
    public Style addStyle(String name, Style parent) {
        return styleContext.addStyle(name, parent);
    }

    @Override
    public void setLogicalStyle(int pos, Style s) {
        // Implémentation simplifiée pour Android
        if (s != null && pos >= 0 && pos <= getLength()) {
            Style span = styleContext.getStyle(s);
            document.setSpan(span, pos, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}