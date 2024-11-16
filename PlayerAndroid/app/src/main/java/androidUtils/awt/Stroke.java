package androidUtils.awt;

import android.graphics.Paint;

public interface Stroke {

    float getLineWidth();

    Paint.Cap getEndCap();

    Paint.Join getLineJoin();

    float getWidth();

    Paint.Cap getCap();

    Paint.Join getJoin();

    public float getMiterLimit();

    public float[] getDashArray();

    public float getDashPhase();
}
