package androidUtils.swing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import androidUtils.awt.BoxLayout;
import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Layout;
import androidUtils.awt.image.BufferedImage;
import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.event.WindowEvent;
import androidUtils.swing.menu.JMenuBar;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.util.SettingsDesktop;

public class JFrame extends LinearLayout implements RootPanel{

    protected String title;
    private int closeOperation = WindowConstants.DO_NOTHING_ON_CLOSE;
    private WindowAdapter windowListener;
    private JPanel contentPane;
    private JMenuBar menubar;
    protected JButton defaultButton;
    private boolean focusTraversalKeysEnabled = true;
    private Layout layout;
    protected JFrame frame = this;

    public JFrame(String title) {
        super(StartAndroidApp.getAppContext());
        this.title = title;

        setOrientation(LinearLayout.VERTICAL);

        // Initialize with proper layout params
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));

        contentPane = new JPanel();
        contentPane.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));

        super.addView(contentPane);
    }
    public JFrame() {
        this("");
    }
    private void init() {
        // Setup basic frame properties

        // Default layout
        layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        setLayout(layout);
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle()
    {
        return title;
    }
    public void setView(Activity activity) {
        frame.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        activity.setContentView(this);
    }
    public void add(View view, Object constraints)
    {
        add(view);
    }
    public void add(View view) {
        if(contentPane != null)
            contentPane.addView(view);
    }
    public void remove(View view)
    {
        if(contentPane != null) contentPane.removeView(view);
    }
    public void setContentPane(JPanel panel) {


        contentPane = panel;
        contentPane.setBackgroundColor(android.graphics.Color.WHITE);
        if(menubar != null)
        {
            removeViewAt(1);
            addView(contentPane, 1);
        }
        else {
            removeViewAt(0);
            contentPane.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
            ));

            addView(contentPane, 0);
        }

        contentPane.setVisibility(View.VISIBLE);
    }
    public void setJMenuBar(JMenuBar mainMenu) {
        if (getChildCount() > 0 && getChildAt(0) instanceof JMenuBar) {
                removeViewAt(0);
        }
        if (mainMenu != null) {

            menubar = mainMenu;
            addView(mainMenu, 0);

            // Set layout params for the menu
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            );
            mainMenu.setLayoutParams(params);
            updateContentPaneLayout();

        }
    }
    private void updateContentPaneLayout() {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                0,
                1f
        );

        contentPane.setLayoutParams(params);

    }
    public void setDefaultButton(JButton button) {

        this.defaultButton = button;
        if (defaultButton != null) {
            removeViewAt(2);
            addView(defaultButton,2);
        }
    }
    public void setVisible(boolean visible) {
        // Handle visibility via setVisibility
        setVisibility(visible ? View.VISIBLE : View.GONE);

    }
    public void setLocationRelativeTo(Object object) {
        // Get the parent of this view
        ViewGroup parent = (ViewGroup) getParent();

        if (parent != null) {
            // Create LayoutParams for the parent container
            ViewGroup.LayoutParams layoutParams = getLayoutParams();

            if (layoutParams instanceof FrameLayout.LayoutParams) {
                // If the parent is a FrameLayout, use FrameLayout.LayoutParams
                FrameLayout.LayoutParams frameParams = (FrameLayout.LayoutParams) layoutParams;
                frameParams.gravity = Gravity.CENTER;
                setLayoutParams(frameParams);
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                // If the parent is a LinearLayout, use LinearLayout.LayoutParams
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layoutParams;
                linearParams.gravity = Gravity.CENTER;
                setLayoutParams(linearParams);
            } else {
                // Fallback to generic ViewGroup.LayoutParams
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(layoutParams);
            }
        }
    }
    public void setIconImage(BufferedImage bufferedImage) {
        Bitmap bitmap = bufferedImage.getBitmap();
        setIcon(bitmap);
    }
    public void setIcon(Bitmap bitmap) {
        // No direct equivalent, could set an ImageView in the layout
    }
    public void setSize(int width, int height) {
        setRight(getLeft() + width);
        setBottom(getTop() + height);
        ViewGroup.LayoutParams params = getLayoutParams();
        if(params != null)
        {
            params.width = width;
            params.height = height;
            setLayoutParams(params);
            return;
        }
        setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }
    public void setSize(Dimension dimension) {

        setSize(dimension.width, dimension.height);
    }
    public void setDefaultCloseOperation(int operation) {
        this.closeOperation = operation;
    }
    public void setFocusTraversalKeysEnabled(boolean enabled) {
        this.focusTraversalKeysEnabled = enabled;
    }
    public void addWindowListener(WindowAdapter windowAdapter) {
        this.windowListener = windowAdapter;
    }
    public void triggerWindowClosingEvent() {
        if (windowListener != null) {
            WindowEvent event = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            windowListener.windowClosing(event);
        }
    }
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }
    public void setBackgroundColor(Color color) {
        setBackgroundColor(color.toArgb());
    }
    public void setPadding(int left, int top, int right, int bottom) {
        setPadding(left, top, right, bottom);
    }
    public void close() {
        // Could remove this view from its parent if needed
    }
    public void dispose() {
        cleanupResources();
        close();
    }
    private void cleanupResources() {
        // Clean up any resources if needed
    }
    public void setMinimumSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);

    }
    public JMenuBar getJMenuBar() {
        return menubar;
    }
    public JPanel getContentPane() {
        return contentPane;
    }
    public JButton getDefaultButton() {
        return defaultButton;
    }
    public void setResizable(boolean b) {

    }
    public void setLocation(int i, int i1) {
    }
    public RootPanel getRootPane() {
        return this;
    }
    public void dispatchEvent(WindowEvent windowEvent) {
    }
    public void revalidate() {
        requestLayout();
    }
    public void repaint() {
        invalidate();
        requestLayout();
    }
    public void setLayout(Layout borderLayout) {
        layout = borderLayout;
        borderLayout.applyLayout(this);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Standard layout - don't modify the coordinates
        super.onLayout(changed, l, t, r, b);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Let parent measure first
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Now enforce minimum dimensions if needed
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();


        width = Math.max(width, SettingsDesktop.defaultWidth);
        height = Math.max(height, SettingsDesktop.defaultHeight);


        setMeasuredDimension(width, height);
    }
}