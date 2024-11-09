package androidUtils.awt;

import static androidUtils.awt.RenderingHints.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidUtils.awt.geom.Ellipse2D;
import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.image.BufferedImage;


public class Graphics2D {

    Canvas canvas;

    Paint paint = new Paint();

    Font font;
    int fontSize;

    Bitmap btp;

    public Graphics2D(Canvas canvas)
    {
        this.canvas = canvas;

    }
    public Graphics2D()
    {
        btp = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(btp);


    }

    public int getWidth()
    {
        return canvas.getWidth();
    }
    public int getHeight()
    {
        return canvas.getHeight();
    }

    public void setFont(Typeface typeface, int fontSize)
    {
        paint = new Paint();
        paint.setTextSize(fontSize);
        paint.setTypeface(typeface);
        font = new Font(typeface,fontSize);
        this.fontSize = fontSize;
    }

    public void setFont(Font font)
    {
        paint = new Paint();
        paint.setTextSize(fontSize);
        paint.setTypeface(font.getFont());
        this.font = font;
        this.fontSize = font.getSize();
    }

    public void setPaint(RadialGradientPaint rgp)
    {
        paint.setShader(rgp.rg);
    }

    public void setPaint(Color color) {
        paint.setColor(color.toArgb());
    }


    public Font getFont()
    {
        return font;
    }


    public static Graphics2D createGraphics(Bitmap btp)
    {
        return new Graphics2D(new Canvas(btp));

    }


    public void setColor(int color)
    {
        paint.setColor(color);
    }
    public void setColor(Color color)
    {
        paint.setColor(color.toArgb());
    }

    public void setStroke(BasicStroke stroke)
    {
        paint.setStrokeWidth( stroke.getWidth());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap( stroke.getCap());
        paint.setStrokeJoin( stroke.getJoin()
        );
    }

    public void setRenderingHint(RenderingHints.Key key, Object value) {

        if(key.equals(KEY_ANTIALIASING))
            if (value == RenderingHints.VALUE_ANTIALIAS_ON) {
                paint.setAntiAlias(true);
            }


        if(key.equals(KEY_RENDERING))
                if (value == RenderingHints.VALUE_RENDER_QUALITY) {
                    paint.setAntiAlias(true);
                    paint.setDither(true);
                }

        if(key.equals(KEY_INTERPOLATION))
                if (value == RenderingHints.VALUE_INTERPOLATION_BICUBIC){
                    paint.setFilterBitmap(true);
                }

        if(key.equals(KEY_COLOR_RENDERING))
                if (value == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
                    paint.setDither(true);
                }

        if(key.equals(KEY_ALPHA_INTERPOLATION))
                if (value == RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY) {
                    paint.setAntiAlias(true);
                }

        if(key.equals(KEY_TEXT_ANTIALIASING))
                if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
                    paint.setAntiAlias(true);
                    paint.setSubpixelText(true);
                }



    }

    public Rect getStringBounds(String text)
    {
        Rect result = new Rect();
        paint.getTextBounds(text,0, text.length(), result );
        return result;
    }

    public void draw(Shape ellipse)
    {
        canvas.drawRect(ellipse.getBounds(), paint);
        paint = new Paint(paint);
    }

    public void draw(GeneralPath path)
    {
        canvas.drawPath(path.getPath(), paint);
        paint = new Paint(paint);
    }

    public void drawString(String text, int x, int y)
    {

        canvas.drawText(text, x, y, paint);
        paint = new Paint(paint);
    }

    public void drawImage(Image image, int x, int y, Paint paint)
    {
        btp = image.getImage();
        canvas.drawBitmap(image.getImage(), x, y, paint);
        this.paint = new Paint(paint);

    }

    public void drawImage(BufferedImage image, String op, int x, int y)
    {
        btp = image.getBitmap();
        canvas.drawBitmap(btp, x, y, paint);
        this.paint = new Paint(paint);

    }

    public void drawImage(BufferedImage image, int x, int y, String op)
    {
        btp = image.getBitmap();
        canvas.drawBitmap(btp, x, y, paint);
        this.paint = new Paint(paint);

    }

    public void  fill(Ellipse2D ellipse)
    {
        RectF bounds = ellipse.getBounds();

        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(bounds, paint);
        paint = new Paint(paint);
    }

    public void fill(Path path)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
        paint = new Paint(paint);
    }
    public void fill(GeneralPath path)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path.getPath(), paint);
        paint = new Paint(paint);
    }

    public void fillRect(int x, int y, int w, int h)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect((float)x, (float)y, (float)(w + x), (float)(h + y), paint);
        paint = new Paint(paint);
    }

    public void fillOval(int x, int y, int w, int h)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval((float)x, (float)y, (float)(w + x), (float)(h + y), paint);
        paint = new Paint(paint);
    }

    public void dispose()
    {
        if(btp != null) btp.recycle();
    }


    public void rotate(double radians, double v, double v1) {
        canvas.rotate((float) Math.toDegrees(radians), (float) v, (float) v1);
    }

    public void setTranslucent()
    {
        paint.setAlpha(128);
    }


    public void setComposite(AlphaComposite composite)
    {
        float alpha = composite.getAlpha();
        if(alpha != -1) paint.setAlpha((int) (alpha * 255));
        paint.setXfermode(composite.getComposite());

    }

}
