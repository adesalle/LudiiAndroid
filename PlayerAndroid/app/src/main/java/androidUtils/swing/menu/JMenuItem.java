package androidUtils.swing.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Dimension;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.Icon;
import androidUtils.swing.KeyStroke;
import playerAndroid.app.StartAndroidApp;

public class JMenuItem implements MenuItem{
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

    private KeyStroke accelerator;
    private String toolTipText;
    private Object parent;
    private Icon iconObj;
    private Icon disabledIcon;
    private boolean armed;
    private final List<ActionListener> actionListeners = new ArrayList<>();
    private final List<Object> changeListeners = new ArrayList<>();

    // Listeners et vues
    private OnMenuItemClickListener clickListener;
    private OnActionExpandListener actionExpandListener;
    private JSubMenu subMenu;
    private View actionView;
    private ActionProvider actionProvider;

    private Context context;

    public JMenuItem(String title)
    {
        this();
        setTitle(title);
    }
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

    public String getText() {
        return title.toString();
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

    public void setSubMenu(JSubMenu subMenu) {
        this.subMenu = subMenu;
        subMenu.setParent(this);
    }

    @NonNull
    @Override
    public MenuItem setOnMenuItemClickListener(@Nullable OnMenuItemClickListener listener) {
        this.clickListener = listener;
        return this;
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


    public void setAccelerator(KeyStroke s) {
        this.accelerator = s;
        if (s != null) {
            setAlphabeticShortcut(s.getKeyChar());
        }
    }

    public KeyStroke getAccelerator() {
        return this.accelerator;
    }

    public void addActionListener(ActionListener al) {
        setOnMenuItemClickListener(item -> {
            al.actionPerformed(new ActionEvent(item));
            ((JMenu)((JMenuItem)item).getParent()).dismissPopupMenu();
            return false;
        });
        ;
    }

    public void removeActionListener(ActionListener al) {
        actionListeners.remove(al);
    }

    public void setToolTipText(String s) {
        this.toolTipText = s;
    }

    public String getToolTipText() {
        return this.toolTipText;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getParent() {
        return this.parent;
    }

    public void setIcon(Icon icon) {
        this.iconObj = icon;
        if (icon != null) {
            setIcon(icon);
        }
    }


    public void addChangeListener(Object listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    public void removeChangeListener(Object listener) {
        changeListeners.remove(listener);
    }

    public void setDisabledIcon(Icon icon) {
        this.disabledIcon = icon;
    }

    public Icon getDisabledIcon() {
        return this.disabledIcon;
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
        // Notifier les changeListeners si nécessaire
        notifyChangeListeners();
    }

    public boolean isArmed() {
        return this.armed;
    }

    private void notifyChangeListeners() {
        for (Object listener : changeListeners) {
            // Implémenter la notification selon votre système d'événements
        }
    }

    private void notifyActionListeners() {
        for (ActionListener al : actionListeners) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String)getTitle()));
        }
    }


    public boolean performClick() {
        if (enabled) {
            notifyActionListeners();
            if (clickListener != null) {
                return clickListener.onMenuItemClick(this);
            }
        }
        return false;
    }

    public Dimension getPreferredSize() {
        // Valeurs par défaut pour les paddings et espacements
        int iconTextGap = 4; // espace entre l'icône et le texte (en dp)
        int horizontalPadding = 16; // padding horizontal (en dp)
        int verticalPadding = 8; // padding vertical (en dp)

        // Convertir les dp en pixels
        float density = context.getResources().getDisplayMetrics().density;
        int iconTextGapPx = (int)(iconTextGap * density);
        int horizontalPaddingPx = (int)(horizontalPadding * density);
        int verticalPaddingPx = (int)(verticalPadding * density);

        // Mesurer la largeur du texte
        Paint textPaint = new Paint();
        textPaint.setTextSize(16 * density);
        float textWidth = 0;

        if (title != null) {
            textWidth = textPaint.measureText(title.toString());
        }

        // Largeur de l'icône
        int iconWidth = 0;
        if (icon != null) {
            iconWidth = icon.getIntrinsicWidth();
            if (iconWidth == -1) {
                iconWidth = (int)(24 * density); // Taille par défaut si non spécifiée
            }
        }

        // Largeur totale
        int totalWidth = horizontalPaddingPx * 2;
        if (iconWidth > 0) {
            totalWidth += iconWidth + iconTextGapPx;
        }
        totalWidth += (int)textWidth;

        // Hauteur totale
        int totalHeight = verticalPaddingPx * 2;
        int textHeight = (int)(textPaint.descent() - textPaint.ascent());
        int iconHeight = 0;

        if (icon != null) {
            iconHeight = icon.getIntrinsicHeight();
            if (iconHeight == -1) {
                iconHeight = (int)(24 * density); // Taille par défaut si non spécifiée
            }
        }

        totalHeight += Math.max(textHeight, iconHeight);

        // Ajouter de l'espace pour le raccourci si présent
        if (accelerator != null) {
            String accelText = accelerator.toString();
            float accelWidth = textPaint.measureText(accelText);
            totalWidth += iconTextGapPx + accelWidth;
        }

        // S'assurer d'une hauteur minimale
        int minHeight = (int)(48 * density); // Hauteur minimale Material Design
        totalHeight = Math.max(totalHeight, minHeight);

        return new Dimension(totalWidth, totalHeight);
    }
}