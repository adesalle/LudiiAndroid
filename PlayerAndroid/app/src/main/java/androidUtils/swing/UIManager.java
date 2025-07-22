package androidUtils.swing;

import androidUtils.awt.Font;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    private static final Map<Object, Object> defaults = new HashMap<>();
    private static LookAndFeel currentLookAndFeel;
    private static String systemLookAndFeelClassName = "androidUtils.swing.SystemLookAndFeel";
    private static String crossPlatformLookAndFeelClassName = "androidUtils.swing.MetalLookAndFeel";

    static {
        // Initialisation des valeurs par défaut de base
        initDefaultValues();
    }

    public static void setLookAndFeel(String className) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<?> lnfClass = Class.forName(className);
        LookAndFeel newLookAndFeel = (LookAndFeel) lnfClass.newInstance();
        setLookAndFeel(newLookAndFeel);
    }

    public static void setLookAndFeel(LookAndFeel newLookAndFeel) {
        if (currentLookAndFeel != null) {
            currentLookAndFeel.uninitialize();
        }

        currentLookAndFeel = newLookAndFeel;
        currentLookAndFeel.initialize();

        // Mise à jour des valeurs par défaut
        updateUIDefaults();
    }

    public static LookAndFeel getLookAndFeel() {
        return currentLookAndFeel;
    }

    public static String getSystemLookAndFeelClassName() {
        return systemLookAndFeelClassName;
    }

    public static String getCrossPlatformLookAndFeelClassName() {
        return crossPlatformLookAndFeelClassName;
    }

    private static void initDefaultValues() {
        // Valeurs par défaut indépendantes du LookAndFeel
        put("Button.font", new Font("Arial", Font.PLAIN, 14));
        put("Label.font", new Font("Arial", Font.PLAIN, 14));
        // ... autres valeurs par défaut ...
    }

    private static void updateUIDefaults() {
        if (currentLookAndFeel != null) {
            defaults.putAll(currentLookAndFeel.getDefaults());
        }
    }

    public static void put(Object key, Object value) {
        defaults.put(key, value);
    }

    public static Object get(Object key) {
        return defaults.get(key);
    }

    public static Font getFont(Object key) {
        Object value = get(key);
        return (value instanceof Font) ? (Font) value : null;
    }

    public static String getString(Object key) {
        Object value = get(key);
        return (value instanceof String) ? (String) value : null;
    }
}