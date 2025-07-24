package playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.defineEditor;

import androidUtils.awt.BorderLayout;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JPanel;
import androidUtils.swing.JScrollPane;
import androidUtils.swing.JTabbedPane;
import androidUtils.swing.SwingConstants;
import androidUtils.swing.menu.JMenuItem;
import androidUtils.swing.menu.JPopupMenu;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.IGraphPanel;

import java.util.HashMap;
import java.util.Map;

public class DefineEditor extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5720022256564467244L;
	private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private final Map<JScrollPane, DefineGraphPanel>  defineGraphPanels = new HashMap<>();
    private final Map<DefineGraphPanel, JScrollPane> defineScrollPanes = new HashMap<>();


    public DefineEditor()
    {
        Handler.defineEditor = this;
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(" + ", null, new JPanel(), "Add a new Define");

        addNewPanel("New Define 1");

        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem addNewPanelMenuItem = new JMenuItem("Add New Define");
        final JMenuItem removePanelMenuItem = new JMenuItem("Remove Selected Define");
        addNewPanelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

            addNewPanel("New Define " + (defineGraphPanels.size()+1));
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }});
        removePanelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                removePanel((JScrollPane) tabbedPane.getSelectedComponent());}});

        popupMenu.add(addNewPanelMenuItem);
        popupMenu.add(removePanelMenuItem);

        tabbedPane.setComponentPopupMenu(popupMenu);

        tabbedPane.addChangeListener(e ->
        {

            // if clicked on + button:
            if(tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab(" + "))
            {
                addNewPanel("New Define " + (defineGraphPanels.size()+1));
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
            // update current graph panel
            IGraphPanel graphPanel = defineGraphPanels.get(tabbedPane.getSelectedComponent());
            Handler.updateCurrentGraphPanel(graphPanel);
        });

        setVisible(true);
    }

    public void addNewPanel(String name)
    {
        DefineGraphPanel graphPanel = new DefineGraphPanel(name, 10000, 10000);
        JScrollPane scrollPane = new JScrollPane(graphPanel);
        centerScrollPane(scrollPane);
        graphPanel.initialize(scrollPane);
        defineGraphPanels.put(scrollPane, graphPanel);
        defineScrollPanes.put(graphPanel, scrollPane);
        tabbedPane.addTab(name, scrollPane);
        tabbedPane.setSelectedComponent(scrollPane);
    }

    private void removePanel(JScrollPane scrollPane)
    {
        tabbedPane.remove(scrollPane);
        Handler.removeGraphPanel(defineGraphPanels.get(scrollPane));
        defineScrollPanes.remove(defineGraphPanels.get(scrollPane));
        defineGraphPanels.remove(scrollPane);
    }

    public void removalGraph(IGraphPanel graphPanel)
    {
        removePanel(defineScrollPanes.get(graphPanel));
    }

    public void updateName(String name)
    {
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), name);
    }

    public JScrollPane currentScrollPane()
    {
        return (JScrollPane) tabbedPane.getSelectedComponent();
    }

    public IGraphPanel currentGraphPanel()
    {
        return defineGraphPanels.get(currentScrollPane());
    }


    private static void centerScrollPane(JScrollPane scrollPane)
    {
        Rectangle rect = scrollPane.getViewport().getViewRect();
        int centerX = (scrollPane.getViewport().getViewSize().width - rect.width) / 2;
        int centerY = (scrollPane.getViewport().getViewSize().height - rect.height) / 2;

        scrollPane.getViewport().setViewPosition(new Point(centerX, centerY));
    }


}
