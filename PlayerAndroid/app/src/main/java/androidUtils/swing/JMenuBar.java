package androidUtils.swing;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidUtils.drawable.BorderDrawable;
import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.drawable.BottomLineDrawable;
import androidUtils.drawable.LeftLineDrawable;
import playerAndroid.app.StartAndroidApp;

public class JMenuBar extends LinearLayout implements ViewComponent{

    private final Context context;
    private Menu menu;
    private MenuInflater menuInflater;

    private int menuSpacing = 8;
    private int menuPadding = 12;
    private Color borderColor = new Color(200, 200, 200);
    private int borderWidth = 1;

    public JMenuBar() {
        super(StartAndroidApp.getAppContext());
        this.context = StartAndroidApp.getAppContext();
        this.setOrientation(HORIZONTAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        this.menuInflater = new MenuInflater(StartAndroidApp.getAppContext());
        setBackground(new BottomLineDrawable(Color.BLACK, 2));
    }

    public JMenuBar(Menu menu) {
        this();
        this.menu = menu;

    }

    public JMenu add(String title) {
        if (menu == null) {
            throw new IllegalStateException("Menu not initialized. Use constructor with Menu parameter.");
        }

        MenuItem menuItem = menu.add(title);

        // Conteneur principal pour le menu
        LinearLayout menuContainer = new LinearLayout(context);
        menuContainer.setOrientation(HORIZONTAL);

        // Paramètres de layout avec marge pour l'espacement
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        if (getChildCount() > 0) {
            params.setMargins(menuSpacing, 0, 0, 0); // Espacement à gauche sauf pour le premier menu
        }
        menuContainer.setLayoutParams(params);

        // Création du menu avec padding et bordure
        JMenu jMenu = new JMenu(title);
        jMenu.setPadding(menuPadding, menuPadding, menuPadding, menuPadding);

        // Ajout de la bordure
        jMenu.setBackground(new LeftLineDrawable(borderColor, borderWidth));


        menuContainer.addView(jMenu);
        addView(menuContainer);

        // Set click listener for the menu
        jMenu.setOnClickListener(v -> {
            menu.performIdentifierAction(menuItem.getItemId(), 0);
        });

        invalidate();

        return jMenu;
    }

    // Méthodes pour configurer l'apparence
    public void setMenuSpacing(int spacing) {
        this.menuSpacing = spacing;
        updateMenuSpacing();
    }

    public void setMenuPadding(int padding) {
        this.menuPadding = padding;
        updateMenuPadding();
    }

    public void setBorder(Color color, int width) {
        this.borderColor = color;
        this.borderWidth = width;
        updateBorders();
    }

    private void updateMenuSpacing() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
            params.setMargins(i == 0 ? 0 : menuSpacing, 0, 0, 0);
            child.setLayoutParams(params);
        }
    }

    private void updateMenuPadding() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof LinearLayout) {
                View menuView = ((LinearLayout) child).getChildAt(0);
                if (menuView != null) {
                    menuView.setPadding(menuPadding, menuPadding, menuPadding, menuPadding);
                }
            }
        }
    }

    private void updateBorders() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof LinearLayout) {
                View menuView = ((LinearLayout) child).getChildAt(0);
                if (menuView != null) {
                    menuView.setBackground(new BorderDrawable(borderColor, borderWidth));
                }
            }
        }
    }


    /**
     * Inflates a menu from XML resource
     * @param menuRes The menu resource ID
     */
    public void inflate(int menuRes) {
        if (menuInflater == null || menu == null) {
            throw new IllegalStateException("MenuInflater or Menu not initialized");
        }
        menuInflater.inflate(menuRes, menu);
    }

    /**
     * Finds a menu item by its ID
     * @param id The menu item ID
     * @return The MenuItem or null if not found
     */
    public MenuItem findItem(int id) {
        return menu != null ? menu.findItem(id) : null;
    }

    /**
     * Adds a click listener to a menu item
     * @param itemId The menu item ID
     * @param listener The click listener
     */
    public void setOnMenuItemClickListener(int itemId, MenuItem.OnMenuItemClickListener listener) {
        MenuItem item = findItem(itemId);
        if (item != null) {
            item.setOnMenuItemClickListener(listener);
        }
    }

    // Swing-compatible method
    public void add(JMenu menu) {
        this.addView(menu);
        menu.setPadding(menuPadding, menuPadding, menuPadding, menuPadding);

        // Ajout de la bordure
        menu.setBackground(new BorderDrawable(borderColor, borderWidth));


    }

    // Swing-compatible method
    public void setJMenuBar(JMenuBar menuBar) {
        // For compatibility with Swing code
    }

    public void removeAllMenus() {
        removeAllViews();
        if (menu != null) {
            menu.clear();
        }
    }

    public int getMenuCount() {
        return getChildCount();
    }


    public void setBackgroundColor(Color color) {
        if (color != null) {
            setBackgroundColor(color.toArgb());
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Calcule la taille préférée basée sur le contenu
        int width = 0;
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewComponent) {
                Dimension childSize = ((ViewComponent) child).getPreferredSize();
                if (childSize != null) {
                    width += childSize.width;
                    height = Math.max(height, childSize.height);
                }
            }
        }

        // Ajoute les paddings
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        return new Dimension(width, height);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        if (dimension != null) {
            // Applique comme taille minimum
            setMinimumWidth(dimension.width);
            setMinimumHeight(dimension.height);
            requestLayout();
        }
    }

    @Override
    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int width, int height) {
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }


    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            // Applique la police à tous les enfants
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof ViewComponent) {
                    ((ViewComponent) child).setFont(font);
                }
            }
        }
    }

    @Override
    public Font getFont() {
        // Retourne la police du premier enfant comme police de référence
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewComponent) {
                Font font = ((ViewComponent) child).getFont();
                if (font != null) {
                    return font;
                }
            }
        }
        return null;
    }

    public void setPosition(int i, int i1) {
        setLeft(i);
        setTop(i1);
    }
}