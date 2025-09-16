package androidUtils.awt;

import android.graphics.Path;

import java.awt.Shape;

import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.geom.Point2D;
import game.util.graph.Poly;

public class Polygon extends GeneralPath {


    public Polygon() {
        path.reset(); // Start with an empty path
    }



    public void close() {
        path.close(); // Close the path to create a polygon
    }

    public Path getPath() {
        return path;
    }

    @Override
    public void acceptFill(Graphics graph) {
        return;
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public Shape copy() {
       Polygon path = new Polygon();
        path.path = new Path(this.path);
        path.currentPoint = new Point2D.Double(currentPoint);
        return path;
    }

    public void addPoint(int i, int i1)
    {
        if(i == 0 && i1 == 0) path.moveTo(i, i1);
        else path.lineTo(i, i1);
    }
}
