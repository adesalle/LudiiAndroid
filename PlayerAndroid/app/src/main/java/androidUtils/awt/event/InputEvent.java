package androidUtils.awt.event;

public class InputEvent {
    public static final int SHIFT_DOWN_MASK = 1;  // 1
    public static final int CTRL_DOWN_MASK  = 1 << 1;  // 2
    public static final int ALT_DOWN_MASK   = 1 << 2;  // 4

    public static final int CTRL_MASK = CTRL_DOWN_MASK;
    public static final int SHIFT_MASK = SHIFT_DOWN_MASK;
    public static final int ALT_MASK = ALT_DOWN_MASK;

}