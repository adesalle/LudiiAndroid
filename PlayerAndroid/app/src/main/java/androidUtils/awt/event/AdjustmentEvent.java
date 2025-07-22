package androidUtils.awt.event;

import androidUtils.swing.JScrollBar;

public class AdjustmentEvent {
    public static final int ADJUSTMENT_VALUE_CHANGED = 1;
    public static final int ADJUSTMENT_FIRST = 1;
    public static final int ADJUSTMENT_LAST = 2;
    public static final int TRACK = 3;
    public static final int BLOCK_DECREMENT = 4;
    public static final int BLOCK_INCREMENT = 5;
    public static final int UNIT_DECREMENT = 6;
    public static final int UNIT_INCREMENT = 7;

    private final JScrollBar source;
    private final int id;
    private final int type;
    private final int value;

    public AdjustmentEvent(JScrollBar source, int id, int type, int value) {
        this.source = source;
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public JScrollBar getSource() {
        return source;
    }

    public int getID() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}