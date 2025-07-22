package playerAndroid.app.display.dialogs.visual_editor.view.panels.menus.viewMenu;

import java.util.List;

import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JMenu;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.menus.EditorMenuBar;

public class ViewMenu extends JMenu
{
    private static final long serialVersionUID = -5378448276139884202L;

    public ViewMenu(EditorMenuBar menuBar)
    {
        super("View");

        JMenu appearance = new JMenu("Colour Scheme");
        JMenu background = new JMenu("Background");
        JMenu fontSize = new JMenu("Font Size");

        add(appearance);
        add(background);
        add(fontSize);

        List<String> paletteNames = Handler.palettes();
        for (String paletteName : paletteNames)
        {
            EditorMenuBar.addJMenuItem(appearance, paletteName, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Handler.setPalette(paletteName);
                }
            });
        }

        EditorMenuBar.addJMenuItem(background, "Dot Grid", dotGrid);
        EditorMenuBar.addJMenuItem(background, "Cartesian Grid", cartesianGrid);
        EditorMenuBar.addJMenuItem(background, "No Grid", noGrid);

        EditorMenuBar.addJMenuItem(fontSize, "Small", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.setFont("Small");
            }
        });

        EditorMenuBar.addJMenuItem(fontSize, "Medium", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.setFont("Medium");
            }
        });

        EditorMenuBar.addJMenuItem(fontSize, "Large", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.setFont("Large");
            }
        });
    }

    final ActionListener dotGrid = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.setBackground(Handler.DotGridBackground);
        }
    };

    final ActionListener cartesianGrid = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.setBackground(Handler.CartesianGridBackground);
        }
    };

    final ActionListener noGrid = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Handler.setBackground(Handler.EmptyBackground);
        }
    };
}
