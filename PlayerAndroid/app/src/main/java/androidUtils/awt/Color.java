package androidUtils.awt;

public class Color {

    public static final Color GRAY = Color.valueOf(android.graphics.Color.GRAY);
    public static Color WHITE = Color.valueOf(android.graphics.Color.WHITE);
    public static Color GREEN = Color.valueOf(android.graphics.Color.GREEN);
    public static Color BLACK = Color.valueOf(android.graphics.Color.BLACK);
    public static Color CYAN = Color.valueOf(android.graphics.Color.CYAN);
    public static Color RED = Color.valueOf(android.graphics.Color.RED);
    public static Color white = Color.valueOf(android.graphics.Color.WHITE);
    public static Color black = Color.valueOf(android.graphics.Color.BLACK);

    android.graphics.Color color;

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

    public static int HSBtoRGB(float H, float S, float B)
    {
        float C = B * S;
        float X = C * (1 - Math.abs(((H / 60) % 2) - 1));
        float m = B - C;

        float r, g, b;
        if (H >= 0 && H < 60) {
            r = C; g = X; b = 0;
        } else if (H >= 60 && H < 120) {
            r = X; g = C; b = 0;
        } else if (H >= 120 && H < 180) {
            r = 0; g = C; b = X;
        } else if (H >= 180 && H < 240) {
            r = 0; g = X; b = C;
        } else if (H >= 240 && H < 300) {
            r = X; g = 0; b = C;
        } else {
            r = C; g = 0; b = X;
        }

        r += m;
        g += m;
        b += m;

        int red = Math.round(r * 255);
        int green = Math.round(g * 255);
        int blue = Math.round(b * 255);

        return new Color(red, green, blue).getRGB();
    }

    public static void RGBtoHSB(int R, int G, int B, float[] hsv) {

        android.graphics.Color.RGBToHSV(R, G, B, hsv);

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

    public float alpha() {
        return color.alpha();
    }

    public int getAlpha() {
        return (int) color.alpha();
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

    public static int parseColor(String color)
    {
        return android.graphics.Color.parseColor(color);
    }
}
