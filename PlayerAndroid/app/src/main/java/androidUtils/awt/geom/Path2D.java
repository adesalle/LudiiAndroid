package androidUtils.awt.geom;

import android.annotation.SuppressLint;
import  android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Graphics2D;
import androidUtils.awt.PathIterator;
import androidUtils.awt.Rectangle;

import java.awt.Shape;

public abstract class Path2D implements Shape {


    public static class Float extends Path2D
    {
        protected  Path path;

        protected List<String> pathCommands = new ArrayList<>();

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
        public void append(Shape shape, boolean b) {

        }

        @SuppressLint("DefaultLocale")
        public void append(Ellipse2D.Double aDouble, boolean b) {
            if(b)
            {
                path.addOval(aDouble.getBounds().rectangleBounds, Path.Direction.CW);
            }
            else
            {
                RectF rect = aDouble.getBounds().rectangleBounds;
                float x = rect.centerY() + rect.width() / 2;
                float y = rect.centerY() + rect.height() / 2;
                path.moveTo(x, y) ;
                pathCommands.add(String.format("M %f %f", x, y));

                path.addOval(aDouble.getBounds().rectangleBounds, Path.Direction.CW);
            }
            RectF bounds = aDouble.getBounds().rectangleBounds;
            float cx = bounds.centerX();
            float cy = bounds.centerY();
            float rx = bounds.width() / 2.0f;
            float ry = bounds.height() / 2.0f;

            float startX = cx + rx;
            float startY = cy;

            pathCommands.add(String.format("M %f %f", startX, startY));
            pathCommands.add(String.format("C %f %f %f %f %f %f", cx + rx, cy - ry, cx + rx, cy + ry, cx, cy + ry));
            pathCommands.add(String.format("C %f %f %f %f %f %f", cx - rx, cy + ry, cx - rx, cy - ry, cx, cy - ry));
            pathCommands.add(String.format("C %f %f %f %f %f %f", cx - rx, cy - ry, cx - rx, cy + ry, cx, cy + ry));
            pathCommands.add(String.format("C %f %f %f %f %f %f", cx + rx, cy + ry, cx + rx, cy - ry, startX, startY));


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
                pathCommands.add(String.format("M %f %f", x, y));
                path.arcTo(arc.arc, arc.getStartAngle(),  arc.getEndAngle(), arc.isNotOpen());
            }
            RectF arcR = arc.getArc();

            float cx = arcR.centerX();
            float cy = arcR.centerY();
            float rx = arcR.width() / 2.0f;
            float ry = arcR.height() / 2.0f;

            float startAngleRad = (float) Math.toRadians(arc.startAngle);
            float endAngleRad = (float) Math.toRadians(arc.endAngle);

            float startX = cx + rx * (float) Math.cos(startAngleRad);
            float startY = cy + ry * (float) Math.sin(startAngleRad);

            float endX = cx + rx * (float) Math.cos(startAngleRad + endAngleRad);
            float endY = cy + ry * (float) Math.sin(startAngleRad + endAngleRad);

            int endFlag = arc.endAngle > 0 ? 1 : 0;


            int largeArcFlag = Math.abs(arc.endAngle) > 180 ? 1 : 0;

            pathCommands.add(String.format(
                    "M %f %f A %f %f 0 %d %d %f %f",
                    startX, startY, rx, ry, largeArcFlag, endFlag, endX, endY
            ));

        }

        public void reset() {
            path.reset();
        }

        @Override
        public void acceptFill(Graphics2D graph) {
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
            return new PathIterator(path);
        }

        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(getRect());
        }
    }

    public static Path.FillType WIND_EVEN_ODD = Path.FillType.WINDING;

    public static  Path.FillType WIND_NON_ZERO = Path.FillType.EVEN_ODD;
}
