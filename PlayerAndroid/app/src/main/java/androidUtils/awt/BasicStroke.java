package androidUtils.awt;

import android.graphics.Paint;

public class BasicStroke {
    public static  Paint.Cap CAP_ROUND = Paint.Cap.ROUND;
    public static Paint.Join JOIN_ROUND = Paint.Join.ROUND;
    public static Paint.Join JOIN_MITER = Paint.Join.MITER;


    public Paint.Cap cap;
    public Paint.Join join;

    public float width;


    public BasicStroke(float width, Paint.Cap cap, Paint.Join join)
    {
        this.width = width;
        this.cap = cap;
        this.join = join;
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
}
