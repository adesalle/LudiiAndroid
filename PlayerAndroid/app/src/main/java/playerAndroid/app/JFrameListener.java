package playerAndroid.app;


import android.view.KeyEvent;
import android.view.View;

import java.io.Serializable;

import androidUtils.swing.JFrame;
import app.PlayerApp;

public class JFrameListener extends JFrame implements View.OnKeyListener{


    private static final long serialVersionUID = 1L;
    
    private View.OnKeyListener keyListener;

    public PlayerApp app;


    JFrameListener(final String appName, final PlayerApp app)
    {
        super(appName);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.app = app;
    }



    public void addKeyListener(View.OnKeyListener listener) {
        this.keyListener = listener;
        setOnKeyListener((view, keyCode, event) -> {
            if (keyListener != null) {
                return keyListener.onKey(view, keyCode, event);
            }
            return false;
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
