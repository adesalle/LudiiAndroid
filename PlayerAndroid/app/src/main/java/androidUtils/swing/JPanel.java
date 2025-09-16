package androidUtils.swing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;

import androidUtils.awt.FlowLayout;
import androidUtils.awt.event.HierarchyListener;
import androidUtils.awt.event.KeyEvent;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.Layout;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import androidUtils.awt.event.FocusEvent;
import androidUtils.awt.event.FocusListener;
import androidUtils.awt.event.HierarchyEvent;

import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;
import androidUtils.awt.event.MouseMotionListener;
import androidUtils.swing.border.Border;
import androidUtils.swing.border.EmptyBorder;
import androidUtils.swing.menu.JSeparator;
import androidUtils.swing.tree.JTree;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.util.SettingsDesktop;

public class JPanel extends LinearLayout implements ViewComponent{

    public static final int LEFT_ALIGNMENT = 0;
    public static final int CENTER_ALIGNMENT = 1;
    public static final int RIGHT_ALIGNMENT = 2;
    public static final int TOP_ALIGNMENT = 4;
    public static final int BOTTOM_ALIGNMENT = 8;

    // Combinaisons prédéfinies
    public static final int TOP_LEFT = TOP_ALIGNMENT | LEFT_ALIGNMENT;
    public static final int TOP_CENTER = TOP_ALIGNMENT | CENTER_ALIGNMENT;
    public static final int TOP_RIGHT = TOP_ALIGNMENT | RIGHT_ALIGNMENT;
    public static final int CENTER_LEFT = CENTER_ALIGNMENT | LEFT_ALIGNMENT; // Note: CENTER est déjà horizontal
    public static final int CENTER_CENTER = CENTER_ALIGNMENT | CENTER_ALIGNMENT;
    public static final int CENTER_RIGHT = CENTER_ALIGNMENT | RIGHT_ALIGNMENT;
    public static final int BOTTOM_LEFT = BOTTOM_ALIGNMENT | LEFT_ALIGNMENT;
    public static final int BOTTOM_CENTER = BOTTOM_ALIGNMENT | CENTER_ALIGNMENT;
    public static final int BOTTOM_RIGHT = BOTTOM_ALIGNMENT | RIGHT_ALIGNMENT;

    private Paint paint;
    protected Graphics graphics;
    private Border border;
    private Layout layoutManager;

    private int alignment = TOP_LEFT;

    private final List<FocusListener> focusListeners = new ArrayList<>();
    private final List<HierarchyListener> hierarchyListeners = new ArrayList<>();
    private final List<MouseListener> mouseListeners = new ArrayList<>();
    private final List<MouseMotionListener> mouseMotionListeners = new ArrayList<>();
    private final List<KeyListener> keyListeners = new ArrayList<>();

    private final JPanel panel;

    public JPanel() {
        super(StartAndroidApp.getAppContext());
        setOrientation(VERTICAL);
        panel = this;
        init();
        layoutManager = new FlowLayout(FlowLayout.LEFT);
        //setLayout();


    }
    public JPanel(AttributeSet set) {
        super(StartAndroidApp.getAppContext(), set);
        setOrientation(VERTICAL);
        panel = this;
        init();
        layoutManager = new FlowLayout(FlowLayout.LEFT);
        //setLayout();


    }

    public JPanel(Layout layout) {
        super(StartAndroidApp.getAppContext());
        panel = this;
        init();
        layoutManager = layout;
        //setLayout();

    }

    public JPanel(JTree tree)
    {
        this();
        add(tree.getView());
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        paint = new Paint();
        paint.setColor(0xFFFFFFFF); // White background by default
        paint.setStyle(Paint.Style.FILL);

        setWillNotDraw(false);
        setOnTouchListener((v, event) -> {
            boolean used = false;
            for (MouseListener l : mouseListeners) {
                if (l.onTouch(v, event))used = true;

            }
             MouseEvent e = new MouseEvent(event, panel);
            for (MouseMotionListener l: mouseMotionListeners) {
                switch (event.getAction()) {
                    case MouseEvent.MOUSE_MOVED:
                        l.mouseMoved(e);
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        l.mouseDragged(e);
                        break;
                }
            }
            return used;
        });
    }


    public void add(View comp) {
        add(comp, null); // Appelle la méthode avec contraintes
    }
    public void add(JSeparator comp) {
    }

    public void add(View comp, Object constraints) {
        addView(comp);
        if (layoutManager != null) {
            layoutManager.addLayoutComponent(comp, constraints);

            layoutManager.applyLayout(this);
        }
    }

    public void remove(View child)
    {
        removeView(child);
    }

    public void removeAll()
    {
        removeAllViews();
    }

    public void setToolTipText(String text) {
        setContentDescription(text);
    }


    public void paint(Graphics g) {
        dispatchDraw(g.getCanvas());
    }


    public void setLayout(Layout layout)
    {
        layoutManager = layout;
        setLayout();
    }
    public void setLayout() {
        // 1. Appliquer le layout manager s'il existe
        if (layoutManager != null) {
            layoutManager.applyLayout(this);
        }
        requestLayout();
    }

    public void repaint() {
        invalidate();
    }

    public Graphics getGraphics() {
        if (graphics == null) {
            graphics = new Graphics();
        }
        return graphics;
    }

    public void setOpaque(boolean isOpaque) {
        setHasTransientState(!isOpaque);

        if (isOpaque) {
            getBackground().setAlpha(255);
        } else {
            getBackground().setAlpha(128);
        }
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public void repaint(Rectangle rectangle) {
        invalidate();
    }

    public void revalidate() {
        invalidate();
    }

    public void setPreferredSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
    }

