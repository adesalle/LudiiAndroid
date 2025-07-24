package playerAndroid.app.display.dialogs.visual_editor.view.panels.menus;

import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.menu.JSeparator;
import androidUtils.swing.KeyStroke;
import androidUtils.swing.menu.JMenu;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;

public class EditMenu extends JMenu
{
    private static final long serialVersionUID = 3590839696737937583L;

    public EditMenu(EditorMenuBar menuBar)
    {
        super("Edit");

        EditorMenuBar.addJMenuItem(this, "Undo", undo, KeyStroke.getKeyStroke("control Z"));
        EditorMenuBar.addJMenuItem(this, "Redo", redo, KeyStroke.getKeyStroke("control Y"));
        add(new JSeparator());

        EditorMenuBar.addJMenuItem(this, "Copy", copy, KeyStroke.getKeyStroke("control C"));
        EditorMenuBar.addJMenuItem(this, "Paste", paste, KeyStroke.getKeyStroke("control V"));
        EditorMenuBar.addJMenuItem(this, "Duplicate", duplicate, KeyStroke.getKeyStroke("control shift D"));
        EditorMenuBar.addJMenuItem(this, "Delete", delete, KeyStroke.getKeyStroke("control D"));
        add(new JSeparator());

        EditorMenuBar.addJMenuItem(this, "Select All", selectAll, KeyStroke.getKeyStroke("control A"));
        EditorMenuBar.addJMenuItem(this, "Unselect All", unselectAll);
        EditorMenuBar.addJMenuItem(this, "Collapse", collapse, KeyStroke.getKeyStroke("control W"));
        EditorMenuBar.addJMenuItem(this, "Expand", expand, KeyStroke.getKeyStroke("control E"));
        EditorMenuBar.addJMenuItem(this, "Expand All", expandAll);
        add(new JSeparator());

        EditorMenuBar.addJCheckBoxMenuItem(this, "Confirm Sensitive Actions", Handler.sensitivityToChanges, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.sensitivityToChanges = !Handler.sensitivityToChanges;
            }
        });
    }

    final ActionListener undo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.undo();
        }
    };

    final ActionListener redo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.redo();
        }
    };

    final ActionListener copy = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.copy();
        }
    };

    final ActionListener paste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.paste(-1, -1);
        }
    };

    final ActionListener duplicate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.duplicate();
        }
    };

    final ActionListener delete = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.remove();
        }
    };

    final ActionListener selectAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.selectAll();
        }
    };

    final ActionListener unselectAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.unselectAll();
        }
    };

    final ActionListener collapse = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.collapse();
        }
    };

    final ActionListener expand = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.expand();
        }
    };

    final ActionListener expandAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.selectAll();
            Handler.expand();
        }
    };
}
