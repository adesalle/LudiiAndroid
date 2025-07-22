package androidUtils.swing.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics;

public class DefaultHighlighter implements Highlighter {
    private List<HighlightInfo> highlights = new ArrayList<>();
    private Paint highlightPaint;

    public DefaultHighlighter() {
        highlightPaint = new Paint();
        highlightPaint.setARGB(100, 255, 255, 0); // Jaune semi-transparent par défaut
        highlightPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public Object addHighlight(int p0, int p1, HighlightPainter p) throws BadLocationException {
        HighlightInfo info = new HighlightInfo(p0, p1, p);
        highlights.add(info);
        return info;
    }

    @Override
    public void removeHighlight(Object tag) {
        highlights.remove(tag);
    }

    @Override
    public void removeAllHighlights() {
        highlights.clear();
    }

    @Override
    public void paint(Canvas canvas) {
        for (HighlightInfo info : highlights) {
            if (info.painter instanceof DefaultHighlightPainter) {
                DefaultHighlightPainter painter = (DefaultHighlightPainter) info.painter;
                RectF rect = new RectF(
                        getXForPosition(info.start),
                        getYForPosition(info.start),
                        getXForPosition(info.end),
                        getYForPosition(info.end)
                );
                canvas.drawRect(rect, painter.getPaint());
            }
        }
    }

    private float getXForPosition(int pos) {
        // Implémentation simplifiée - à adapter selon votre layout
        return pos * 10f;
    }

    private float getYForPosition(int pos) {
        // Implémentation simplifiée
        return 0f;
    }

    private static class HighlightInfo {
        int start;
        int end;
        HighlightPainter painter;

        HighlightInfo(int start, int end, HighlightPainter painter) {
            this.start = start;
            this.end = end;
            this.painter = painter;
        }
    }

    public static class DefaultHighlightPainter implements HighlightPainter {
        private Paint paint;

        public DefaultHighlightPainter(int color) {
            paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);
        }

        public DefaultHighlightPainter(Color color) {
            this(color.toArgb());
        }

        public Paint getPaint() {
            return paint;
        }

        @Override
        public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
            // Implémentation pour Swing - non utilisée dans Android
        }
    }
}