package androidUtils.awt;

import java.util.HashMap;
import java.util.Map;

public class RenderingHints {
    private final Map<Key, Object> hintsMap = new HashMap<>();

    public RenderingHints(Key key, Object value)
    {
        hintsMap.put(key, value);
    }
    public RenderingHints()
    {
    }

    public void add(RenderingHints hint) {
        hintsMap.putAll(hint.hintsMap);
    }

    public void put(Key key, Object value) {
        hintsMap.put(key, value);
    }

    public static class Key {
        private final int key;

        public Key(int key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Key && ((Key) obj).key == this.key;
        }
    }

    public static final Key KEY_ALPHA_INTERPOLATION = new Key(1);
    public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = "AlphaInterpolationQuality";

    public static final Key KEY_INTERPOLATION = new Key(2);
    public static final Object VALUE_INTERPOLATION_BICUBIC = "InterpolationBicubic";

    public static final Key KEY_ANTIALIASING = new Key(3);
    public static final Object VALUE_ANTIALIAS_ON = "AntialiasOn";

    public static final Key KEY_COLOR_RENDERING = new Key(4);
    public static final Object VALUE_COLOR_RENDER_QUALITY = "ColorRenderQuality";

    public static final Key KEY_DITHERING = new Key(5);
    public static final Object VALUE_DITHER_DISABLE = "DitherDisable";

    public static final Key KEY_RENDERING = new Key(6);
    public static final Object VALUE_RENDER_QUALITY = "RenderQuality";

    public static final Key KEY_STROKE_CONTROL = new Key(7);
    public static final Object VALUE_STROKE_PURE = "StrokePure";

    public static final Key KEY_FRACTIONALMETRICS = new Key(8);
    public static final Object VALUE_FRACTIONALMETRICS_ON = "FractionalMetricsOn";

    public static final Key KEY_TEXT_ANTIALIASING = new Key(9);
    public static final Object VALUE_TEXT_ANTIALIAS_ON = "TextAntialiasOn";

}