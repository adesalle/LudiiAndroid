package androidUtils.swing;

import android.view.MenuItem;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.ItemEvent;
import androidUtils.awt.event.ItemListener;

public class JRadioButtonMenuItem extends JMenuItem
{
    private boolean isSelected = false;
    private ItemListener itemListener;
    private ButtonGroup buttonGroup;

    public JRadioButtonMenuItem(String text)
    {
        super(text);
    }

    public JRadioButtonMenuItem(String text, boolean selected)
    {
        super(text);
        this.isSelected = selected;
    }



    public void setSelected(boolean selected)
    {
        if (selected != isSelected)
        {
            this.isSelected = selected;


            if (selected && buttonGroup != null) {
                buttonGroup.setSelected(this, true);
            }

            fireItemStateChanged();
        }
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void addItemListener(ItemListener listener)
    {
        this.itemListener = listener;
    }

    void setButtonGroup(ButtonGroup group)
    {
        this.buttonGroup = group;
    }

    private void fireItemStateChanged()
    {
        if (itemListener != null)
        {
            ItemEvent event = new ItemEvent(
                    this,
                    ItemEvent.ITEM_STATE_CHANGED,
                    this,
                    isSelected ? ItemEvent.SELECTED : ItemEvent.DESELECTED
            );
            itemListener.itemStateChanged(event);
        }
    }
}