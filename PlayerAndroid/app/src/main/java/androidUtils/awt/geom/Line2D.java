package androidUtils.awt.geom;

import android.graphics.Rect;
import androidUtils.awt.Point;
import java.awt.Shape;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;


public abstract class Line2D implements Shape {

    public static class Double extends Line2D {
        private float x1, y1, x2, y2;

        public Double(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Double(Point p1, Point p2)
        {
            this((float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY());
        }

        public Double(double rungLx, double rungLy, double rungRx, double rungRy) {
            this((float) rungLx, (float) rungLy, (float) rungRx, (float)rungRy);
        }

        @Override
        public int getX1() {
            return (int) x1;
        }
        @Override
        public int getX2() {
            return (int) x2;
        }

        @Override
        public int getY1() {
            return (int) y1;
        }

        @Override
        public int getY2() {
            return (int) y2;
        }

        @Override
        public void acceptFill(Graphics2D graph) {
        }

        @Override
        public Rectangle getBounds() {
            int left = (int) Math.min(x1, x2);
            int top = (int) Math.min(y1, y2);
            int right = (int) Math.max(x1, x2);
            int bottom = (int) Math.max(y1, y2);
            return new Rectangle(new Rect(left, top, right, bottom));
        }

        @Override
        public Shape copy() {
            return new Double(x1, y1, x2, y2);
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
    public abstract int getX1();

    public abstract int getX2();

    public abstract int getY1();

    public abstract int getY2();
}
