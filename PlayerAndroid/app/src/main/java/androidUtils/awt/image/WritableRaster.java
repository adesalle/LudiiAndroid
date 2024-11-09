package androidUtils.awt.image;

public class WritableRaster {

    int width;
    int height;
    int[] pixels;

    public WritableRaster(){}

    public WritableRaster(int w, int h, int[] pixel)
    {
        setData(w, h, pixel);
    }

    public void setData(int w, int h, int[] pixel)
    {
        width = w;
        height = h;
        pixels = pixel;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
}
