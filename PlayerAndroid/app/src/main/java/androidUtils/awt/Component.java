package androidUtils.awt;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidUtils.swing.ImageIcon;
import playerAndroid.app.StartAndroidApp;

public abstract class Component extends View {
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

    protected int alignment = CENTER_CENTER;


    private String name;


    public Component(String name) {
        super(StartAndroidApp.getAppContext());
        this.name = name;

    }

    public Component() {
        super(StartAndroidApp.getAppContext());
        this.name = "";
    }


    public String getName() {
        return name;
    }

    public Dimension getSize() {
        Rect rect = getClipBounds();
        return new Dimension(rect.width(), rect.height());
    }


    protected void paintComponent(Graphics g) {

    }

    public Dimension getPreferredSize() {
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }
    
    public Point getLocation()
    {
        return new Point(getX(), getY());
    }
    
    public void setLocation(int x, int y)
    {
        setX(x);
        setY(y);
    }

    public void repaint() {
        invalidate();
    }

    public void revalidate() {
        requestLayout();
        invalidate();
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


    }

    public void setBounds(int x, int y, int width, int height) {
        if (!isInLayout())
         layout(x, y, x + width, y + height);

        invalidate();
    }

    public boolean isInLayout() {
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            return parent.indexOfChild(this) >= 0;
        }
        return false;
    }


    public Rectangle getViewRect() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return new Rectangle(
                location[0],
                location[1],
                getWidth(),
                getHeight()
        );
    }

    public Dimension getViewSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void setViewPosition(Point point) {
        if (point != null) {
            setX(point.x);
            setY(point.y);
        }
    }

    public void setSize(Dimension dimension) {
        if (dimension != null) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(
                        dimension.width,
                        dimension.height
                );
            } else {
                params.width = dimension.width;
                params.height = dimension.height;
            }
            setLayoutParams(params);
            requestLayout();
        }
    }

    public void setPreferredSize(Dimension dimension) {
        if (dimension != null) {
            setMinimumWidth(dimension.width);
            setMinimumHeight(dimension.height);
        }
    }

    public void setIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            // Implémentation de base - à adapter selon le type de composant
            setBackground(imageIcon.getDrawable());
        }
    }
}