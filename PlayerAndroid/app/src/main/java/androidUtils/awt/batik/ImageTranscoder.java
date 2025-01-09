package androidUtils.awt.batik;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidUtils.awt.Point;
import androidUtils.awt.RenderingHints;
import androidUtils.awt.image.BufferedImage;

public abstract class ImageTranscoder {

    public static Key KEY_FORCE_TRANSPARENT_WHITE = new Key("KEY_FORCE_TRANSPARENT_WHITE");

    private final List<Duo> hintsList = new ArrayList<>();

    public abstract BufferedImage createImage(int width, int height);

    public void transcode(TranscoderInput input, TranscoderOutput output) throws Exception {
        InputStream stream = input.getInputStream();

        SVG svg = SVG.getFromInputStream(stream);


        float width = svg.getDocumentWidth();
        float height = svg.getDocumentHeight();

        BufferedImage image = createImage((int) width, (int) height);

        ImageRenderer renderer = new ImageRenderer(image.getBitmap());

        for (Duo hint : hintsList) {
            applyHint(hint, renderer, image);
        }
        BufferedImage dest = this.createImage(image.getWidth(), image.getHeight());
        svg.renderToCanvas(renderer.getCanvas());
        this.writeImage(dest, output);
    }

    private BufferedImage applyHint(Duo hint, ImageRenderer renderer, BufferedImage image) {
        BufferedImage imageModifie = new BufferedImage(image.getBitmap());
        switch (hint.getKey().toString()) {
            case "KEY_WIDTH":
                int newWidth = ((Float) hint.getObject()).intValue();
                if (newWidth > 0) {
                    imageModifie = new BufferedImage(Bitmap.createScaledBitmap(image.getBitmap(), newWidth, image.getBitmap().getHeight(), true));
                }
                break;

            case "KEY_HEIGHT":
                int newHeight = ((Float) hint.getObject()).intValue();
                if (newHeight > 0) {
                    imageModifie = new BufferedImage(Bitmap.createScaledBitmap(image.getBitmap(), image.getBitmap().getWidth(), newHeight, true));
                }
                break;

            case "KEY_FORCE_TRANSPARENT_WHITE":
                if (!(Boolean) hint.getObject()) {

                    renderer.getCanvas().drawColor(Color.TRANSPARENT);
                } else {
                    renderer.getCanvas().drawColor(Color.WHITE);
                }
                break;

            default:
                break;
        }
        return imageModifie;
    }

    protected ImageRenderer createRenderer()
    {
        return new ImageRenderer();
    }

    public void addTranscodingHint(Key key, Object obj)
    {
        hintsList.add(new Duo(key, obj));
    }
    public abstract void writeImage(BufferedImage img, TranscoderOutput output);


    public static class Key {
        public final String key;

        public Key(String key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Key && ((Key) obj).key == this.key;
        }
    }

    private static class Duo {
        private final Key key;
        private final Object object;

        public Duo(Key key, Object object)
        {
            this.key = key;
            this.object = object;
        }

        public Key getKey() {
            return key;
        }

        public Object getObject() {
            return object;
        }
    }
}