package androidUtils.awt.geom;

import android.annotation.SuppressLint;
import android.graphics.Path;
import android.graphics.RectF;
import java.awt.Shape;

import java.util.List;

import androidUtils.awt.Graphics2D;

public class GeneralPath extends  Path2D.Float{


    Point2D.Double point2D = new Point2D.Double(0f,0f);

    private String svgTransform = "";

    @Override
    public Shape copy() {
        GeneralPath path = new GeneralPath();
        path.path = new Path(this.path);
        point2D = new Point2D.Double(point2D);
        return path;
    }

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

    @SuppressLint("DefaultLocale")
    public void lineTo(double x, double y)
    {
        path.lineTo((float) x, (float) y);
        point2D.setLocation(x, y);
        pathCommands.add(String.format("L %f %f", x, y));
    }
    @SuppressLint("DefaultLocale")
    public void lineTo(float x, float y)
    {
        path.lineTo(x, y);
        point2D.setLocation(x, y);
        pathCommands.add(String.format("L %f %f", x, y));
    }

    @SuppressLint("DefaultLocale")
    public void moveTo(double x, double y)
    {
        path.moveTo((float) x, (float) y);
        point2D.setLocation(x, y);
        pathCommands.add(String.format("M %f %f", x, y));
    }
    @SuppressLint("DefaultLocale")
    public void moveTo(float x, float y)
    {
        path.moveTo(x, y);
        point2D.setLocation(x, y);
        pathCommands.add(String.format("M %f %f", x, y));
    }
    @SuppressLint("DefaultLocale")
    public void quadTo(float x, float y, float x0, float y0)
    {
        path.quadTo(x, y, x0, y0);
        point2D.setLocation(x0, y0);
        pathCommands.add(String.format("Q %f %f %f %f", x, y, x0, y0));
    }
    @SuppressLint("DefaultLocale")
    public void quadTo(double x, double y, double x0, double y0)
    {
        path.quadTo((float) x, (float) y, (float) x0, (float) y0);
        point2D.setLocation(x0, y0);
        pathCommands.add(String.format("Q %f %f %f %f", x, y, x0, y0));
    }

    @SuppressLint("DefaultLocale")
    public void curveTo(double x, double y, double x0, double y0, double x1, double y1)
    {
        path.cubicTo((float) x, (float) y, (float) x0, (float) y0, (float) x1, (float) y1);
        pathCommands.add(String.format("C %f %f %f %f %f %f", x, y, x0, y0, x1, y1));
    }

    public void transform(AffineTransform at)
    {
        path.transform(at.getMatrix());
        svgTransform = at.toString();
    }

    public void closePath()
    {
        path.close();
        pathCommands.add("Z");
    }

    public List<String> getPathCommands()
    {
        return pathCommands;
    }
    public String toSVG() {
        StringBuilder svgPath = new StringBuilder();

        if (!svgTransform.isEmpty()) {
            svgPath.append(String.format(" transform=\"%s\"", svgTransform));
        }

        svgPath.append(" d=\"");
        for (String command : pathCommands) {
            svgPath.append(command).append(" ");
        }
        svgPath.append("\"");

        return svgPath.toString();
    }
    @Override
    public void acceptFill(Graphics2D graph) {
        graph.fill(this);
    }


}
