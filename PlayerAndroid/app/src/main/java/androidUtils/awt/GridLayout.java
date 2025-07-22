package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GridLayout implements Layout {
    private int rows;
    private int cols;
    private int hgap;
    private int vgap;
    private ViewGroup target;

    public GridLayout(int rows, int cols) {
        this(rows, cols, 0, 0);
    }
    public GridLayout() {
        this(1, 0, 0, 0);
    }

    public GridLayout(int rows, int cols, int hgap, int vgap) {
        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    @Override
    public void applyLayout(ViewGroup vg) {
        target = vg;
        applyLayout();
    }

    @Override
    public void addLayoutComponent(View comp, Object constraints) {
        if (constraints instanceof GridConstraints) {
            GridConstraints gc = (GridConstraints) constraints;
            comp.setTag(gc); // Stocke les contraintes
        }
    }

    @Override
    public void removeLayoutComponent(View comp) {
        comp.setTag(null);
    }

    private void applyLayout() {
        if (target == null) return;

        int width = target.getWidth();
        int height = target.getHeight();
        if (width == 0 || height == 0) return;

        // Calcul des dimensions des cellules
        int cellWidth = (width - (cols - 1) * hgap) / cols;
        int cellHeight = (height - (rows - 1) * vgap) / rows;

        for (int i = 0; i < target.getChildCount(); i++) {
            View child = target.getChildAt(i);
            GridConstraints gc = (GridConstraints) child.getTag();
            int row;
            int col;
            if (gc == null) {
                // Position par défaut si pas de contraintes
                row = i / cols;
                col = i % cols;
                gc = new GridConstraints(row, col, 1, 1);
            }
            else {
                row = gc.row;
                col = gc.col;
            }
            // Calcul des bounds
            int left = col * (cellWidth + hgap);
            int top = row * (cellHeight + vgap);
            int right = left + cellWidth * gc.colspan;
            int bottom = top + cellHeight * gc.rowspan;

            child.layout(left, top, right, bottom);
        }
    }

    public static class GridConstraints {
        public int row;
        public int col;
        public int rowspan;
        public int colspan;

        public GridConstraints(int row, int col, int rowspan, int colspan) {
            this.row = row;
            this.col = col;
            this.rowspan = rowspan;
            this.colspan = colspan;
        }
    }

    // Getters/Setters...
}