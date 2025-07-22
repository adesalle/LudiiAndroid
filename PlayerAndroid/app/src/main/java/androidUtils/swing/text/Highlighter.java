package androidUtils.swing.text;

import android.graphics.Canvas;

import androidUtils.awt.Graphics;
import java.awt.Shape;

public interface Highlighter {
    Object addHighlight(int p0, int p1, HighlightPainter p) throws BadLocationException;
    void removeHighlight(Object tag);
    void removeAllHighlights();

    void paint(Canvas canvas);

    interface HighlightPainter {
        void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c);
    }
}