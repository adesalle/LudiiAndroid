package playerAndroid.app.display.dialogs.visual_editor.view.panels.editor;

import androidUtils.awt.Image;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.ImageIcon;
import androidUtils.swing.menu.JMenuItem;
import androidUtils.swing.menu.JPopupMenu;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.IGraphPanel;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.gameEditor.GameGraphPanel;


public class EditorPopupMenu extends JPopupMenu
{

    private static final long serialVersionUID = 5378508370225281683L;

    public EditorPopupMenu(IGraphPanel graphPanel, int x, int y)
    {
        JMenuItem newLudeme = new JMenuItem("New Ludeme");
        JMenuItem paste = new JMenuItem("Paste");

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.paste(graphPanel.graph(), x, y);
                graphPanel.deselectEverything();
            }
        });

        JMenuItem compact = new JMenuItem("Arrange graph");
        compact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.getLayoutHandler().executeLayout();
            }
        });

        newLudeme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.showAllAvailableLudemes();
            }
        });

        JMenuItem collapse = new JMenuItem("Collapse");
        collapse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.collapse(graphPanel.graph());
            }
        });

        int iconHeight = (int)(newLudeme.getPreferredSize().getHeight() * 0.75);

        ImageIcon newLudemeIcon = new ImageIcon(DesignPalette.ADD_ICON.getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_SMOOTH));
        newLudeme.setIcon(newLudemeIcon);
        ImageIcon pasteIcon = new ImageIcon(DesignPalette.PASTE_ICON.getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_SMOOTH));
        paste.setIcon(pasteIcon);
        ImageIcon collapseIcon = new ImageIcon(DesignPalette.COLLAPSE_ICON().getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_SMOOTH));
        collapse.setIcon(collapseIcon);

        paste.setEnabled(!Handler.clipboard().isEmpty());
        collapse.setEnabled(!Handler.selectedNodes(graphPanel.graph()).isEmpty());

        add(newLudeme);

        if (graphPanel instanceof GameGraphPanel)
        {
            JMenuItem addDefine = new JMenuItem("Add Define");
            GameGraphPanel ggp = (GameGraphPanel) graphPanel;
            addDefine.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ggp.showAddDefinePanel();
                }
            });
            add(addDefine);
        }

        add(paste);
        add(compact);
        add(collapse);

        JMenuItem fix = new JMenuItem("Fix group");
        fix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.graph().getNode(graphPanel.graph().selectedRoot()).setFixed(true);
            }
        });

        JMenuItem unfix = new JMenuItem("Unfix group");
        unfix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.graph().getNode(graphPanel.graph().selectedRoot()).setFixed(false);
            }
        });

        if (graphPanel.graph().selectedRoot() != -1 &&
                graphPanel.graph().getNode(graphPanel.graph().selectedRoot()).fixed())
        {
            add(unfix);
            graphPanel.repaint();
        }
        else if (graphPanel.graph().selectedRoot() != -1)
        {
            add(fix);
            graphPanel.repaint();
        }

        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.undo();
            }
        });
        add(undo);

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.redo();
            }
        });
        add(redo);
    }

}
