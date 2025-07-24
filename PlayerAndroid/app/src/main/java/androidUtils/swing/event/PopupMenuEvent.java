package androidUtils.swing.event;

import androidUtils.swing.menu.JPopupMenu;

public class PopupMenuEvent extends java.util.EventObject {
    public PopupMenuEvent(JPopupMenu source) {
        super(source);
    }
}