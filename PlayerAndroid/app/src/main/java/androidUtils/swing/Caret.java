package androidUtils.swing;

import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import androidUtils.awt.Point;

public class Caret {
    public static final int ALWAYS_UPDATE = 0;
    public static final int NEVER_UPDATE = 1;
    public static final int UPDATE_WHEN_ON_EDT = 2;

    private int updatePolicy = ALWAYS_UPDATE;


    private final AppCompatEditText textPane;
    private boolean visible = true;
    private int blinkRate = 500; // Default blink rate in ms
    private int position = 0;
    private Runnable blinkRunnable;
    private boolean shouldBlink = false;

    public Caret(AppCompatEditText textPane) {
        this.textPane = textPane;
        initBlinking();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            startBlinking();
        } else {
            stopBlinking();
        }
        updateCaretVisibility();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setBlinkRate(int rate) {
        this.blinkRate = rate;
        if (shouldBlink) {
            stopBlinking();
            startBlinking();
        }
    }

    public void setUpdatePolicy(int policy) {
        this.updatePolicy = policy;
    }

    public int getUpdatePolicy() {
        return updatePolicy;
    }

    public int getBlinkRate() {
        return blinkRate;
    }

    public void setDot(int position) {
        this.position = position;
        textPane.setSelection(position);
        updateCaretVisibility();
    }

    public int getDot() {
        return position;
    }

    public void moveDot(int position) {
        setDot(position);
    }

    private void initBlinking() {
        blinkRunnable = new Runnable() {
            private boolean show = true;

            @Override
            public void run() {
                if (shouldBlink && visible) {
                    show = !show;
                    updateCaretVisibility(show);
                    textPane.postDelayed(this, blinkRate);
                }
            }
        };
    }

    private void startBlinking() {
        if (!shouldBlink) {
            shouldBlink = true;
            textPane.postDelayed(blinkRunnable, blinkRate);
        }
    }

    private void stopBlinking() {
        shouldBlink = false;
        textPane.removeCallbacks(blinkRunnable);
        updateCaretVisibility(true);
    }

    private void updateCaretVisibility() {
        updateCaretVisibility(visible && shouldBlink);
    }

    private void updateCaretVisibility(boolean show) {
        if (textPane.getText() == null) return;

        Spannable text = new SpannableString(textPane.getText());
        text.removeSpan(new BackgroundColorSpan(Color.TRANSPARENT));

        if (show && visible) {
            // Highlight the caret position (simple implementation)
            text.setSpan(
                    new BackgroundColorSpan(Color.BLACK),
                    position,
                    position + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        textPane.setText(text);
        textPane.setSelection(position);
    }

    public Point getMagicCaretPosition() {
        // Get the layout of the text
        Layout layout = textPane.getLayout();
        if (layout == null) {
            return new Point(0, 0); // Return default position if layout isn't ready
        }

        // Get the line number for the current caret position
        int line = layout.getLineForOffset(position);

        // Get the primary horizontal position of the caret
        float x = layout.getPrimaryHorizontal(position);

        // Get the vertical position of the line
        float y = layout.getLineTop(line);

        // Convert to screen coordinates
        int[] location = new int[2];
        textPane.getLocationOnScreen(location);

        // Adjust for padding and scrolling
        int paddingLeft = textPane.getPaddingLeft();
        int paddingTop = textPane.getPaddingTop();
        int scrollX = textPane.getScrollX();
        int scrollY = textPane.getScrollY();

        // Calculate final position
        int screenX = location[0] + (int)x + paddingLeft - scrollX;
        int screenY = location[1] + (int)y + paddingTop - scrollY;

        return new Point(screenX, screenY);
    }
}