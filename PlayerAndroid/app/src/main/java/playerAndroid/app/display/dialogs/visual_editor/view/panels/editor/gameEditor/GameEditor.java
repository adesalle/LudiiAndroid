package playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.gameEditor;

import androidUtils.awt.BorderLayout;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import androidUtils.swing.JPanel;
import androidUtils.swing.JScrollPane;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.IGraphPanel;

/**
 * Panel for the game graph panel
 */

public class GameEditor extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7742630637240183L;
	private final JScrollPane SCROLL_PANE;
    private final GameGraphPanel GRAPH_PANEL;

    public GameEditor()
    {
        setLayout(new BorderLayout());
        GRAPH_PANEL = new GameGraphPanel(DesignPalette.DEFAULT_GRAPHPANEL_SIZE.width,  DesignPalette.DEFAULT_GRAPHPANEL_SIZE.height);
        SCROLL_PANE = new JScrollPane(GRAPH_PANEL);
        centerScrollPane(); // center scroll pane position
        add(SCROLL_PANE, BorderLayout.CENTER);
        GRAPH_PANEL.initialize(SCROLL_PANE);
        setVisible(true);
    }

    private void centerScrollPane()
    {
        Rectangle rect = SCROLL_PANE.getViewport().getViewRect();
        int centerX = (SCROLL_PANE.getViewport().getViewSize().width - rect.width) / 2;
        int centerY = (SCROLL_PANE.getViewport().getViewSize().height - rect.height) / 2;

        SCROLL_PANE.getViewport().setViewPosition(new Point(centerX, centerY));
    }

    public IGraphPanel graphPanel()
    {
        return GRAPH_PANEL;
    }

}
