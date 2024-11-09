package androidUtils.awt.geom;

import android.graphics.RectF;

import androidUtils.awt.Shape;

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

        @Override
        public RectF getBounds() {
            return ovalBounds;
        }
    }

    public abstract  RectF getBounds();
}
