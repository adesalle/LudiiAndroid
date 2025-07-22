package androidUtils.swing.event;

public class ListDataEvent {
    public static final int CONTENTS_CHANGED = 0;
    public static final int INTERVAL_ADDED = 1;
    public static final int INTERVAL_REMOVED = 2;

    private Object source;
    private int type;
    private int index0;
    private int index1;

    public ListDataEvent(Object source, int type, int index0, int index1) {
        this.source = source;
        this.type = type;
        this.index0 = index0;
        this.index1 = index1;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex0() {
        return index0;
    }

    public void setIndex0(int index0) {
        this.index0 = index0;
    }
    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public int getIndex1() {
        return index1;
    }
}