package androidUtils.awt;

import android.graphics.Paint;
import android.graphics.Rect;

import androidUtils.awt.geom.Rectangle2D;


public class FontRenderContext {

    Paint paint;
    Paint.FontMetrics metrics;

    public FontRenderContext(Paint paint)
    {
        this.paint = paint;
        metrics = paint.getFontMetrics();
    }

    public Rectangle2D getStringBounds(String text, Graphics2D g2d)
    {
        Rect result = new Rect();
        paint.getTextBounds(text,0, text.length(), result );
        return new Rectangle2D.Double(result);
    }

}
