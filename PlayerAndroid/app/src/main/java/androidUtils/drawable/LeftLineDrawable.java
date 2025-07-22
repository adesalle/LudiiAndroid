package androidUtils.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidUtils.awt.Color;

public class LeftLineDrawable extends Drawable {
    private final Paint paint;
    private final int borderWidth;
    private final int textPadding;

    public LeftLineDrawable(Color color, int borderWidth) {
        this(color, borderWidth, 0);
    }

    public LeftLineDrawable(Color color, int borderWidth, int textPadding) {
        this.borderWidth = borderWidth;
        this.textPadding = textPadding;
        this.paint = new Paint();
        paint.setColor(color.toArgb());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        // Dessine la ligne en tenant compte du padding texte
        float x = textPadding + (borderWidth / 2f);
        canvas.drawLine(x, 0, x, getBounds().bottom, paint);
    }


    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }
}