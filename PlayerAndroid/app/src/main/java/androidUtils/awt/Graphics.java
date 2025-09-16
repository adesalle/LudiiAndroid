package androidUtils.awt;

import static androidUtils.awt.RenderingHints.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import java.awt.Shape;

import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Arc2D;
import androidUtils.awt.geom.Ellipse2D;
import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.geom.affineTransform.AffineTransformObject;
import androidUtils.awt.geom.Line2D;
import androidUtils.awt.image.BufferedImage;

import com.caverock.androidsvg.SVG;

public class Graphics{

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


    public Graphics(Canvas canvas, Bitmap btp)
    {

        this.btp = btp;
        this.canvas = canvas;
        canvas.disableZ();
        this.clip = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
        font = new Font(Typeface.SANS_SERIF, 12);

    }
    public Graphics()
    {
        btp = Bitmap.createBitmap(500, 300, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(btp);
        canvas.disableZ();

        this.clip = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
        font = new Font(Typeface.SANS_SERIF, 12);
    }

    public Graphics(Bitmap bt)
    {
        btp = bt;
        this.canvas = new Canvas(btp);

        this.clip = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
        font = new Font(Typeface.SANS_SERIF, 12);
    }

    public Graphics(Canvas canvas) {
        this(canvas, Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB));
        canvas.setBitmap(btp);
    }

    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
        this.clip = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public Canvas getCanvas()
    {
        return canvas;
    }

    public Bitmap getBitmap()
    {
        return btp;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setBitmap(Bitmap btp) {
        this.btp = btp;
        canvas = new Canvas(btp);
    }

    public Graphics create() {

        Graphics g2dnew = new Graphics();
        g2dnew.btp  = Bitmap.createBitmap(btp);
        g2dnew.canvas = new Canvas(g2dnew.btp);
        g2dnew.paint =  new Paint(paint);
        g2dnew.font = new Font(font);
        if(at != null) g2dnew.at = new AffineTransform(at);
        g2dnew.fontSize = fontSize;
        g2dnew.clip = clip.copy();
        return g2dnew;
    }

    public Graphics create(int x, int y, int width, int height) {
        Graphics g2dnew = new Graphics();

        // Create a new bitmap with the specified dimensions
        g2dnew.btp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        g2dnew.canvas = new Canvas(g2dnew.btp);

        // Copy the relevant portion from the original bitmap
        Canvas tempCanvas = new Canvas(g2dnew.btp);
        Rect srcRect = new Rect(x, y, x + width, y + height);
        Rect destRect = new Rect(0, 0, width, height);
        tempCanvas.drawBitmap(btp, srcRect, destRect, null);

        // Copy graphic attributes
        g2dnew.paint = new Paint(paint);
        g2dnew.font = new Font(font);
        g2dnew.fontSize = fontSize;

        // Adjust and copy the affine transform
        if (at != null) {
            g2dnew.at = new AffineTransform(at);
            g2dnew.at.translate(-x, -y);  // Adjust for the new origin
        }

        // Adjust and copy the clip
        if (clip != null) {
            g2dnew.clip = clip.copy();
            // Intersect with the new bounds
            Rectangle2D newBounds = new Rectangle2D.Double(0, 0, width, height);
            g2dnew.clip = g2dnew.clip.createIntersection(newBounds);
        } else {
            g2dnew.clip = new Rectangle2D.Double(0, 0, width, height);
        }

        return g2dnew;
    }

    public static Graphics createGraphics(Bitmap btp)
    {
        return new Graphics(btp);

    }



    public void setFont(Typeface typeface, int fontSize)
    {
        paint.setTextSize(fontSize);
        paint.setTypeface(typeface);
        font = new Font(typeface,fontSize);
        this.fontSize = fontSize;
        
    }

    public void setFont(Font font)
    {
        this.font = font;
        this.fontSize = font.getSize();
        paint.setTextSize(fontSize);
        paint.setTypeface(font.getFont());
        

    }

    public void setPaint(RadialGradientPaint rgp)
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(rgp.rg);
    }

    public void setPaint(Color color) {
        paint.setColor(color.toArgb());
    }


