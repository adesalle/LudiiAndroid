package androidUtils.swing.tree;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class DefaultTreeCellEditor implements TreeCellEditor {

    private EditText editor;

    @Override
    public View getTreeCellEditorComponent(Context context, JTree tree, Object value,
                                           boolean selected, boolean expanded,
                                           boolean leaf, int row) {
        editor = new EditText(context);
        editor.setText(value != null ? value.toString() : "");
        editor.setPadding(16, 8, 16, 8);
        return editor;
    }

    @Override
    public Object getCellEditorValue() {
        return editor != null ? editor.getText().toString() : null;
    }

    @Override
    public boolean isCellEditable(Object event) {
        // Tu peux améliorer cela : vérifier s’il s’agit d’un double-clic, long-click, etc.
        return true;
    }
}
