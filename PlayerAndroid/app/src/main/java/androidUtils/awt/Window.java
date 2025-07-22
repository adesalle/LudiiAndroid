package androidUtils.awt;

import android.app.Activity;

public class Window {
    private final Activity activity;

    public Window(Activity activity) {
        this.activity = activity;
    }

    public void setResizable(boolean resizable) {
        if (!resizable) {
            activity.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        }
    }
}