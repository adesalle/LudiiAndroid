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
        x = rect.left;
        y = rect.top;
        width = rect.width();
        height = rect.height();
    }


    public boolean contains(Point point)
    {
        return rectangleBounds.contains(point.x, point.y);
    }

    public RectF getRectBound()
    {
        return rectangleBounds;
    }


    public void setBounds(int startX, int startY, int width, int toolHeight) {
        super.setBounds(startX, startY, width, toolHeight);
        x = startX;
        y = startY;
        this.width = width;
        height = toolHeight;
    }

    public Rectangle clone()
    {
        return new Rectangle(x, y, width, height);
    }
}
