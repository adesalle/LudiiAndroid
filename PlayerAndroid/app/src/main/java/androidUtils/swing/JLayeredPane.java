package androidUtils.swing;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class JLayeredPane extends LinearLayout implements ViewComponent {
    // Couches standard comme dans Swing
    public static final Integer DEFAULT_LAYER = 0;
    public static final Integer PALETTE_LAYER = 100;
    public static final Integer MODAL_LAYER = 200;
    public static final Integer POPUP_LAYER = 300;
    public static final Integer DRAG_LAYER = 400;

    private Dimension preferredSize;

    public JLayeredPane(Context context) {
        super(context);
        setVisibility(VISIBLE);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize != null ? preferredSize : new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
    }

    @Override
    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int width, int height) {
        setRight(getLeft() + width);
        setBottom(getTop() + height);

        ViewGroup.LayoutParams currentParams = getLayoutParams();
        if(currentParams == null)
        {
            currentParams = new FrameLayout.LayoutParams(width, height);
        }
        else
        {
            currentParams.width = width;
            currentParams.height = height;
        }

        setLayoutParams(currentParams);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    // Méthodes spécifiques à JLayeredPane
    public void add(View component, Integer layer) {
        super.addView(component);
        setLayer(component, layer);
    }

    public void setLayer(View component, Integer layer) {
        component.setZ(layer.floatValue());
    }

    public int getLayer(View component) {
        return (int) component.getZ();
    }

    public void moveToFront(View component) {
        component.bringToFront();
    }

    public void moveToBack(View component) {
        // Android n'a pas de méthode directe, on utilise setZ
        component.setZ(0);
    }

    public View getComponentAt(int x, int y) {
        // Implémentation simplifiée
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE &&
                    x >= child.getLeft() && x <= child.getRight() &&
                    y >= child.getTop() && y <= child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("Layered Panel " + getWidth() + " " + getHeight());
        canvas.drawColor(Color.BLACK.toArgb());
    }
}