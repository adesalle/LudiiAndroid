package androidUtils.awt.geom;

import android.graphics.RectF;

import androidUtils.awt.Shape;

public abstract class Rectangle2D implements Shape {


    public abstract double getWidth();
    public abstract double getHeight();
    public abstract RectF getBounds();

    public abstract int getX();

    public abstract int getY();

    public static class Double extends Rectangle2D
    {
        protected RectF rectangleBounds;

        public float width;
        public float height;
        public float x;
        public float y;

        public Double()
        {
            rectangleBounds = new RectF();
            setPoint(0,0,0,0);
        }

        private void setPoint(float x, float y, float w, float h)
        {
            width = w;
            height = h;
            this.x = x;
            this.y = y;
        }

        public Double(int x, int y, int w, int h)
        {
            rectangleBounds = new RectF(x,y,w,h);
            setPoint(x, y, w, h);
        }
        public Double(double x, double y, double w, double h)
        {
            rectangleBounds = new RectF((float) x, (float) y, (float) w, (float) h);
            setPoint((float) x, (float) y, (float) w, (float) h);
        }

        public Double(float x, float y, float w, float h)
        {
            rectangleBounds = new RectF(x, y, w, h);
            setPoint(x, y, w, h);
        }

        public Double(int x, int y, double w, double h)
        {
            rectangleBounds = new RectF(x,y,(float) w, (float)h);
            setPoint((float) x, (float) y, (float) w, (float) h);
        }

        public RectF getBounds() {
            return rectangleBounds;
        }

        public int getX() {
            return (int) rectangleBounds.centerX();
        }

        public int getY() {
            return (int) rectangleBounds.centerX();
        }

        @Override
        public double getWidth() {
            return (int) rectangleBounds.width();
        }

        @Override
        public double getHeight() {
            return (int) rectangleBounds.height();
        }

        public void setRect(Rectangle2D.Double rect) {
            rectangleBounds = rect.getBounds();
        }
        public void setRect(double x, double y, double width, double height) {
            rectangleBounds = new RectF((float) x, (float) y, (float) width, (float) height);
        }

        public void add(Rectangle2D.Double rect)
        {
            rectangleBounds.union(rect.getBounds());
        }



    }


}
