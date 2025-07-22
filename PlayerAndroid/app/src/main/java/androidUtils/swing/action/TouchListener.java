package androidUtils.swing.action;

import androidUtils.awt.geom.Point2D;
import android.view.MotionEvent;
import android.view.View;

public interface TouchListener extends View.OnTouchListener {
    // Seuil pour considérer un mouvement comme un déplacement (en pixels)
    int CLICK_THRESHOLD = 20;
    // Durée maximale pour considérer un toucher comme un clic (en ms)
    long CLICK_DURATION = 200;

    void onClick(MotionEvent e);
    void onPress(MotionEvent e);
    void onDrag(MotionEvent e);
    void onMove(MotionEvent e);
    void onRelease(MotionEvent e);
    void onMouseEnter(MotionEvent e);
    void onMouseExit(MotionEvent e);

    Point2D getMousePosition();
    void setLastTouchPosition(float x, float y);

    @Override
    default boolean onTouch(View v, MotionEvent event) {
        Point2D lastPos = getMousePosition();
        float x = event.getX();
        float y = event.getY();
        long time = event.getEventTime();
        float dx;
        float dy;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setLastTouchPosition(x, y);
                onPress(event);
                // Pourrait être considéré comme une entrée de souris
                onMouseEnter(event);
                break;

            case MotionEvent.ACTION_MOVE:
                // Calcul de la distance depuis le point de contact initial
                dx = (float) (x - lastPos.getX());
                dy = (float) (y - lastPos.getY());
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                onMove(event);

                // Si la distance dépasse le seuil, c'est un drag
                if (distance > CLICK_THRESHOLD) {
                    onDrag(event);
                }
                setLastTouchPosition(x, y);
                break;

            case MotionEvent.ACTION_UP:
                // Vérifier si c'est un clic (temps court et petite distance)
                long duration = time - event.getDownTime();
                dx = (float) (x - lastPos.getX());
                dy = (float) (y - lastPos.getY());
                distance = (float) Math.sqrt(dx * dx + dy * dy);

                onRelease(event);

                if (duration < CLICK_DURATION && distance < CLICK_THRESHOLD) {
                    onClick(event);
                }

                // Pourrait être considéré comme une sortie de souris
                onMouseExit(event);
                break;

            case MotionEvent.ACTION_CANCEL:
                onRelease(event);
                onMouseExit(event);
                break;
        }

        return true;
    }
}