package androidUtils.swing;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.event.PopupMenuEvent;
import androidUtils.swing.event.PopupMenuListener;
import playerAndroid.app.StartAndroidApp;

import java.util.ArrayList;
import java.util.List;

public class JPopupMenu implements ViewComponent {
    private final Context context;
    private final PopupWindow popupWindow;
    private final LinearLayout contentView;
    private final List<View> components = new ArrayList<>();
    private PopupMenuListener listener;
    private View invokerView;
    private JMenu invokerMenu;
    private Font currentFont;
    private Dimension preferredSize;

    public JPopupMenu() {
        this(StartAndroidApp.getAppContext(), null);
    }

    public JPopupMenu(View invoker) {
        this(invoker.getContext(), invoker);
    }

    private JPopupMenu(Context context, View invoker) {
        this.context = context;
        this.invokerView = invoker;

        // Création de la PopupWindow
        popupWindow = new PopupWindow(context);
        popupWindow.setFocusable(true);

        // Configuration du contenu
        contentView = new LinearLayout(context);
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.setBackgroundColor(Color.WHITE);

        // Ajout d'un ScrollView si nécessaire
        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(contentView);
        popupWindow.setContentView(scrollView);

        // Si l'invoker est une JMenu, on garde la référence
        if (invoker instanceof JMenu) {
            this.invokerMenu = (JMenu) invoker;
        }
    }

    public void add(View component) {
        components.add(component);
        contentView.addView(component);

    }

    public void addSeparator() {
        View separator = new View(context);
        separator.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2));
        separator.setBackgroundColor(Color.GRAY);
        contentView.addView(separator);
    }

    public int getComponentIndex(View component) {
        return components.indexOf(component);
    }

    public View[] getComponents() {
        return components.toArray(new View[0]);
    }

    public void removeAll() {
        components.clear();
        contentView.removeAllViews();
    }

    public void addPopupMenuListener(PopupMenuListener listener) {
        this.listener = listener;
        popupWindow.setOnDismissListener(() -> {
            if (listener != null) {
                listener.popupMenuWillBecomeInvisible(new PopupMenuEvent(this));
            }
        });
    }

    public void show(View anchor, int x, int y) {
        // Calculer la taille si nécessaire
        if (preferredSize != null) {
            popupWindow.setWidth(preferredSize.width);
            popupWindow.setHeight(preferredSize.height);
        } else {
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Afficher le menu
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);

        if (listener != null) {
            listener.popupMenuWillBecomeVisible(new PopupMenuEvent(this));
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            show(invokerView, 0, 0);
        } else {
            popupWindow.dismiss();
        }
    }

    public void removePopupMenuListener(PopupMenuListener listener) {
        if (this.listener == listener) {
            this.listener = null;
            popupWindow.setOnDismissListener(null);
        }
    }

    public JMenu getInvoker() {
        return invokerMenu;
    }

    public View getInvokerView() {
        return invokerView;
    }

    @Override
    public Dimension getPreferredSize() {
        return getSize();
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = new Dimension(dimension);
    }

    @Override
    public void setSize(Dimension dimension) {
        popupWindow.setWidth(dimension.width);
        popupWindow.setHeight(dimension.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(popupWindow.getWidth(), popupWindow.getHeight());
    }

    @Override
    public void setFont(Font font) {
        this.currentFont = font;
        // Appliquer la police à tous les composants enfants
        for (View component : components) {
            if (component instanceof ViewComponent) {
                ((ViewComponent) component).setFont(font);
            }
        }
    }

    @Override
    public Font getFont() {
        return currentFont;
    }

    public void pack() {
        // Mesurer le contenu
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        // Définir la taille selon le contenu mesuré
        setSize(new Dimension(
                contentView.getMeasuredWidth(),
                contentView.getMeasuredHeight()
        ));
    }

    public void addMenuItem(String text, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        add(menuItem);
    }
}