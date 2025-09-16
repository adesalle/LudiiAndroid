package androidUtils.swing.tree;

import android.content.Context;
import android.view.View;

public interface TreeCellRenderer {

    View getTreeCellRendererComponent(
            Context context,
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus
    );
}
