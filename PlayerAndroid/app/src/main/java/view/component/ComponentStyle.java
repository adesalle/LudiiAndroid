package view.component;

import androidUtils.awt.Color;
import androidUtils.awt.Point;
import androidUtils.awt.geom.Point2D;
import java.util.ArrayList;

import androidUtils.awt.SVGGraphics2D;

import other.context.Context;

/**
 * Something to be drawn.
 * View part of MVC for equipment.
 *
 * @author matthew.stephenson and cambolbro
 */
public interface ComponentStyle {
    /**
     * Sets the (localState specific) image for the component.
     */
    void renderImageSVG(final Context context, final int containerIndex, final int imageSize, final int localState, final int value, final boolean secondary, final int maskedValue, final int rotation);

    /**
     * Gets the SVG image for this component for a given localState value.
     */
    SVGGraphics2D getImageSVG(final int localState);

    // Functions for large pieces
    ArrayList<Point> origin();

    Point largePieceSize();

    ArrayList<Point2D> getLargeOffsets();

    // Getters
    Color getSecondaryColour();

    double scale(final Context context, final int containerIndex, final int localState, final int value);
}
