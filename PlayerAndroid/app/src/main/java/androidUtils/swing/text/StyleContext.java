package androidUtils.swing.text;

import static androidUtils.swing.text.StyleConstants.*;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ParagraphStyle;

import java.util.HashMap;
import java.util.Map;

import androidUtils.awt.Color;

public class StyleContext {
    private static StyleContext defaultInstance;
    private Map<Style, CharacterStyle> styleMap = new HashMap<>();
    private Map<Style, ParagraphStyle> paragraphStyleMap = new HashMap<>();

    public static StyleContext getDefaultStyleContext() {
        if (defaultInstance == null) {
            defaultInstance = new StyleContext();
        }
        return defaultInstance;
    }

    public Style addStyle(String name, Style parent) {
        return new DefaultStyle(name, parent);
    }

    public CharacterStyle getCharacterStyle(AttributeSet attr) {
        if (attr instanceof Style) {
            return styleMap.computeIfAbsent((Style) attr, k -> {
                Style style = (Style) attr;
                CharacterStyle characterStyle = createCharacterStyle(style);
                return characterStyle;
            });
        }
        return null;
    }

    public ParagraphStyle getParagraphStyle(AttributeSet attr) {
        if (attr instanceof Style) {
            return paragraphStyleMap.computeIfAbsent((Style) attr, k -> {
                Style style = (Style) attr;
                ParagraphStyle paragraphStyle = createParagraphStyle(style);
                return paragraphStyle;
            });
        }
        return null;
    }

    private CharacterStyle createCharacterStyle(Style style) {
        CharacterStyle span = null;

        // Combinaison de plusieurs styles si nécessaire
        int textStyle = Typeface.NORMAL;
        if (style.isBold() && style.isItalic()) {
            textStyle = Typeface.BOLD_ITALIC;
        } else if (style.isBold()) {
            textStyle = Typeface.BOLD;
        } else if (style.isItalic()) {
            textStyle = Typeface.ITALIC;
        }

        if (textStyle != Typeface.NORMAL) {
            span = new StyleSpan(textStyle);
        }

        if (style.getForeground() != null) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(style.getForeground());
            if (span == null) {
                span = colorSpan;
            } else {
                span = new CompositeCharacterStyle(span, colorSpan);
            }
        }

        if (style.getSize() != 0) {
            float size = style.getSize() / 12.0f; // Taille relative par rapport à 12pt
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(size);
            if (span == null) {
                span = sizeSpan;
            } else {
                span = new CompositeCharacterStyle(span, sizeSpan);
            }
        }

        return span != null ? span : new CharacterStyle() {
            @Override
            public void updateDrawState(TextPaint tp) {}
        };
    }

    private ParagraphStyle createParagraphStyle(Style style) {
        switch (style.getAlignment()) {
            case StyleConstants.ALIGN_LEFT:
                return new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
            case StyleConstants.ALIGN_CENTER:
                return new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
            case StyleConstants.ALIGN_RIGHT:
                return new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
            case StyleConstants.ALIGN_JUSTIFIED:
                return new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
            default:
                return null;
        }
    }
    public static AttributeSet addAttribute(AttributeSet baseStyle, Object key, Object value) {


        AttributeSet currentStyle = baseStyle != null ? baseStyle : new Style("custom", null);

        if (key.equals(Foreground) && value instanceof Color) {
            setForeground(currentStyle, (Color) value);
        } else if (key.equals(Size) && value instanceof Integer) {
            setSize(currentStyle, (Integer) value);
        } else if (key.equals(FontFamily) && value instanceof String) {
            setFontFamily(currentStyle, (String) value);
        } else if (key.equals(Bold) && value instanceof Boolean) {
            setBold(currentStyle, (Boolean) value);
        } else if (key.equals(Alignment) && value instanceof Integer) {
            setAlignment(currentStyle, (Integer) value);
        }


        return currentStyle;
    }

    public Style getStyle(AttributeSet attr) {
        if (attr instanceof Style) {
            return (Style) attr;
        }

        // Create a new style based on the attribute set
        if (attr != null) {
            Style style = new Style("generated", null);

            // Copy all attributes from the input AttributeSet to the new Style
            for (int i = 0; i < attr.getAttributeCount(); i++) {
                Object name = attr.getAttributeName(i);
                Object value = attr.getAttribute(name);
                style.addAttribute(name, value);
            }

            return style;
        }

        return null;
    }

    // Classe pour combiner plusieurs CharacterStyle
    private static class CompositeCharacterStyle extends CharacterStyle {
        private final CharacterStyle[] styles;

        CompositeCharacterStyle(CharacterStyle... styles) {
            this.styles = styles;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            for (CharacterStyle style : styles) {
                style.updateDrawState(tp);
            }
        }
    }
}