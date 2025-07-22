package androidUtils.swing.border;


import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics;

public class LineBorder extends AbstractBorder {
    private final int color;
    private final int thickness;
    private final boolean roundedCorners;
    private final Paint paint;

    public LineBorder(int color, int thickness) {
        this(color, thickness, false);
    }

    public LineBorder(Color color, int thickness) {
        this(color.toArgb(), thickness, false);
    }

    public LineBorder(Color color) {

        this(color.toArgb(), 1, false);
    }

    public LineBorder(int color, int thickness, boolean roundedCorners) {
        this.color = color;
        this.thickness = thickness;
        this.roundedCorners = roundedCorners;

        this.paint = getDefaultPaint();
        paint.setColor(color);
        paint.setStrokeWidth(thickness);
    }

    @Override
    public void paintBorder(View c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);

        if (roundedCorners) {
            float radius = Math.min(width, height) / 8f;
            g.drawRoundRect(
                    x + thickness/2f,
                    y + thickness/2f,
                    x + width - thickness/2f,
                    y + height - thickness/2f,
                    radius, radius
            );
        } else {
            g.drawRect(
                    x + thickness/2f,
                    y + thickness/2f,
                    x + width - thickness/2f,
                    y + height - thickness/2f
            );
        }
    }

    @Override
    public Rect getBorderInsets(View c) {
        return new Rect(thickness, thickness, thickness, thickness);
    }



    @Override
    public Rect getBorderInsets(View c, Rect insets) {
        insets.set(thickness, thickness, thickness, thickness);
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public Color getBorderColor()
    {
        return new Color(paint.getColor());
    }

}