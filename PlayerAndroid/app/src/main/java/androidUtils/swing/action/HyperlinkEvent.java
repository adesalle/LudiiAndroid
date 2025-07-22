package androidUtils.swing.action;

import java.net.URL;

public class HyperlinkEvent {
    public enum EventType {
        ACTIVATED,
        ENTERED,
        EXITED
    }

    private final EventType eventType;
    private final URL url;

    public HyperlinkEvent(Object source, EventType type, URL url) {
        this.eventType = type;
        this.url = url;
    }

    public EventType getEventType() {
        return eventType;
    }

    public URL getURL() {
        return url;
    }
}