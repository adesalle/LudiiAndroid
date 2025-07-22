package androidUtils.awt;

import android.util.Size;

import androidUtils.awt.geom.Dimension2D;

public class Dimension extends Dimension2D {

    public int width;
    public int height;
    public Dimension(float width, float height) {
        super(width, height);
        this.width = (int) width;
        this.height = (int) height;
    }

    public Dimension(Dimension dimension) {
        super(dimension.width, dimension.height);
        this.width = dimension.width;
        this.height = dimension.height;
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
