package androidUtils.swing.border;

import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics;


public abstract class AbstractBorder implements Border {

    private static final Rect EMPTY_INSETS = new Rect(0, 0, 0, 0);


    @Override
    public Rect getBorderInsets(View c) {
        return new Rect(EMPTY_INSETS);
    }

    /**
     * Version avec insets de destination fournis
     */
    public Rect getBorderInsets(View c, Rect insets) {
        insets.set(EMPTY_INSETS);
        return insets;
    }

    /**
     * Par défaut, le border n'est pas opaque
     */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public abstract void paintBorder(View c, Graphics g, int x, int y, int width, int height);

    @Override
    public void paintBorder(View c, Graphics g) {
        Rect rect = getBorderInsets(c);
        paintBorder(c, g, rect.left, rect.top, rect.width(), rect.height());
    }

    public Rect getInteriorRectangle(View c, int x, int y, int width, int height) {
        Rect insets = getBorderInsets(c);
        return new Rect(
                x + insets.left,
                y + insets.top,
                x + width - insets.right,
                y + height - insets.bottom
        );
    }

    protected Paint getDefaultPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    @Override
    public Color getBorderColor() {
        return new Color(getDefaultPaint().getColor());
    }
}