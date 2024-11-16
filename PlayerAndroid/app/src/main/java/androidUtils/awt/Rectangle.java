package androidUtils.awt;


import android.graphics.Rect;
import android.graphics.RectF;

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

    public Rectangle(Rect rect)
    {
        super(rect);
        x = rect.centerX();
        y = rect.centerY();
        width = rect.width();
        height = rect.height();
    }

    public float getCenterX()
    {
        return rectangleBounds.centerX();
    }
    public float getCenterY()
    {
        return rectangleBounds.centerY();
    }

    public boolean contains(Point point)
    {
        return rectangleBounds.contains(point.x, point.y);
    }

    public RectF getRectBound()
    {
        return rectangleBounds;
    }


}
