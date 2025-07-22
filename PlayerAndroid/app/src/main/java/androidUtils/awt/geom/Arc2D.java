package androidUtils.awt.geom;

import android.graphics.RectF;

import java.awt.Shape;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

public abstract class Arc2D implements Shape {

    public static boolean OPEN = false;
    public static boolean CLOSE = true;


    public static class Double extends Arc2D
    {

        RectF arc;

        double startAngle;
        double endAngle;

        boolean isNotOpen;

        public Double(int x, int y, int r, int r1, int startAngle, int extent, boolean isNotOpen)
        {
            this.isNotOpen = isNotOpen;
            arc = new RectF(x, y, x + r, y + r1);
            this.startAngle = startAngle;
            endAngle = extent + startAngle;
        }

        public Double(int x, int y, int r, int r1, int startAngle, int extent, int isNotOpenInt)
        {
            this.isNotOpen = isNotOpenInt == 0;

            arc = new RectF(x, y, x + r, y + r1);
            this.startAngle = startAngle;
            endAngle = extent + startAngle;
        }

        public Double(double x, double y, double r, double r1, double startAngle, double extent, boolean isNotOpen)
        {
            this.isNotOpen = isNotOpen;
            arc = new RectF((float) x, (float) y, (float) (x + r), (float) (y + r1));
            this.startAngle = startAngle;
            endAngle = extent + startAngle;
        }

        public Double(double x, double y, double r, double r1, int startAngle, int extent, int isNotOpenInt) {
            this.isNotOpen = isNotOpenInt == 0;
            arc = new RectF((float) x, (float) y, (float) (x + r), (float) (y + r1));
            this.startAngle = startAngle;
            endAngle = extent + startAngle;
        }


        public RectF getArc() {
            return arc;
        }

        public float getStartAngle() {
            return (float) startAngle;
        }

        public float getEndAngle() {
            return (float) endAngle;
        }



        public boolean isNotOpen() {
            return isNotOpen;
        }

        @Override
        public void acceptFill(Graphics2D graph) {
            return;
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle(
                    (int) arc.left,
                    (int) arc.top,
                    (int) (arc.right - arc.left),
                    (int) (arc.bottom - arc.top)
            );
        }
        @Override
        public Shape copy() {
            return null;
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
}
