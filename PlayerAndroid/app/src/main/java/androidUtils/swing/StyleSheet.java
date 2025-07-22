package androidUtils.swing;

import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.graphics.Typeface;
import android.text.style.UnderlineSpan;

import androidUtils.awt.Font;

import java.util.HashMap;
import java.util.Map;

public class StyleSheet {
    private final Map<String, Style> styles = new HashMap<>();
    private Font defaultFont;

    public StyleSheet() {
        this.defaultFont = new Font("sans-serif", Font.PLAIN, 12);
    }

    public void setDefaultFont(Font font) {
        this.defaultFont = font;
        // Update basic selectors with default font
        addRule("body", "font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt;");
        addRule("p", "font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt;");
        addRule("*", "font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt;");
    }

    public void addRule(String cssRule) {
        // Parse full CSS rule with selector and declaration
        int braceIndex = cssRule.indexOf('{');
        if (braceIndex > 0 && cssRule.endsWith("}")) {
            String selector = cssRule.substring(0, braceIndex).trim();
            String declaration = cssRule.substring(braceIndex + 1, cssRule.length() - 1).trim();
            addRule(selector, declaration);
        }
    }

    public void addRule(String selector, String declaration) {
        Style style = new Style(selector, null);

        // Parse declaration
        String[] properties = declaration.split(";");
        for (String property : properties) {
            String[] keyValue = property.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // Handle common CSS properties
                switch (key) {
                    case "font-family":
                        style.addAttribute(key, value.replaceAll("['\"]", ""));
                        break;
                    case "font-size":
                        try {
                            int size = Integer.parseInt(value.replaceAll("[^0-9]", ""));
                            style.addAttribute(key, size);
                        } catch (NumberFormatException e) {
                            style.addAttribute(key, defaultFont.getSize());
                        }
                        break;
                    case "color":
                        style.addAttribute(key, parseColor(value));
                        break;
                    case "font-weight":
                        style.addAttribute(key, value.equalsIgnoreCase("bold"));
                        break;
                    case "font-style":
                        style.addAttribute(key, value.equalsIgnoreCase("italic"));
                        break;
                    case "text-decoration":
                        style.addAttribute(key, value.equalsIgnoreCase("underline"));
                        break;
                    default:
                        style.addAttribute(key, value);
                }
            }
        }

        styles.put(selector, style);
    }

    private Integer parseColor(String colorValue) {
        // Simple color parser (would need enhancement for full CSS color support)
        try {
            if (colorValue.startsWith("#")) {
                return (int) Long.parseLong(colorValue.substring(1), 16);
            } else if (colorValue.startsWith("rgb(")) {
                String[] rgb = colorValue.substring(4, colorValue.length() - 1).split(",");
                int r = Integer.parseInt(rgb[0].trim());
                int g = Integer.parseInt(rgb[1].trim());
                int b = Integer.parseInt(rgb[2].trim());
                return 0xff000000 | (r << 16) | (g << 8) | b;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Style getStyle(String selector) {
        return styles.get(selector);
    }

    public void applyStyle(SpannableStringBuilder builder, int start, int end, String selector) {
        Style style = styles.get(selector);
        if (style != null) {
            // Apply font family
            String fontFamily = (String) style.getAttribute("font-family");
            if (fontFamily != null) {
                builder.setSpan(new TypefaceSpan(fontFamily),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Apply font size (would need conversion from pt to pixels)
            Integer fontSize = (Integer) style.getAttribute("font-size");
            if (fontSize != null) {
                // Would need proper conversion to pixels
                // builder.setSpan(new AbsoluteSizeSpan(fontSize, true), ...);
            }

            // Apply color
            Integer color = (Integer) style.getAttribute("color");
            if (color != null) {
                builder.setSpan(new ForegroundColorSpan(color),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Apply font styles
            Boolean bold = (Boolean) style.getAttribute("font-weight");
            Boolean italic = (Boolean) style.getAttribute("font-style");
            if (bold != null && italic != null && bold && italic) {
                builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (bold != null && bold) {
                builder.setSpan(new StyleSpan(Typeface.BOLD),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (italic != null && italic) {
                builder.setSpan(new StyleSpan(Typeface.ITALIC),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Apply underline
            Boolean underline = (Boolean) style.getAttribute("text-decoration");
            if (underline != null && underline) {
                builder.setSpan(new UnderlineSpan(),
                        start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static class Style {
        private final String name;
        private final Style parent;
        private final Map<String, Object> attributes = new HashMap<>();

        public Style(String name, Style parent) {
            this.name = name;
            this.parent = parent;
        }

        public void addAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        public Object getAttribute(String name) {
            if (attributes.containsKey(name)) {
                return attributes.get(name);
            }
            return parent != null ? parent.getAttribute(name) : null;
        }

        public Integer getForeground() {
            Object color = getAttribute("color");
            if (color instanceof Integer) {
                return (Integer) color;
            }
            return null;
        }
    }
}