package androidUtils.awt.event;

import android.view.View;

import androidUtils.awt.Dialog;
import androidUtils.swing.JDialog;

public class WindowEvent {

    public static final int WINDOW_OPENED = 100;
    public static final int WINDOW_CLOSING = 101;
    public static final int WINDOW_CLOSED = 102;

    private final View source;
    private final int id;

    public WindowEvent(View source, int id) {
        this.source = source;
        this.id = id;
    }

    public WindowEvent(Dialog source, int id) {
        this.source = source.getContentPane();
        this.id = id;
    }


    public View getSource() {
        return source;
    }

    public int getID() {
        return id;
    }
}
