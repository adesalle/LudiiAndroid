package androidUtils.imageio;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidUtils.imageio.metadata.IIOMetadata;

import java.io.IOException;

public class GifImageWriter extends ImageWriter {
    private static final byte[] GIF_HEADER = { 'G', 'I', 'F', '8', '9', 'a' };
    private static final byte[] GIF_TRAILER = { 0x3B };
    private static final byte[] APPLICATION_EXTENSION = {
            0x21, (byte) 0xFF, 0x0B,
            'N', 'E', 'T', 'S', 'C', 'A', 'P', 'E',
            '2', '.', '0',
            0x03, 0x01, 0x00, 0x00
    };

    @Override
    public void prepareWriteSequence(IIOMetadata streamMetadata) throws IOException {
        super.prepareWriteSequence(streamMetadata);
        output.write(GIF_HEADER);
        // Écrire Logical Screen Descriptor
        output.writeShort(320); // Largeur (à adapter)
        output.writeShort(240); // Hauteur (à adapter)
        output.write(0xF0); // Packed Fields
        output.write(0); // Background Color Index
        output.write(0); // Pixel Aspect Ratio
    }

    @Override
    protected void writeFrame(Bitmap bitmap) throws IOException {
        // Écrire Graphic Control Extension
        output.write(0x21); // Extension Introducer
        output.write(0xF9); // Graphic Control Label
        output.write(4);    // Block Size
        output.write(0);    // Disposal Method, User Input, Transparent Color
        output.writeShort(10); // Delay Time (1 = 10ms)
        output.write(0);    // Transparent Color Index
        output.write(0);    // Block Terminator

        // Écrire Image Descriptor
        output.write(0x2C); // Image Separator
        output.writeShort(0); // Left Position
        output.writeShort(0); // Top Position
        output.writeShort(bitmap.getWidth());
        output.writeShort(bitmap.getHeight());
        output.write(0);    // Packed Fields

        // Écrire les données d'image (simplifié)
        writeBitmapData(bitmap);
    }

    private void writeBitmapData(Bitmap bitmap) throws IOException {
        // Implémentation simplifiée - devrait utiliser LZW compression
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        output.write(8); // LZW Minimum Code Size

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                // Convertir en palette (simplifié)
                byte colorIndex = (byte) ((Color.red(pixel) > 128) ? 1 : 0);
                output.write(colorIndex);
            }
        }
        output.write(0); // Block Terminator
    }

    @Override
    protected void writeSingle(Bitmap bitmap) throws IOException {
        prepareWriteSequence(null);
        writeFrame(bitmap);
        endWriteSequence();
    }

    @Override
    public void endWriteSequence() throws IOException {
        output.write(GIF_TRAILER);
        super.endWriteSequence();
    }
}