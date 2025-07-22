package androidUtils.awt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.image.BufferedImage;

public class Graphics extends Graphics2D{

    public Graphics(Canvas canvas, Bitmap btp) {
        super(canvas, btp);
    }
    public Graphics() {
        super();
    }

    public Graphics(Canvas canvas) {
        super(canvas, Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB));
        canvas.setBitmap(btp);

    }

    @Override
    public Graphics2D create(int x, int y, int width, int height) {
        Graphics g2dnew = new Graphics();

        // Create a new bitmap with the specified dimensions
        g2dnew.btp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        g2dnew.canvas = new Canvas(g2dnew.btp);

        // Copy the relevant portion from the original bitmap
        Canvas tempCanvas = new Canvas(g2dnew.btp);
        Rect srcRect = new Rect(x, y, x + width, y + height);
        Rect destRect = new Rect(0, 0, width, height);
        tempCanvas.drawBitmap(btp, srcRect, destRect, null);

        // Copy graphic attributes
        g2dnew.paint = new Paint(paint);
        g2dnew.font = new Font(font);
        g2dnew.fontSize = fontSize;

        // Adjust and copy the affine transform
        if (at != null) {
            g2dnew.at = new AffineTransform(at);
            g2dnew.at.translate(-x, -y);  // Adjust for the new origin
        }

        // Adjust and copy the clip
        if (clip != null) {
            g2dnew.clip = clip.copy();
            // Intersect with the new bounds
            Rectangle2D newBounds = new Rectangle2D.Double(0, 0, width, height);
            g2dnew.clip = g2dnew.clip.createIntersection(newBounds);
        } else {
            g2dnew.clip = new Rectangle2D.Double(0, 0, width, height);
        }

        return g2dnew;
    }

}
