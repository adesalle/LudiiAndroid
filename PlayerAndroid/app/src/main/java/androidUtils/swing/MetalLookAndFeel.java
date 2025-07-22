
package androidUtils.swing;

import java.util.HashMap;
import java.util.Map;

import androidUtils.awt.Color;
import androidUtils.awt.Font;

public class MetalLookAndFeel extends LookAndFeel {
    @Override
    public String getName() {
        return "Metal";
    }

    @Override
    public String getID() {
        return "Metal";
    }

    @Override
    public String getDescription() {
        return "The Java Metal cross-platform look and feel";
    }

    @Override
    public Map<Object, Object> getDefaults() {
        Map<Object, Object> defaults = new HashMap<>();

        // Valeurs spécifiques au look Metal
        defaults.put("Button.background", new Color(0xFFEEEEEE));
        defaults.put("Button.foreground", new Color(0xFF000000));
        defaults.put("Button.font", new Font("Dialog", Font.PLAIN, 12));

        defaults.put("Label.background", new Color(0x00000000)); // Transparent
        defaults.put("Label.foreground", new Color(0xFF000000));
        defaults.put("Label.font", new Font("Dialog", Font.PLAIN, 12));

        // ... autres valeurs par défaut ...

        return defaults;
    }
}