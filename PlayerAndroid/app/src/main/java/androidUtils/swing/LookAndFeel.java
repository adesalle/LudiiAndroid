package androidUtils.swing;

import java.util.Map;

public abstract class LookAndFeel {
    public abstract String getName();
    public abstract String getID();
    public abstract String getDescription();

    public abstract Map<Object, Object> getDefaults();

    public void initialize() {
        // Initialisation optionnelle
    }

    public void uninitialize() {
        // Nettoyage optionnel
    }
}
