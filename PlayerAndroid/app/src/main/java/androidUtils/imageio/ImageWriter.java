package androidUtils.imageio;

import android.graphics.Bitmap;
import androidUtils.imageio.metadata.IIOMetadata;
import java.io.IOException;

public abstract class ImageWriter {
    protected ImageOutputStream output;
    protected ImageWriteParam param;
    protected IIOMetadata metadata;
    protected boolean sequenceMode = false;

    public static ImageWriter getWriterInstance(String formatName) {
        if ("gif".equalsIgnoreCase(formatName)) {
            return new GifImageWriter();
        }
        throw new UnsupportedOperationException("Format not supported: " + formatName);
    }

    public void setOutput(Object output) {
        if (output instanceof ImageOutputStream) {
            this.output = (ImageOutputStream) output;
        } else {
            throw new IllegalArgumentException("Requires ImageOutputStream");
        }
    }

    public ImageWriteParam getDefaultWriteParam() {
        return new ImageWriteParam() {
            // Paramètres par défaut
        };
    }

    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType,
                                               ImageWriteParam param) {
        return new IIOMetadata();
    }

    public void prepareWriteSequence(IIOMetadata streamMetadata) throws IOException {
        this.sequenceMode = true;
        // Écrire l'en-tête GIF si nécessaire
    }

    public void writeToSequence(IIOImage image, ImageWriteParam param) throws IOException {
        if (!sequenceMode) {
            throw new IllegalStateException("Not in sequence mode");
        }

        Bitmap bitmap = image.getRenderedImage();
        writeFrame(bitmap);
    }

    protected abstract void writeFrame(Bitmap bitmap) throws IOException;

    public void endWriteSequence() throws IOException {
        if (output != null) {
            output.flush();
        }
        sequenceMode = false;
        // Écrire le pied de page GIF si nécessaire
    }

    public void write(IIOMetadata streamMetadata, IIOImage image,
                      ImageWriteParam param) throws IOException {
        if (sequenceMode) {
            writeToSequence(image, param);
        } else {
            Bitmap bitmap = image.getRenderedImage();
            writeSingle(bitmap);
        }
    }

    protected abstract void writeSingle(Bitmap bitmap) throws IOException;

    public void dispose() {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            // Ignorer
        }
    }
}