package androidUtils.awt.event;

public class ActionEvent {

    public static final int ACTION_PERFORMED = 1001;

    private final Object source;
    private final long when;
    private final int id;
    private final String command;

    public ActionEvent(Object source, int id, String command) {
        this.source = source;
        this.when = System.currentTimeMillis();
        this.id = id;
        this.command = command;
    }

    public ActionEvent() {
        this.source = null;
        this.when = System.currentTimeMillis();
        this.id = 0;
        this.command = "";
    }

    public ActionEvent(Object source) {
        this.source = source;
        this.when = System.currentTimeMillis();
        this.id = 0;
        this.command = "";
    }

    public Object getSource() {
        return source;
    }

    public long getWhen() {
        return when;
    }

    public int getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }
    // ... autres getters
}