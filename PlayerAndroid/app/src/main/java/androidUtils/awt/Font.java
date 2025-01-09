package androidUtils.awt;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidUtils.awt.geom.Rectangle2D;

public class Font {

    public static final int TRUETYPE_FONT = -1;
    public static int BOLD = Typeface.BOLD;
    public static int ITALIC = Typeface.ITALIC;
    protected String name;
    public static int PLAIN = Typeface.NORMAL;
    public static Typeface SANS_SERIF = Typeface.SANS_SERIF;
    public static Typeface SERIF = Typeface.SERIF;
    protected int 	style;

    Typeface font;
    int size  = -1;

    public Font(String name, int style, int size)
    {
        this.name = name;
        this.font = Typeface.create(name, style);
        this.size = size;
        this.style = style;
    }
    public Font(Font font)
    {
        this.font = Typeface.create(font.getFont(),font.getFont().getStyle());
        this.style = font.getFont().getStyle();
        this.size = font.getSize();
        name = font.name;
    }

    public Font(Typeface tp, int fontSize)
    {
        this.font = tp;
        this.style = font.getStyle();
        this.size = fontSize;
        name = "";
    }

    public Font(Typeface tp)
    {
        this.font = tp;
        this.style = font.getStyle();
        this.size = 12;
        name = "";
    }

    public static Font createFont(int typeFont, InputStream in) {
        if(typeFont == -1)
        {
            try {
                File tempFile = File.createTempFile("tempFont", ".ttf");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                } finally {
                    in.close();
                }
                Typeface typeface = Typeface.createFromFile(tempFile);

                tempFile.delete();

                return new Font(typeface);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public Typeface getFont()
    {
        return font;
    }

    public int getSize()
    {
        return size;
    }

    public String getFontName()
    {
        return name;
    }

    public Rectangle2D getStringBounds(String stringValue, FontRenderContext metrics)
    {
        Paint paint = new Paint();
        float textW = paint.measureText(stringValue);
        RectF bounds = new RectF(0, metrics.metrics.top, textW, metrics.metrics.bottom);
        return new Rectangle2D.Double(bounds);
    }

    public Font deriveFont(float v) {
        size = (int) v;

        return this;
    }
}
