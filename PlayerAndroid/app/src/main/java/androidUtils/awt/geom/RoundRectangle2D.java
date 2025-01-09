package androidUtils.awt.geom;

import android.graphics.Rect;

import java.awt.Shape;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

public abstract class RoundRectangle2D extends Rectangle2D.Double{


    public RoundRectangle2D(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public static class Double extends RoundRectangle2D{
        private float x, y, width, height, arcWidth, arcHeight;

        public Double(float x, float y, float width, float height, float arcWidth, float arcHeight) {
            super(x, y, width, height);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
        }
        public Double(double x, double y, double width, double height, double arcWidth, double arcHeight) {
            super((float) x, (float) y, (float) width, (float) height);
            this.x = (float) x;
            this.y = (float) y;
            this.width = (float) width;
            this.height = (float) height;
            this.arcWidth = (float) arcWidth;
            this.arcHeight = (float) arcHeight;
        }

        @Override
        public double getWidth() {
            return width;
        }

        @Override
        public double getHeight() {
            return height;
        }

        @Override
        public int getX() {
            return (int) (x + width/2)  ;
        }

        @Override
        public int getY() {
            return (int)  (y + height/2);
        }

        @Override
        public int getMinX() {
            return (int) x;
        }

        @Override
        public int getMinY() {
            return (int) y;
        }

        @Override
        public int getMaxX() {
            return (int) (x + width);
        }

        @Override
        public int getMaxY() {
            return (int) (y + height) ;
        }


        @Override
        public void acceptFill(Graphics2D graph) {

        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle(new Rect((int) x, (int) y, (int) (x + width), (int) (y + height)));
        }

        @Override
        public Shape copy() {
            return new RoundRectangle2D.Double(this.x, this.y, this.width, this.height, this.arcWidth, this.arcHeight);
        }
    }
}
