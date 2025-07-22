package androidUtils.awt;

import android.graphics.Rect;
import android.graphics.RectF;

import androidUtils.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D.Double {

    public int x;
    public int y;
    public int width;
    public int height;

    public Rectangle(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public Rectangle(float x, float y, float w, float h) {
        super(x, y, w, h);
        this.x = (int) x;
        this.y = (int) y;
        this.width = (int) w;
        this.height = (int) h;
    }

    public Rectangle() {
        super();
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Rectangle(Rect rect) {
        super(rect);
        this.x = rect.left;
        this.y = rect.top;
        this.width = rect.width();
        this.height = rect.height();
    }

    public Rectangle(int w, int h) {
        super(0, 0, w, h);
        this.x = 0;
        this.y = 0;
        this.width = w;
        this.height = h;
    }

    public Rectangle(Point drawPosn) {
    }

    @Override
    public void setRect(double x, double y, double width, double height) {
        super.setRect(x, y, width, height);
        this.x = (int) x;
        this.y = (int) y;
        this.width = (int) width;
        this.height = (int) height;
    }

    public boolean contains(Point point) {
        return rectangleBounds.contains(point.x, point.y);
    }

    public RectF getRectBound() {
        return rectangleBounds;
    }

    public void setBounds(int startX, int startY, int width, int height) {
        setRect(startX, startY, width, height);
    }

    @Override
    public Rectangle clone() {
        return new Rectangle(x, y, width, height);
    }
}