
package androidUtils.swing;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberFormatter extends AbstractFormatter {
    private NumberFormat format;
    private Class<?> valueClass;

    public NumberFormatter() {
        this(NumberFormat.getInstance());
    }

    public NumberFormatter(NumberFormat format) {
        this.format = format;
        this.valueClass = Number.class;
    }

    @Override
    public Object stringToValue(String text){
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return format.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String valueToString(Object value){
        if (value == null) {
            return "";
        }
        return format.format(value);
    }

    @Override
    public int getInputType() {
        return android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }
}