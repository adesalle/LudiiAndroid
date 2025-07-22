package androidUtils.swing;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import androidUtils.awt.Point;
import androidUtils.awt.event.MouseEvent;

public class SwingUtilities {

    public static Window getWindowAncestor(View view) {
        View rootView = view.getRootView();
        if (rootView != null) {
            ViewParent parent = rootView.getParent();
            if (parent instanceof ViewGroup) {
                Context context = ((ViewGroup) parent).getContext();
                if (context instanceof Activity) {
                    return ((Activity) context).getWindow();
                }
            }
        }

        return null;
    }

    public static boolean isRightMouseButton(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON3;
    }

    public static void convertPointToScreen(Point screenPos, View source) {
        int[] location = new int[2];
        source.getLocationOnScreen(location);
        screenPos.setLocation(location[0] + screenPos.getX(),
                location[1] + screenPos.getY());
    }

    /**
     * Obtient la racine de la hiérarchie de vues
     */
    public static View getRoot(View component) {
        View root = component;
        ViewParent parent = component.getParent();
        while (parent != null && parent instanceof View) {
            root = (View) parent;
            parent = parent.getParent();
        }
        return root;
    }

    public static JDialog getRootJDialog(View component) {
        Object root = getRoot(component);
        if (root instanceof JDialog) {
            return (JDialog) root;
        }
        return null;
    }


    /**
     * Exécute un Runnable sur le thread UI
     */
    public static void invokeLater(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * Trouve le premier ancêtre d'une classe spécifique
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAncestorOfClass(Class<T> c, View component) {
        ViewParent parent = component.getParent();
        while (parent != null) {
            if (c.isInstance(parent)) {
                return (T) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * Version spécialisée pour JPanel
     */
    public static Object getAncestorOfClass(Class<Object> c, JPanel jPanel) {
        return getAncestorOfClass(c, (View) jPanel);
    }

    /**
     * Convertit un Point depuis les coordonnées d'un composant source vers un composant destination
     */
    public static void convertPoint(View source, Point aPoint, View destination) {
        convertPointToScreen(aPoint, source);
        convertPointFromScreen(aPoint, destination);
    }

    /**
     * Convertit un Point depuis les coordonnées écran vers les coordonnées d'un composant
     */
    public static void convertPointFromScreen(Point aPoint, View destination) {
        int[] location = new int[2];
        destination.getLocationOnScreen(location);
        aPoint.setLocation(aPoint.getX() - location[0],
                aPoint.getY() - location[1]);
    }

    /**
     * Vérifie si une vue est contenue dans une autre vue (hiérarchie parent/enfant)
     */
    public static boolean isDescendingFrom(View child, View parent) {
        ViewGroup viewGroup;
        while (child != null) {
            if (child == parent) {
                return true;
            }
            ViewParent p = child.getParent();
            if (!(p instanceof ViewGroup)) {
                return false;
            }
            viewGroup = (ViewGroup) p;
            child = viewGroup;
        }
        return false;
    }
}