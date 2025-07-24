package androidUtils.swing;

import androidUtils.awt.Color;
import androidUtils.awt.Dialog;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Layout;
import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.geom.Point2D;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class JDialog extends Dialog {
    protected Point2D lastPosition;
    protected int closeOperation = WindowConstants.DISPOSE_ON_CLOSE;
    protected Layout layout;
    protected List<KeyListener> keyListeners = new ArrayList<>();
    protected Font font;

    public JDialog() {
        super();
        setContentPane(new JPanel());
    }

    public JDialog(ViewGroup owner) {
        super(owner, ModalityType.MODELESS);
        setContentPane(new JPanel());
    }

    public JDialog(JDialog owner) {
        super(owner, ModalityType.MODELESS);
        setContentPane(new JPanel());
    }

    public JDialog(ViewGroup owner, ModalityType modalityType) {
        super(owner, modalityType);
        setContentPane(new JPanel());
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (contentPane != null) {
            contentPane.setLayout(layout);
        }
    }

    public void setDefaultCloseOperation(int operation) {
        this.closeOperation = operation;
    }

    public void setLocationRelativeTo(View view) {
        if (view != null) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            setLocation(location[0] + view.getWidth()/2 - getWidth()/2,
                    location[1] + view.getHeight()/2 - getHeight()/2);
        }
    }

    public void setLocation(double x, double y) {
        super.setLocation((int)x, (int)y);
    }

    public void setLocation(Point2D point2D) {
        if (point2D != null) {
            setLocation(point2D.getX(), point2D.getY());
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    public void addWindowListener(WindowAdapter windowAdapter) {
        addWindowFocusListener(windowAdapter);
    }



    public void requestFocus() {
        android.app.Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().requestFocus();
        }
    }

    public void validate() {
        if (contentPane != null) {
            contentPane.invalidate();
        }
    }

    @Override
    protected void configureDialog(android.app.Dialog dialog) {
        super.configureDialog(dialog);

        if (layout != null && contentPane != null) {
            setLayout(layout);
        }

        // Applique le fond transparent pour un meilleur rendu
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT.toArgb()));
        }
    }



    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void setFont(Font font) {
        this.font = font;
        if (contentPane != null) {
            contentPane.setFont(font);
        }
    }

    public Font getFont() {
        return font;
    }

    public Point2D getLastPosition() {
        return lastPosition != null ? lastPosition : new Point2D.Float(x, y);
    }

    public int getCloseOperation() {
        return closeOperation;
    }

    public Layout getLayout() {
        return layout;
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void setSize(Dimension dimension) {
        if (dimension != null) {
            setSize(dimension.width, dimension.height);
        }
    }
}