package androidUtils.awt;

import static androidUtils.awt.RenderingHints.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.awt.Shape;

import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Arc2D;
import androidUtils.awt.geom.Ellipse2D;
import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.geom.affineTransform.AffineTransformObject;
import androidUtils.awt.geom.Line2D;
import androidUtils.awt.image.BufferedImage;


public class Graphics2D {

    Canvas canvas;

    Paint paint = new Paint();

    Font font;
    int fontSize;

    Bitmap btp;

    AffineTransform at = null;

    private Color backgroundColor = Color.WHITE;

    protected Shape clip;

    private DashPathEffect dashEffect;
    private float[] dashEffectArray = new float[0];
    private float dashPhase = 0;

    public Graphics2D(Canvas canvas)
    {
        this.canvas = canvas;

    }
    public Graphics2D()
    {
        btp = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(btp);


    }

    public Graphics2D create() {

        Graphics2D g2dnew = new Graphics2D();
        Bitmap btpnew = Bitmap.createBitmap(btp);
        g2dnew.canvas = new Canvas(btpnew);
        g2dnew.btp = btpnew;
        g2dnew.paint =  new Paint(paint);
        g2dnew.font = new Font(font);
        if(at != null) g2dnew.at = new AffineTransform(at);
        g2dnew.fontSize = fontSize;
        g2dnew.clip = clip.copy();
        return g2dnew;
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

    public void setStroke(Stroke stroke)
    {
        paint.setStrokeWidth( stroke.getWidth());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap( stroke.getCap());
        paint.setStrokeJoin( stroke.getJoin());
        paint.setStrokeMiter(stroke.getMiterLimit());
        setDashArray(stroke.getDashArray(), stroke.getDashPhase());
    }

    public void setDashArray(float[] dashArray, float phase) {
        dashEffect = new DashPathEffect(dashArray, phase);
        this.dashEffectArray = dashArray;
        dashPhase = phase;
        paint.setPathEffect(dashEffect);
    }

    public Stroke getStroke()
    {
        return new BasicStroke(paint.getStrokeWidth(), paint.getStrokeCap(), paint.getStrokeJoin(), paint.getStrokeMiter(),dashEffectArray, dashPhase);
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

    public void setBackground(Color color) {
        backgroundColor = color;
        clearCanvas();
    }

    public void clearCanvas() {
        Paint originalPaint = new Paint(paint);

        paint.setColor(backgroundColor.toArgb());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        paint = originalPaint;
    }

    public Rectangle2D getStringBounds(String text)
    {
        Rect result = new Rect();
        paint.getTextBounds(text,0, text.length(), result );
        return new Rectangle2D.Double(result);
    }

    public void draw(Shape ellipse)
    {
        if(ellipse instanceof Line2D.Double)draw((Line2D.Double)ellipse);
        else if(ellipse instanceof Ellipse2D.Double)draw((Ellipse2D.Double)ellipse);
        else if(ellipse instanceof Arc2D.Double)drawArc((Arc2D.Double) ellipse);
    }

    public void draw(GeneralPath path)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path.getPath(), paint);
        paint = new Paint(paint);
    }

