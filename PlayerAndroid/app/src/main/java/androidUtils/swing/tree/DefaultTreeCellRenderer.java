package androidUtils.swing.tree;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DefaultTreeCellRenderer implements TreeCellRenderer {

    // Configuration visuelle
    private static final int BACKGROUND_COLOR = Color.WHITE;
    private static final int TEXT_COLOR = Color.BLACK;
    private static final int SELECTED_COLOR = Color.parseColor("#3399FF");
    private static final int INDENT_PER_LEVEL = 30; // en dp
    private static final int PADDING_VERTICAL = 12; // en dp
    private static final int PADDING_HORIZONTAL = 8; // en dp



    @Override
    public View getTreeCellRendererComponent(Context context, JTree tree, Object value,
                                             boolean selected, boolean expanded,
                                             boolean leaf, int row, boolean hasFocus) {

        // Convertir dp en pixels
        int indentPx = dpToPx(context, INDENT_PER_LEVEL);
        int paddingVertPx = dpToPx(context, PADDING_VERTICAL);
        int paddingHorzPx = dpToPx(context, PADDING_HORIZONTAL);

        // Conteneur principal
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setBackgroundColor(selected ? SELECTED_COLOR : BACKGROUND_COLOR);

        // Calcul du niveau hiérarchique
        int level = getNodeLevel(tree, value);

        // Indentation proportionnelle au niveau
        View indent = new View(context);
        LinearLayout.LayoutParams indentParams = new LinearLayout.LayoutParams(
                level * indentPx,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        indent.setLayoutParams(indentParams);
        layout.addView(indent);

        // Icône (dossier/feuille)
        ImageView icon = new ImageView(context);
        icon.setPadding(paddingHorzPx, 0, paddingHorzPx, 0);
        layout.addView(icon);

        // Texte du noeud
        TextView text = new TextView(context);
        text.setText(value.toString());
        text.setTextColor(selected ? Color.WHITE : TEXT_COLOR);
        text.setPadding(0, paddingVertPx, paddingHorzPx, paddingVertPx);
        layout.addView(text);

        return layout;
    }

    private int getNodeLevel(JTree tree, Object node) {
        int level = 0;
        TreeNode currentNode = (TreeNode) node;
        while (currentNode.getParent() != null) {
            level++;
            currentNode = currentNode.getParent();
        }
        return level;
    }

    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}