package androidUtils.awt.image;

import android.graphics.Bitmap;



public interface RenderedImage {

    Bitmap getBitmap();
    int getMinX();
    int getMinY();
    WritableRaster copyData(WritableRaster raster);
    ColorModel getColorModel();
}
