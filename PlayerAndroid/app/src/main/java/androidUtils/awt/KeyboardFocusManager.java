package androidUtils.awt;


import android.app.Activity;
import android.view.View;

import androidUtils.awt.event.KeyEventDispatcher;
import playerAndroid.app.StartAndroidApp;

public class KeyboardFocusManager {
    private static KeyboardFocusManager instance;
    private KeyEventDispatcher dispatcher;
    private final Activity activity;

    private KeyboardFocusManager(Activity activity) {
        this.activity = activity;
    }

    public static KeyboardFocusManager getCurrentKeyboardFocusManager() {
        if (instance == null) {
            instance = new KeyboardFocusManager(StartAndroidApp.startAndroidApp());
        }
        return instance;
    }

    public void addKeyEventDispatcher(KeyEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setOnKeyListener((v, keyCode, event) -> {
            androidUtils.awt.event.KeyEvent awtEvent = new androidUtils.awt.event.KeyEvent(event);
            return dispatcher.dispatchKeyEvent(awtEvent);
        });
    }

    public void removeKeyEventDispatcher(KeyEventDispatcher dispatcher) {
        if (this.dispatcher == dispatcher) {
            this.dispatcher = null;
            View rootView = activity.getWindow().getDecorView().getRootView();
            rootView.setOnKeyListener(null);
        }
    }
}