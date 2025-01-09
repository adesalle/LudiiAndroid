package androidUtils.awt;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

import java.util.NoSuchElementException;

public class PathIterator {

    public static final int SEG_MOVETO = 0;
    public static final int SEG_LINETO = 1;
    public static final int SEG_QUADTO = 2;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_CLOSE = 4;

    private final PathMeasure pathMeasure;
    private final float[] position = new float[2];
    private final float[] tangent = new float[2];
    private float distance = 0;
    private final float pathLength;

    private boolean isDone = false;

    public PathIterator(Path path) {
        pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();
        if (pathLength == 0) {
            isDone = true;
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void next() {
        if (isDone) {
            throw new NoSuchElementException("No more elements in path.");
        }

        distance += 1; // Ajustez le pas si nécessaire.
        if (distance > pathLength) {
            isDone = true;
        }
    }

    public int currentSegment(double[] coords) {
        if (isDone) {
            throw new IllegalStateException("PathIterator is done.");
        }

        boolean hasPos = pathMeasure.getPosTan(distance, position, tangent);
        if (!hasPos) {
            isDone = true;
            return SEG_CLOSE;
        }

        coords[0] = position[0];
        coords[1] = position[1];


        return SEG_LINETO;
    }
}
