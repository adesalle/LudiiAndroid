package androidUtils.swing;

import android.view.View;

import androidUtils.awt.Component;
import androidUtils.awt.Graphics;

public interface Icon {
    void paintIcon(View c, Graphics g, int x, int y);


    int getIconWidth();
    int getIconHeight();
}