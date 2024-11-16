package androidUtils.awt.image;

import android.graphics.Bitmap;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Image;

public class BufferedImage {
    public static Bitmap.Config TYPE_INT_ARGB = Bitmap.Config.ARGB_8888;
    public static Bitmap.Config TYPE_INT_RGB = Bitmap.Config.RGB_565;


    Bitmap bitmap;
    boolean translucent = false;

    public BufferedImage(int x, int y, Bitmap.Config config)
    {
        if( config == Bitmap.Config.RGBA_F16)
        {
            bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
            translucent = true;
        }
        else bitmap = Bitmap.createBitmap(x, y, config);
    }


    public BufferedImage(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public BufferedImage(ColorModel cm, WritableRaster wr, boolean isAlphaPre, String fill)
    {
        int width = wr.width;
        int height = wr.height;
        bitmap = Bitmap.createBitmap(width, height, cm.config);
        bitmap.setPixels(wr.pixels, 0, width, 0, 0, width, height);
    }

    public Graphics2D createGraphics()
    {
        Graphics2D g2d =  Graphics2D.createGraphics(bitmap);
        if(translucent) g2d.setTranslucent();
        return g2d;
    }



    public Image getScaledInstance(int newW, int newH, boolean imageScale)
    {
        return new Image(Bitmap.createScaledBitmap(bitmap, newW, newH, true));
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }
    public int getHeight()
    {
        return  bitmap.getHeight();
    }

    public void setRGB(int x, int y, int color)
    {
        bitmap.setPixel(x,y,color);
    }

    public int getRGB(int x, int y)
    {

        return bitmap.getPixel(x,y);
    }

    public ColorModel getColorModel()
    {
        return new ColorModel(bitmap.getConfig(), bitmap.isPremultiplied());
    }

    public WritableRaster copyData(WritableRaster raster)
    {
        if(raster == null) raster = new WritableRaster();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        raster.setData(width, height, pixels);
        return raster;
    }

    public Graphics2D getGraphics() {
        return createGraphics();
    }
}
