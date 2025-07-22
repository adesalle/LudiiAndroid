package androidUtils.awt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidUtils.swing.JDialog;
import androidUtils.swing.JPanel;
import androidUtils.swing.JRootPane;
import androidUtils.swing.RootPanel;
import androidUtils.swing.WindowConstants;
import playerAndroid.app.StartAndroidApp;

public class Dialog extends android.app.Dialog implements RootPanel {
    private ModalityType modalityType = ModalityType.MODELESS;
    private int defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE;
    private boolean isResizable = true;
    private Bitmap iconImage;
    private JRootPane rootPane;

    private boolean undecorated = false;

    public Dialog() {
        super(StartAndroidApp.startAndroidApp());
        initDialog();
    }

    public Dialog(ViewGroup owner, ModalityType modalityType) {
        super(owner != null ? owner.getContext() : StartAndroidApp.getAppContext());
        this.modalityType = modalityType;
        initDialog();
    }

    public Dialog(JDialog owner, ModalityType modalityType) {
        super(owner != null ? owner.getContext() : StartAndroidApp.getAppContext());
        this.modalityType = modalityType;
        initDialog();
    }

    protected void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(modalityType != ModalityType.MODELESS);


        rootPane = new JRootPane();

    }

    public JRootPane getRootPane() {
        return rootPane;
    }


    public JPanel getContentPane()
    {
        return rootPane.getContentPane();
    }

    public void setRootPane(JRootPane pane)
    {
        rootPane = pane;
    }

    public void setContentPane(JPanel pane)
    {
        rootPane.setContentPane(pane);
    }

    public void toFront() {
        Window window = getWindow();
        if (window != null) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            );
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            );
        }
    }

    public void add(View view) {
        if(getContentPane() != null)
        {
            getContentPane().addView(view);
        }

    }

    public void add(View view, ViewGroup.LayoutParams params) {
        add(view);
    }

    public void remove(View view) {
        if(getContentPane() != null)
        {
            getContentPane().removeView(view);
        }

    }

    public void removeAllViews() {
        if(getContentPane() != null)
        {
            getContentPane().removeAllViews();
        }
    }


    public Window getWindow() {
        return super.getWindow();
    }

    public void setModalityType(ModalityType type) {
        this.modalityType = type;
        setCancelable(type != ModalityType.MODELESS);

        if (getWindow() != null) {
            if (type == ModalityType.APPLICATION_MODAL || type == ModalityType.DOCUMENT_MODAL) {
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                );
            }
        }
    }

    public ModalityType getModalityType() {
        return modalityType;
    }

    public void setModal(boolean modal) {
        setModalityType(modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    public void setTitle(String title) {
        setTitle((CharSequence)title);
    }

    public void setIconImage(Image image)
    {
        if(image == null)
        {
            iconImage = null;
            return;
        }
        setIconImage(image.bitmap);
    }

    public void setIconImage(Bitmap image) {
        this.iconImage = image;
        if (getWindow() != null && image != null) {

            Drawable iconDrawable = new BitmapDrawable(getContext().getResources(), image);

            getWindow().setFeatureDrawable(
                    Window.FEATURE_LEFT_ICON,
                    iconDrawable
            );

        }
    }

    public void setDefaultCloseOperation(int operation) {
        this.defaultCloseOperation = operation;
        switch (operation) {
            case WindowConstants.DO_NOTHING_ON_CLOSE:
                setCancelable(false);
                break;
            case WindowConstants.DISPOSE_ON_CLOSE:
            case WindowConstants.HIDE_ON_CLOSE:
                setCancelable(true);
                break;
        }
    }

    public void setLocationRelativeTo(View view) {
        Window window = getWindow();
        if (window != null && view != null) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = location[0];
            params.y = location[1];
            window.setAttributes(params);
        }
    }

    public void setLocation(int x, int y) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = x;
            params.y = y;
            window.setAttributes(params);
        }
    }

    public void setSize(int width, int height) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = width;
            params.height = height;
            window.setAttributes(params);
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            show();
        } else {
            dismiss();
        }
    }

    public void dispose() {
        dismiss();
    }

    public void setResizable(boolean resizable) {
        this.isResizable = resizable;
        Window window = getWindow();
        if (window != null) {
            if (resizable) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            }
        }
    }
    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
        Window window = getWindow();
        if (window != null) {
            if (undecorated) {
                // Remove title bar and all window decorations
                window.requestFeature(Window.FEATURE_NO_TITLE);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT.toArgb()));
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                // Restore default decorations
                window.requestFeature(Window.FEATURE_NO_TITLE); // Keep no title for consistency

                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }

    /**
     * @return true if this dialog has no window decorations
     */
    public boolean isUndecorated() {
        return undecorated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (undecorated) {
            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT.toArgb()));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    public boolean isResizable() {
        return this.isResizable;
    }

    @Override
    public int getWidth() {
        int width = 0;
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            width = params.width;
            window.setAttributes(params);
        }
        return width;
    }

    @Override
    public int getHeight() {
        int height = 0;
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            height = params.height;
            window.setAttributes(params);
        }
        return height;
    }


    public enum ModalityType {
        APPLICATION_MODAL,  // Bloque toutes les fenêtres
        DOCUMENT_MODAL,    // Bloque les fenêtres du même document (traité comme APPLICATION_MODAL sur Android)
        TOOLKIT_MODAL,     // Bloque seulement la fenêtre parente (support partiel)
        MODELESS           // Non modal
    }
}