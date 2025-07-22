package androidUtils.swing;

import androidUtils.swing.event.ListSelectionListener;
import playerAndroid.app.display.dialogs.editor.SuggestionDialog;

public interface ListSelectionModel {
    int SINGLE_SELECTION = 0;
    int SINGLE_INTERVAL_SELECTION = 1;
    int MULTIPLE_INTERVAL_SELECTION = 2;

    void setSelectionMode(int selectionMode);
    void setSelectionInterval(int index0, int index1);
    int[] getSelectedIndices();
    int getMinSelectionIndex();
    int getMaxSelectionIndex();
    boolean isSelectedIndex(int index);

    void addListSelectionListener(ListSelectionListener listener);
    void removeListSelectionListener(ListSelectionListener listener);

}
