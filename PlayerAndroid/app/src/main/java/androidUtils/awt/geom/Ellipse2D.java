package androidUtils.awt.geom;

import android.graphics.Rect;
import android.graphics.RectF;

import androidUtils.awt.Graphics;
import androidUtils.awt.Rectangle;

import java.awt.Shape;

public abstract class Ellipse2D implements Shape {
    public static class Double extends Ellipse2D
    {
        RectF ovalBounds;
        public double x;
        public double y;
        public double w;
        public double h;

        public Double(int x, int y, int w, int h)
        {
            setup(x,y,w,h);
            updateRectF();
        }



        public Double(int x, int y, double w, double h)
        {
            setup(x,y,w,h);
            updateRectF();
        }
        public Double(double x, double y, double w, double h)
        {

            setup(x,y,w,h);
            updateRectF();
        }
        public Double(RectF rect)
        {
            this(rect.left, rect.top, rect.width(), rect.height());
        }

        private void updateRectF() {
            ovalBounds = new RectF(
                    (float)this.x,
                    (float)this.y,
                    (float)(this.x + this.w),
                    (float)(this.y + this.h)
            );
        }
        public void setup(double x, double y, double w, double h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        @Override
        public void acceptFill(Graphics graph) {
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

        @Override
        public Shape createIntersection(Rectangle2D newBounds) {
            return null;
        }

        @Override
        public Point2D getLocation() {
            return null;
        }
    }

    public abstract Rectangle getBounds();
}
