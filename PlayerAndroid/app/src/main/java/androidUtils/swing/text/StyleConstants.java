package androidUtils.swing.text;

import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;

import androidUtils.awt.Color;

public class StyleConstants {
    // Font styles
    public static final int BOLD = Typeface.BOLD;
    public static final int ITALIC = Typeface.ITALIC;
    public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;
    public static final int NORMAL = Typeface.NORMAL;

    // Attribute keys
    public static final Object Bold = "bold";
    public static final Object Italic = "italic";
    public static final Object Underline = "underline";
    public static final Object Foreground = "foreground";
    public static final Object Size = "size";
    public static final Object Alignment = "alignment";
    public static final Object FontFamily = "fontFamily";

    // Alignment constants
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_JUSTIFIED = 3;

    public static void setForeground(AttributeSet style, Color color) {
        if (style != null) {
            style.addAttribute(Foreground, color.toArgb());
            style.setStyle(new ForegroundColorSpan(color.toArgb()));
        }
    }

    public static void setBold(AttributeSet style, boolean bold) {
        if (style != null) {
            style.addAttribute(Bold, bold);
        }
    }

    public static void setItalic(AttributeSet style, boolean italic) {
        if (style != null) {
            style.addAttribute(Italic, italic);
        }
    }

    public static void setUnderline(AttributeSet style, boolean underline) {
        if (style != null) {
            style.addAttribute(Underline, underline);
        }
    }

    public static void setSize(AttributeSet style, int size) {
        if (style != null) {
            style.addAttribute(Size, size);
        }
    }

    public static void setAlignment(AttributeSet style, int alignment) {
        if (style != null) {
            style.addAttribute(Alignment, alignment);
        }
    }

    public static void setFontFamily(AttributeSet style, String value) {
        if (style != null) {
            style.addAttribute(FontFamily, value);
        }
    }
}