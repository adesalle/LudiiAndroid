package playerAndroid.app.display.dialogs.visual_editor.view.panels.menus;

import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JCheckBoxMenuItem;
import androidUtils.swing.JMenu;
import androidUtils.swing.JSeparator;
import androidUtils.swing.KeyStroke;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;

public class RunMenu extends JMenu
{
    private static final long serialVersionUID = -7766009187364586583L;

    public RunMenu(EditorMenuBar menuBar)
    {
        super("Run");
        EditorMenuBar.addJMenuItem(this, "Compile", compile, KeyStroke.getKeyStroke("control shift R"));
        EditorMenuBar.addJMenuItem(this, "Play Game", play, KeyStroke.getKeyStroke("control P"));
        add(new JSeparator());
        EditorMenuBar.addJCheckBoxMenuItem(this, "Auto Compile", Handler.liveCompile, autocompile);
    }

    final ActionListener autocompile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.liveCompile = ((JCheckBoxMenuItem) e.getSource()).isSelected();
        }
    };

    final ActionListener compile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.compile(true);
        }
    };

    final ActionListener play = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.play();
        }
    };
}
