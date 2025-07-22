package androidUtils.swing;

import android.os.Handler;
import android.os.Looper;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class SwingWorker<T, V> {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<T> future;
    private int progress;
    private volatile StateValue state = StateValue.PENDING;
    private final List<V> chunksBuffer = new ArrayList<>();

    public enum StateValue {
        PENDING, STARTED, DONE
    }

    protected abstract T doInBackground() throws Exception;

    protected void process(List<V> chunks) {
        // Default implementation does nothing
    }

    protected void done() {
        // Default implementation does nothing
    }

    public final void execute() {
        if (state != StateValue.PENDING) {
            throw new IllegalStateException("Worker already executed");
        }

        state = StateValue.STARTED;
        firePropertyChange("state", StateValue.PENDING, StateValue.STARTED);

        future = executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    T result = doInBackground();
                    mainHandler.post(() -> {
                        state = StateValue.DONE;
                        firePropertyChange("state", StateValue.STARTED, StateValue.DONE);
                        done();
                    });
                    return result;
                } catch (Exception e) {
                    cancel(true);
                    throw e;
                }
            }
        });
    }

    protected final void publish(V... chunks) {
        synchronized (chunksBuffer) {
            for (V chunk : chunks) {
                chunksBuffer.add(chunk);
            }
        }
        mainHandler.post(this::processChunks);
    }

    private void processChunks() {
        List<V> chunksToProcess;
        synchronized (chunksBuffer) {
            chunksToProcess = new ArrayList<>(chunksBuffer);
            chunksBuffer.clear();
        }
        if (!chunksToProcess.isEmpty()) {
            process(chunksToProcess);
        }
    }

    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public final void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        int oldProgress = this.progress;
        this.progress = progress;
        mainHandler.post(() -> firePropertyChange("progress", oldProgress, progress));
    }

    public final int getProgress() {
        return progress;
    }

    public final StateValue getState() {
        return state;
    }

    public final T get() throws InterruptedException, ExecutionException {
        try {
            return future != null ? future.get() : null;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        return future != null && future.cancel(mayInterruptIfRunning);
    }

    public final boolean isCancelled() {
        return future != null && future.isCancelled();
    }

    public final boolean isDone() {
        return state == StateValue.DONE;
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}