package androidUtils.swing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.event.WindowEvent;
import androidUtils.awt.image.BufferedImage;

public class JFrame extends Activity {

    private LinearLayout contentPanel;

    private int closeOperation = WindowConstants.DO_NOTHING_ON_CLOSE;

    private WindowAdapter windowListener;

    public JFrame(String name)
    {
        setTitle(name);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentPanel = new LinearLayout(this);
        contentPanel.setOrientation(LinearLayout.VERTICAL);

        setContentView(contentPanel);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT, new OnBackInvokedCallback() {
                        @Override
                        public void onBackInvoked() {
                            if (closeOperation == WindowConstants.DO_NOTHING_ON_CLOSE) {

                            } else {
                                finish();
                            }
                        }
                    });
        }

    }

    public void add(Component component) {
        contentPanel.addView(component);
    }


    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setContentPane(LinearLayout contentPanel) {
        this.contentPanel = contentPanel;
        setContentView(contentPanel);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            super.onStart();
        } else {
            super.onStop();
        }
    }

    public void setLocationRelativeTo(Object object)
    {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
    }
    public void setIconImage(BufferedImage bufferedImage) {
        Bitmap bitmap = bufferedImage.getBitmap();
        setIcon(bitmap);
    }

    public void setIcon(Bitmap bitmap) {

    }

    public void setSize(int width, int height) {
        contentPanel.setLayoutParams(new FrameLayout.LayoutParams(width, height));
    }

    public void close() {
        finish();
    }

    private void cleanupResources() {
    }

    public void dispose() {
        cleanupResources();
        finish();
    }
    public void setDefaultCloseOperation(int operation) {
        this.closeOperation = operation;
    }

    public void addWindowListener(WindowAdapter windowAdapter) {
        this.windowListener = windowAdapter;
    }

    public void triggerWindowClosingEvent() {
        if (windowListener != null) {
            WindowEvent event = new WindowEvent(WindowEvent.WINDOW_CLOSING);
            windowListener.windowClosing(event);
        }
    }

}
