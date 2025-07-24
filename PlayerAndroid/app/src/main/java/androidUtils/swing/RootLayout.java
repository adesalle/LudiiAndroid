package androidUtils.swing;

import android.view.View;
import android.view.ViewGroup;
import androidUtils.awt.Layout;
import androidUtils.swing.menu.JMenuBar;

public class RootLayout implements Layout {

    @Override
    public void applyLayout(ViewGroup group) {
        if (!(group instanceof JRootPane)) {
            throw new IllegalArgumentException("RootLayout can only be used with JRootPane");
        }

        JRootPane rootPane = (JRootPane) group;
        layoutComponents(rootPane);
    }

    public void onMeasure(JRootPane rootPane, int widthMeasureSpec, int heightMeasureSpec) {
        rootPane.setSize(widthMeasureSpec, heightMeasureSpec);

    }

    public void onLayout(JRootPane rootPane, boolean changed, int left, int top, int right, int bottom) {
        layoutComponents(rootPane);
    }

    private void layoutComponents(JRootPane rootPane) {
        int width = rootPane.getMeasuredWidth();
        int height = rootPane.getMeasuredHeight();
        int contentTop = 0;

        // Layout menu bar if present
        JMenuBar menuBar = rootPane.getJMenuBar();
        if (menuBar != null && menuBar.getParent() == rootPane) {
            int menuHeight = menuBar.getMeasuredHeight();
            menuBar.layout(0, 0, width, menuHeight);
            contentTop = menuHeight;
        }

        // Layout content pane
        JPanel contentPane = rootPane.getContentPane();
        if (contentPane != null && contentPane.getParent() == rootPane) {
            contentPane.layout(0, contentTop, width, height);
        }

        // Layout default button if present
        JButton defaultButton = rootPane.getDefaultButton();
        if (defaultButton != null && defaultButton.getParent() == rootPane) {
            // Position the button according to your layout needs
            int buttonWidth = defaultButton.getMeasuredWidth();
            int buttonHeight = defaultButton.getMeasuredHeight();
            int buttonLeft = width - buttonWidth - 20; // 20px from right
            int buttonTop = height - buttonHeight - 20; // 20px from bottom
            defaultButton.layout(buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight);
        }
    }

    @Override
    public void addLayoutComponent(View comp, Object constraints) {
        // Handle layout constraints if needed
        if (constraints != null) {
            comp.setTag(constraints);
        }
    }

    @Override
    public void removeLayoutComponent(View comp) {
        comp.setTag(null);
    }
}