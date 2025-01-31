package androidUtils.awt.geom;

import android.graphics.Rect;
import android.graphics.RectF;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

import java.awt.Shape;

public abstract class Rectangle2D implements Shape {


    public abstract double getWidth();

    public abstract double getHeight();

    public abstract int getX();

    public abstract int getY();

    public abstract int getMinX();

    public abstract int getMinY();

    public abstract int getMaxX();

    public abstract int getMaxY();

    public abstract double getCenterX();
    public abstract double getCenterY();

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
        public Double(RectF rect)
        {
            rectangleBounds = rect;
            setPoint(rect.centerX(), rect.centerY(), rect.right,rect.bottom);
        }

        public Double(RectF rect, float x, float y, float w, float h)
        {
            rectangleBounds = rect;
            setPoint(rect.centerX(), rect.centerY(), rect.right,rect.bottom);
        }
        public Double(Rect rect)
        {
            rectangleBounds = new RectF(rect);
            setPoint(rect.centerX(), rect.centerY(), rect.right,rect.bottom);
        }

        private void setPoint(float x, float y, float w, float h)
        {
            width = w + x;
            height = h + y;
            this.x = x;
            this.y = y;
        }

        public Double(int x, int y, int w, int h)
        {
            rectangleBounds = new RectF(x,y,w + x,h + y);
            setPoint(x, y, w, h);
        }
        public Double(double x, double y, double w, double h)
        {
            rectangleBounds = new RectF((float) x, (float) y, (float) (w+ x), (float)(h+ y));
            setPoint((float) x, (float) y, (float) w, (float) h);
        }

        public Double(float x, float y, float w, float h)
        {
            rectangleBounds = new RectF(x, y, w+ x, h+ y);
            setPoint(x, y, w, h);
        }

        public Double(int x, int y, double w, double h)
        {
            rectangleBounds = new RectF(x,y,(float) (w+ x), (float)(h+ y));
            setPoint((float) x, (float) y, (float) w, (float) h);
        }

        @Override
        public void acceptFill(Graphics2D graph) {
            graph.fillRect(x, y, width, height);
        }

        public Rectangle getBounds() {
            Rect rect = new Rect((int) rectangleBounds.left, (int) rectangleBounds.top, (int) rectangleBounds.right, (int) rectangleBounds.bottom);
            return new Rectangle(rect);
        }

        @Override
        public Shape copy() {
            RectF rectangle = new RectF(rectangleBounds);

            float width = this.width;
            float height = this.height;
            float x = this.x;
            float y = this.y;
            return new Rectangle2D.Double(rectangle, x, y, x + width, y + height);
        }
        @Override
        public int getX() {
            return (int) rectangleBounds.centerX();
        }
        @Override
        public int getY() {
            return (int) rectangleBounds.centerX();
        }

        @Override
        public int getMinX() {
            return (int) rectangleBounds.left;
        }

        @Override
        public int getMinY() {
            return (int) rectangleBounds.top;
        }

        @Override
        public int getMaxX() {
            return (int) rectangleBounds.right;
        }

        @Override
        public int getMaxY() {
            return (int) rectangleBounds.bottom;
        }

        @Override
        public double getCenterX() {
            return rectangleBounds.centerX();
        }

        @Override
        public double getCenterY() {
            return rectangleBounds.centerY();
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
            rectangleBounds = rect.getBounds().rectangleBounds;
        }
        public void setRect(double x, double y, double width, double height) {
            rectangleBounds = new RectF((float) x, (float) y, (float) (width + x), (float)(height + y));
        }

        public void add(Rectangle2D.Double rect)
        {
            rectangleBounds.union(rect.getBounds().rectangleBounds);
        }

        public void setBounds(int startX, int startY, int width, int toolHeight) {
            rectangleBounds = new RectF(x, y, x+ width, y + height);
        }

    }


}
