package androidUtils.swing;

import android.graphics.Canvas;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import androidUtils.awt.Color;
import playerAndroid.app.StartAndroidApp;

public class JSeparator extends View {
    private int orientation = SwingConstants.HORIZONTAL;
    private Paint paint;

    public JSeparator() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK.toArgb());
        paint.setStrokeWidth(2);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (orientation == SwingConstants.HORIZONTAL) {
            canvas.drawLine(0, 0, getWidth(), 0, paint);
        } else {
            canvas.drawLine(0, 0, 0, getHeight(), paint);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }
}