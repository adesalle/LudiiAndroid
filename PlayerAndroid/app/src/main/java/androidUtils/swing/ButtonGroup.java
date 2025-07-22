package androidUtils.swing;

import java.util.ArrayList;
import java.util.List;

public class ButtonGroup
{
    private List<JRadioButtonMenuItem> buttons = new ArrayList<>();
    private JRadioButtonMenuItem selectedButton;

    public void add(JRadioButtonMenuItem button)
    {
        if (button == null) {
            return;
        }

        buttons.add(button);
        button.setButtonGroup(this);

        if (button.isSelected()) {
            if (selectedButton == null) {
                selectedButton = button;
            } else {
                button.setSelected(false);
            }
        }
    }

    public void remove(JRadioButtonMenuItem button)
    {
        if (button == null) {
            return;
        }

        buttons.remove(button);

        if (button == selectedButton) {
            selectedButton = null;
        }
    }

    void setSelected(JRadioButtonMenuItem button, boolean selected)
    {
        if (!selected || button == selectedButton) {
            return;
        }

        if (selectedButton != null) {
            selectedButton.setSelected(false);
        }

        selectedButton = button;
    }

    public JRadioButtonMenuItem getSelected()
    {
        return selectedButton;
    }

    public int getButtonCount()
    {
        return buttons.size();
    }
}