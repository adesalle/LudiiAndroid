package playerAndroid.app.display.dialogs.visual_editor.model.UserActions;


import java.util.List;

import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.model.DescriptionGraph;
import playerAndroid.app.display.dialogs.visual_editor.model.LudemeNode;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.IGraphPanel;

/**
 * Created when the user pastes node(s).
 */

public class PasteAction implements IUserAction
{

    private final IGraphPanel graphPanel;
    private final DescriptionGraph graph;
    private final List<LudemeNode> pastedNodes;
    private  RemovedNodesAction removedNodesAction;

    public PasteAction(IGraphPanel graphPanel, List<LudemeNode> pastedNodes)
    {
        this.graphPanel = graphPanel;
        this.graph = graphPanel.graph();
        this.pastedNodes = pastedNodes;
    }

    /**
     * @return The type of the action
     */
    @Override
    public ActionType actionType() {
        return ActionType.PASTED;
    }

    /**
     * @return The graph panel that was affected by the action
     */
    @Override
    public IGraphPanel graphPanel() {
        return graphPanel;
    }

    /**
     * @return The description graph that was affected by the action
     */
    @Override
    public DescriptionGraph graph() {
        return graph;
    }

    /**
     * Undoes the action
     */
    @Override
    public void undo() {
        removedNodesAction = new RemovedNodesAction(graphPanel, pastedNodes);
        Handler.removeNodes(graph, pastedNodes);
    }

    /**
     * Redoes the action
     */
    @Override
    public void redo() {
        removedNodesAction.undo();
    }
}
