package androidUtils.swing.event;

public interface ListDataListener {
    void intervalAdded(ListDataEvent e);
    void intervalRemoved(ListDataEvent e);
    void contentsChanged(ListDataEvent e);
}