    public Font getFont()
    {
        return font;
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
        if (dashArray != null) {
            // Validate dash array
            if (dashArray.length < 2 || dashArray.length % 2 != 0) {
                dashEffect = null; // Fall back to solid line
            } else {
                boolean valid = true;
                for (float f : dashArray) {
                    if (f <= 0) {
                        valid = false;
                        break;
                    }
                }
                dashEffect = valid ? new DashPathEffect(dashArray, phase) : null;
            }
        } else {
            dashEffect = null;
        }

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

    public void resetPaint()
    {
        paint = new Paint();
        paint.setAlpha(1);
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

    public void draw(Shape shape)
    {
        if (shape instanceof Line2D.Double) {
            draw((Line2D.Double) shape);
        } else if (shape instanceof Ellipse2D.Double) {
            draw((Ellipse2D.Double) shape);
        } else if (shape instanceof Arc2D.Double) {
            drawArc((Arc2D.Double) shape);
        } else if (shape instanceof GeneralPath) {
            draw((GeneralPath) shape);
        } else if (shape instanceof Rectangle2D) {
            drawRect((float)((Rectangle2D)shape).getX(),
                    (float)((Rectangle2D)shape).getY(),
                    (float)((Rectangle2D)shape).getWidth(),
                    (float)((Rectangle2D)shape).getHeight());
        }
        
    }

    public void draw(GeneralPath path)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path.getPath(), paint);
        paint = new Paint(paint);
        
    }

    public void draw(Ellipse2D.Double ellipse)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval((float) ellipse.x, (float) ellipse.y, (float) ellipse.w, (float) ellipse.h, paint);
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
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);          // Lissage activé (essentiel)
        paint.setSubpixelText(true);
        canvas.drawText(text, x, y, paint);
        paint = new Paint(paint);
        
    }



    public void drawRoundRect(int x, int y, int w, int h, int a, int z) {

        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF((float)x, (float)y, (float)(w + x), (float)(h + y));
        canvas.drawRoundRect(rectF, a, z, paint);
        paint = new Paint(paint);
        
    }


    public void drawRoundRect(float x, float y, float w, float h, float a, float z) {
        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(x, y, w + x, h + y);
        canvas.drawRoundRect(rectF, a, z, paint);
        paint = new Paint(paint);
    }

    public void drawRoundRect(RectF rect, int i, int i1) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rect, i, i1, paint);
        paint = new Paint(paint);
    }

    public void drawImage(Image image, int x, int y, Paint paint)
    {

        Paint p = null;
        if (paint != null)
        {
            p = new Paint(paint);
            p.setStyle(Paint.Style.FILL);
        }

        Bitmap src = image.getImage();
        if (at != null) {
            src = Bitmap.createBitmap(
                    src, 0, 0, src.getWidth(), src.getHeight(),
                    at.getMatrix(), true
            );
        }
        canvas.drawBitmap(src, x, y, p);


    }

    public void drawImage(BufferedImage img, int srcX, int srcY,
                          int srcWidth, int srcHeight,
                          int destX, int destY,
                          int destWidth, int destHeight,
                          Object observer) {
        if (img == null) return;

        Rect srcRect = new Rect(srcX, srcY, srcX + srcWidth, srcY + srcHeight);
        Rect destRect = new Rect(destX, destY, destX + destWidth, destY + destHeight);

        canvas.drawBitmap(img.getBitmap(), srcRect, destRect, null);
    }
    public void drawImage(BufferedImage image, Paint op, int x, int y)
    {
        drawImage(image, x, y, op);

    }


    public void drawRect(float x, float y, float w, float h)
    {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x, y, (w + x), (h + y), paint);
        Log.e("G2D 443", "drawRect");
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

    public void  fill(Ellipse2D.Double ellipse)
    {

        paint.setStyle(Paint.Style.FILL);
        //canvas.drawCircle((float) ellipse.x, (float) ellipse.y, (float) (ellipse.w/2d), paint);
        canvas.drawOval((float) ellipse.x, (float) ellipse.y, (float) ellipse.w, (float) ellipse.h, paint);
        //canvas.drawOval(bounds, paint);
        paint = new Paint(paint);
        
    }

    public void fill(Shape shape)
    {
        shape.acceptFill(this);
        
    }


    public void fill(GeneralPath path)
    {
        Paint newPaint = new Paint(paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path.getPath(), paint);

        paint = newPaint;
        
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(x, y, x + width, y + height, startAngle, arcAngle, true, paint);
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

    public void fillOval(int x, int y, int w, int h) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(x, y, w, h, paint);
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

    public void setClip(Shape clip) {
        if (clip == null) {
            this.clip = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            this.clip = clip;
        }
    }

    public Color getColor() {
        return new Color(paint.getColor());
    }



    public int getWidth() {
        if (canvas != null) return canvas.getWidth();
        return btp.getWidth();
    }


    public int getHeight() {
        if (canvas != null) return canvas.getHeight();
        return btp.getHeight();
    }

    public void save()
    {
        canvas.save();
    }

    public void restore()
    {
        canvas.restore();
    }

    public void drawPaint(Paint paint)
    {
        canvas.drawPaint(paint);
    }

    public void renderToCanvas(SVG svg, int width, int height)
    {
        btp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(btp);
        svg.renderToCanvas(canvas);
    }


    public AffineTransform getTransform() {

        return at;
    }
}
