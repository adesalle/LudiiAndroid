package java.awt;


import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

public interface Shape {

    void acceptFill(Graphics2D graph);

    Rectangle getBounds();

    Shape copy();
}
