package androidUtils.swing.event;
;


/**
 * Defines the interface for an object that listens to changes in a TreeModel.
 */
public interface TreeModelListener extends java.util.EventListener {
    /**
     * Invoked after a node (or a set of siblings) has changed in some way.
     */
    void treeNodesChanged(TreeModelEvent e);

    /**
     * Invoked after nodes have been inserted into the tree.
     */
    void treeNodesInserted(TreeModelEvent e);

    /**
     * Invoked after nodes have been removed from the tree.
     */
    void treeNodesRemoved(TreeModelEvent e);

    /**
     * Invoked after the tree has drastically changed structure.
     */
    void treeStructureChanged(TreeModelEvent e);
}