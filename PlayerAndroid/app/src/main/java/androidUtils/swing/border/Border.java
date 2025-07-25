package androidUtils.swing.border;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;


import androidUtils.awt.Color;
import androidUtils.awt.Graphics;
import androidUtils.swing.JPanel;

public interface Border {
    void paintBorder(View c, Graphics g, int x, int y, int width, int height);

    boolean isBorderOpaque();
    Rect getBorderInsets(View c);

    void paintBorder(View jPanel, Graphics graphics);

    default void paintBorder(ViewGroup parent, ViewGroup view) {
        Canvas canvas = new Canvas();
        Rect bounds = new Rect();
        view.getDrawingRect(bounds);
        paintBorder(view, new Graphics(canvas), bounds.left, bounds.top, bounds.width(), bounds.height());
    }

    Color getBorderColor();
}