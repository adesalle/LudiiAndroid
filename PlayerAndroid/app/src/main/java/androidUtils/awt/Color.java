package androidUtils.awt;

public class Color {
    public static final Color GRAY = Color.valueOf(android.graphics.Color.GRAY);
    android.graphics.Color color;

    public static Color WHITE = Color.valueOf(android.graphics.Color.WHITE);
    public static Color BLACK = Color.valueOf(android.graphics.Color.BLACK);
    public static Color RED = Color.valueOf(android.graphics.Color.RED);
    public static Color white = Color.valueOf(android.graphics.Color.WHITE);
    public static Color black = Color.valueOf(android.graphics.Color.BLACK);

    public Color(int r, int g, int b) {
        color = android.graphics.Color.valueOf(android.graphics.Color.rgb(r, g, b));
    }

    public Color(int c) {
        color = android.graphics.Color.valueOf(c);
    }
    public Color(int a, int r, int g, int b) {
        color = color(a, r, g, b);
    }
    public Color(float a, float r, float g, float b) {
        color = color(a, r, g, b);
    }

    public Color(android.graphics.Color color) {
        this.color = color;
    }


    private static android.graphics.Color color(int a, int r, int g, int b)
    {
        return android.graphics.Color.valueOf(android.graphics.Color.argb(a, r, g, b));
    }
    private static android.graphics.Color color(float a, float r, float g, float b)
    {
        return android.graphics.Color.valueOf(android.graphics.Color.argb(a, r, g, b));
    }

    private static android.graphics.Color color(int r, int g, int b)
    {
        return android.graphics.Color.valueOf(android.graphics.Color.rgb(r, g, b));
    }

    public float red() {
        return color.red();
    }

    public float green() {
        return color.green();
    }

    public float blue() {
        return color.blue();
    }

    public int getRed() {
        return (int) color.red();
    }

    public int getGreen() {
        return (int) color.green();
    }

    public int getBlue() {
        return (int) color.blue();
    }

    public int toArgb()
    {
        return color.toArgb();
    }

    public static int argb(int a, int r, int g, int b)
    {
        return color(a, r, g, b).toArgb();
    }
    public static int argb(float a, float r, float g, float b)
    {
        return color(a, r, g, b).toArgb();
    }


    public static Color rgb(int a, int r, int g, int b)
    {
        return new Color(a,r,g,b);
    }

    public static Color valueOf(int argb)
    {
        return new Color(android.graphics.Color.valueOf(argb));
    }

    public static Color decode(String value)
    {
        return new Color(android.graphics.Color.parseColor(value));
    }

    public int getRGB()
    {
        return color.toArgb();
    }
}
