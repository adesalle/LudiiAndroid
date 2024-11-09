package androidUtils.awt.geom;


public abstract class Point2D {


    public static class Double extends Point2D
    {
        public float x;
        public float y;

        public Double() {
            x = 0;
            y = 0;
        }
        public Double(double x, double y) {

            this.x = (float) x;
            this.y = (float) y;
        }

        public Double(float x, float y) {

            this.x = x;
            this.y = y;
        }

        @Override
        public double getX() {
            return 0;
        }

        @Override
        public double getY() {
            return 0;
        }

        @Override
        public void setLocation(double x, double y)
        {
            this.x = (float) x;
            this.y = (float) y;
        }
        @Override
        public void setLocation(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distance(Point2D pt) {
            double dx = this.x - pt.getX();
            double dy = this.y - pt.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

    }

    public static class Float extends Double
    {
        public Float() {
            x = 0;
            y = 0;
        }
        public Float(double x, double y) {

            this.x = (float) x;
            this.y = (float) y;
        }

        public Float(float x, float y) {

            this.x = x;
            this.y = y;
        }
    }

    public abstract double getX();
    public abstract double getY();
    public abstract void setLocation(double x, double y);
    public abstract void setLocation(float x, float y);
    public abstract double distance(Point2D pt);
}
