package androidUtils.awt.event;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidUtils.awt.Component;
import androidUtils.awt.Window;
import androidUtils.swing.SwingUtilities;
import playerAndroid.app.StartAndroidApp;

public class HierarchyEvent {
    // Événements de hiérarchie
    public static final int HIERARCHY_CHANGED = 0;
    public static final int ANCESTOR_MOVED = 1;
    public static final int ANCESTOR_RESIZED = 2;
    public static final int DISPLAYABILITY_CHANGED = 3;
    public static final int SHOWING_CHANGED = 4;

    private final View source;
    private final View changed;
    private final int id;
    private final ViewParent parent;
    private final long when;

    /**
     * Constructeur complet
     * @param source Le composant source de l'événement
     * @param changed Le composant qui a changé
     * @param parent Le parent du composant
     * @param id Le type d'événement
     */
    public HierarchyEvent(View source, View changed, ViewParent parent, int id) {
        this.source = source;
        this.changed = changed;
        this.parent = parent;
        this.id = id;
        this.when = System.currentTimeMillis();
    }

    public HierarchyEvent(View source, View changed, int id) {
        this.source = source;
        this.changed = changed;
        this.parent = source.getParent();
        this.id = id;
        this.when = System.currentTimeMillis();
    }

    /**
     * Constructeur simplifié
     * @param source Le composant source de l'événement
     */
    public HierarchyEvent(View source) {
        this(source, source, source.getParent(), HIERARCHY_CHANGED);
    }

    /**
     * @return Le composant source de l'événement
     */
    public Component getSource() {
        return (Component) source;
    }

    /**
     * @return Le composant qui a changé
     */
    public Component getChanged() {
        return (Component) changed;
    }

    public ViewParent getChangedParent() {
        return parent;
    }


    public int getID() {
        return id;
    }

    public ViewGroup getWindow() {
        View root = SwingUtilities.getRoot(source);
        if (root instanceof ViewGroup) {
            return (ViewGroup) root;
        }
        return StartAndroidApp.startAndroidApp().frame();
    }

    /**
     * @return Le moment où l'événement s'est produit (timestamp)
     */
    public long getWhen() {
        return when;
    }

    /**
     * @return Vrai si l'événement correspond à un changement de hiérarchie
     */
    public boolean isHierarchyChanged() {
        return id == HIERARCHY_CHANGED;
    }

    /**
     * @return Vrai si la visibilité a changé
     */
    public boolean isShowingChanged() {
        return id == SHOWING_CHANGED;
    }

    /**
     * @return Vrai si la capacité d'affichage a changé
     */
    public boolean isDisplayabilityChanged() {
        return id == DISPLAYABILITY_CHANGED;
    }

    @Override
    public String toString() {
        return "HierarchyEvent[" +
                "id=" + id +
                ",source=" + source +
                ",changed=" + changed +
                ",parent=" + parent +
                ",when=" + when +
                "]";
    }
}