package androidUtils.awt;

import android.graphics.Paint;

public class BasicStroke implements Stroke{
    public static  Paint.Cap CAP_ROUND = Paint.Cap.ROUND;
    public static  Paint.Cap CAP_BUTT= Paint.Cap.BUTT;
    public static  Paint.Cap CAP_SQUARE = Paint.Cap.SQUARE;
    public static Paint.Join JOIN_ROUND = Paint.Join.ROUND;
    public static Paint.Join JOIN_MITER = Paint.Join.MITER;


    Paint.Cap cap;
    Paint.Join join;

    float width;

    float mitterLimit = 1;
    float[] dash = new float[0];
    float dash_phase = 0;

    public BasicStroke(float width, Paint.Cap cap, Paint.Join join)
    {
        this.width = width;
        this.cap = cap;
        this.join = join;
    }

    public BasicStroke(float width)
    {
        this.width = width;
        this.cap = CAP_SQUARE;
        this.join = JOIN_MITER;
    }

    public BasicStroke(float width, Paint.Cap cap, Paint.Join join, float mitterL, float[] dash,float dash_phase) {
        this.width = width;
        this.cap = cap;
        this.join = join;
        mitterLimit = mitterL;

    }

    public float getLineWidth()
    {
        return width;
    }
    public Paint.Cap getEndCap()
    {
        return cap;
    }

    public Paint.Join getLineJoin()
    {
        return join;
    }

    public float getWidth() {
        return width;
    }

    public Paint.Cap getCap() {
        return cap;
    }

    public Paint.Join getJoin() {
        return join;
    }

    public float getMiterLimit()
    {
        return mitterLimit;
    }
    public float[] getDashArray()
    {
        return  dash;
    }

    public float getDashPhase()
    {
        return dash_phase;
    }
}
