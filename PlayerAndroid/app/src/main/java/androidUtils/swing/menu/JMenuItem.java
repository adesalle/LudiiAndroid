package androidUtils.swing.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import playerAndroid.app.StartAndroidApp;

public class JMenuItem implements MenuItem {
    // États de l'item
    private int itemId;
    private int groupId;
    private int order;
    private CharSequence title;
    private CharSequence titleCondensed;
    private Drawable icon;
    private Intent intent;
    private char numericShortcut;
    private char alphabeticShortcut;
    private boolean checkable;
    private boolean checked;
    private boolean visible = true;
    private boolean enabled = true;
    private boolean expanded = false;

    // Listeners et vues
    private OnMenuItemClickListener clickListener;
    private OnActionExpandListener actionExpandListener;
    private SubMenu subMenu;
    private View actionView;
    private ActionProvider actionProvider;

    private Context context;

    public JMenuItem()
    {
        this(StartAndroidApp.getAppContext());
    }
    public JMenuItem(Context context) {

        this.context = context;
    }

    // Implémentation complète des méthodes
    @Override
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int id) {
        this.itemId = id;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @NonNull
    @Override
    public MenuItem setTitle(@Nullable CharSequence title) {
        this.title = title;
        return this;
    }

    @NonNull
    @Override
    public MenuItem setTitle(int titleRes) {
        this.title = context.getText(titleRes);
        return this;
    }

    @Nullable
    @Override
    public CharSequence getTitle() {
        return title;
    }

    @NonNull
    @Override
    public MenuItem setTitleCondensed(@Nullable CharSequence title) {
        this.titleCondensed = title;
        return this;
    }

    @Nullable
    @Override
    public CharSequence getTitleCondensed() {
        return titleCondensed != null ? titleCondensed : title;
    }

    @NonNull
    @Override
    public MenuItem setIcon(@Nullable Drawable icon) {
        this.icon = icon;
        return this;
    }

    @NonNull
    @Override
    public MenuItem setIcon(int iconRes) {
        this.icon = context.getDrawable(iconRes);
        return this;
    }

    @Nullable
    @Override
    public Drawable getIcon() {
        return icon;
    }

    @NonNull
    @Override
    public MenuItem setIntent(@Nullable Intent intent) {
        this.intent = intent;
        return this;
    }

    @Nullable
    @Override
    public Intent getIntent() {
        return intent;
    }

    @NonNull
    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        this.numericShortcut = numericChar;
        this.alphabeticShortcut = alphaChar;
        return this;
    }

    @NonNull
    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        this.numericShortcut = numericChar;
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return numericShortcut;
    }

    @NonNull
    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        this.alphabeticShortcut = alphaChar;
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return alphabeticShortcut;
    }

    @NonNull
    @Override
    public MenuItem setCheckable(boolean checkable) {
        this.checkable = checkable;
        return this;
    }

    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @NonNull
    @Override
    public MenuItem setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @NonNull
    @Override
    public MenuItem setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @NonNull
    @Override
    public MenuItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean hasSubMenu() {
        return subMenu != null;
    }

    @Nullable
    @Override
    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    @NonNull
    @Override
    public MenuItem setOnMenuItemClickListener(@Nullable OnMenuItemClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    public boolean performClick() {
        if (enabled && clickListener != null) {
            return clickListener.onMenuItemClick(this);
        }
        return false;
    }

    @Nullable
    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
        // Implémentation basique - normalement gère le mode d'affichage
    }

    @NonNull
    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        setShowAsAction(actionEnum);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setActionView(@Nullable View view) {
        this.actionView = view;
        return this;
    }

    @NonNull
    @Override
    public MenuItem setActionView(int resId) {
        this.actionView = View.inflate(context, resId, null);
        return this;
    }

    @Nullable
    @Override
    public View getActionView() {
        return actionView;
    }

    @NonNull
    @Override
    public MenuItem setActionProvider(@Nullable ActionProvider provider) {
        this.actionProvider = provider;
        return this;
    }

    @Nullable
    @Override
    public ActionProvider getActionProvider() {
        return actionProvider;
    }

    @Override
    public boolean expandActionView() {
        if (actionView != null) {
            if (actionExpandListener == null || actionExpandListener.onMenuItemActionExpand(this)) {
                expanded = true;
                actionView.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collapseActionView() {
        if (actionView != null) {
            if (actionExpandListener == null || actionExpandListener.onMenuItemActionCollapse(this)) {
                expanded = false;
                actionView.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return expanded;
    }

    @NonNull
    @Override
    public MenuItem setOnActionExpandListener(@Nullable OnActionExpandListener listener) {
        this.actionExpandListener = listener;
        return this;
    }


    public void toggle() {
        setChecked(!isChecked());
    }
}