    public void setBorder(EmptyBorder emptyBorder) {
        this.border = emptyBorder;
        if (emptyBorder != null) {
            setPadding(
                    emptyBorder.getBorderInsets(this).left,
                    emptyBorder.getBorderInsets(this).top,
                    emptyBorder.getBorderInsets(this).right,
                    emptyBorder.getBorderInsets(this).bottom
            );
        }
        invalidate();
    }

    public void setBorder(Border border) {
        this.border = border;
        invalidate();
    }


    protected void paintComponent(Graphics g) {
    }

    public void setVisible(boolean visible) {
        if(visible)setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);
    }

    public void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }
    public void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    public Point getLocation()
    {
        return new Point(getX(), getY());
    }

    public Dimension getPreferredSize() {
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }

    public Dimension getMinimumSize() {
        return new Dimension(getMinimumWidth(), getMeasuredHeight());
    }

    public void setMinimumSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
    }

    public void setPosition(int l, int t)
    {
        setLeft(l);
        setTop(t);
    }

    public void setSize(Dimension dimension)
    {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int width, int height) {
        setRight(getLeft() + width);
        setBottom(getTop() + height);

    }
    public void setBackground(Color color) {
        if (color != null) {
            paint.setColor(color.toArgb());
            setBackgroundColor(color.toArgb());
            invalidate();
        }
    }

    public void setForeground(Color color) {
        // Implémentation de base - peut être étendue selon les besoins
        if (color != null) {
            paint.setColor(color.toArgb());
            invalidate();
        }
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    public void setBounds(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    public Rectangle getBounds() {
        return new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight());
    }


    public void addHierarchyListener(HierarchyListener listener) {
        if (!hierarchyListeners.contains(listener)) {
            hierarchyListeners.add(listener);
        }

        if(hierarchyListeners.size() == 1) addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            HierarchyEvent event = new HierarchyEvent(panel, panel, HierarchyEvent.HIERARCHY_CHANGED);
            for (HierarchyListener l : hierarchyListeners) {
                l.hierarchyChanged(event);
            }
        });
    }

    public void addFocusListener(FocusListener listener) {
        if (!focusListeners.contains(listener)) {
            focusListeners.add(listener);
        }

        if(focusListeners.size() == 1) setOnFocusChangeListener((v, hasFocus) -> {
            FocusEvent event = new FocusEvent(panel);
            for (FocusListener l : focusListeners) {
                if (hasFocus) l.focusGained(event);
                else l.focusLost(event);
            }
        });
    }

    public void addMouseListener(MouseListener listener) {
        if (!mouseListeners.contains(listener)) {
            mouseListeners.add(listener);
        }

    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        if (!mouseMotionListeners.contains(listener)) {
            mouseMotionListeners.add(listener);
        }
    }

    public void addKeyListener(KeyListener listener) {
        if (!keyListeners.contains(listener)) {
            keyListeners.add(listener);
        }

        // S'assurer que le JPanel peut recevoir les événements clavier
        setFocusable(true);
        setFocusableInTouchMode(true);

        if (keyListeners.size() == 1) {
            setOnKeyListener((v, keyCode, event) -> {
                for (KeyListener l : keyListeners) {
                    switch (event.getAction()) {
                        case KeyEvent.KEY_PRESSED:
                            l.keyPressed(new KeyEvent(event));
                            break;
                        case KeyEvent.KEY_RELEASED:
                            l.keyReleased(new KeyEvent(event));
                            break;
                    }
                }
                return false;
            });
        }
    }

    public void scrollRectToVisible(Rectangle rect) {
        // Calcule les coordonnées du rectangle à rendre visible
        int scrollX = Math.max(0, rect.x);
        int scrollY = Math.max(0, rect.y);

        // Vérifie si un scroll est nécessaire
        if (getScrollX() > rect.x || rect.x + rect.width > getScrollX() + getWidth()
                || getScrollY() > rect.y || rect.y + rect.height > getScrollY() + getHeight()) {

            scrollTo(scrollX, scrollY);
        }
    }

    // Implémentation des méthodes abstraites restantes
    @Override
    public void setFont(Font font) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }


    public void setDoubleBuffered(boolean doubleBuffered) {
        // Android gère déjà le double buffering
        setLayerType(doubleBuffered ? LAYER_TYPE_HARDWARE : LAYER_TYPE_NONE, null);
    }




    public void setAlignment(int align) {
        this.alignment = align;
        updateLayoutGravity();
    }


    public void setAlignmentX(int horizontal) {
        // Conserve le vertical actuel
        this.alignment = (this.alignment & 0xC) | (horizontal & 0x3);
        updateLayoutGravity();
    }

    public void setAlignmentY(int vertical) {
        // Conserve l'horizontal actuel
        this.alignment = (this.alignment & 0x3) | (vertical & 0xC);
        updateLayoutGravity();
    }

    private void updateLayoutGravity() {
        int gravity = 0;

        // Partie horizontale
        switch (alignment & 0x3) {
            case LEFT_ALIGNMENT: gravity |= android.view.Gravity.LEFT; break;
            case CENTER_ALIGNMENT: gravity |= android.view.Gravity.CENTER_HORIZONTAL; break;
            case RIGHT_ALIGNMENT: gravity |= android.view.Gravity.RIGHT; break;
        }

        // Partie verticale
        switch (alignment & 0xC) {
            case TOP_ALIGNMENT: gravity |= android.view.Gravity.TOP; break;
            case BOTTOM_ALIGNMENT: gravity |= android.view.Gravity.BOTTOM; break;
            // CENTER vertical n'est pas défini dans les constantes de base
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        if (params != null) {
            params.gravity = gravity;
            setLayoutParams(params);
        }
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