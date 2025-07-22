package androidUtils.swing.border;

import android.graphics.Rect;
import android.view.View;


import androidUtils.awt.Color;
import androidUtils.awt.Graphics;
import androidUtils.swing.JPanel;

public interface Border {
    void paintBorder(View c, Graphics g, int x, int y, int width, int height);

    boolean isBorderOpaque();
    Rect getBorderInsets(View c);

    void paintBorder(View jPanel, Graphics graphics);

    Color getBorderColor();
}