package androidUtils.swing;

import androidUtils.swing.event.ListDataListener;

public interface ListModel<E> {
    int getSize();
    E getElementAt(int index);
    void addListDataListener(ListDataListener l);
    void removeListDataListener(ListDataListener l);

    void setElements(E[] elements);

    void clear();
}