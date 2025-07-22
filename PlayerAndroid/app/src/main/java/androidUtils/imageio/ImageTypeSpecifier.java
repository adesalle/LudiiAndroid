package androidUtils.imageio;

import android.graphics.Bitmap;

public class ImageTypeSpecifier {
    private final Bitmap.Config config;

    public ImageTypeSpecifier(Bitmap.Config config) {
        this.config = config;
    }

    public static ImageTypeSpecifier createFromBufferedImageType(int imageType) {
        // Conversion des types AWT vers Android
        switch(imageType) {
            case 1: // TYPE_INT_RGB
                return new ImageTypeSpecifier(Bitmap.Config.RGB_565);
            case 2: // TYPE_INT_ARGB
            default:
                return new ImageTypeSpecifier(Bitmap.Config.ARGB_8888);
        }
    }

    public Bitmap.Config getBitmapConfig() {
        return config;
    }
}