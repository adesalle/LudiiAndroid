package androidUtils.swing;

import java.util.HashMap;
import java.util.Map;

import androidUtils.awt.Color;
import androidUtils.awt.Font;

public class SystemLookAndFeel extends LookAndFeel {
    @Override
    public String getName() {
        return "System";
    }

    @Override
    public String getID() {
        return "System";
    }

    @Override
    public String getDescription() {
        return "The system platform look and feel";
    }

    @Override
    public Map<Object, Object> getDefaults() {
        Map<Object, Object> defaults = new HashMap<>();

        // Valeurs spécifiques au look système
        defaults.put("Button.background", new Color(0xFFDDDDDD));
        defaults.put("Button.foreground", new Color(0xFF000000));
        defaults.put("Button.font", new Font("Roboto", Font.PLAIN, 14));

        defaults.put("Label.background", new Color(0x00000000)); // Transparent
        defaults.put("Label.foreground", new Color(0xFF000000));
        defaults.put("Label.font", new Font("Roboto", Font.PLAIN, 14));

        // ... autres valeurs par défaut ...

        return defaults;
    }
}