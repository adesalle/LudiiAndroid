package androidUtils.swing;


import androidUtils.awt.Dimension;
import androidUtils.awt.FlowLayout;
import androidUtils.awt.Font;
import androidUtils.awt.Layout;
import androidUtils.awt.event.KeyEvent;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Dialog;
import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.event.WindowEvent;
import androidUtils.awt.geom.Point2D;

public class JDialog extends Dialog implements ViewComponent{
    private WindowAdapter windowAdapter;
    private Point2D lastPosition;
    private int closeOperation = WindowConstants.DISPOSE_ON_CLOSE;

    private Layout layout;

    public JDialog() {
        super();
    }

    public JDialog(ViewGroup owner) {
        super(owner, ModalityType.MODELESS);
    }
    public JDialog(JDialog owner) {
        super(owner, ModalityType.MODELESS);
    }

    public JDialog(ViewGroup owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        lastPosition = new Point2D.Float(0, 0);
        setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    public void setLayout(Layout layout)
    {
        this.layout = layout;
        this.layout.applyLayout(this.getContentPane());
    }




    public void setDefaultCloseOperation(int operation) {
        this.closeOperation = operation;
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

    @Override
    public void setLocationRelativeTo(View view) {
        super.setLocationRelativeTo(view);
    }

    public void setLocation(double x, double y) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = (int) x;
            params.y = (int) y;
            window.setAttributes(params);
            lastPosition = new Point2D.Float(x, y);
        }
    }

    public void setLocation(Point2D point2D) {
        setLocation(point2D.getX(), point2D.getY());
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    public int getWidth() {
        Window window = getWindow();
        return window != null ? window.getAttributes().width : 0;
    }

    public int getHeight() {
        Window window = getWindow();
        return window != null ? window.getAttributes().height : 0;
    }

    public void setBounds(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    public void addWindowListener(WindowAdapter windowAdapter) {
        this.windowAdapter = windowAdapter;
    }

    private void fireWindowEvent(int eventType) {
        if (windowAdapter != null) {
            WindowEvent e = new WindowEvent(this, eventType);
            switch (eventType) {
                case WindowEvent.WINDOW_OPENED:
                    windowAdapter.windowOpened(e);
                    break;
                case WindowEvent.WINDOW_CLOSING:
                    windowAdapter.windowClosing(e);
                    break;
                case WindowEvent.WINDOW_CLOSED:
                    windowAdapter.windowClosed(e);
                    break;
            }
        }
    }

    @Override
    public void show() {
        super.show();
        fireWindowEvent(WindowEvent.WINDOW_OPENED);
    }

    @Override
    public void dismiss() {
        fireWindowEvent(WindowEvent.WINDOW_CLOSING);
        super.dismiss();
        fireWindowEvent(WindowEvent.WINDOW_CLOSED);
    }

    public void requestFocus() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView().requestFocus();
        }
    }

    public void invalidate() {
        getContentPane().invalidate();
    }

    public void validate() {
        getContentPane().requestLayout();
    }

    public void repaint() {
        invalidate();
    }

    public void pack() {
        View contentView = getContentPane();
        if (contentView != null) {
            // Mesurer le contenu
            contentView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
            );

            // Définir la taille de la fenêtre selon le contenu
            Window window = getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = contentView.getMeasuredWidth();
                params.height = contentView.getMeasuredHeight();
                window.setAttributes(params);
            }
        }
    }

    private List<KeyListener> keyListeners = new ArrayList<>();

    public void addKeyListener(KeyListener listener) {
        if (listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);
            setupKeyListener();
        }
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
        if (keyListeners.isEmpty()) {
            getWindow().getDecorView().setOnKeyListener(null);
        }
    }

    private void setupKeyListener() {
        View decorView = getWindow().getDecorView();
        decorView.setOnKeyListener((v, keyCode, event) -> {
            for (KeyListener listener : keyListeners) {
                switch (event.getAction()) {
                    case KeyEvent.KEY_PRESSED:
                        listener.keyPressed(new KeyEvent(
                                event
                        ));
                        break;

                    case KeyEvent.KEY_RELEASED:
                        listener.keyReleased(new KeyEvent(
                                event
                        ));
                        break;
                }
            }
            return false;
        });
        decorView.setFocusableInTouchMode(true);
        decorView.requestFocus();
    }

    @Override
    public Dimension getPreferredSize() {
        View contentView = getContentPane();
        if (contentView != null) {
            contentView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
            );
            return new Dimension(
                    contentView.getMeasuredWidth(),
                    contentView.getMeasuredHeight()
            );
        }
        return new Dimension(0, 0);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        Window window = getWindow();
        if (window != null && dimension != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = dimension.width;
            params.height = dimension.height;
            window.setAttributes(params);
        }
    }

    @Override
    public void setSize(Dimension dimension) {
        if (dimension != null) {
            setSize(dimension.width, dimension.height);
        }
    }

    @Override
    public void setFont(Font font) {
        View contentView = getContentPane();
        if (contentView != null && font != null) {
            if (contentView instanceof TextView) {
                ((TextView) contentView).setTypeface(font.getFont());
                ((TextView) contentView).setTextSize(font.getSize());
            }
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        View contentView = getContentPane();
        if (contentView instanceof TextView) {
            TextView textView = (TextView) contentView;
            return new Font(
                    textView.getTypeface(),
                    (int) textView.getTextSize()
            );
        }
        return new Font(Typeface.DEFAULT, 14); // Valeur par défaut
    }
}