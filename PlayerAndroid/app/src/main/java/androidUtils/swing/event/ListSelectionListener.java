package androidUtils.swing.event;

/**
 * The listener interface for receiving list selection events.
 */
public interface ListSelectionListener {
    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change
     */
    void valueChanged(ListSelectionEvent e);
}