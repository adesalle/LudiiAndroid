package androidUtils.imageio;


import android.graphics.Bitmap;

import androidUtils.awt.image.RenderedImage;
import androidUtils.imageio.metadata.IIOMetadata;

public class IIOImage {
    private final Bitmap image;
    private final Bitmap[] thumbnails;
    private final IIOMetadata metadata;

    public IIOImage(RenderedImage image, RenderedImage thumbnails, IIOMetadata metadata) {
        if (image == null) {
            throw new IllegalArgumentException("image == null!");
        }
        this.image = image.getBitmap();
        if (thumbnails != null) {
            this.thumbnails = new Bitmap[1];
            this.thumbnails[0] = thumbnails.getBitmap();

        } else {
            this.thumbnails = null;
        }
        this.metadata = metadata;
    }

    public Bitmap getRenderedImage() {
        return image;
    }

    public Bitmap[] getThumbnails() {
        return thumbnails != null ? thumbnails.clone() : null;
    }

    public IIOMetadata getMetadata() {
        return metadata;
    }

    public boolean hasRaster() {
        return true; // Toujours vrai pour Bitmap
    }

    public boolean hasMetadata() {
        return metadata != null;
    }
}