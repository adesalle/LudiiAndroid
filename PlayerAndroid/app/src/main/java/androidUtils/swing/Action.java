package androidUtils.swing;

import android.graphics.drawable.Drawable;

public class Action {

    private String name;
    private Drawable icon;
    private boolean enabled;
    private Runnable action;

    public Action(String name, Drawable icon, Runnable action) {
        this.name = name;
        this.icon = icon;
        this.action = action;
        this.enabled = true; // Default to enabled
    }

    /**
     * Gets the name of the action.
     *
     * @return The name of the action.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the action.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the icon of the action.
     *
     * @return The icon of the action.
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Sets the icon of the action.
     *
     * @param icon The icon to set.
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Checks if the action is enabled.
     *
     * @return True if the action is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of the action.
     *
     * @param enabled The enabled state to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Executes the action.
     */
    public void execute() {
        if (enabled && action != null) {
            action.run();
        }
    }

    public void actionPerformed(Object o) {
        execute();
    }
}