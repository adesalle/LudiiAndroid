package androidUtils.awt.geom;

import android.graphics.Path;
import android.graphics.PointF;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;

public class GeneralPath extends Path2D.Float {

    protected Point2D.Double currentPoint = new Point2D.Double(0,0);
    private String svgTransform = "";
    private List<String> pathCommands = new ArrayList<>();

    public GeneralPath() {
        this(Path.FillType.WINDING);
    }

    public GeneralPath(Path.FillType rule) {
        this(rule, 20); // Capacité initiale par défaut
    }

    public GeneralPath(Path.FillType rule, int initialCapacity) {
        path = new Path();
        path.setFillType(rule);
    }

    @Override
    public Shape copy() {
        GeneralPath copy = new GeneralPath(path.getFillType());
        copy.path = new Path(this.path);
        copy.currentPoint = new Point2D.Double(currentPoint.x, currentPoint.y);
        copy.svgTransform = this.svgTransform;
        copy.pathCommands = new ArrayList<>(this.pathCommands);
        return copy;
    }

    public Path getPath() {
        return path;
    }

    public Point2D getCurrentPoint() {
        return new Point2D.Double(currentPoint.x, currentPoint.y);
    }

    public void lineTo(double x, double y) {
        path.lineTo((float) x, (float) y);
        segments.add(new PathIterator.Segment(PathIterator.SEG_LINETO, new PointF((float) x, (float) y)));
        updateCurrentPoint(x, y);
    }

    public void lineTo(float x, float y) {
        path.lineTo(x, y);
        segments.add(new PathIterator.Segment(PathIterator.SEG_LINETO, new PointF(x, y)));
        updateCurrentPoint(x, y);
    }

    public void moveTo(double x, double y) {
        path.moveTo((float) x, (float) y);
        segments.add(new PathIterator.Segment(PathIterator.SEG_MOVETO, new PointF((float) x, (float) y)));
        updateCurrentPoint(x, y);
    }

    public void moveTo(float x, float y) {
        path.moveTo(x, y);
        segments.add(new PathIterator.Segment(PathIterator.SEG_MOVETO, new PointF(x, y)));
        updateCurrentPoint(x, y);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        path.quadTo(x1, y1, x2, y2);
        segments.add(new PathIterator.Segment(
                PathIterator.SEG_QUADTO,
                new PointF(x1, y1), // point de contrôle
                new PointF(x2, y2)  // point final
        ));
        updateCurrentPoint(x2, y2);
    }

    public void quadTo(double x1, double y1, double x2, double y2) {
        path.quadTo((float) x1, (float) y1, (float) x2, (float) y2);
        segments.add(new PathIterator.Segment(
                PathIterator.SEG_QUADTO,
                new PointF((float) x1, (float) y1), // point de contrôle
                new PointF((float) x2, (float) y2)  // point final
        ));
        updateCurrentPoint(x2, y2);
    }

    public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
        path.cubicTo((float) x1, (float) y1, (float) x2, (float) y2, (float) x3, (float) y3);
        segments.add(new PathIterator.Segment(
                PathIterator.SEG_CUBICTO,
                new PointF((float) x1, (float) y1), // premier point de contrôle
                new PointF((float) x2, (float) y2), // second point de contrôle
                new PointF((float) x3, (float) y3)  // point final
        ));
        updateCurrentPoint(x3, y3);
    }

    public void transform(AffineTransform at) {
        if (at != null && !at.isIdentity()) {
            path.transform(at.getMatrix());

        }
    }

    public void closePath() {
        if (!path.isEmpty()) {
            path.close();
        }
    }


    public void reset() {
        path.reset();
        currentPoint.setLocation(0, 0);

    }

    @Override
    public void acceptFill(Graphics graph) {
        if (graph != null) {
            graph.fill(this);
        }
    }

    private void updateCurrentPoint(double x, double y) {
        currentPoint.setLocation(x, y);
    }

    private void addPathCommand(String command, Object... coords) {

    }
}