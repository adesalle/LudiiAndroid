package androidUtils.swing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.TextView;

import androidUtils.awt.Dimension;
import playerAndroid.app.StartAndroidApp;

public class JProgressBar extends View {
    private int value = 0;
    private int max = 100;
    private boolean stringPainted = true;
    private String progressText = "";
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF progressRect;
    private TextView textView;

    public JProgressBar() {
        this(0);
    }

    public JProgressBar(int value) {
        super(StartAndroidApp.getAppContext());
        this.value = value;
        init();
    }

    private void init() {
        // Initialisation des peintures
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint();
        progressPaint.setColor(Color.BLUE);
        progressPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        progressRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float progressWidth = (width * value) / max;

        // Dessin du fond
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        // Dessin de la progression
        progressRect.set(0, 0, progressWidth, height);
        canvas.drawRect(progressRect, progressPaint);

        // Dessin du texte si activé
        if (stringPainted) {
            String text = progressText.isEmpty() ? value + "%" : progressText;
            float x = width / 2;
            float y = height / 2 - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setValue(int value) {
        this.value = Math.min(Math.max(value, 0), max);
        invalidate();
        requestLayout();
    }

    public int getValue() {
        return value;
    }

    public void setMaximum(int max) {
        this.max = max;
        invalidate();
        requestLayout();
    }

    public void setStringPainted(boolean stringPainted) {
        this.stringPainted = stringPainted;
        invalidate();
    }

    public void setString(String text) {
        this.progressText = text;
        invalidate();
    }

    public void setPreferredSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    public void setVisible(boolean b) {
        if(b) setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);
    }
}