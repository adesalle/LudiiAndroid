package androidUtils.swing;

import androidUtils.swing.event.ListDataEvent;
import androidUtils.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultListModel<E> implements ListModel<E> {
    private List<E> data = new ArrayList<>();
    private final List<ListDataListener> listeners = new ArrayList<>();

    public void addElement(E element) {
        data.add(element);
        fireIntervalAdded(data.size() - 1, data.size() - 1);
    }

    public void removeElement(E element) {
        int index = data.indexOf(element);
        if (index >= 0) {
            data.remove(index);
            fireIntervalRemoved(index, index);
        }
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public E getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    @Override
    public void setElements(E[] elements) {
        data = Arrays.asList(elements);
    }

    @Override
    public void clear() {
        data.clear();
    }

    private void fireIntervalAdded(int index0, int index1) {
        ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
        for (ListDataListener listener : listeners) {
            listener.intervalAdded(e);
        }
    }

    private void fireIntervalRemoved(int index0, int index1) {
        ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
        for (ListDataListener listener : listeners) {
            listener.intervalRemoved(e);
        }
    }
}
