package androidUtils.awt;

import android.graphics.Bitmap;

import androidUtils.awt.image.ColorModel;
import androidUtils.awt.image.WritableRaster;

public abstract class Image {

    public static boolean SCALE_SMOOTH = true;

    protected Bitmap bitmap;

    public Image(int width, int height, Bitmap.Config config)
    {
        if (config == Bitmap.Config.RGBA_F16) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(width, height, config);
        }
    }

    public Image(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
    public Image(ColorModel cm, WritableRaster wr, boolean isAlphaPremultiplied, String fill) {
        int width = wr.width;
        int height = wr.height;
        bitmap = Bitmap.createBitmap(width, height, cm.config);
        bitmap.setPixels(wr.pixels, 0, width, 0, 0, width, height);
    }

    public Bitmap getImage()
    {
        return bitmap;
    }

    public Bitmap getBitmap() {return getImage();}
}
