package androidUtils.swing.tree;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.unnamed.b.atv.model.TreeNode;

import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;

public class StyledNodeViewHolder extends TreeNode.BaseNodeViewHolder<Object> {


    public StyledNodeViewHolder(Context context) {
        super(context);
    }


    @Override
    public View createNodeView(TreeNode node, Object value) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(context);
        tv.setText(value.toString());
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(16);
        tv.setPadding(30, 15, 30, 15);

        // Fond gris clair, arrondi pour effet elliptique
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.LTGRAY);
        background.setCornerRadius(1000);
        tv.setBackground(background);

        // Indentation
        int indent = node.getLevel() * 40;
        layout.setPadding(indent, 10, 10, 10);
        layout.addView(tv);

        return layout;
    }
}