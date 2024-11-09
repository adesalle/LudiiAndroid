package androidUtils.awt;


import androidUtils.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D.Double {

    public int width;
    public int height;
    public int x;
    public int y;

    public Rectangle(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        width = w;
        height = h;
    }
    public Rectangle()
    {
        super();
    }

    public float getCenterX()
    {
        return rectangleBounds.centerX();
    }
    public float getCenterY()
    {
        return rectangleBounds.centerY();
    }

}
