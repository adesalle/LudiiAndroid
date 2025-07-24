package androidUtils.swing.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import playerAndroid.app.StartAndroidApp;

public class JMenu implements Menu {
    private final Context context;
    private final List<JMenuItem> items = new ArrayList<>();
    private boolean qwertyMode = true;
    private JPopupMenu popupMenu;
    private String title;
    private Object parent = null;

    public JMenu(String title) {
        this(StartAndroidApp.getAppContext());
        this.title = title;
    }

    public JMenu() {
        this(StartAndroidApp.getAppContext());
    }

    public JMenu(Context context) {
        this.context = context;
        this.popupMenu = new JPopupMenu(context, this);
    }


    public JMenu add(JMenu subMenu) {
        JMenuItem item = new JMenuItem(subMenu.getTitle());
        if(!(subMenu instanceof JSubMenu)) subMenu = new JSubMenu(subMenu, item);
        item.setSubMenu((JSubMenu) subMenu);
        items.add(item);
        return subMenu;
    }

    public JMenuItem add(JMenuItem menuItem) {
        items.add(menuItem);
        menuItem.setParent(this);
        return menuItem;
    }

    @Override
    public MenuItem add(CharSequence title) {
        return add(0, 0, 0, title);
    }

    @Override
    public MenuItem add(int titleRes) {
        return add(0, 0, 0, context.getString(titleRes));
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        JMenuItem item = new JMenuItem(context);
        item.setGroupId(groupId);
        item.setItemId(itemId);
        item.setOrder(order);
        item.setTitle(title);
        item.setParent(this);
        return add(item);
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return add(groupId, itemId, order, context.getString(titleRes));
    }

    @Override
    public SubMenu addSubMenu(CharSequence title) {
        return addSubMenu(0, 0, 0, title);
    }

    @Override
    public SubMenu addSubMenu(int titleRes) {
        return addSubMenu(0, 0, 0, context.getString(titleRes));
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        JMenuItem item = (JMenuItem) add(groupId, itemId, order, title);
        JSubMenu subMenu = new JSubMenu(context, item);
        item.setSubMenu(subMenu);
        subMenu.setParent(item);
        item.setParent(this);
        return subMenu;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return addSubMenu(groupId, itemId, order, context.getString(titleRes));
    }

    @Override
    public int addIntentOptions(int groupId, int itemId, int order,
                                ComponentName caller, Intent[] specifics,
                                Intent intent, int flags, MenuItem[] outSpecificItems) {
        int count = 0;
        if (specifics != null) {
            for (Intent specific : specifics) {
                MenuItem item = add(groupId, itemId, order, specific.getAction());
                item.setIntent(specific);
                if (outSpecificItems != null && outSpecificItems.length > count) {
                    outSpecificItems[count] = item;
                }
                count++;
            }
        }
        return count;
    }

    @Override
    public void removeItem(int id) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getItemId() == id) {
                items.remove(i);
                return;
            }
        }
    }

    @Override
    public void removeGroup(int groupId) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getGroupId() == groupId) {
                items.remove(i);
            }
        }
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        for (JMenuItem item : items) {
            if (item.getGroupId() == group) {
                item.setCheckable(checkable);
                if (exclusive && checkable) {
                    item.setChecked(false);
                }
            }
        }
    }

    @Override
    public void setGroupVisible(int group, boolean visible) {
        for (JMenuItem item : items) {
            if (item.getGroupId() == group) {
                item.setVisible(visible);
            }
        }
    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {
        for (JMenuItem item : items) {
            if (item.getGroupId() == group) {
                item.setEnabled(enabled);
            }
        }
    }

    @Override
    public boolean hasVisibleItems() {
        for (JMenuItem item : items) {
            if (item.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MenuItem findItem(int id) {
        for (JMenuItem item : items) {
            if (item.getItemId() == id) {
                return item;
            }
            if (item.hasSubMenu()) {
                MenuItem found = item.getSubMenu().findItem(id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public MenuItem getItem(int index) {
        return items.get(index);
    }

    @Override
    public void close() {
        for (JMenuItem item : items) {
            if (item.hasSubMenu()) {
                item.getSubMenu().close();
            }
        }
    }

    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        for (JMenuItem item : items) {
            if (item.isEnabled() &&
                    (item.getAlphabeticShortcut() == keyCode ||
                            item.getNumericShortcut() == keyCode)) {
                return item.performClick();
            }
        }
        return false;
    }

    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        for (JMenuItem item : items) {
            if (item.isEnabled() &&
                    (item.getAlphabeticShortcut() == keyCode ||
                            item.getNumericShortcut() == keyCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {

        return false;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
        this.qwertyMode = isQwerty;
    }

    public List<JMenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addSeparator() {
        // Méthode laissée vide intentionnellement selon les spécifications
    }

    public void add(JSeparator separator)
    {

    }

    public void showMenuPopup(View anchor) {
        popupMenu.showAsDropDown(anchor);
    }

    public void dismissPopupMenu()
    {
        popupMenu.dismiss();
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void removeAll() {
        items.clear();
    }

    public void setToolTipText(String description) {
    }

    public void setParent(Object parent)
    {
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }

    public String getText() {
        return getTitle();
    }
}