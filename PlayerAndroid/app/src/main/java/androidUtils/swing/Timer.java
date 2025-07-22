package androidUtils.swing;

import android.os.Handler;
import android.os.Looper;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;

public class Timer {
    private final Handler handler;
    private Runnable timerTask;
    private int delay;
    private int initialDelay;
    private boolean repeats = false;
    private boolean isRunning = false;
    private ActionListener listener;
    private boolean coalesce = true;
    private boolean logTimers = false;

    public Timer(int delay, ActionListener listener) {
        this.delay = delay;
        this.initialDelay = delay;
        this.listener = listener;
        this.handler = new Handler(Looper.getMainLooper());
        this.repeats = true; // Par défaut, le timer répète pour les animations
    }

    public void start() {
        if (isRunning) return;

        isRunning = true;
        timerTask = new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                fireActionPerformed();

                if (repeats && isRunning) {
                    handler.postDelayed(this, delay);
                } else {
                    stop();
                }
            }
        };

        handler.postDelayed(timerTask, initialDelay);
    }

    public void stop() {
        isRunning = false;
        if (timerTask != null) {
            handler.removeCallbacks(timerTask);
        }
    }

    public void restart() {
        stop();
        start();
    }

    private void fireActionPerformed() {
        if (listener != null) {
            ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "timer");
            listener.actionPerformed(e);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setDelay(int delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Invalid delay: " + delay);
        }
        this.delay = delay;
        if (isRunning) {
            restart();
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setInitialDelay(int initialDelay) {
        if (initialDelay < 0) {
            throw new IllegalArgumentException("Invalid initial delay: " + initialDelay);
        }
        this.initialDelay = initialDelay;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }

    public boolean isRepeats() {
        return repeats;
    }

    public void setCoalesce(boolean coalesce) {
        this.coalesce = coalesce;
    }

    public boolean isCoalesce() {
        return coalesce;
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

    public ActionListener getActionListener() {
        return listener;
    }

    public static void setLogTimers(boolean log) {
        // Implémentation optionnelle pour le débogage
    }
}