package androidUtils.swing;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidUtils.awt.Component;
import androidUtils.awt.Container;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.Layout;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.border.Border;
import playerAndroid.app.StartAndroidApp;

public abstract class JComponent extends Container implements ViewComponent{
    private boolean opaque = true;
    private boolean doubleBuffered = false;
    private Layout layoutManager;
    private Border border;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private List<MouseListener> mouseListeners = new ArrayList<>();

    public JComponent() {
        super(new ViewGroup(StartAndroidApp.getAppContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        });
        initDefaults();
    }

    public void setLayout(Layout manager) {
        this.layoutManager = manager;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDefaults() {

        setOnTouchListener((View v, MotionEvent event) -> {
            boolean isActive = false;
            for (OnTouchListener listener:mouseListeners) {
                if(listener.onTouch(v, event))
                {
                    isActive = true;
                }
            }
            return isActive;
        });
    }

    public void add(View comp, Object constraints) {
        if (layoutManager != null) {
            // Si un layout est défini, on utilise sa méthode addLayoutComponent
            layoutManager.addLayoutComponent(comp, constraints);

            // On ajoute la vue au container parent
            getView().addView(comp);

            // On applique le layout
            layoutManager.applyLayout(getView());
        } else {
            add(comp);
        }
    }

    public void setBorder(Border border) {
        this.border = border;


        // Mise à jour du padding
        updateBorderInsets();
        revalidate();
        repaint();
    }

    public Border getBorder() {
        return border;
    }

    private void updateBorderInsets() {
        if (border != null) {
            Rect insets = border.getBorderInsets(this);
            getView().setPadding(insets.left, insets.top, insets.right, insets.bottom);
        } else {
            getView().setPadding(0, 0, 0, 0);
        }
    }



    public Layout getLayout() {
        return layoutManager;
    }


    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        getView().setBackgroundColor(opaque ? 0xFF000000 : 0x00000000);
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setDoubleBuffered(boolean doubleBuffered) {
        this.doubleBuffered = doubleBuffered;
    }

    public boolean isDoubleBuffered() {
        return doubleBuffered;
    }

    public void paint(Graphics g) {
        // À implémenter par les sous-classes
    }

    public void repaint() {
        getView().invalidate();
    }

    public void revalidate() {
        getView().requestLayout();
    }

    public Dimension getPreferredSize() {
        return new Dimension(
                getView().getMeasuredWidth(),
                getView().getMeasuredHeight());
    }

    public void setPreferredSize(Dimension dimension) {
        getView().setMinimumWidth(dimension.width);
        getView().setMinimumHeight(dimension.height);
    }

    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    public void setSize(int width, int height) {
        setSize(new Dimension(width, height));
    }

    public void setVisible(boolean visible) {
        getView().setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //border.paintBorder(this, new Graphics(canvas));
    }

    protected void addMouseListener(MouseListener clickListener) {
        setOnTouchListener(clickListener);
    }

    private ViewGroup getGroup()
    {
        if (this.getParent() instanceof ViewGroup) {
            return (ViewGroup) this.getParent();

        }
        if(getParent() instanceof Container)
        {
            return  ((Container) getParent()).getView();

        }
        return null;
    }

    public float getParentX() {
        ViewGroup group = getGroup();
        if(group == null) return 0;
        else return group.getX();
    }

    public float getParentY() {
        ViewGroup group = getGroup();
        if(group == null) return 0;
        else return group.getY();
    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
        getPropertyChangeListeners().clone();
    }


    public ArrayList<PropertyChangeListener> getPropertyChangeListeners() {
        return new ArrayList<>(Arrays.asList(propertyChangeSupport.getPropertyChangeListeners()));
    }
}