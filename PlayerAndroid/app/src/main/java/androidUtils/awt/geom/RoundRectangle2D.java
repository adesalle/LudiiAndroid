package androidUtils.awt.geom;

import android.graphics.Rect;

import java.awt.Shape;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

public abstract class RoundRectangle2D extends Rectangle2D{


    public static class Double extends RoundRectangle2D{
        private float x, y, width, height, arcWidth, arcHeight;

        public Double(float x, float y, float width, float height, float arcWidth, float arcHeight) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
        }
        public Double(double x, double y, double width, double height, double arcWidth, double arcHeight) {
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
            return (int) x;
        }

        @Override
        public int getY() {
            return (int) y;
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
