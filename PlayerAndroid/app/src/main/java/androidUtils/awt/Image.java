package androidUtils.awt;

import android.graphics.Bitmap;

public class Image {

    public static boolean SCALE_SMOOTH = true;

    private Bitmap image;

    public Image(Bitmap bitmap)
    {
        image = bitmap;
    }

    public Bitmap getImage()
    {
        return image;
    }
}
