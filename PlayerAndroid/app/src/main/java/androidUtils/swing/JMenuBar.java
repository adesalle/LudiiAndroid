package androidUtils.swing;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
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

    private boolean proportionalWeights = false;

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
            throw new IllegalStateException("Menu not initialized.");
        }

        MenuItem menuItem = menu.add(title);

        // Conteneur du menu
        LinearLayout menuContainer = new LinearLayout(context);
        menuContainer.setOrientation(HORIZONTAL);

        // Paramètres de layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                proportionalWeights ? 0 : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                proportionalWeights ? 1.0f : 0
        );

        // Espacement seulement si pas de poids proportionnels
        if (!proportionalWeights) {
            params.setMargins(getChildCount() > 0 ? borderWidth : 0, 0, 0, 0);
        }
        menuContainer.setLayoutParams(params);

        // Création du menu
        JMenu jMenu = new JMenu(title);
        jMenu.setGravity(Gravity.CENTER);

        // Padding minimal pour la lisibilité
        int horizontalPadding = proportionalWeights ?
                Math.max(menuPadding, borderWidth) : menuPadding;

        jMenu.setPadding(horizontalPadding, menuPadding, horizontalPadding, menuPadding);

        if (!proportionalWeights && getChildCount() > 0) {
            jMenu.setBackground(new LeftLineDrawable(borderColor, borderWidth, menuPadding));
        }

        menuContainer.addView(jMenu);
        addView(menuContainer);

        jMenu.setOnClickListener(v -> {
            menu.performIdentifierAction(menuItem.getItemId(), 0);
        });

        return jMenu;
    }

    public void add(JMenu menu) {
        // Conteneur pour le menu
        LinearLayout menuContainer = new LinearLayout(context);
        menuContainer.setOrientation(HORIZONTAL);

        // Paramètres de layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                proportionalWeights ? 0 : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                proportionalWeights ? 1.0f : 0
        );

        // Espacement seulement en mode fixe
        if (!proportionalWeights) {
            params.setMargins(getChildCount() > 0 ? borderWidth : 0, 0, 0, 0);
        }
        menuContainer.setLayoutParams(params);

        // Configuration du menu
        menu.setGravity(Gravity.CENTER);

        // Padding adaptatif
        int leftPadding = menuPadding;
        if (proportionalWeights && getChildCount() > 0) {
            leftPadding += borderWidth; // Ajoute l'espace pour la bordure
        }
        menu.setPadding(leftPadding, menuPadding, menuPadding, menuPadding);

        // Gestion des bordures (toujours visibles)
        if (getChildCount() > 0) { // Pas de bordure pour le premier menu
            if (proportionalWeights) {
                // En mode proportionnel, on utilise un Drawable personnalisé
                menu.setBackground(new LeftLineDrawable(borderColor, borderWidth, menuPadding));
            } else {
                // En mode fixe, bordure standard
                menu.setBackground(new LeftLineDrawable(borderColor, borderWidth));
            }
        }

        menuContainer.addView(menu);
        addView(menuContainer);

    }

    public void setProportionalWeights(boolean proportional) {
        this.proportionalWeights = proportional;
        updateMenuLayout();
    }

    private void updateMenuLayout() {
        if (proportionalWeights) {
            // Mode proportionnel
            float totalWidth = calculateTotalTextWidth();
            applyProportionalWeights(totalWidth);
        } else {
            // Mode fixe
            applyFixedWidthLayout();
        }
        updateMenuBorders();
    }

    private float calculateTotalTextWidth() {
        float totalWidth = 0;
        Paint paint = new Paint();

        for (int i = 0; i < getChildCount(); i++) {
            JMenu jMenu = getJMenuAt(i);
            if (jMenu != null) {
                paint.setTextSize(jMenu.getTextSize());
                float textWidth = paint.measureText(jMenu.getText().toString());
                // Calcul largeur totale = texte + padding + bordure
                totalWidth += textWidth + (2 * menuPadding) + (i > 0 ? borderWidth : 0);
            }
        }

        return totalWidth;
    }

    private void applyProportionalWeights(float totalWidth) {
        Paint paint = new Paint();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
            JMenu jMenu = getJMenuAt(i);

            if (jMenu != null) {
                paint.setTextSize(jMenu.getTextSize());
                float textWidth = paint.measureText(jMenu.getText().toString());
                // Poids = (texte + padding + bordure) / total
                float itemWidth = textWidth + (2 * menuPadding) + (i > 0 ? borderWidth : 0);
                params.weight = itemWidth / totalWidth;
            }

            params.width = 0;
            params.setMargins(0, 0, 0, 0);
            child.setLayoutParams(params);
        }
    }

    private void applyFixedWidthLayout() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();

            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.weight = 0;
            params.setMargins(i > 0 ? borderWidth : 0, 0, 0, 0);
            child.setLayoutParams(params);
        }
    }

    private void updateMenuBorders() {
        for (int i = 0; i < getChildCount(); i++) {
            JMenu jMenu = getJMenuAt(i);
            if (jMenu != null) {
                // Applique une bordure à tous les menus sauf le premier
                if (i > 0) {
                    int leftPadding = menuPadding + (proportionalWeights ? borderWidth : 0);
                    jMenu.setBackground(new LeftLineDrawable(borderColor, borderWidth, leftPadding));
                } else {
                    jMenu.setBackground(null);
                }

                // Ajuste le padding
                int left = i > 0 ? menuPadding + borderWidth : menuPadding;
                jMenu.setPadding(left, menuPadding, menuPadding, menuPadding);
            }
        }
    }

    private JMenu getJMenuAt(int position) {
        View child = getChildAt(position);
        if (child instanceof LinearLayout) {
            View menuView = ((LinearLayout) child).getChildAt(0);
            if (menuView instanceof JMenu) {
                return (JMenu) menuView;
            }
        }
        return null;
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