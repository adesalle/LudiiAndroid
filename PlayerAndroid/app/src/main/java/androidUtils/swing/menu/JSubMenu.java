package androidUtils.swing.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.SubMenu;
import android.view.MenuItem;
import android.view.View;

public class JSubMenu extends JMenu implements SubMenu {
    private final JMenuItem parentItem;

    public JSubMenu(Context context, JMenuItem parentItem) {
        super(context);
        this.parentItem = parentItem;
    }

    @Override
    public SubMenu setHeaderTitle(CharSequence title) {
        parentItem.setTitle(title);
        return this;
    }

    @Override
    public SubMenu setHeaderTitle(int titleRes) {
        parentItem.setTitle(titleRes);
        return this;
    }

    @Override
    public SubMenu setHeaderIcon(Drawable icon) {
        parentItem.setIcon(icon);
        return this;
    }

    @Override
    public SubMenu setHeaderIcon(int iconRes) {
        parentItem.setIcon(iconRes);
        return this;
    }

    @Override
    public SubMenu setHeaderView(View view) {
        // Implémentation optionnelle pour les vues custom
        return this;
    }

    @Override
    public void clearHeader() {
        parentItem.setTitle("");
        parentItem.setIcon(null);
    }

    @Override
    public SubMenu setIcon(int iconRes) {
        return this;
    }

    @Override
    public SubMenu setIcon(Drawable icon) {
        return this;
    }

    @Override
    public MenuItem getItem() {
        return parentItem;
    }
}