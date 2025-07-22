package androidUtils.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidUtils.awt.Color;

public class BorderDrawable extends Drawable {
    private final Paint paint;
    private final int borderWidth;

    public BorderDrawable(Color color, int borderWidth) {
        this.borderWidth = borderWidth;
        this.paint = new Paint();
        paint.setColor(color.toArgb());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, getBounds().width(), getBounds().height(), paint);
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