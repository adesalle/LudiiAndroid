package graphics.svg.element.style;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics2D;

import graphics.svg.element.Element;

//-----------------------------------------------------------------------------

/**
 * SVG stroke colour property.
 *
 * @author cambolbro
 */
public class Stroke extends Style {
    // Format:
    //	  stroke="rgb(255,0,0)"
    //    Handle stroke types here

    private final Color colour = new Color(0, 0, 0);

    //-------------------------------------------------------------------------

    // **
    // ** BEWARE: Label "stroke" will also match "stroke-width" etc.
    // **

    public Stroke() {
        super("stroke");
    }

    //-------------------------------------------------------------------------

    public Color colour() {
        return colour;
    }

    //-------------------------------------------------------------------------

    @Override
    public Element newOne() {
        return new Stroke();
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
    public void render(Graphics2D g2d, double x0, double y0, Color footprintColour, Color fillColour,
                       Color strokeColour) {
        // ...
    }

    @Override
    public void setBounds() {
        // ...
    }

    //-------------------------------------------------------------------------

}
