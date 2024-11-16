package androidUtils.awt.geom;

import android.graphics.RectF;

public class Arc2D {

    public static boolean OPEN = false;
    public static boolean CLOSE = true;

    public static class Double
    {

        RectF arc;

        double startAngle;
        double endAngle;

        boolean isNotOpen;

        public Double(int x, int y, int r, int r1, int startAngle, int endAngle, boolean isNotOpen)
        {
            this.isNotOpen = isNotOpen;
            arc = new RectF(x, y, x + r, y + r1);
            this.startAngle = startAngle;
            this.endAngle = endAngle;
        }

        public Double(double x, double y, double r, double r1, double startAngle, double endAngle, boolean isNotOpen)
        {
            this.isNotOpen = isNotOpen;
            arc = new RectF((float) x, (float) y, (float) (x + r), (float) (y + r1));
            this.startAngle = startAngle;
            this.endAngle = endAngle;
        }
        public RectF getArc() {
            return arc;
        }

        public float getStartAngle() {
            return (float) startAngle;
        }

        public float getEndAngle() {
            return (float) endAngle;
        }



        public boolean isNotOpen() {
            return isNotOpen;
        }

    }
}
