package androidUtils.swing;

import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.ItemEvent;
import androidUtils.awt.event.ItemListener;
import playerAndroid.app.StartAndroidApp;

public class JCheckBoxMenuItem extends JMenuItem {
    private boolean isSelected = false;
    private ItemListener itemListener;
    private CheckBox checkBoxView; // Pour une utilisation possible en tant que View

    public JCheckBoxMenuItem(String text) {
        super(text);
    }

    public JCheckBoxMenuItem(String text, boolean selected) {
        super(text);
        this.isSelected = selected;
    }

    public void setAndroidMenuItem(MenuItem menuItem) {
        menuItem.setCheckable(true);
        menuItem.setChecked(isSelected);

        // Utilisation d'un OnMenuItemClickListener personnalisé
        menuItem.setOnMenuItemClickListener(item -> {
            setSelected(!isSelected);
            ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText());
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(e);
            }
            return true;
        });
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (checkBoxView != null) {
            checkBoxView.setChecked(selected);
        }
        fireItemStateChanged();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void addItemListener(ItemListener listener) {
        this.itemListener = listener;
    }

    private void fireItemStateChanged() {
        if (itemListener != null) {
            ItemEvent event = new ItemEvent(
                    this,
                    ItemEvent.ITEM_STATE_CHANGED,
                    this,
                    isSelected ? ItemEvent.SELECTED : ItemEvent.DESELECTED
            );
            itemListener.itemStateChanged(event);
        }
    }

    // Méthode pour obtenir une View CheckBox si nécessaire
    public CheckBox getCheckBoxView() {
        if (checkBoxView == null) {
            checkBoxView = new CheckBox(StartAndroidApp.getAppContext());
            checkBoxView.setText(getText());
            checkBoxView.setChecked(isSelected);
            checkBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setSelected(isChecked);
            });
        }
        return checkBoxView;
    }
}