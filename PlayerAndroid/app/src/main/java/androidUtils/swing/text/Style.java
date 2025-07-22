package androidUtils.swing.text;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import java.util.HashMap;
import java.util.Map;

import androidUtils.awt.Color;

public class Style implements AttributeSet {
    protected String name;
    protected Style parent;
    private Map<Object, Object> attributes = new HashMap<>();
    private Object styleSpan;

    public Style(String name, Style parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Style getParent() {
        return parent;
    }

    public void addAttribute(Object name, Object value) {
        attributes.put(name, value);
        updateStyleSpan();
    }

    @Override
    public boolean containsAttribute(Object name) {
        return attributes.containsKey(name) ||
                (parent != null && parent.containsAttribute(name));
    }

    @Override
    public Object getAttribute(Object name) {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }
        return parent != null ? parent.getAttribute(name) : null;
    }

    @Override
    public int getAttributeCount() {
        return attributes.size() + (parent != null ? parent.getAttributeCount() : 0);
    }

    // Utility methods
    public boolean isBold() {
        Object bold = getAttribute(StyleConstants.Bold);
        return bold instanceof Boolean && (Boolean) bold;
    }

    public boolean isItalic() {
        Object italic = getAttribute(StyleConstants.Italic);
        return italic instanceof Boolean && (Boolean) italic;
    }

    public boolean isUnderline() {
        Object underline = getAttribute(StyleConstants.Underline);
        return underline instanceof Boolean && (Boolean) underline;
    }

    public Integer getForeground() {
        Object fg = getAttribute(StyleConstants.Foreground);
        if (fg instanceof Integer) {
            return (Integer) fg;
        }
        return null; // Return null if not set
    }

    public int getSize() {
        Object size = getAttribute(StyleConstants.Size);
        return size instanceof Integer ? (Integer) size : 12;
    }

    public int getAlignment() {
        Object align = getAttribute(StyleConstants.Alignment);
        return align instanceof Integer ? (Integer) align : StyleConstants.ALIGN_LEFT;
    }

    public Object getStyle() {
        return styleSpan;
    }

    public void setStyle(Object styleSpan) {
        this.styleSpan = styleSpan;
    }

    @Override
    public Object getAttributeName(int index) {
        if (index < 0 || index >= attributes.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return attributes.keySet().toArray()[index];
    }

    private void updateStyleSpan() {
        // Create appropriate span based on attributes
        if (isBold() && isItalic()) {
            styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        } else if (isBold()) {
            styleSpan = new StyleSpan(Typeface.BOLD);
        } else if (isItalic()) {
            styleSpan = new StyleSpan(Typeface.ITALIC);
        }

        if (getForeground() != Color.BLACK.toArgb()) {
            styleSpan = new ForegroundColorSpan(getForeground());
        }

        if (isUnderline()) {
            styleSpan = new UnderlineSpan();
        }
    }

    public void apply(Editable editable, int start, int end) {
        if (editable == null || start < 0 || end > editable.length() || start > end) {
            return;
        }

        // Apply font style
        if (isBold() || isItalic()) {
            int style = Typeface.NORMAL;
            if (isBold() && isItalic()) {
                style = Typeface.BOLD_ITALIC;
            } else if (isBold()) {
                style = Typeface.BOLD;
            } else if (isItalic()) {
                style = Typeface.ITALIC;
            }
            editable.setSpan(new StyleSpan(style), start, end,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Apply foreground color
        Integer foreground = getForeground();
        if (foreground != null) {
            editable.setSpan(new ForegroundColorSpan(foreground), start, end,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Apply underline
        if (isUnderline()) {
            editable.setSpan(new UnderlineSpan(), start, end,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Apply other spans from styleSpan if set
        if (styleSpan != null && styleSpan instanceof CharacterStyle) {
            editable.setSpan(CharacterStyle.wrap((CharacterStyle)styleSpan),
                    start, end,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}