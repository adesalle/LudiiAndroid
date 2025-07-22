package playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.defineEditor;


import androidUtils.swing.JScrollPane;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.model.DescriptionGraph;
import playerAndroid.app.display.dialogs.visual_editor.model.LudemeNode;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.GraphPanel;

public class DefineGraphPanel extends GraphPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 493013782396870985L;
	private final DescriptionGraph GRAPH;

    public DefineGraphPanel(String name, int width, int height)
    {
        super(width, height);
        this.GRAPH = new DescriptionGraph(name, true);
        Handler.addGraphPanel(graph(), this);
    }

    @Override
    public DescriptionGraph graph()
    {
        return GRAPH;
    }

    @Override
	public void initialize(JScrollPane scrollPane)
    {
        super.initialize(scrollPane);

        // Create a "define" root node
        LudemeNode defineRoot = new LudemeNode(scrollPane.getViewport().getViewRect().x + (int)(scrollPane.getViewport().getViewRect().getWidth()/2),
                scrollPane.getViewport().getViewRect().y + (int)(scrollPane.getViewport().getViewRect().getHeight()/2), GraphPanel.symbolsWithoutConnection, true);
        defineRoot.setProvidedInput(defineRoot.currentNodeArguments().get(0),graph().title());

        Handler.recordUserActions = false;
        Handler.addNode(graph(), defineRoot);
        Handler.updateInput(graph(), defineRoot, defineRoot.currentNodeArguments().get(0), graph().title());
        Handler.recordUserActions = true;

    }

    @Override
    public boolean isDefineGraph()
    {
        return true;
    }
}
