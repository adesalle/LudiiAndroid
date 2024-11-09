package androidUtils.awt;

import androidUtils.awt.geom.Point2D;

public class Point extends Point2D {

    public int x ;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public double getX() {
        return x;
    }


    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setLocation(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    @Override
    public double distance(Point2D pt) {
        double dx = this.x - pt.getX();
        double dy = this.y - pt.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
