package androidUtils.awt;

import android.graphics.Rect;

public class SVGGraphics2D extends Graphics2D{

    public SVGGraphics2D(int xSize, int ySize)
    {
        super();
        btp.setWidth(xSize);
        btp.setHeight(ySize);
    }

    @Override
    public Rect getStringBounds(String text) {
        return super.getStringBounds(text);
    }
}
