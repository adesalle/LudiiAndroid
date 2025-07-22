package androidUtils.awt;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.PixelCopy;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidUtils.awt.image.BufferedImage;
import playerAndroid.app.StartAndroidApp;

public class Robot {
    private final View targetView;

    public Robot() {
        this(getRootView());
    }

    public Robot(View targetView) {
        this.targetView = targetView;


    }

    private static View getRootView() {
        Activity activity = StartAndroidApp.startAndroidApp();
        if (activity != null) {
            return activity.getWindow().getDecorView().getRootView();
        }
        return null;
    }

    public BufferedImage createScreenCapture(Rectangle bounds) {
        return captureWithPixelCopy(bounds);

    }
    private BufferedImage captureWithPixelCopy(Rectangle bounds) {
        AppCompatActivity activity = StartAndroidApp.startAndroidApp();
        Bitmap bitmap = Bitmap.createBitmap(bounds.width, bounds.height, Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);

        try {
            PixelCopy.request(activity.getWindow(), rect, bitmap, copyResult -> {
                if (copyResult != PixelCopy.SUCCESS) {
                    android.util.Log.e("Robot", "PixelCopy failed: " + copyResult);
                }
            }, new android.os.Handler());
        } catch (IllegalArgumentException e) {
            android.util.Log.e("Robot", "PixelCopy bounds error", e);
            return null;
        }

        return new BufferedImage(bitmap);
    }
}