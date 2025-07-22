package androidUtils.awt.event;

import android.view.View;

public abstract class FocusAdapter implements FocusListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focusGained(new FocusEvent(v, FocusEvent.FOCUS_GAINED));
        } else {
            focusLost(new FocusEvent(v, FocusEvent.FOCUS_LOST));
        }
    }

    public abstract void focusGained(FocusEvent e);
    public abstract void focusLost(FocusEvent e);
}