    public void draw(Ellipse2D.Double ellipse2D)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(ellipse2D.getBounds().getRectBound(), paint);
        paint = new Paint(paint);
    }

    public void draw(Line2D.Double line)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), paint);
        paint = new Paint(paint);
    }

    public void drawString(String text, int x, int y)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawText(text, x, y, paint);
        paint = new Paint(paint);
    }

    public void drawImage(Image image, int x, int y, Paint paint)
    {
        paint.setStyle(Paint.Style.STROKE);
        btp = image.getImage();
        if(at != null) btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(), btp.getHeight(), at.getMatrix(), true);
        canvas.drawBitmap(image.getImage(), x, y, paint);
        this.paint = new Paint(paint);

    }

    public void drawRoundRect(int x, int y, int w, int h, int a, int z) {

        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF((float)x, (float)y, (float)(w + x), (float)(h + y));
        canvas.drawRoundRect(rectF, a, z, paint);
        paint = new Paint(paint);
    }

    public void drawImage(BufferedImage image, String op, int x, int y)
    {
        paint.setStyle(Paint.Style.STROKE);
        btp = image.getBitmap();
        if(at != null) btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(), btp.getHeight(), at.getMatrix(), true);
        canvas.drawBitmap(btp, x, y, paint);
        this.paint = new Paint(paint);

    }

    public void drawImage(BufferedImage image, int x, int y, String op)
    {
        paint.setStyle(Paint.Style.STROKE);
        btp = image.getBitmap();
        if(at != null) btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(), btp.getHeight(), at.getMatrix(), true);
        canvas.drawBitmap(btp, x, y, paint);
        this.paint = new Paint(paint);

    }

    public void drawRect(float x, float y, float w, float h)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x, y, (w + x), (h + y), paint);
        paint = new Paint(paint);
    }

    public GeneralPath drawPolygon(int[] x, int[] y, float w)
    {
        GeneralPath path = new GeneralPath();

        path.moveTo(x[0], y[0]);

        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
        }

        path.closePath();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(w);

        canvas.drawPath(path.getPath(), paint);

        return path;

    }


    public void drawLine(float x, float y, float w, float h)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x, y, (w + x), (h + y), paint);
        paint = new Paint(paint);
    }

    public void drawOval(int x, int y, int w, int h)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval((float)x, (float)y, (float)(w + x), (float)(h + y), paint);
        paint = new Paint(paint);
    }


    public void drawArc(int x, int y, int r, int r1, int startAngle, int endAngle)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(x, y, r, r1, startAngle, endAngle, true, paint);
        paint = new Paint(paint);
    }

    public void drawArc(Arc2D.Double arc)
    {
        paint.setStyle(Paint.Style.STROKE);
        RectF arc2d = arc.getArc();
        canvas.drawArc(arc2d.left, arc2d.top, arc2d.width() - arc2d.left, arc2d.height() - arc2d.top, arc.getStartAngle(), arc.getEndAngle(), true, paint);
        paint = new Paint(paint);
    }

    public void  fill(Ellipse2D ellipse)
    {

        RectF bounds = ellipse.getBounds().getRectBound();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(bounds, paint);
        paint = new Paint(paint);
    }

    public void fill(Shape shape)
    {
        shape.acceptFill(this);
    }


    public void fill(GeneralPath path)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path.getPath(), paint);
        paint = new Paint(paint);
    }

    public void fillArc(int x, int y, int r, int r1, int startAngle, int endAngle)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(x, y, r, r1, startAngle, endAngle, true, paint);
        paint = new Paint(paint);
    }
    public void fillRect(float x, float y, float w, float h)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, (w + x), (h + y), paint);
        paint = new Paint(paint);
    }

    public void fillRect(int x, int y, int w, int h)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect((float)x, (float)y, (float)(w + x), (float)(h + y), paint);
        paint = new Paint(paint);
    }

    public void fillRoundRect(int x, int y, int w, int h, int a, int z)
    {
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF((float)x, (float)y, (float)(w + x), (float)(h + y));
        canvas.drawRoundRect(rectF, a, z, paint);
        paint = new Paint(paint);
    }

    public void fillOval(int x, int y, int w, int h)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval((float)x, (float)y, (float)(w + x), (float)(h + y), paint);
        paint = new Paint(paint);
    }

    public GeneralPath fillPolygon(int[] x, int[] y, float w)
    {
        paint.setStyle(Paint.Style.FILL);
        GeneralPath path = new GeneralPath();

        path.moveTo(x[0], y[0]);

        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
        }

        path.closePath();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(w);

        canvas.drawPath(path.getPath(), paint);

        return path;

    }

    public void dispose()
    {
        if(btp != null) btp.recycle();
    }


    public void rotate(double radians, double v, double v1) {
        canvas.rotate((float) Math.toDegrees(radians), (float) v, (float) v1);
    }

    public void rotate(int rotate) {
        canvas.rotate((float) Math.toDegrees(rotate));
    }

    public void translate(int x, int y)
    {
        canvas.translate(x, y);
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
    


    public void transform(AffineTransform at)
    {
        for (AffineTransformObject obj: at.getAto()) {
            obj.performPost();

        }
        this.at = at;
    }

    public void setTransform(AffineTransform at)
    {
        for (AffineTransformObject obj: at.getAto()) {
            obj.performSet();

        }
        this.at = at;
    }

    public FontRenderContext getFontRenderContext()
    {
        return new FontRenderContext(paint);
    }

    public FontRenderContext getFontMetrics()
    {
        return new FontRenderContext(paint);
    }

    public Shape getClip()
    {
        return clip;
    }

    public void setClip(Shape clip)
    {
        this.clip = clip;
    }


    public Color getColor() {
        return new Color(paint.getColor());
    }


}
