package androidUtils.awt;

import android.graphics.Paint;
import android.graphics.Rect;

import androidUtils.awt.geom.AffineTransform;
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

    public FontRenderContext(AffineTransform affineTransform, boolean antiAliased, boolean fractionalMetrics) {
        this.antiAliased = antiAliased;
        this.fractionalMetrics = fractionalMetrics;
        this.paint = new Paint();
        this.paint.setAntiAlias(antiAliased);
        this.paint.setSubpixelText(fractionalMetrics);
        this.metrics = paint.getFontMetrics();

    }

    public Rectangle2D getStringBounds(String text, Graphics2D g2d)
    {
        return g2d.font.getStringBounds(text, g2d.getFontRenderContext());
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
