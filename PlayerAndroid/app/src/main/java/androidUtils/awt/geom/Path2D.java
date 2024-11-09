package androidUtils.awt.geom;

import  android.graphics.Path;
import android.graphics.RectF;

public class Path2D {

    public static class Float
    {
        protected  Path path;

        public void append(Ellipse2D.Double aDouble, boolean b) {
            if(b)
            {
                path.addOval(aDouble.getBounds(), Path.Direction.CW);
            }
            else
            {
                RectF rect = aDouble.getBounds();
                path.moveTo(rect.centerY() + rect.width() / 2, rect.centerY() + rect.height() / 2);
                path.addOval(aDouble.getBounds(), Path.Direction.CW);
            }

        }

        public void reset() {
            path.reset();
        }
    }

    public static Path.FillType WIND_EVEN_ODD = Path.FillType.WINDING;

    public static  Path.FillType WIND_NON_ZERO = Path.FillType.EVEN_ODD;
}
