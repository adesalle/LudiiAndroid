package androidUtils.awt.geom;

import android.graphics.Rect;
import android.graphics.RectF;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

import java.awt.Shape;

public abstract class Ellipse2D implements Shape {
    public static class Double extends Ellipse2D
    {
        RectF ovalBounds;

        public Double(int x, int y, int w, int h)
        {
            ovalBounds = new RectF(x,y,w,h);
        }

        public Double(int x, int y, double w, double h)
        {
            ovalBounds = new RectF(x,y,(float) w, (float)h);
        }
        public Double(double x, double y, double w, double h)
        {
            ovalBounds = new RectF((float) x, (float) y,(float) w, (float)h);
        }
        public Double(RectF rect)
        {
            ovalBounds = rect;
        }

        @Override
        public void accept(Graphics2D graph) {
            graph.fill(this);
        }

        @Override
        public Rectangle getBounds() {
            Rect rect = new Rect((int) ovalBounds.centerX(), (int) ovalBounds.bottom, (int) ovalBounds.centerY(), (int) ovalBounds.top);
            return new Rectangle(rect);
        }

        @Override
        public Shape copy() {
            return new Ellipse2D.Double(new RectF(ovalBounds));
        }
    }

    public abstract Rectangle getBounds();
}
