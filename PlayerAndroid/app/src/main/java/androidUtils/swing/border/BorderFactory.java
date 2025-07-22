package androidUtils.swing.border;

import androidUtils.awt.Color;
import androidUtils.swing.border.Border;
import androidUtils.swing.border.EmptyBorder;
import androidUtils.swing.border.LineBorder;

public class BorderFactory {

    private BorderFactory() {
        // Private constructor to prevent instantiation
    }

    public static Border createEmptyBorder() {
        return new EmptyBorder(0, 0, 0, 0);
    }

    public static Border createEmptyBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static Border createLineBorder(Color color) {
        return new LineBorder(color, 1);
    }

    public static Border createLineBorder(Color color, int thickness) {
        return new LineBorder(color, thickness);
    }


}