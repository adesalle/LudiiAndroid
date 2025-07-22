package androidUtils.awt.event;

public class ItemEvent
{
    public static final int ITEM_FIRST = 701;
    public static final int ITEM_LAST = 701;
    public static final int ITEM_STATE_CHANGED = ITEM_FIRST;

    public static final int SELECTED = 1;
    public static final int DESELECTED = 2;

    private final Object source;
    private final int id;
    private final Object item;
    private final int stateChange;

    public ItemEvent(Object source, int id, Object item, int stateChange)
    {
        if (id != ITEM_STATE_CHANGED)
            throw new IllegalArgumentException("Invalid event id");

        this.source = source;
        this.id = id;
        this.item = item;
        this.stateChange = stateChange;
    }

    public Object getSource()
    {
        return source;
    }

    public int getID()
    {
        return id;
    }

    public Object getItem()
    {
        return item;
    }

    public int getStateChange()
    {
        return stateChange;
    }

    @Override
    public String toString()
    {
        return "ItemEvent[source=" + source +
                ",id=" + id +
                ",item=" + item +
                ",stateChange=" + stateChange + "]";
    }
}