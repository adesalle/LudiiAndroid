package app.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidUtils.awt.RenderingHints;
import androidUtils.awt.batik.ImageRenderer;
import androidUtils.awt.batik.ImageTranscoder;
import androidUtils.awt.batik.SVGAbstractTranscoder;
import androidUtils.awt.batik.TranscoderInput;
import androidUtils.awt.batik.TranscoderOutput;
import androidUtils.awt.image.BufferedImage;

/**
 * Functions for helping out the SVG rendering
 *
 * @author Matthew Stephenson
 */
public class SVGUtil {

    //-------------------------------------------------------------------------

    public static BufferedImage createSVGImage(final String imageEntry, final double width, final double height) {
        // Return null immediately for empty input
        if (imageEntry == null || imageEntry.isEmpty()) {
            return null;
        }

        try {

            final InputStream inputStream = new ByteArrayInputStream(imageEntry.getBytes(StandardCharsets.UTF_8));

            final TranscoderInput input = new TranscoderInput(inputStream);

            BufferedImage[] bfImage = {null};
            // Create a transcoder that will handle the image creation
            final ImageTranscoder t = new ImageTranscoder() {
                private BufferedImage image;

                @Override
                protected ImageRenderer createRenderer() {
                    final ImageRenderer r = super.createRenderer();
                    final RenderingHints rh = r.getRenderingHints();

                    rh.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
                    rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC));
                    rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON));
                    rh.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_QUALITY));
                    rh.add(new RenderingHints(RenderingHints.KEY_DITHERING,
                            RenderingHints.VALUE_DITHER_DISABLE));
                    rh.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY));
                    rh.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_PURE));
                    rh.add(new RenderingHints(RenderingHints.KEY_FRACTIONALMETRICS,
                            RenderingHints.VALUE_FRACTIONALMETRICS_ON));
                    rh.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

                    r.setRenderingHints(rh);
                    return r;
                }

                @Override
                public BufferedImage createImage(final int w, final int h) {
                    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    return image;
                }

                @Override
                public void writeImage(final BufferedImage img, final TranscoderOutput output) {
                    bfImage[0] = image;
                }
            };

            t.addTranscodingHint(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);

            if (width > 0 && height > 0) {
                t.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, Float.valueOf((float) width));
                t.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, Float.valueOf((float) height));
            }

            t.transcode(input, null);

            // Get the image from the transcoder
            if (bfImage[0] != null) {
                return bfImage[0];
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //-------------------------------------------------------------------------

}
