package androidUtils.awt.event;

public class WindowEvent {

    public static final int WINDOW_CLOSING = 1;
    public static final int WINDOW_CLOSED = 2;
    public static final int WINDOW_OPENED = 3;

    private final int id;

    public WindowEvent(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}