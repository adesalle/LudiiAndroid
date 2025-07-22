package androidUtils.awt.event;

import android.view.MotionEvent;
import android.view.View;
import androidUtils.awt.Point;

public class MouseEvent {
    public static final int ACTION_HOVER_MOVE = MotionEvent.ACTION_HOVER_MOVE;
    public static final int MOUSE_CLICKED = MotionEvent.ACTION_UP;
    public static final int MOUSE_PRESSED = MotionEvent.ACTION_DOWN;
    public static final int MOUSE_RELEASED = MotionEvent.ACTION_UP;
    public static final int MOUSE_ENTERED = 1001; // Valeur arbitraire
    public static final int MOUSE_EXITED = 1002;  // Valeur arbitraire
    public static final int MOUSE_DRAGGED = MotionEvent.ACTION_MOVE;
    public static final int MOUSE_MOVED = 1003;   // Valeur arbitraire
    public static final int BUTTON1 = MotionEvent.BUTTON_PRIMARY ;
    public static final int BUTTON3 = MotionEvent.BUTTON_TERTIARY;


    private final MotionEvent event;
    private final View view;
    private final int id;
    private boolean popupTrigger = false;

    public MouseEvent(MotionEvent event, View view) {
        this(event, view, event.getAction());
    }

    public MouseEvent(MotionEvent event, View view, int id) {
        this.event = event;
        this.view = view;
        this.id = id;
    }

    // Méthodes existantes (conservées)
    public int getClickCount() {
        return event.getAction() == MotionEvent.ACTION_UP ? 1 : 0;
    }

    public int getX() {
        return (int) event.getX();
    }

    public int getY() {
        return (int) event.getY();
    }

    public Point getPoint() {
        return new Point(event.getX(), event.getY());
    }

    // Nouvelles méthodes ajoutées
    public int getID() {
        return id;
    }

    public View getComponent() {
        return view;
    }

    public boolean isPopupTrigger() {
        return popupTrigger;
    }

    public void setPopupTrigger(boolean popupTrigger) {
        this.popupTrigger = popupTrigger;
    }

    public long getWhen() {
        return event.getEventTime();
    }

    public int getButton() {
        return event.getButtonState();
    }

    public int getModifiers() {
        return event.getMetaState();
    }

    public void translatePoint(int x, int y)
    {
        event.setLocation(x, y);
    }

    public Point getLocationOnScreen() {
        return getPoint();
    }
}