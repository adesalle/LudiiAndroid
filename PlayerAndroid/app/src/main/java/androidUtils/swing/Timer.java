package androidUtils.swing;

import android.os.Handler;
import android.os.Looper;

import androidUtils.swing.action.ActionListener;

public class Timer {
    Handler timer = new Handler(Looper.getMainLooper());

    int delay;
    ActionListener listener;

    public Timer(int ms, ActionListener listener)
    {
        this.delay = ms;
        this.listener = listener;
    }

    public void start()
    {
        timer.postDelayed(listener, delay);
    }
    public void stop() {
        timer.removeCallbacks(listener);
    }
}
