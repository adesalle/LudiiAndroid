package androidUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidUtils.awt.image.BufferedImage;

public class ImageIO {

    public static void write(BufferedImage img, String outputExtension, File file) throws IOException {
        FileOutputStream outputStream = null;

            outputStream = new FileOutputStream(file);
            Bitmap bitmap = img.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // 100 is full quality
            outputStream.flush();

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static BufferedImage read(URL url) throws IOException {
        InputStream inputStream = url.openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        return new BufferedImage(bitmap);
    }
}
