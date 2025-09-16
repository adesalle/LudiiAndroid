package androidUtils.awt;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import androidUtils.awt.event.OnOptionSelectedListener;
import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.event.WindowEvent;
import androidUtils.swing.JButton;
import androidUtils.swing.JPanel;
import androidUtils.swing.RootPanel;
import androidUtils.swing.WindowConstants;
import playerAndroid.app.StartAndroidApp;

public class Dialog extends DialogFragment implements RootPanel {
    protected ModalityType modalityType = ModalityType.MODELESS;
    protected int defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE;
    protected boolean isResizable = true;
    protected Bitmap iconImage;
    protected JPanel contentPane;
    protected boolean undecorated = false;
    protected OnOptionSelectedListener optionSelectedListener;
    protected WindowAdapter windowAdapter;
    protected String title;
    protected int width = -1;
    protected int height = -1;
    protected int x = -1;
    protected int y = -1;
    private DialogInterface.OnShowListener showListener;
    private boolean isShowing = false;

    public Dialog() {
        super();
    }

    public Dialog(ViewGroup owner, ModalityType modalityType) {
        this();
        this.modalityType = modalityType;
    }

    public Dialog(Dialog owner, ModalityType modalityType) {
        this();
        this.modalityType = modalityType;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        android.app.Dialog dialog = super.onCreateDialog(savedInstanceState);
        configureDialog(dialog);
        dialog.setContentView(contentPane,new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT // Doit être match_parent
        ));
        return dialog;
    }

    protected void configureDialog(android.app.Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            if (undecorated) {
                window.requestFeature(Window.FEATURE_NO_TITLE);
            }

            // Set transparency
            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE.toArgb()));

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            window.setDimAmount(0.9f); // Niveau d'assombrissement (0 = transparent, 1 = noir)
            // Set layout params
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP | Gravity.START;

            if (width > 0 && height > 0) {
                params.width = width;
                params.height = height;
            } else {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }

            if (x >= 0 && y >= 0) {
                params.x = x;
                params.y = y;
            }

            window.setAttributes(params);
        }

        dialog.setCancelable(modalityType != ModalityType.APPLICATION_MODAL);

        if (title != null) {
            dialog.setTitle(title);
        }
    }


    public Dialog getRootPane()
    {
        return this;
    }


    public JPanel getContentPane() {
        return contentPane;
    }

    public JPanel getContentView() {
        return contentPane;
    }

    public void setModalityType(ModalityType type) {
        this.modalityType = type;
        android.app.Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(type != ModalityType.APPLICATION_MODAL);
        }
    }

    public ModalityType getModalityType() {
        return modalityType;
    }

    public void setModal(boolean modal) {
        setModalityType(modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
    }

    public void setTitle(String title) {
        this.title = title;
        android.app.Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setTitle(title);
        }
    }


    public void setIconImage(Image image) {
        this.iconImage = image.getImage();
        // Implémentation de la logique pour l'icône
    }

    public void setDefaultCloseOperation(int operation) {
        this.defaultCloseOperation = operation;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        android.app.Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.x = x;
            params.y = y;
            dialog.getWindow().setAttributes(params);
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        android.app.Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = width;
            params.height = height;
            dialog.getWindow().setAttributes(params);
        }
    }

    public void show() {

        try {
            show(StartAndroidApp.startAndroidApp().getSupportFragmentManager(), "jdialog_" + hashCode());
        } catch (IllegalStateException e) {
            System.out.println("error" + e.getMessage());
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            isShowing = true;
            show();
        } else {
            dismiss();
        }

    }

    public void dispose() {
        isShowing = false;
        dismiss();
    }

    public void setResizable(boolean resizable) {
        this.isResizable = resizable;
        android.app.Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (resizable) {
                params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            } else {
                params.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            }
            dialog.getWindow().setAttributes(params);
        }
    }

    public boolean isResizable() {
        return isResizable;
    }

    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
    }

    public boolean isUndecorated() {
        return undecorated;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDefaultButton(JButton okButton) {
        // Implémentation de la logique pour le bouton par défaut
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener o) {
        this.optionSelectedListener = o;
    }

    public void notifyOptionSelected(int option) {
        if (optionSelectedListener != null) {
            optionSelectedListener.onOptionSelected(option);
        }
    }

    public void addWindowFocusListener(WindowAdapter adapter) {
        this.windowAdapter = adapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        isShowing = true;
        if (windowAdapter != null) {
            windowAdapter.windowOpened(new WindowEvent(this, WindowEvent.WINDOW_OPENED));
        }
        if (showListener != null && getDialog() != null) {
            showListener.onShow(getDialog());
        }
        updateContentPane();
    }

    private void updateContentPane() {
        android.app.Dialog dialog = getDialog();
        if (dialog != null && contentPane != null) {
            // Applique les paramètres de layout si nécessaire
            if (contentPane.getLayoutParams() == null) {
                contentPane.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }

            // Définit le contentPane comme vue principale
            dialog.setContentView(contentPane);

            // Applique la taille si elle a été spécifiée
            if (width > 0 && height > 0) {
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(width, height);
                }
            }
        }
    }

    public void setContentPane(JPanel pane) {
        if (this.contentPane == pane) {
            return;
        }

        // Remove old contentPane if it exists
        if (this.contentPane != null && getDialog() != null) {
            ViewGroup parent = (ViewGroup) this.contentPane.getParent();
            if (parent != null) {
                parent.removeView(this.contentPane);
            }
        }

        this.contentPane = pane;

        // Apply layout params if not set
        if (contentPane.getLayoutParams() == null) {
            contentPane.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        // Update dialog if showing
        if (isAdded() && getDialog() != null) {
            getDialog().setContentView(contentPane);
            if (width > 0 && height > 0) {
                getDialog().getWindow().setLayout(width, height);
            }
            contentPane.requestLayout();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isShowing = false;
        if (windowAdapter != null) {
            windowAdapter.windowClosed(new WindowEvent(this, WindowEvent.WINDOW_CLOSED));
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setOnShowListener(DialogInterface.OnShowListener o) {
        this.showListener = o;
    }

    public void toFront() {
        android.app.Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Pour les API Level >= 21 (Lollipop)
                dialog.getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                // Affiche la boîte de dialogue
                dialog.show();

                // Rétablit les flags normaux
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            // Force le focus sur la boîte de dialogue
            dialog.getWindow().getDecorView().requestFocus();
        }
    }

    /**
     * Positionne la boîte de dialogue au centre du composant spécifié ou de l'écran
     * @param frame Le composant parent (peut être null pour centrer sur l'écran)
     */
    public void setLocationRelativeTo(@Nullable View frame) {
        if (!isAdded()) {
            Log.w("JDialog", "Dialog not attached to FragmentManager");
            return;
        }
        android.app.Dialog dialog = getDialog();
        if (dialog == null || dialog.getWindow() == null) {
            return;
        }

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        if (frame != null && frame.isAttachedToWindow()) {
            try {
                // 1. Obtenir la position et taille du parent
                int[] parentLocation = new int[2];
                frame.getLocationOnScreen(parentLocation);

                // 2. Obtenir la taille du dialogue
                window.getDecorView().measure(
                        View.MeasureSpec.UNSPECIFIED,
                        View.MeasureSpec.UNSPECIFIED);
                int dialogWidth = window.getDecorView().getMeasuredWidth();
                int dialogHeight = window.getDecorView().getMeasuredHeight();

                // 3. Calculer la position centrale
                params.x = parentLocation[0] + (frame.getWidth() - dialogWidth) / 2;
                params.y = parentLocation[1] + (frame.getHeight() - dialogHeight) / 2;

                // 4. Ajuster pour ne pas sortir de l'écran
                DisplayMetrics metrics = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                params.x = Math.max(0, Math.min(params.x, metrics.widthPixels - dialogWidth));
                params.y = Math.max(0, Math.min(params.y, metrics.heightPixels - dialogHeight));

            } catch (Exception e) {
                // Fallback au centrage écran en cas d'erreur
                params.gravity = Gravity.CENTER;
            }
        } else {
            // Centrage simple sur l'écran
            params.gravity = Gravity.CENTER;
        }

        // Appliquer les changements
        window.setAttributes(params);

        // Forcer un recalcul du layout
        window.getDecorView().requestLayout();
    }

    public void invalidate() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            View decorView = getDialog().getWindow().getDecorView();
            decorView.post(() -> {
                decorView.invalidate();
                if (contentPane != null) {
                    contentPane.invalidate();
                }
            });
        }
    }

    public void repaint() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            View decorView = getDialog().getWindow().getDecorView();
            decorView.post(() -> {
                decorView.invalidate();
                decorView.requestLayout();
                if (contentPane != null) {
                    contentPane.invalidate();
                    contentPane.requestLayout();
                }
            });
        }
    }

    public void add(View view) {
        if (contentPane != null) {

            contentPane.add(view);

            if (getDialog() != null && getDialog().getWindow() != null) {
                getDialog().getWindow().getDecorView().post(() -> {
                    contentPane.measure(
                            View.MeasureSpec.UNSPECIFIED,
                            View.MeasureSpec.UNSPECIFIED);
                    pack();
                });
            }
        }
    }

    public void pack() {
        android.app.Dialog dialog = getDialog();
        if (dialog != null && contentPane != null) {
            contentPane.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(
                        contentPane.getMeasuredWidth(),
                        contentPane.getMeasuredHeight());
            }
        }
    }

    public enum ModalityType {
        APPLICATION_MODAL,
        DOCUMENT_MODAL,
        TOOLKIT_MODAL,
        MODELESS
    }
}