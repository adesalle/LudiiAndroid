package androidUtils.awt.event;

import android.view.MotionEvent;
import android.view.View;

public abstract class MouseAdapter implements MouseListener, MouseMotionListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        MouseEvent mouseEvent = new MouseEvent(event, v);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mousePressed(mouseEvent);
                return true;
            case MotionEvent.ACTION_UP:
                mouseClicked(mouseEvent);
                mouseReleased(mouseEvent);
                return true;
            case MotionEvent.ACTION_MOVE:
                mouseDragged(mouseEvent);
                mouseMoved(mouseEvent);
                return true;
        }
        return false;
    }

    // MouseListener methods
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // MouseMotionListener methods
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}