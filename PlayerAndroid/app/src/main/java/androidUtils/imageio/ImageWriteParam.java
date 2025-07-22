package androidUtils.imageio;

public class ImageWriteParam {
    public static final int MODE_DISABLED = 0;
    public static final int MODE_DEFAULT = 1;
    public static final int MODE_EXPLICIT = 2;
    public static final int MODE_COPY_FROM_METADATA = 3;

    private int compressionMode = MODE_DISABLED;
    private float compressionQuality = 1.0f;

    public void setCompressionMode(int mode) {
        if (mode < MODE_DISABLED || mode > MODE_COPY_FROM_METADATA) {
            throw new IllegalArgumentException("Invalid compression mode");
        }
        this.compressionMode = mode;
    }

    public int getCompressionMode() {
        return compressionMode;
    }

    public void setCompressionQuality(float quality) {
        if (quality < 0.0f || quality > 1.0f) {
            throw new IllegalArgumentException("Quality out of bounds");
        }
        this.compressionQuality = quality;
    }

    public float getCompressionQuality() {
        return compressionQuality;
    }

}