package androidUtils.swing.tree;

import android.view.View;
import android.content.Context;

public interface TreeCellEditor {

    View getTreeCellEditorComponent(
            Context context,
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row
    );

    Object getCellEditorValue();


    boolean isCellEditable(Object event);
}
