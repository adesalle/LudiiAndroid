package androidUtils.swing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import androidx.core.widget.NestedScrollView;

import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Rectangle;
import androidUtils.swing.tree.JTree;
import playerAndroid.app.StartAndroidApp;

public class JScrollPane extends JPanel implements ViewComponent{
    private CustomScrollView verticalScrollView;
    private CustomHorizontalScrollView horizontalScrollView;
    private View viewportView;
    private View contentView;
    private FrameLayout viewportContainer;

    public JScrollPane() {
        super();
        init();
    }


    public JScrollPane(View view) {
        super();
        init();
        setViewportView(view);
    }

    public JScrollPane(AttributeSet attrs) {
        super(attrs);
        init();
    }

    private void init() {
        verticalScrollView = new CustomScrollView(getContext());
        horizontalScrollView = new CustomHorizontalScrollView(getContext());
        viewportContainer = new FrameLayout(getContext());

        // Important pour le dessin
        viewportContainer.setWillNotDraw(false);

        verticalScrollView.setFillViewport(true);
        horizontalScrollView.setFillViewport(true);

        // Configuration de la hiérarchie
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        horizontalScrollView.addView(viewportContainer, params);

        verticalScrollView.addView(horizontalScrollView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                ));

        this.addView(verticalScrollView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));
    }

    public void setViewportView(View view) {
        if (this.viewportContainer != null) {
            viewportContainer.removeAllViews();
        }
        this.contentView = view;
        if (view != null) {
            viewportContainer.addView(view,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    ));

            // Force la mise à jour
            requestLayout();
            invalidate();
        }
    }

    public void setHorizontalScrollEnabled(boolean enabled) {
        horizontalScrollView.setHorizontalScrollBarEnabled(enabled);
        horizontalScrollView.setScrollEnabled(enabled);
    }

    public void setVerticalScrollEnabled(boolean enabled) {
        verticalScrollView.setVerticalScrollBarEnabled(enabled);
        verticalScrollView.setScrollEnabled(enabled);
    }



    public Component getViewport() {
        return new Component() {
        };
    }

    @Override
    public Dimension getPreferredSize() {
        if (contentView instanceof ViewComponent) {
            return ((ViewComponent) contentView).getPreferredSize();
        } else if (contentView != null) {
            contentView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );
            return new Dimension(
                    contentView.getMeasuredWidth(),
                    contentView.getMeasuredHeight()
            );
        }
        return new Dimension(0, 0);
    }

    public void setPreferredSize(Dimension dimension) {
        if (dimension != null) {
            setMinimumWidth(dimension.width);
            setMinimumHeight(dimension.height);
        }
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        // La police s'applique au contenu, pas au scroll pane lui-même
        if (contentView instanceof ViewComponent) {
            ((ViewComponent) contentView).setFont(font);
        }
    }

    @Override
    public Font getFont() {
        if (contentView instanceof ViewComponent) {
            return ((ViewComponent) contentView).getFont();
        }
        return null;
    }

    public void setHorizontalScrollBarPolicy(int policy) {
        setHorizontalScrollEnabled(policy != HORIZONTAL_SCROLLBAR_NEVER);
        horizontalScrollView.setHorizontalScrollBarEnabled(policy == HORIZONTAL_SCROLLBAR_ALWAYS ||
                (policy == HORIZONTAL_SCROLLBAR_AS_NEEDED &&
                        contentView != null &&
                        contentView.getWidth() > getWidth()));
    }

    public JScrollBar getVerticalScrollBar() {
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.setRange(0, contentView.getHeight(), getHeight());

        scrollBar.addAdjustmentListener(e -> {
            verticalScrollView.scrollTo(0, e.getValue());
        });

        return scrollBar;
    }

    // Constantes pour les politiques de scrollbar
    public static final int HORIZONTAL_SCROLLBAR_AS_NEEDED = 0;
    public static final int HORIZONTAL_SCROLLBAR_NEVER = 1;
    public static final int HORIZONTAL_SCROLLBAR_ALWAYS = 2;

    public void setBounds(Rectangle placement) {
    }

    public void setBorder(Object o) {
    }

    public void setVisible(boolean b) {
    }

    public void setVerticalScrollBarPolicy(int verticalScrollbarNever) {

    }


    private static class CustomScrollView extends NestedScrollView {
        private boolean scrollEnabled = true;

        public CustomScrollView(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean enabled) {
            this.scrollEnabled = enabled;
        }

        @Override
        public boolean canScrollVertically(int direction) {
            return scrollEnabled && super.canScrollVertically(direction);
        }
    }

    private static class CustomHorizontalScrollView extends HorizontalScrollView {
        private boolean scrollEnabled = true;

        public CustomHorizontalScrollView(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean enabled) {
            this.scrollEnabled = enabled;
        }

        @Override
        public boolean canScrollHorizontally(int direction) {
            return scrollEnabled && super.canScrollHorizontally(direction);
        }
    }
}