package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;

public class FlowLayout implements Layout {
    private int alignment;
    private int hgap;
    private int vgap;
    private ViewGroup target;

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int LEADING = 3;
    public static final int TRAILING = 4;

    public FlowLayout(int alignment) {
        this(alignment, 0, 0);
    }

    public FlowLayout(int alignment, int hgap, int vgap) {
        this.alignment = alignment;
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
        // FlowLayout n'utilise généralement pas de contraintes
        // Mais on peut stocker des propriétés spécifiques si besoin
        if (constraints instanceof FlowConstraints) {
            comp.setTag(constraints);
        }
    }

    @Override
    public void removeLayoutComponent(View comp) {
        comp.setTag(null);
    }

    private void applyLayout() {
        if (target == null) return;

        int availableWidth = target.getWidth() - target.getPaddingLeft() - target.getPaddingRight();
        if (availableWidth <= 0) return;

        int x = target.getPaddingLeft();
        int y = target.getPaddingTop();
        int rowHeight = 0;
        int lineStart = 0;

        for (int i = 0; i < target.getChildCount(); i++) {
            View child = target.getChildAt(i);
            if (child.getVisibility() == View.GONE) continue;

            // Mesure avec les contraintes spécifiques si elles existent
            FlowConstraints fc = (FlowConstraints) child.getTag();
            int childWidth = fc != null && fc.width > 0 ? fc.width :
                    ViewGroup.LayoutParams.WRAP_CONTENT;
            int childHeight = fc != null && fc.height > 0 ? fc.height :
                    ViewGroup.LayoutParams.WRAP_CONTENT;

            child.measure(
                    View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.AT_MOST)
            );

            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            if (x + measuredWidth > availableWidth) {
                // Alignement de la ligne complète
                alignLine(lineStart, i, x - hgap, availableWidth);

                x = target.getPaddingLeft();
                y += rowHeight + vgap;
                rowHeight = 0;
                lineStart = i;
            }

            child.layout(x, y, x + measuredWidth, y + measuredHeight);
            x += measuredWidth + hgap;
            rowHeight = Math.max(rowHeight, measuredHeight);
        }

        // Alignement de la dernière ligne
        if (lineStart < target.getChildCount()) {
            alignLine(lineStart, target.getChildCount(), x - hgap, availableWidth);
        }
    }

    private void alignLine(int start, int end, int lineWidth, int availableWidth) {
        int offset = 0;
        switch (alignment) {
            case CENTER:
                offset = (availableWidth - lineWidth) / 2;
                break;
            case RIGHT:
            case TRAILING:
                offset = availableWidth - lineWidth;
                break;
            case LEFT:
            case LEADING:
            default:
                offset = 0;
        }

        if (offset > 0) {
            for (int i = start; i < end; i++) {
                View child = target.getChildAt(i);
                child.offsetLeftAndRight(offset);
            }
        }
    }

    public static class FlowConstraints {
        public int width;
        public int height;

        public FlowConstraints(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    // Getters/Setters...
}