package androidUtils.awt.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Image;

public class BufferedImage extends Image implements RenderedImage {
    public static final Bitmap.Config TYPE_INT_ARGB = Bitmap.Config.ARGB_8888;
    public static final Bitmap.Config TYPE_INT_RGB = Bitmap.Config.RGB_565;

    public static Bitmap.Config getBitmapConfigByInt(int type) {
        switch (type) {
            case 2:
                return TYPE_INT_ARGB;  // ARGB_8888
            case 1:
                return TYPE_INT_RGB;   // RGB_565
            default:
                return Bitmap.Config.ARGB_8888; // safer fallback (was RGBA_F16 before, but ARGB_8888 is safer)
        }
    }


    private boolean translucent = false;

    private Graphics2D graphics2D = null;
    private Graphics graphics = null;

    public BufferedImage(int width, int height, Bitmap.Config config) {
        super(width, height, config);

    }

    public BufferedImage(int width, int height, int type) {
        this(width, height, getBitmapConfigByInt(type));
    }

    public BufferedImage(Bitmap bitmap) {
        super(bitmap);
    }

    public BufferedImage(ColorModel cm, WritableRaster wr, boolean isAlphaPremultiplied, String fill) {
        super(cm, wr, isAlphaPremultiplied, fill);
    }

    public Graphics2D createGraphics() {
        if(graphics2D == null) {
            graphics2D = Graphics2D.createGraphics(bitmap);
            if (translucent) {
                graphics2D.setTranslucent();
            }
        }
        return graphics2D;
    }

    public Graphics createGraphicsD() {
        if(graphics == null) {
            graphics = (Graphics) Graphics.createGraphics(bitmap);
            if (translucent) {
                graphics2D.setTranslucent();
            }
        }
        return graphics;
    }

    public Image getScaledInstance(int newW, int newH, boolean smooth) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, smooth);
        return new BufferedImage(Bitmap.createBitmap(scaledBitmap));
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public void setRGB(int x, int y, int color) {
        bitmap.setPixel(x, y, color);
    }
    public void setRGB(int i, int i1, int width, int height, int[] pixels, int i2, int width1) {
    }

    public int getRGB(int x, int y) {
        return bitmap.getPixel(x, y);
    }

    public ColorModel getColorModel() {
        return new ColorModel(bitmap.getConfig(), bitmap.isPremultiplied());
    }

    @Override
    public int getMinX() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    public WritableRaster copyData(WritableRaster raster) {
        if (raster == null) {
            raster = new WritableRaster();
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        raster.setData(width, height, pixels);
        return raster;
    }

    public Graphics2D getGraphics() {
        if(graphics2D == null) Graphics2D.createGraphics(bitmap);
        return graphics2D;
    }

    public Canvas createCanvas() {
        return new Canvas(bitmap);
    }


    public int getType() {
        Bitmap.Config config = bitmap.getConfig();
        if (config == Bitmap.Config.ARGB_8888) {
            return 2;
        } else if (config == Bitmap.Config.RGB_565) {
            return 1;
        } else {
            return 0;
        }
    }
}
