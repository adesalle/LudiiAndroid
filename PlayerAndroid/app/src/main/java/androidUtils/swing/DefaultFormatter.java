
package androidUtils.swing;

import android.text.InputFilter;

import java.text.ParseException;

public class DefaultFormatter extends AbstractFormatter {
    private boolean allowsInvalid = false;
    private boolean commitsOnValidEdit = true;

    @Override
    public Object stringToValue(String text){
        return text;
    }

    @Override
    public String valueToString(Object value){
        return value != null ? value.toString() : "";
    }

    public void setAllowsInvalid(boolean allowsInvalid) {
        this.allowsInvalid = allowsInvalid;
    }

    public boolean getAllowsInvalid() {
        return allowsInvalid;
    }

    public void setCommitsOnValidEdit(boolean commitsOnValidEdit) {
        this.commitsOnValidEdit = commitsOnValidEdit;
    }

    public boolean getCommitsOnValidEdit() {
        return commitsOnValidEdit;
    }

    @Override
    public InputFilter getInputFilter() {
        if (allowsInvalid) {
            return null;
        }
        return super.getInputFilter();
    }
}