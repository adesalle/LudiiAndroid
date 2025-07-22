package androidUtils.swing.tree;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidUtils.awt.Font;

public class DefaultTreeCellRenderer implements TreeCellRenderer {
    private Context context;
    private int paddingLeft = 50;
    private int defaultTextSize = 16;
    private int selectedTextColor = 0xFF0000FF; // Blue
    private int defaultTextColor = 0xFF000000; // Black

    private Font font = null;

    public DefaultTreeCellRenderer(Context context) {
        this.context = context;
    }

    @Override
    public View getTreeCellView(JTree tree, Object value,
                                boolean selected, boolean expanded,
                                boolean leaf, int row) {
        TextView textView = new TextView(context);
        textView.setText(value.toString());
        textView.setTextSize(defaultTextSize);
        textView.setPadding(paddingLeft * (row + 1), 20, 20, 20);

        if (selected) {
            textView.setTextColor(selectedTextColor);
            textView.setBackgroundColor(0x200000FF); // Light blue background
        } else {
            textView.setTextColor(defaultTextColor);
            textView.setBackgroundColor(0x00000000); // Transparent
        }

        // Optionally add icon based on node state
        if (leaf) {
            // Set leaf icon if needed
        } else if (expanded) {
            // Set expanded icon
        } else {
            // Set collapsed icon
        }

        return textView;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    // Méthodes de configuration
    public void setTextColor(int color) {
        this.defaultTextColor = color;
    }

    public void setSelectedTextColor(int color) {
        this.selectedTextColor = color;
    }

    public void setPaddingLeft(int padding) {
        this.paddingLeft = padding;
    }
}