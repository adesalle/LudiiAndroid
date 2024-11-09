package androidUtils.awt;

import android.graphics.RadialGradient;
import android.graphics.Shader;

public class RadialGradientPaint {

    RadialGradient rg;

    public RadialGradientPaint(Point point, int r, float[] dist, Color[] colors)
    {
        int[] color = new int[colors.length];
        for(int i =0; i< color.length; i++)
        {
            color[i] = colors[i].toArgb();
        }
        rg = new RadialGradient(point.x, point.y, r, color, dist, Shader.TileMode.CLAMP);
    }
}
