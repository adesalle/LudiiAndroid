package playerAndroid.app;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCanvasView extends View {
    private Paint paint;

    private Canvas canvas;

    public MyCanvasView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE); // Fond blanc
        canvas.drawLine(50, 50, 300, 300, paint); // Ligne rouge

    }

    public void drawBlueLine() {
        paint.setColor(Color.BLUE);
        canvas.drawLine(100, 100, 400, 400, paint);
    }
}
