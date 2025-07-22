package androidUtils.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidUtils.swing.event.ListSelectionEvent;
import androidUtils.swing.event.ListSelectionListener;
import playerAndroid.app.display.dialogs.editor.SuggestionDialog;

public class DefaultListSelectionModel implements ListSelectionModel {
    private int selectionMode = MULTIPLE_INTERVAL_SELECTION;
    private final List<Integer> selectedIndices = new ArrayList<>();
    private final List<ListSelectionListener> listeners = new CopyOnWriteArrayList<>();
    private boolean isAdjusting = false;


    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
    }


    public void setSelectionInterval(int index0, int index1) {
        clearSelection();
        addSelectionInterval(index0, index1);
    }


    public void addSelectionInterval(int index0, int index1) {
        if (selectionMode == SINGLE_SELECTION) {
            selectedIndices.clear();
            selectedIndices.add(index0);
        } else if (selectionMode == SINGLE_INTERVAL_SELECTION) {
            selectedIndices.clear();
            for (int i = Math.min(index0, index1); i <= Math.max(index0, index1); i++) {
                selectedIndices.add(i);
            }
        } else { // MULTIPLE_INTERVAL_SELECTION
            for (int i = Math.min(index0, index1); i <= Math.max(index0, index1); i++) {
                if (!selectedIndices.contains(i)) {
                    selectedIndices.add(i);
                }
            }
        }
        fireValueChanged();
    }


    public void removeSelectionInterval(int index0, int index1) {
        for (int i = Math.min(index0, index1); i <= Math.max(index0, index1); i++) {
            selectedIndices.remove(Integer.valueOf(i));
        }
        fireValueChanged();
    }


    public void clearSelection() {
        if (!selectedIndices.isEmpty()) {
            selectedIndices.clear();
            fireValueChanged();
        }
    }


    public boolean isSelectionEmpty() {
        return selectedIndices.isEmpty();
    }


    public void setValueIsAdjusting(boolean isAdjusting) {
        this.isAdjusting = isAdjusting;
    }


    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    @Override
    public int[] getSelectedIndices() {
        int[] indices = new int[selectedIndices.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = selectedIndices.get(i);
        }
        return indices;
    }

    @Override
    public int getMinSelectionIndex() {
        if (selectedIndices.isEmpty()) return -1;
        int min = selectedIndices.get(0);
        for (int index : selectedIndices) {
            if (index < min) min = index;
        }
        return min;
    }

    @Override
    public int getMaxSelectionIndex() {
        if (selectedIndices.isEmpty()) return -1;
        int max = selectedIndices.get(0);
        for (int index : selectedIndices) {
            if (index > max) max = index;
        }
        return max;
    }

    @Override
    public boolean isSelectedIndex(int index) {
        return selectedIndices.contains(index);
    }


    @Override
    public void addListSelectionListener(ListSelectionListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListSelectionListener(ListSelectionListener listener) {
        listeners.remove(listener);
    }

    protected void fireValueChanged() {
        if (listeners.isEmpty()) return;

        int firstIndex = getMinSelectionIndex();
        int lastIndex = getMaxSelectionIndex();
        ListSelectionEvent e = new ListSelectionEvent(
                this,
                firstIndex,
                lastIndex,
                getValueIsAdjusting()
        );

        for (ListSelectionListener listener : listeners) {
            listener.valueChanged(e);
        }
    }
}