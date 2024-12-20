package graphics.svg.element.style;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics2D;

import graphics.svg.element.Element;

//-----------------------------------------------------------------------------

/**
 * SVG stroke opacity property.
 *
 * @author cambolbro
 */
public class StrokeOpacity extends Style {
    // Format:  stroke-opacity=".5"

    private final double opacity = 1;

    //-------------------------------------------------------------------------

    public StrokeOpacity() {
        super("stroke-opacity");
    }

    //-------------------------------------------------------------------------

    public double opacity() {
        return opacity;
    }

    //-------------------------------------------------------------------------

    @Override
    public Element newOne() {
        return new StrokeOpacity();
    }

    //-------------------------------------------------------------------------

    @Override
    public boolean load(final String expr) {
        final boolean okay = true;

        // ...

        return okay;
    }

    @Override
    public Element newInstance() {
        return null;
    }

    @Override
    public void render
            (
                    Graphics2D g2d, double x0, double y0, Color footprintColour,
                    Color fillColour, Color strokeColour
            ) {
        // ...
    }

    @Override
    public void setBounds() {
        // ...
    }

    //-------------------------------------------------------------------------

}
