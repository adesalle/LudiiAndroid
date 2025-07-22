package androidUtils.swing;

import android.view.View;
import androidUtils.awt.event.ActionListener;

public interface ComboBoxEditor {

    View getEditorComponent();

    void setItem(Object anObject);


    Object getItem();


    void selectAll();

    void addActionListener(ActionListener l);

    void removeActionListener(ActionListener l);
}