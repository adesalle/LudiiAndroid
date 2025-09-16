package androidUtils.awt.geom;

import android.annotation.SuppressLint;
import  android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

import java.awt.Shape;

public abstract class Path2D implements Shape {

    protected final List<PathIterator.Segment> segments = new ArrayList<>();
    public List<PathIterator.Segment> getSegments() {
        return segments;
    }

    public static class Float extends Path2D
    {
        protected  Path path;


        public Float()
        {
            path = new Path();
        }

        public Float(Path path)
        {
            this.path = path;
        }

        @Override
        public Shape copy() {
            Path2D.Float path = new Path2D.Float();
            path.path = new Path(this.path);
            return path;
        }

        @Override
        public Shape createIntersection(Rectangle2D newBounds) {
            // Crée une nouvelle région pour le path actuel
            Region currentRegion = new Region();
            RectF bounds = new RectF();
            path.computeBounds(bounds, true);
            currentRegion.setPath(path, new Region((int)bounds.left, (int)bounds.top,
                    (int)bounds.right, (int)bounds.bottom));

            // Crée une région pour le rectangle de découpage
            Region clipRegion = new Region();
            clipRegion.set((int)newBounds.getX(), (int)newBounds.getY(),
                    (int)(newBounds.getX() + newBounds.getWidth()),
                    (int)(newBounds.getY() + newBounds.getHeight()));

            // Effectue l'intersection
            currentRegion.op(clipRegion, Region.Op.INTERSECT);

            // Convertit le résultat en Path
            Path resultPath = new Path();
            RegionIterator iter = new RegionIterator(currentRegion);
            Rect rect = new Rect();
            while (iter.next(rect)) {
                resultPath.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
            }

            return new Path2D.Float(resultPath);
        }

        @Override
        public Point2D getLocation() {
            RectF bounds = new RectF();
            path.computeBounds(bounds, true);
            return new Point2D.Float(bounds.left, bounds.top);
        }

        @SuppressLint("DefaultLocale")
        public void append(Ellipse2D.Double aDouble, boolean b) {
            if(b)
            {
                path.addOval(aDouble.getBounds().rectangleBounds, Path.Direction.CW);
                addOvalSegments(aDouble.getBounds().rectangleBounds);
            }
            else
            {
                RectF rect = aDouble.getBounds().rectangleBounds;
                float x = rect.centerY() + rect.width() / 2;
                float y = rect.centerY() + rect.height() / 2;
                path.moveTo(x, y) ;

                path.addOval(aDouble.getBounds().rectangleBounds, Path.Direction.CW);
                addOvalSegments(aDouble.getBounds().rectangleBounds);
            }



        }
        @SuppressLint("DefaultLocale")
        public void append(Arc2D.Double arc, boolean b) {
            if(b)
            {

                path.arcTo(arc.arc, arc.getStartAngle(),  arc.getEndAngle(), arc.isNotOpen());
            }
            else
            {
                RectF rect = arc.arc;
                float x = rect.centerY() + rect.width() / 2;
                float y = rect.centerY() + rect.height() / 2;
                path.moveTo(x, y);
                path.arcTo(arc.arc, arc.getStartAngle(),  arc.getEndAngle(), arc.isNotOpen());
            }

        }

        public void append(Shape shape, boolean b) {
            if(shape instanceof Arc2D.Double) append((Arc2D.Double) shape, b);
            else if (shape instanceof  Ellipse2D.Double)append((Ellipse2D.Double) shape, b);

        }

        public void reset() {
            path.reset();
        }

        @Override
        public void acceptFill(Graphics graph) {
            return;
        }

        @Override
        public Rectangle getBounds() {
            RectF bounds = getRect();
            Rect rect = new Rect((int) bounds.centerX(), (int) bounds.bottom, (int) bounds.centerY(), (int) bounds.top);
            return new Rectangle(rect);
        }

        private RectF getRect()
        {
            RectF bounds = new RectF();
            path.computeBounds(bounds, true);
            return bounds;
        }

        public boolean isEmpty(){
            return path.isEmpty();
        }

        public PathIterator getPathIterator(AffineTransform transform)
        {
            return new PathIterator(this);
        }

        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(getRect());
        }

        private void addOvalSegments(RectF oval) {
            float cx = oval.centerX();
            float cy = oval.centerY();
            float rx = oval.width() / 2;
            float ry = oval.height() / 2;

            // Constante magique pour approx Bézier
            float k = 0.552284749831f;

            PointF[] points = new PointF[] {
                    new PointF(cx + rx, cy),
                    new PointF(cx + rx, cy + ry * k), new PointF(cx + rx * k, cy + ry), new PointF(cx, cy + ry),
                    new PointF(cx - rx * k, cy + ry), new PointF(cx - rx, cy + ry * k), new PointF(cx - rx, cy),
                    new PointF(cx - rx, cy - ry * k), new PointF(cx - rx * k, cy - ry), new PointF(cx, cy - ry),
                    new PointF(cx + rx * k, cy - ry), new PointF(cx + rx, cy - ry * k), new PointF(cx + rx, cy),
            };

            // MoveTo au départ
            segments.add(new PathIterator.Segment(PathIterator.SEG_MOVETO, points[0]));

            // 4 courbes cubiques
            for (int i = 0; i < 4; i++) {
                int idx = 1 + i * 3;
                segments.add(new PathIterator.Segment(PathIterator.SEG_CUBICTO,
                        points[idx], points[idx + 1], points[idx + 2]));
            }

            segments.add(new PathIterator.Segment(PathIterator.SEG_CLOSE));
        }

    }

    public static Path.FillType WIND_EVEN_ODD = Path.FillType.EVEN_ODD;

    public static  Path.FillType WIND_NON_ZERO = Path.FillType.WINDING;

    public static class Double extends Float {
    }
}
