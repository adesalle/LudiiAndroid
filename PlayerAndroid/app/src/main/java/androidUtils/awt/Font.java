package androidUtils.awt;

import android.graphics.Typeface;

public class Font {

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
}
