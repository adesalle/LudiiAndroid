package androidUtils.awt.event;

import android.view.View;

public interface FocusListener extends View.OnFocusChangeListener {
    @Override
    default void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focusGained(new FocusEvent(v, FocusEvent.FOCUS_GAINED));
        } else {
            focusLost(new FocusEvent(v, FocusEvent.FOCUS_LOST));
        }
    }

    void focusGained(FocusEvent e);
    void focusLost(FocusEvent e);
}