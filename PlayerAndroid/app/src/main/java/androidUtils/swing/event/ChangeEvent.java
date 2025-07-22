package androidUtils.swing.event;

import androidUtils.swing.JComponent;

public class ChangeEvent extends java.util.EventObject {
    public ChangeEvent(JComponent source) {
        super(source);
    }

    // Pour une utilisation plus générale avec n'importe quel objet
    public ChangeEvent(Object source) {
        super(source);
    }
}