package androidUtils.awt.geom;

import android.graphics.Rect;
import android.graphics.RectF;

import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;
import java.awt.Shape;

public abstract class Rectangle2D implements Shape {
    public abstract boolean contains(double x, double y);
    public abstract double getWidth();
    public abstract double getHeight();
    public abstract double getX();
    public abstract double getY();
    public abstract double getMinX();
    public abstract double getMinY();
    public abstract double getMaxX();
    public abstract double getMaxY();
    public abstract double getCenterX();
    public abstract double getCenterY();
    public abstract RectF getRectangleBounds();
    public abstract void setRect(double x, double y, double width, double height);

    public static class Double extends Rectangle2D {
        public double x;
        public double y;
        public double width;
        public double height;
        protected RectF rectangleBounds;

        public Double() {
            this(0, 0, 0, 0);
        }

        public Double(RectF rect) {
            this(rect.left, rect.top, rect.width(), rect.height());
        }

        public Double(Rect rect) {
            this(rect.left, rect.top, rect.width(), rect.height());
        }

        public Double(double x, double y, double w, double h) {
            setRect(x, y, w, h);
        }

        public Double(float x, float y, float w, float h) {
            setRect(x, y, w, h);
        }

        private void updateRectF() {
            rectangleBounds = new RectF(
                    (float)this.x,
                    (float)this.y,
                    (float)(this.x + this.width),
                    (float)(this.y + this.height)
            );
        }

        @Override
        public void setRect(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            updateRectF();
        }

        public void setRect(Rectangle2D.Double rect) {
            setRect(rect.x, rect.y, rect.width, rect.height);
        }

        @Override
        public double getX() { return x; }
        @Override
        public double getY() { return y; }
        @Override
        public double getWidth() { return width; }
        @Override
        public double getHeight() { return height; }
        @Override
        public double getMinX() { return x; }
        @Override
        public double getMinY() { return y; }
        @Override
        public double getMaxX() { return x + width; }
        @Override
        public double getMaxY() { return y + height; }
        @Override
        public double getCenterX() { return x + width/2; }
        @Override
        public double getCenterY() { return y + height/2; }

        @Override
        public RectF getRectangleBounds() {
            return rectangleBounds;
        }

        @Override
        public boolean contains(double x, double y) {
            return x >= this.x && x <= this.x + width &&
                    y >= this.y && y <= this.y + height;
        }

        public boolean intersects(Rectangle rect) {
            return !(rect.x >= this.x + this.width ||
                    rect.x + rect.width <= this.x ||
                    rect.y >= this.y + this.height ||
                    rect.y + rect.height <= this.y);
        }

        public void add(Rectangle2D.Double rect) {
            double minX = Math.min(this.x, rect.x);
            double minY = Math.min(this.y, rect.y);
            double maxX = Math.max(this.x + this.width, rect.x + rect.width);
            double maxY = Math.max(this.y + this.height, rect.y + rect.height);
            setRect(minX, minY, maxX - minX, maxY - minY);
        }

        public void setBounds(int startX, int startY, int width, int height) {
            setRect(startX, startY, width, height);
        }

        public void setWidthHeight(double w, double h)
        {
            setRect(x, y, w, h);
        }

        public Rectangle getBounds() {
            return new Rectangle(
                    (int)x,
                    (int)y,
                    (int)width,
                    (int)height
            );
        }

        @Override
        public void acceptFill(Graphics graph) {
            graph.fillRect((float)x, (float)y, (float)width, (float)height);
        }

        @Override
        public Shape copy() {
            return new Rectangle2D.Double(x, y, width, height);
        }

        @Override
        public Shape createIntersection(Rectangle2D newBounds) {
            return null;
        }

        @Override
        public Point2D getLocation() {
            return new Point2D.Double(x, y);
        }
    }
}