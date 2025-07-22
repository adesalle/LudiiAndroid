package androidUtils.swing.text;


import android.text.SpannableStringBuilder;

public interface AttributeSet {
    boolean containsAttribute(Object name);
    Object getAttribute(Object name);
    int getAttributeCount();
    void addAttribute(Object name, Object value);

    void setStyle(Object foregroundColorSpan);

    Object getAttributeName(int i);
}