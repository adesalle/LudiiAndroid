package androidUtils.awt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.WindowMetrics;

import playerAndroid.app.AndroidApp;
import playerAndroid.app.StartAndroidApp;

public class Toolkit {

    private static Toolkit instance;
    private final Context context;

    private Toolkit(Context context) {
        this.context = context;
    }

    public static Toolkit getDefaultToolkit() {
        if (instance == null) {
            instance = new Toolkit(StartAndroidApp.getAppContext());
        }
        return instance;
    }

    public Bitmap getImage(int resourceId) {
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public Dimension getScreenSize() {
        Rect info = StartAndroidApp.startAndroidApp().getWindowManager().getCurrentWindowMetrics().getBounds();
        return new Dimension(info.width(), info.height());
    }

    public String getSystemProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
