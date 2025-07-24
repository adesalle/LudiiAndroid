package androidUtils.swing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidUtils.awt.BorderLayout;
import androidUtils.awt.Layout;
import androidUtils.swing.menu.JMenuBar;
import playerAndroid.app.StartAndroidApp;

public class JRootPane extends LinearLayout {

    // Constants for layers (z-index)
    public static final int DEFAULT_LAYER = 0;
    public static final int PALETTE_LAYER = 100;
    public static final int MENU_LAYER = 200;
    public static final int CONTENT_LAYER = 300;
    public static final int BUTTON_LAYER = 400;

    protected JMenuBar menuBar;
    protected JPanel contentPane;
    protected JButton defaultButton;
    protected RootLayout rootLayout;

    public JRootPane() {
        this(StartAndroidApp.getAppContext());
    }

    public JRootPane(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        // Create default content pane with BorderLayout
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Set default layout manager
        rootLayout = new RootLayout();
        setLayout(rootLayout);

        // Add content pane
        addView(contentPane);
        contentPane.setZ(CONTENT_LAYER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (rootLayout != null) {
            rootLayout.onMeasure(this, getMeasuredWidth(), getMeasuredWidth());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLeftTopRightBottom(left, top, right, bottom);

        if (rootLayout != null) {
            rootLayout.onLayout(this, changed, left, top, right, bottom);
        }
    }

    public void setContentPane(JPanel pane) {
        if (pane == null) {
            throw new IllegalArgumentException("Content pane cannot be null");
        }

        if (contentPane != null) {
            removeView(contentPane);
        }

        contentPane = pane;
        addView(pane);
        contentPane.setZ(CONTENT_LAYER);
        requestLayout();
    }

    public void setJMenuBar(JMenuBar menuBar) {
        if (this.menuBar != null) {
            removeView(this.menuBar);
        }

        this.menuBar = menuBar;
        if (menuBar != null) {
            addView(menuBar);
            menuBar.setZ(MENU_LAYER);
        }
        requestLayout();
    }

    public void setDefaultButton(JButton button) {
        if (this.defaultButton != null) {
            removeView(this.defaultButton);
        }

        this.defaultButton = button;
        if (defaultButton != null) {
            addView(defaultButton);
            defaultButton.setZ(BUTTON_LAYER);
        }
        requestLayout();
    }

    // Getters
    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JButton getDefaultButton() {
        return defaultButton;
    }

    public void setLayout()
    {
        requestLayout();
    }

    public void setLayout(Layout layout) {
        if (layout instanceof RootLayout) {
            this.rootLayout = (RootLayout) layout;
        } else {
            throw new IllegalArgumentException("JRootPane only accepts RootLayout");
        }
    }

    public void addToLayer(View component, int layer) {
        addView(component);
        component.setZ(layer);
    }

    public void setLayer(View component, int layer) {
        if (indexOfChild(component) != -1) {
            component.setZ(layer);
            requestLayout();
        }
    }

    public int getLayer(View component) {
        return (int) component.getZ();
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new FrameLayout.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        setLayoutParams(params);
        requestLayout();
    }
}