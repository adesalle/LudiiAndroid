package androidUtils.awt;

import android.util.Size;

import androidUtils.awt.geom.Dimension2D;

public class Dimension extends Dimension2D {

    private int width;
    private int height;
    public Dimension(float width, float height) {
        super(width, height);
        this.width = (int) width;
        this.height = (int) height;
    }
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = (int) width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = (int) height;
    }
}
