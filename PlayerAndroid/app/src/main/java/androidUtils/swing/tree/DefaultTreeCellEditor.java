package androidUtils.swing.tree;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class DefaultTreeCellEditor implements TreeCellEditor {
    private Context context;
    private Object currentValue;
    private EditText editText;

    public DefaultTreeCellEditor(Context context) {
        this.context = context;
    }

    @Override
    public View getTreeCellEditorView(JTree tree, Object value,
                                      boolean selected, boolean expanded,
                                      boolean leaf, int row) {
        this.currentValue = value;
        editText = new EditText(context);
        editText.setText(value.toString());
        editText.setSelectAllOnFocus(true);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return editText;
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    @Override
    public boolean stopCellEditing() {
        // Validation optionnelle ici
        return true;
    }

    @Override
    public void cancelCellEditing() {
        // Réinitialiser à la valeur originale si nécessaire
    }
}