package androidUtils.awt;



import java.awt.Shape;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Base64;


import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Ellipse2D;
import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.image.BufferedImage;

public class SVGGraphics2D extends Graphics2D{

    private List<String> svgElements = new ArrayList<>();;

    @SuppressLint("DefaultLocale")
    public SVGGraphics2D(int xSize, int ySize)
    {
        super();
        btp.reconfigure(xSize, ySize, btp.getConfig());
        svgElements.add(String.format("<svg width=\"%d\" height=\"%d\" xmlns=\"http://www.w3.org/2000/svg\">", xSize, ySize));

    }

    private SVGGraphics2D()
    {}

    @Override
    public Graphics2D create() {

        SVGGraphics2D g2dnew = new SVGGraphics2D();
        Bitmap btpnew = Bitmap.createBitmap(btp);
        g2dnew.canvas = new Canvas(btpnew);
        g2dnew.btp = btpnew;
        g2dnew.paint =  new Paint(paint);
        g2dnew.font = new Font(font);
        if(at != null) g2dnew.at = new AffineTransform(at);
        g2dnew.fontSize = fontSize;
        g2dnew.clip = clip.copy();
        g2dnew.svgElements = new ArrayList<>(svgElements);

        return g2dnew;
    }

    @Override
    public Rectangle2D getStringBounds(String text) {
        return super.getStringBounds(text);
    }

    @Override
    public void draw(Shape ellipse)
    {
        super.draw(ellipse);
    }
    @Override
    public void draw(GeneralPath path)
    {
        super.draw(path);
        String pathCommands = path.toSVG();

        String pathElement = String.format(
                "<path d=\"%s\" fill=\"%s\" />",
                pathCommands,
                paint.getColor()
        );

        svgElements.add(pathElement);
    }
    @Override
    public void drawString(String text, int x, int y)
    {
        super.drawString(text, x, y);
        @SuppressLint("DefaultLocale") String textElement = String.format(
                "<text x=\"%f\" y=\"%f\" fill=\"%s\" font-size=\"%d\">%s</text>",
                x, y, paint.getColor(), fontSize, text
        );
        svgElements.add(textElement);
    }


    @Override
    public void drawRoundRect(int x, int y, int w, int h, int a, int z) {

        super.drawRoundRect(x, y, w, h, a, z);
        @SuppressLint("DefaultLocale") String roundRectOutline = String.format(
                "<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"%f\" ry=\"%f\" stroke=\"%s\" stroke-width=\"%f\" fill=\"none\" />",
                x, y, w, h, a / 2, z / 2, paint.getColor(), paint.getStrokeWidth()
        );
        svgElements.add(roundRectOutline);
    }
    @Override
    public void drawImage(Image image, int x, int y, Paint paint)
    {

        super.drawImage(image, x, y, paint);
        Bitmap img = image.getImage();
        addImageToSVG(img, x, y, img.getWidth(), img.getHeight());
    }


    @Override
    public void drawImage(BufferedImage image, String op, int x, int y)
    {

        super.drawImage(image, op, x, y);
        Bitmap img = image.getBitmap();
        addImageToSVG(img, x, y, img.getWidth(), img.getHeight());
    }
    @Override
    public void drawImage(BufferedImage image, int x, int y, String op)
    {

        super.drawImage(image, x, y, op);
        Bitmap img = image.getBitmap();
        addImageToSVG(img, x, y, img.getWidth(), img.getHeight());
    }
    public void addImageToSVG(Bitmap image, int x, int y, int width, int height)
    {
        String base64Image = bitmapToBase64(image);
        if (base64Image != null) {
            @SuppressLint("DefaultLocale") String imageTag = String.format(
                    "<image x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" href=\"data:image/png;base64,%s\" />",
                    x, y, width, height, base64Image
            );
            svgElements.add(imageTag);
        }
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    @Override
    public GeneralPath drawPolygon(int[] x, int[] y, float w)
    {
        GeneralPath path = super.drawPolygon(x, y, w);

        List<String> pathCommands = path.getPathCommands();
        String pathData = String.join(" ", pathCommands);

        String pathElement = String.format(
                "<path d=\"%s\" fill=\"%s\" />",
                pathData,
                paint.getColor()
        );

        svgElements.add(pathElement);
        return path;
    }
    @Override
    public void  fill(Ellipse2D ellipse)
    {
        super.fill(ellipse);
        RectF rectf = ellipse.getBounds().getRectBound();
        float x = rectf.centerX();
        float y = rectf.centerY();
        float width = rectf.width();
        float height = rectf.height();
        @SuppressLint("DefaultLocale")
        String ellipseSVG = String.format(
                "<ellipse cx=\"%f\" cy=\"%f\" rx=\"%f\" ry=\"%f\" fill=\"%s\" />",
                x, y, width / 2, height / 2, paint.getColor()
        );
        svgElements.add(ellipseSVG);
    }
    @Override
    public void fill(GeneralPath path)
    {
        super.fill(path);
        List<String> pathCommands = path.getPathCommands();
        String pathData = String.join(" ", pathCommands);

        String pathElement = String.format(
                "<path d=\"%s\" fill=\"%s\" />",
                pathData,
                paint.getColor()
        );

        svgElements.add(pathElement);
    }
    @Override
    public void fillRect(int x, int y, int w, int h)
    {
        super.fillRect(x, y, w, h);
        @SuppressLint("DefaultLocale") String rect = String.format(
                "<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" fill=\"%s\" />",
                x, y, w, h, paint.getColor()
        );
        svgElements.add(rect);
    }
    @Override
    public void fillRoundRect(int x, int y, int w, int h, int a, int z)
    {
        super.fillRoundRect(x, y, w, h, a, z);
        @SuppressLint("DefaultLocale") String roundRect = String.format(
                "<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"%f\" ry=\"%f\" fill=\"%s\" />",
                x, y, w, h, a / 2, z / 2, paint.getColor()
        );
        svgElements.add(roundRect);
    }
    @Override
    public void fillOval(int x, int y, int w, int h)
    {
        super.fillOval(x, y, w, h);
        @SuppressLint("DefaultLocale") String ellipseElement = String.format(
                "<ellipse cx=\"%f\" cy=\"%f\" rx=\"%f\" ry=\"%f\" fill=\"%s\" />",
                x + w / 2.0,
                y + h / 2.0,
                w / 2.0,
                h / 2.0,
                paint.getColor()
        );

        svgElements.add(ellipseElement);
    }
    public AffineTransform getTransform()
    {

        return at;
    }

    public String getSVGDocument() {
        svgElements.add("</svg>");
        StringWriter writer = new StringWriter();
        for (String element : svgElements) {
            writer.write(element);
        }
        return writer.toString();
    }

    public String getSVGElement()
    {
        return getSVGDocument();
    }

}
