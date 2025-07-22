package androidUtils.awt.event;

import android.app.Activity;
import android.view.View;

public class FocusEvent {

    public static final int FOCUS_GAINED = View.FOCUS_UP;
    public static final int FOCUS_LOST = View.FOCUS_DOWN;

    private final View view;
    private final int id;

    public FocusEvent(View view, int id) {
        this.view = view;
        this.id = id;
    }

    public FocusEvent(View view) {
        this.view = view;
        this.id = -1;
    }

    public int getID() {
        return id;
    }
}