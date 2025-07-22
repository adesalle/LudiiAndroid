package androidUtils.swing;

public interface ComboBoxModel<E> extends ListModel<E> {
    void setSelectedItem(E anItem);
    E getSelectedItem();
}
