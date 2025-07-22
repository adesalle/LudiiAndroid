package androidUtils.awt.event;

import android.view.MotionEvent;
import android.view.View;

import androidUtils.awt.geom.Point2D;
import androidUtils.swing.action.TouchListener;

public interface MouseListener extends View.OnTouchListener {
    void mouseClicked(MouseEvent e);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseEntered(MouseEvent e);
    void mouseExited(MouseEvent e);

    @Override
    default boolean onTouch(View v, MotionEvent event) {
        MouseEvent mouseEvent = new MouseEvent(event, v);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mousePressed(mouseEvent);
                return true;
            case MotionEvent.ACTION_UP:
                mouseClicked(mouseEvent);
                mouseReleased(mouseEvent);
                return true;
        }
        return false;
    }

}