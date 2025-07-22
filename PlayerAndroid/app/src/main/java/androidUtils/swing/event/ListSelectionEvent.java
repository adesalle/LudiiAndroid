package androidUtils.swing.event;

/**
 * An event that characterizes a change in the current selection.
 */
public class ListSelectionEvent {
    private Object source;
    private int firstIndex;
    private int lastIndex;
    private boolean isAdjusting;

    /**
     * Constructs a new ListSelectionEvent
     * @param source the source of the event
     * @param firstIndex the first index in the range
     * @param lastIndex the last index in the range
     * @param isAdjusting whether this is one of multiple events
     */
    public ListSelectionEvent(Object source, int firstIndex, int lastIndex, boolean isAdjusting) {
        this.source = source;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.isAdjusting = isAdjusting;
    }

    /**
     * @return the first index in the range
     */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
     * @return the last index in the range
     */
    public int getLastIndex() {
        return lastIndex;
    }

    /**
     * @return whether this is one of multiple events
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * @return the source of the event
     */
    public Object getSource() {
        return source;
    }
}