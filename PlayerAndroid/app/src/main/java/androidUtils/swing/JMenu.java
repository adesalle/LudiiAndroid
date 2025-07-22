package androidUtils.swing;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;

import androidUtils.swing.border.Border;
import playerAndroid.app.StartAndroidApp;

public class JMenu extends AppCompatButton implements ViewComponent {

    private final String title;
    private Menu baseMenu;
    private MenuItem menuItem;
    private SubMenu subMenu;
    private PopupMenu popupMenu;
    private int currentOrder = 0;


    protected Graphics graphics;

    private Border border;

    public JMenu(String title) {
        super(StartAndroidApp.getAppContext());
        graphics = new Graphics();
        this.title = title;
        initView();
        initPopupMenu();
    }

    public JMenu(String title, Menu baseMenu) {
        this(title);
        this.baseMenu = baseMenu;
        this.subMenu = baseMenu.addSubMenu(title);
        this.menuItem = subMenu.getItem();
    }

    private void initView() {
        setText(title);
        setAllCaps(false);
        setOnClickListener(v -> showSubMenu());
    }

    private void initPopupMenu() {
        if (popupMenu != null) {
            popupMenu.dismiss();
            popupMenu = null;
        }

        Context wrapper = new ContextThemeWrapper(getContext(),
                androidx.appcompat.R.style.Theme_AppCompat_Light);
        popupMenu = new PopupMenu(wrapper, this);

        this.baseMenu = popupMenu.getMenu();
        if (this.subMenu == null) {
            this.subMenu = baseMenu.addSubMenu(title);
            this.menuItem = subMenu.getItem();
        }
    }

    public void add(View view) {
        StartAndroidApp.startAndroidApp().runOnUiThread(() -> {
            android.widget.PopupWindow popupWindow = new android.widget.PopupWindow(
                    view
            );
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.showAsDropDown(this);
        });
    }

    public MenuItem add(JMenu menu) {
        if (subMenu == null) {
            throw new IllegalStateException("SubMenu not initialized");
        }

        MenuItem subMenuItem = subMenu.add(menu.getText());
        SubMenu childSubMenu = subMenu.addSubMenu(menu.getText());
        menu.setBaseMenu(childSubMenu);

        for (int i = 0; i < menu.subMenu.size(); i++) {
            MenuItem item = menu.subMenu.getItem(i);
            MenuItem newItem = childSubMenu.add(item.getTitle());
            JMenuItem jMenuItem = new JMenuItem(item.getTitle().toString());
            newItem.setOnMenuItemClickListener(jMenuItem.getOnMenuItemClickListener());
        }

        return subMenuItem;
    }

    protected void setBaseMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
        this.menuItem = subMenu.getItem();
    }

    public MenuItem add(String title) {
        if (subMenu == null) {
            initPopupMenu();
        }
        return subMenu.add(Menu.NONE, Menu.NONE, currentOrder++, title);
    }

    public MenuItem add(String title, MenuItem.OnMenuItemClickListener listener) {
        MenuItem item = add(title);
        item.setOnMenuItemClickListener(listener);
        return item;
    }

    public void addActionListener(ActionListener listener) {
        setOnClickListener(v -> {
            ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, title);
            listener.actionPerformed(e);
        });
    }

    public void addSeparator() {
        if (subMenu != null) {
            subMenu.add(Menu.NONE, Menu.NONE, currentOrder++, "----------")
                    .setEnabled(false);
        }
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void showSubMenu() {
        // Toujours créer un nouveau PopupMenu à chaque affichage
        Context wrapper = new ContextThemeWrapper(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light);
        popupMenu = new PopupMenu(wrapper, this);

        // Copier les items du subMenu vers le popupMenu
        if (subMenu != null) {
            for (int i = 0; i < subMenu.size(); i++) {
                MenuItem item = subMenu.getItem(i);
                MenuItem newItem = popupMenu.getMenu().add(item.getTitle());

                // Copier toutes les propriétés importantes
                newItem.setIcon(item.getIcon());
                newItem.setEnabled(item.isEnabled());
                newItem.setCheckable(item.isCheckable());
                newItem.setChecked(item.isChecked());

                // Gestion des écouteurs
                JMenuItem tmpItem = new JMenuItem(item.getTitle().toString());
                newItem.setOnMenuItemClickListener(tmpItem.getOnMenuItemClickListener());
            }
        }

        popupMenu.show();
    }
    public void dismissSubMenu() {
        if (popupMenu != null) {
            popupMenu.dismiss();
            popupMenu = null;
        }
    }

    public String getText() {
        return title;
    }


    public void removeAll() {
        if (subMenu != null) {
            subMenu.clear();
        }
    }
    /**
     * Adds a JMenuItem (Swing compatibility)
     */
    public void add(JMenuItem item) {
        add(item.getText()).setOnMenuItemClickListener(item.getMenuItemClickListener());
    }

    @Override
    protected void onDetachedFromWindow() {
        dismissSubMenu();
        super.onDetachedFromWindow();
    }

    public JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            // Créer le PopupMenu Android si pas encore existant
            popupMenu = new PopupMenu(getContext(), this);

            // Copier les items du SubMenu vers le PopupMenu
            if (subMenu != null) {
                for (int i = 0; i < subMenu.size(); i++) {
                    MenuItem item = subMenu.getItem(i);
                    MenuItem popupItem = popupMenu.getMenu().add(item.getTitle());
                    JMenuItem itemTmp = new JMenuItem();
                    popupItem.setOnMenuItemClickListener(itemTmp.getOnMenuItemClickListener());
                }
            }
        }

        return new JPopupMenu() {
            @Override
            public void show(View anchor, int x, int y) {
                popupMenu.show();
            }


            public void dismiss() {
                popupMenu.dismiss();
            }


            public Menu getMenu() {
                return popupMenu.getMenu();
            }
        };
    }

    @Override
    public Dimension getPreferredSize() {
        // Mesure le texte pour déterminer la taille préférée
        int width = (int) getPaint().measureText(getText()) + getPaddingLeft() + getPaddingRight();
        int height = getLineHeight() + getPaddingTop() + getPaddingBottom();
        return new Dimension(width, height);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        if (dimension != null) {
            // Convertit la dimension en pixels et applique comme taille minimum
            int widthPx = dimension.width;
            int heightPx = dimension.height;
            setMinimumWidth(widthPx);
            setMinimumHeight(heightPx);
        }
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            setTypeface(font.getFont());
            setTextSize(font.getSize());
            // Mise à jour du padding pour correspondre à la nouvelle police
            int padding = (int)(font.getSize() * 0.4f);
            setPadding(padding, padding, padding, padding);
        }
    }


    @Override
    public Font getFont() {
        return new Font(getTypeface(), (int)getTextSize());
    }

    public void setToolTipText(String description) {
    }

    public void setBorder(Border lineBorder) {
        border = lineBorder;
        invalidate();
    }
}