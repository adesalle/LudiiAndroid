package androidUtils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import androidUtils.awt.image.BufferedImage;
import playerAndroid.app.StartAndroidApp;

public class FileManager {

    public static BufferedImage getImageInAssets(String path)
    {
        try
        {
            AssetManager manager = StartAndroidApp.getAppContext().getAssets();
            final InputStream resource = manager.open(path);
            final Bitmap btp = BitmapFactory.decodeStream(resource);
            return new BufferedImage(btp);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
