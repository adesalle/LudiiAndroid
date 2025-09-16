package androidUtils.swing;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Rectangle;
import androidUtils.swing.tree.JTree;
import playerAndroid.app.StartAndroidApp;

public class JScrollPane extends ScrollView {
    private View contentView;

    public JScrollPane() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JScrollPane(AttributeSet attrs) {
        super(StartAndroidApp.getAppContext(), attrs);
        init();
    }

    public JScrollPane(View view) {
        this();
        setViewportView(view);
    }

    public JScrollPane(JTree view) {
        this();
        setViewportView(view.getView());
    }

    private void init() {
        setFillViewport(true);
    }

    @Override
    public void addView(View child) {
        setViewportView(child);
    }

    public View getViewportView() {
        return contentView;
    }

    public void setViewportView(View view) {
        if (contentView != null) {
            removeView(contentView);
        }
        contentView = view;
        if (view != null) {
            super.addView(view);
        }
    }

    public void setVerticalScrollBarPolicy(int policy) {
        setScrollBarStyle(policy);
    }

    public Rect getVisibleRect() {
        Rect scrollBounds = new Rect();
        getHitRect(scrollBounds);
        return scrollBounds;
    }

    public void setBounds(Rectangle placement) {
        setX(placement.x);
        setY(placement.y);
        setLayoutParams(new LayoutParams(placement.width, placement.height));
    }

    public void setBorder(Object o) {
        // Implémentation optionnelle pour les bordures
    }

    public void setVisible(boolean b) {
        setVisibility(b ? VISIBLE : INVISIBLE);
    }

    public Component getViewport() {
        return new Component() {
            // Implémentation de base du viewport
        };
    }

    public ScrollBar getVerticalScrollBar() {
        return new ScrollBar() {
            @Override
            public void setValue(int value) {
                scrollTo(0, value);
            }

            @Override
            public int getValue() {
                return getScrollY();
            }

            @Override
            public int getMaximum() {
                View child = getChildAt(0);
                if (child != null) {
                    return Math.max(0, child.getHeight() - getHeight());
                }
                return 0;
            }
        };
    }

    public void setHorizontalScrollBarPolicy(int horizontalScrollbarNever) {
    }

    public interface ScrollBar {
        void setValue(int value);
        int getValue();
        int getMaximum();
    }

    public Dimension getPreferredSize() {
        if (contentView instanceof Component) {
            return ((Component) contentView).getPreferredSize();
        }
        return new Dimension(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void setPreferredSize(Dimension dimension) {
        setLayoutParams(new LayoutParams(
                dimension.width == LayoutParams.WRAP_CONTENT ?
                        LayoutParams.WRAP_CONTENT : (int) dimension.getWidth(),
                dimension.height == LayoutParams.WRAP_CONTENT ?
                        LayoutParams.WRAP_CONTENT : (int) dimension.getHeight()));
    }
}