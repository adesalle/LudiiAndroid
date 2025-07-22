package androidUtils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A utility class for managing background tasks and UI updates.
 * Uses a thread pool for background tasks and a Handler for UI updates.
 */
public class ThreadManager {

    // Singleton instance
    private static ThreadManager instance;

    // Thread pool for background tasks
    private final ExecutorService executor;

    // Handler for UI updates
    private final Handler mainHandler;

    // Private constructor to enforce singleton pattern
    private ThreadManager() {
        // Create a fixed thread pool with 4 threads (adjust based on device capabilities)
        executor = Executors.newFixedThreadPool(4);

        // Create a Handler attached to the main thread
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Returns the singleton instance of ThreadManager.
     */
    public static synchronized ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    /**
     * Executes a task on a background thread.
     *
     * @param task The task to execute.
     */
    public void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * Executes a task on the main (UI) thread.
     *
     * @param task The task to execute.
     */
    public void runOnUiThread(Runnable task) {
        mainHandler.post(task);
    }

    /**
     * Shuts down the thread pool. Call this when the application is closing.
     */
    public void shutdown() {
        executor.shutdown();
    }
}