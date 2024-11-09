package androidUtils.awt.geom;

import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

public class GeneralPath extends  Path2D.Float{


    Point2D point2D = new Point2D.Double(0f,0f);


    public GeneralPath()
    {
        path = new Path();
    }
    public GeneralPath(Path.FillType rule)
    {
        path = new Path();
        path.setFillType(rule);
    }
    public GeneralPath(Path.FillType rule, int initialCapacity)
    {
        path = new Path();
        path.setFillType(rule);
    }
    public GeneralPath(Shape shape)
    {
        path = new Path();

    }

    public Path getPath()
    {
        return path;
    }

    public Point2D getCurrentPoint()
    {
        return point2D;
    }

    public void lineTo(double x, double y)
    {
        path.lineTo((float) x, (float) y);
        point2D.setLocation(x, y);
    }
    public void lineTo(float x, float y)
    {
        path.lineTo(x, y);
        point2D.setLocation(x, y);
    }

    public void moveTo(double x, double y)
    {
        path.moveTo((float) x, (float) y);
        point2D.setLocation(x, y);
    }
    public void moveTo(float x, float y)
    {
        path.moveTo(x, y);
        point2D.setLocation(x, y);
    }
    public void quadTo(float x, float y, float x0, float y0)
    {
        path.quadTo(x, y, x0, y0);
        point2D.setLocation(x0, y0);
    }
    public void quadTo(double x, double y, double x0, double y0)
    {
        path.quadTo((float) x, (float) y, (float) x0, (float) y0);
        point2D.setLocation(x0, y0);
    }

    public void curveTo(double x, double y, double x0, double y0, double x1, double y1)
    {
        path.cubicTo((float) x, (float) y, (float) x0, (float) y0, (float) x1, (float) y1);
    }

    public void closePath()
    {
        path.close();
    }


}
