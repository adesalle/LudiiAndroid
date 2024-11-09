package androidUtils.awt;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;

public class EventQueue{
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void invokeLater(Runnable task) {
        mainHandler.post(task);
    }

    public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
        if (Looper.getMainLooper().isCurrentThread()) {
            task.run();
        } else {

            final Object lock = new Object();
            synchronized (lock) {
                mainHandler.post(() -> {
                    try {
                        task.run();
                    } finally {
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                });
                lock.wait();

            }
        }
    }
}