
package androidUtils.swing;

import android.text.InputFilter;
import android.text.Spanned;

import java.text.ParseException;

public abstract class AbstractFormatter {
    public abstract Object stringToValue(String text);
    public abstract String valueToString(Object value);

    public int getInputType() {
        return android.text.InputType.TYPE_CLASS_TEXT;
    }

    public InputFilter getInputFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                String newText = dest.subSequence(0, dstart).toString() +
                        source.subSequence(start, end) +
                        dest.subSequence(dend, dest.length()).toString();
                stringToValue(newText);
                return null; // Accept the input

            }
        };
    }
}