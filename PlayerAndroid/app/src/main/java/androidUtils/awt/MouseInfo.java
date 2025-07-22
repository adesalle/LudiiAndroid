package androidUtils.awt;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import playerAndroid.app.StartAndroidApp;

public class MouseInfo {
    private static PointerInfo pointerInfo = new PointerInfo();
    private static boolean isTracking = false;
    private static int lastRawX = 0;
    private static int lastRawY = 0;


    public static class PointerInfo {
        private int x = 0;
        private int y = 0;
        private boolean pressed = false;

        public Point getLocation() {
            return new Point(x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isPressed() {
            return pressed;
        }

        private void updatePosition(int x, int y, boolean pressed) {
            this.x = x;
            this.y = y;
            this.pressed = pressed;
            lastRawX = x;
            lastRawY = y;
        }
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static PointerInfo getPointerInfo() {
        // Maintient la dernière position même sans toucher actif
        if (!pointerInfo.pressed) {
            pointerInfo.updatePosition(lastRawX, lastRawY, false);
        }
        return pointerInfo;
    }

    /**
     * Configure le suivi des événements de toucher sur toute l'activité
     * @param activity L'activité à surveiller
     */
    public static void setupTouchTracking(Activity activity) {
        if (isTracking) return;

        View rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        setupTouchRecursive(rootView);

        isTracking = true;
    }

    private static void setupTouchRecursive(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setupTouchRecursive(group.getChildAt(i));
            }
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);

                int x = (int) event.getRawX(); // Coordonnées absolues
                int y = (int) event.getRawY();

                boolean pressed = event.getAction() != MotionEvent.ACTION_UP &&
                        event.getAction() != MotionEvent.ACTION_CANCEL;

                pointerInfo.updatePosition(x, y, pressed);
                return false; // Ne pas consommer l'événement
            }
        });
    }

    /**
     * Force la mise à jour de la position (pour usage avec des InputManager personnalisés)
     */
    public static void updatePointerPosition(int x, int y, boolean pressed) {
        pointerInfo.updatePosition(x, y, pressed);
    }
}