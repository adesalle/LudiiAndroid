package androidUtils.imageio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.ServiceLoader;

import androidUtils.awt.image.BufferedImage;

public class ImageIO {

    public static void write(BufferedImage img, String outputExtension, File file) throws IOException {
        FileOutputStream outputStream = null;

            outputStream = new FileOutputStream(file);
            Bitmap bitmap = img.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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

    public static BufferedImage read(File file) throws IOException {
        InputStream inputStream = Files.newInputStream(file.toPath());
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        return new BufferedImage(bitmap);
    }

    public static Iterator<ImageWriter> getImageWritersBySuffix(String suffix) {
        ServiceLoader<ImageWriter> loader = ServiceLoader.load(ImageWriter.class);
        Iterator<ImageWriter> iterator = loader.iterator();

        // Filtrage par format (simplifié)
        return new Iterator<ImageWriter>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ImageWriter next() {
                ImageWriter writer = iterator.next();
                if ("gif".equalsIgnoreCase(suffix) && writer instanceof GifImageWriter) {
                    return writer;
                }
                return null;
            }
        };
    }
}
