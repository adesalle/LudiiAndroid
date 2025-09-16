package app.views;

import androidUtils.awt.BasicStroke;
import androidUtils.awt.Color;
import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import app.PlayerApp;
import playerAndroid.app.StartAndroidApp;

//-----------------------------------------------------------------------------

/**
 * Abstract View concept for defining different sections of the Main Window
 *
 * @author Matthew.Stephenson and cambolbro
 */
public abstract class View extends android.view.View {
    protected final PlayerApp app;
    private final boolean debug = false;
    /**
     * Panel's placement.
     */
    public Rectangle placement;

    //-------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public View(final PlayerApp app) {
        super(StartAndroidApp.getAppContext());
        placement = new Rectangle(0, 0, app.width(), app.height());
        this.app = app;
    }

    //-------------------------------------------------------------------------

    /**
     * @return Panel's placement.
     */
    public Rectangle placement() {
        return placement;
    }

    /**
     * @param rect
     */
    public void setPlacement(final Rectangle rect) {
        placement = (Rectangle) rect.clone();
    }

    //-------------------------------------------------------------------------

    /**
     * Paint this panel.
     */
    public abstract void paint(Graphics2D g2d);

    /**
     * Paint a debug rectangle around this panel.
     */
    public void paintDebug(final Graphics2D g2d, final Color colour) {
        if (debug) {
            g2d.setColor(colour);
            g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            g2d.drawRect(placement.x, placement.y, placement.width, placement.height);
        }
    }

    //-------------------------------------------------------------------------

    /**
     * @return Index of the container associated with this view.
     */
    @SuppressWarnings("static-method")
    public int containerIndex() {
        return -1;
    }

    //-------------------------------------------------------------------------

    /**
     * Perform necessary functionality for cursor being on this View
     */
    public void mouseOverAt(final Point pt) {
        // By default, do nothing.
    }

}
