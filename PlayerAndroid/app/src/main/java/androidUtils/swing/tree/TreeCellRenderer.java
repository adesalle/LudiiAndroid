package androidUtils.swing.tree;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidUtils.awt.Font;

public interface TreeCellRenderer {
    View getTreeCellView(JTree tree, Object value,
                         boolean selected, boolean expanded,
                         boolean leaf, int row);

    void setFont(Font font);
}