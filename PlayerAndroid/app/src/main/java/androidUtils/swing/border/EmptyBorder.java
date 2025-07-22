package androidUtils.swing.border;

import android.graphics.Rect;
import android.view.View;

import androidUtils.awt.Graphics;

/**
 * EmptyBorder - Crée une bordure vide mais qui occupe de l'espace
 */
public class EmptyBorder extends AbstractBorder {
    private final Rect insets;

    public EmptyBorder(int top, int left, int bottom, int right) {
        this.insets = new Rect(left, top, right, bottom);
    }

    public EmptyBorder(int horizontal, int vertical) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public EmptyBorder(int margin) {
        this(margin, margin, margin, margin);
    }

    public EmptyBorder() {
        this(0, 0, 0, 0);
    }

    @Override
    public Rect getBorderInsets(View c) {
        return new Rect(insets);
    }

    @Override
    public Rect getBorderInsets(View c, Rect destInsets) {
        destInsets.set(insets);
        return destInsets;
    }

    @Override
    public void paintBorder(View c, Graphics g, int x, int y, int width, int height) {

    }

    public void paintBorder(View c, Graphics g)
    {

    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public void setInsets(int top, int left, int bottom, int right) {
        insets.set(left, top, right, bottom);
    }

    public int getTop() {
        return insets.top;
    }

    public int getLeft() {
        return insets.left;
    }

    public int getBottom() {
        return insets.bottom;
    }

    public int getRight() {
        return insets.right;
    }
}