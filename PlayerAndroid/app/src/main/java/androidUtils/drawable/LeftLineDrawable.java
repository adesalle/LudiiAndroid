package androidUtils.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidUtils.awt.Color;

public class LeftLineDrawable extends Drawable {
    private final Paint paint;
    private final int width;

    public LeftLineDrawable(Color color, int width) {
        this.width = width;
        this.paint = new Paint();
        paint.setColor(color.toArgb());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(getBounds().right, 0, getBounds().right, getBounds().bottom, paint);
        //canvas.drawRect(0, 0, getBounds().width(), getBounds().height(), paint);
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