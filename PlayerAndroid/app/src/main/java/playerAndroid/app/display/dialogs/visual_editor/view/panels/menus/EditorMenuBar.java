package playerAndroid.app.display.dialogs.visual_editor.view.panels.menus;


import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JCheckBoxMenuItem;
import androidUtils.swing.KeyStroke;
import androidUtils.swing.menu.JMenu;
import androidUtils.swing.menu.JMenuBar;
import androidUtils.swing.menu.JMenuItem;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.menus.viewMenu.ViewMenu;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.userGuide.UserGuideFrame;

/**
 * Menu bar of visual editor
 * @author nic0gin
 */
public class EditorMenuBar extends JMenuBar {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1223558045694729428L;

	public EditorMenuBar() {

        JMenu settings = new JMenu("Settings"); // adjust editor settings e.g. font size, colors etc.
        // adding settings menu items
        addJMenuItem(settings, "Open settings...", null);

        JMenu about = new JMenu("Help"); // read about the editor: documentation, research report, DLP
        // adding about menu items
        addJMenuItem(about, "User Guide", new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent event) {
                new UserGuideFrame();
            }
        });
        addJMenuItem(about, "Documentation", null);
        addJMenuItem(about, "About Visual Editor...", null); // opens research paper
        addJMenuItem(about, "About DLP...", null);

        add(new FileMenu(this));
        add(new EditMenu(this));
        add(new ViewMenu(this));
        add(new TreeLayoutMenu(this));
        //add(settings);
        add(new RunMenu(this));
        add(about);
    }

    public static void addJMenuItem(JMenu menu, String itemName, ActionListener actionListener)
    {
        JMenuItem jMenuItem = new JMenuItem(itemName);
        jMenuItem.addActionListener(actionListener);
        menu.add(jMenuItem);
    }

    public static void addJMenuItem(JMenu menu, String itemName, ActionListener actionListener, KeyStroke keyStroke)
    {
        JMenuItem jMenuItem = new JMenuItem(itemName);
        jMenuItem.addActionListener(actionListener);
        jMenuItem.setAccelerator(keyStroke);
        menu.add(jMenuItem);
    }

    public static void addJCheckBoxMenuItem(JMenu menu, String itemName, boolean selected, ActionListener actionListener)
    {
        JCheckBoxMenuItem jMenuItem = new JCheckBoxMenuItem(itemName);
        jMenuItem.addActionListener(actionListener);
        jMenuItem.setSelected(selected);
        menu.add(jMenuItem);
    }

}

