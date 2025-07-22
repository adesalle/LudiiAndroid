package androidUtils.swing;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.text.ParseException;

public class JFormattedTextField extends JTextField {
    private AbstractFormatter formatter;

    public JFormattedTextField() {
        super();
    }

    public JFormattedTextField(AbstractFormatter formatter) {
        super();
        setFormatter(formatter);
    }

    public void setFormatter(AbstractFormatter formatter) {
        this.formatter = formatter;
        if (formatter != null) {
            setInputType(formatter.getInputType());
            setFilters(new InputFilter[]{formatter.getInputFilter()});
        }
    }

    public AbstractFormatter getFormatter() {
        return formatter;
    }

    public Object getValue() {
        if (formatter != null) {
            return formatter.stringToValue(String.valueOf(getText()));

        }
        return getText();
    }

    public void setValue(Object value) {
        if (formatter != null) {
            setText(formatter.valueToString(value));
        } else {
            setText(value != null ? value.toString() : "");
        }
    }

    public void commitEdit() throws ParseException {
        if (formatter != null) {
            formatter.stringToValue(String.valueOf(getText()));
        }
    }

    public EditText getTextField() {
        return this;
    }
}
