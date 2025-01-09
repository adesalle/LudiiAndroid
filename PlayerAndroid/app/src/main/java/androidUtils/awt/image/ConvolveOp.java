package androidUtils.awt.image;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ConvolveOp {
    public static final int EDGE_NO_OP = 0;

    private final Kernel kernel;
    private final int edgeCondition;

    public ConvolveOp(Kernel kernel, int edgeCondition, Object hints) {
        this.kernel = kernel;
        this.edgeCondition = edgeCondition;
    }

    public Bitmap filter(Bitmap src, Bitmap dest) {
        if (dest == null) {
            dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        }

        int width = src.getWidth();
        int height = src.getHeight();
        int kernelWidth = kernel.getWidth();
        int kernelHeight = kernel.getHeight();
        float[] kernelData = kernel.getData();

        int kwHalf = kernelWidth / 2;
        int khHalf = kernelHeight / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float r = 0, g = 0, b = 0;

                for (int ky = -khHalf; ky <= khHalf; ky++) {
                    for (int kx = -kwHalf; kx <= kwHalf; kx++) {
                        int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
                        int pixelY = Math.min(Math.max(y + ky, 0), height - 1);

                        int color = src.getPixel(pixelX, pixelY);
                        float kernelValue = kernelData[(ky + khHalf) * kernelWidth + (kx + kwHalf)];

                        r += Color.red(color) * kernelValue;
                        g += Color.green(color) * kernelValue;
                        b += Color.blue(color) * kernelValue;
                    }
                }

                int newColor = Color.rgb(
                        Math.min(Math.max((int) r, 0), 255),
                        Math.min(Math.max((int) g, 0), 255),
                        Math.min(Math.max((int) b, 0), 255)
                );

                dest.setPixel(x, y, newColor);
            }
        }

        return dest;
    }
}