package androidUtils.swing;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidUtils.awt.Component;
import androidUtils.awt.Graphics;
import playerAndroid.app.StartAndroidApp;

public class IconToDrawable {
    public static Drawable convertIconToDrawable(Icon icon, View component) {
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(
                icon.getIconWidth(),
                icon.getIconHeight(),
                android.graphics.Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        Graphics graphics = new Graphics(canvas);
        icon.paintIcon(component, graphics, 0, 0);

        return new android.graphics.drawable.BitmapDrawable(
                StartAndroidApp.getAppContext().getResources(),
                bitmap);
    }
}
