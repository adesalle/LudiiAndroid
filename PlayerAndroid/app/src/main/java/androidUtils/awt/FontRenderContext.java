package androidUtils.awt;

import android.graphics.Paint;
import android.graphics.Rect;

import androidUtils.awt.geom.Rectangle2D;


public class FontRenderContext {

    Paint paint;
    Paint.FontMetrics metrics;

    private final boolean antiAliased;
    private final boolean fractionalMetrics;

    public FontRenderContext(Paint paint)
    {
        this.paint = paint;
        metrics = paint.getFontMetrics();
        this.antiAliased = paint.isAntiAlias();
        this.fractionalMetrics = paint.isSubpixelText();
    }

    public Rectangle2D getStringBounds(String text, Graphics2D g2d)
    {
        Rect result = new Rect();
        paint.getTextBounds(text,0, text.length(), result );
        return new Rectangle2D.Double(result);
    }

    public boolean isAntiAliased() {
        return antiAliased;
    }

    /**
     * Returns whether fractional metrics (subpixel rendering) are used.
     *
     * @return True if fractional metrics are enabled, false otherwise.
     */
    public boolean usesFractionalMetrics() {
        return fractionalMetrics;
    }

}
