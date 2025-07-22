package androidUtils.swing.tree;

import android.view.View;

public interface TreeCellEditor {
    View getTreeCellEditorView(JTree tree, Object value,
                               boolean selected, boolean expanded,
                               boolean leaf, int row);
    Object getCellEditorValue();
    boolean stopCellEditing();
    void cancelCellEditing();
}