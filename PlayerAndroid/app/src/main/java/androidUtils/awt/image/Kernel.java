package androidUtils.awt.image;

public class Kernel {
    private final int width;
    private final int height;
    private final float[] data;

    public Kernel(int width, int height, float[] data) {
        if (data.length != width * height) {
            throw new IllegalArgumentException("Data length does not match kernel dimensions.");
        }
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float[] getData() {
        return data;
    }
}