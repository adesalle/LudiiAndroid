package androidUtils.awt.font;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;

import java.awt.Shape;

import androidUtils.awt.Font;
import androidUtils.awt.FontRenderContext;
import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Path2D;

public final class TextLayout {

    private final String text;
    private final TextPaint textPaint;
    private final StaticLayout layout;


    public TextLayout(String src, Font font, FontRenderContext frc) {
        this.text = src;

        this.textPaint = new TextPaint();
        this.textPaint.setTypeface(font.getFont());
        this.textPaint.setTextSize(font.getSize());
        this.textPaint.setAntiAlias(frc.isAntiAliased());
        this.textPaint.setSubpixelText(frc.usesFractionalMetrics());

        int width = (int) Math.ceil(textPaint.measureText(src));
        this.layout = StaticLayout.Builder.obtain(src, 0, text.length(), textPaint, width).build();
    }

    public float getAscent() {
        return -textPaint.ascent();
    }

    public float getDescent() {
        return textPaint.descent();
    }

    public float getLeading() {
        return textPaint.getFontMetrics().leading;
    }

    public Shape getOutline(AffineTransform at)
    {
        Path path = new Path();
        textPaint.getTextPath(text, 0, text.length(), 0, 0, path);

        if (at != null) {
            Matrix matrix = at.getMatrix();
            path.transform(matrix);
        }

        return new Path2D.Float(path);
    }

    public Rect getBounds() {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.save();
        canvas.translate(x, y);
        layout.draw(canvas);
        canvas.restore();
    }
}