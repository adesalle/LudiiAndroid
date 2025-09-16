package java.awt;


import androidUtils.awt.Graphics;
import androidUtils.awt.Rectangle;
import androidUtils.awt.geom.Point2D;
import androidUtils.awt.geom.Rectangle2D;

public interface Shape {

    void acceptFill(Graphics graph);

    Rectangle getBounds();

    Shape copy();

    Shape createIntersection(Rectangle2D newBounds);

    Point2D getLocation();
}
