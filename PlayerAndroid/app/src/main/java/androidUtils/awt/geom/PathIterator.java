package androidUtils.awt.geom;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PathIterator {

    public static final int SEG_MOVETO = 0;
    public static final int SEG_LINETO = 1;
    public static final int SEG_QUADTO = 2;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_CLOSE = 4;

    private final List<Segment> segments = new ArrayList<>();
    private int index = 0;

    public PathIterator(Path2D path) {
        segments.addAll(path.getSegments());
    }

    public boolean isDone() {
        return index >= segments.size();
    }

    public void next() {
        if (isDone()) {
            throw new NoSuchElementException("No more segments in path.");
        }
        index++;
    }

    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new IllegalStateException("PathIterator is done.");
        }

        Segment seg = segments.get(index);
        for (int i = 0; i < seg.points.length; i++) {
            coords[i * 2] = seg.points[i].x;
            coords[i * 2 + 1] = seg.points[i].y;
        }

        return seg.type;
    }

    // --- Support classes below ---

    public static class Segment {
        public final int type;
        public final PointF[] points;

        public Segment(int type, PointF... points) {
            this.type = type;
            this.points = points;
        }
    }
}