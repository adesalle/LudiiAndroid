package androidUtils.swing.text;

import android.text.SpannableStringBuilder;

public interface StyledDocument extends Document {
    void setLogicalStyle(int pos, Style s);
    Style addStyle(String name, Style parent);
    void setHTML(String htmlText);
    SpannableStringBuilder getDocument();
}