package androidUtils.awt.batik;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidUtils.awt.RenderingHints;

public class ImageRenderer {
    private final Canvas canvas;
    private final Paint paint;
    private RenderingHints hints;

    public ImageRenderer(Bitmap bitmap) {
        this.canvas = new Canvas(bitmap);

        this.paint = new Paint();
        this.hints = new RenderingHints();

    }

    public ImageRenderer() {
        this.canvas = new Canvas();

        this.paint = new Paint();
        this.hints = new RenderingHints();
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public Paint getPaint() {
        return paint;
    }

    public RenderingHints getRenderingHints() {
        return hints;
    }

    public void setRenderingHints(RenderingHints hints) {
        this.hints = hints;
    }
}
