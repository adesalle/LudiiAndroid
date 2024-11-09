package androidUtils.awt.image;


import android.graphics.Bitmap;

public class ColorModel {

    Bitmap.Config config;

    boolean isPremultiplied;

    public ColorModel(Bitmap.Config conf, boolean isPremultiplied)
    {
        config = conf;
        this.isPremultiplied = isPremultiplied;
    }
    public int getNumComponents() {
        switch (config) {
            case ARGB_8888:
            case RGBA_F16:
                return 4;  // Alpha, Red, Green, Blue
            case RGB_565:
                return 3;  // Red, Green, Blue
            default:
                return 0;  // Unknown
        }
    }

    public int getBitsPerComponent() {
        switch (config) {
            case ARGB_8888:
                return 8;  // 8 bits per component
            case RGBA_F16:
                return 16; // 16 bits per component
            case RGB_565:
                return 5;  // 5 bits for Red, 6 bits for Green, 5 bits for Blue
            default:
                return 0;
        }
    }

    public boolean hasAlpha() {
        return config == Bitmap.Config.ARGB_8888 || config == Bitmap.Config.RGBA_F16;
    }

    public boolean isAlphaPremultiplied()
    {
        return (hasAlpha() && isPremultiplied);

    }

}