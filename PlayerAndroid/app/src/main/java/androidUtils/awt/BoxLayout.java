package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidUtils.swing.JComponent;

public class BoxLayout implements Layout {
    private ViewGroup target;
    private int axis;

    public static final int X_AXIS = LinearLayout.HORIZONTAL;
    public static final int Y_AXIS = LinearLayout.VERTICAL;
    public static final int CENTER_AXIS = LinearLayout.TEXT_ALIGNMENT_CENTER;
    public static final int LINE_AXIS = LinearLayout.HORIZONTAL;
    public static final int PAGE_AXIS = LinearLayout.VERTICAL;

    public BoxLayout(ViewGroup target, int axis) {
        this.target = target;
        this.axis = axis;
        applyLayout();
    }

    public BoxLayout(JComponent target, int axis) {
        this(target.getView(), axis);
    }

    @Override
    public void applyLayout(ViewGroup vg) {
        applyLayout();
    }

    @Override
    public void addLayoutComponent(View comp, Object constraints) {
    }

    @Override
    public void removeLayoutComponent(View comp) {
    }

    private void applyLayout() {
        if (target == null) return;

        if (target instanceof LinearLayout) {
            ((LinearLayout) target).setOrientation(axis);
        } else {
            configureChildrenLayoutParams();
        }

        ViewGroup.LayoutParams targetParams = target.getLayoutParams();
        if (targetParams == null) {
            ViewGroup parent = (ViewGroup) target.getParent();
            if (parent != null) {
                if (parent instanceof LinearLayout) {
                    targetParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                } else if (parent instanceof FrameLayout) {
                    targetParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    targetParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                target.setLayoutParams(targetParams);
            }
        }
    }

    private void configureChildrenLayoutParams() {
        for (int i = 0; i < target.getChildCount(); i++) {
            View child = target.getChildAt(i);
            ViewGroup.LayoutParams params = child.getLayoutParams();

            if (params == null) {
                if (target instanceof LinearLayout) {
                    params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                } else if (target instanceof FrameLayout) {
                    params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }

            // Créer de nouveaux paramètres de mise en page du type correct
            ViewGroup.LayoutParams newParams;
            if (target instanceof LinearLayout) {
                newParams = new LinearLayout.LayoutParams(params.width, params.height);
            } else if (target instanceof FrameLayout) {
                newParams = new FrameLayout.LayoutParams(params.width, params.height);
            } else {
                newParams = new ViewGroup.LayoutParams(params.width, params.height);
            }

            if (axis == X_AXIS) {
                newParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                newParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                newParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                newParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            child.setLayoutParams(newParams);
        }
    }
}