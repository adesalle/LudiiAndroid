package androidUtils.swing.tree;

import java.util.List;

import androidUtils.swing.event.TreeModelEvent;
import androidUtils.swing.event.TreeModelListener;

public class DefaultTreeModel implements TreeModel {
    private TreeNode root;
    private List<TreeModelListener> listeners;
    protected boolean asksAllowsChildren;

    public DefaultTreeModel(TreeNode root) {
        this(root, false);
    }

    public DefaultTreeModel(TreeNode root, boolean asksAllowChildren) {
        this.root = root;
        this.asksAllowsChildren = asksAllowChildren;
    }
    
    public boolean asksAllowsChildren()
    {
        return asksAllowsChildren;
    }
    
    
    @Override
     public boolean isLeaf(TreeNode node) 
     {
        TreeNode treeNode = (TreeNode) node;
        boolean leaf;
        if (asksAllowsChildren)
            leaf = ! treeNode.getAllowsChildren();
        else 
            leaf = treeNode.isLeaf();
        return leaf;
     }


    @Override
    public TreeNode getRoot() {
        return root;
    }

    @Override
    public TreeNode getChild(TreeNode parent, int index) {
        return ((TreeNode)parent).getChildAt(index);
    }

    @Override
    public int getChildCount(TreeNode parent) {
        return parent.getChildCount();
    }

    @Override
    public void valueForPathChanged(TreePath path, TreeNode newValue) {

    }

    @Override
    public int getIndexOfChild(TreeNode parent, TreeNode child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void reload() {

    }

    protected void fireTreeNodesChanged(TreeNode source, TreePath path,
                                        int[] childIndices, TreeNode[] children) {
        TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesChanged(event);
        }
    }

    protected void fireTreeNodesInserted(TreeNode source, TreePath path,
                                         int[] childIndices, TreeNode[] children) {
        TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesInserted(event);
        }
    }

    protected void fireTreeNodesRemoved(TreeNode source, TreePath path,
                                        int[] childIndices, TreeNode[] children) {
        TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesRemoved(event);
        }
    }

    protected void fireTreeStructureChanged(TreeNode source, TreePath path) {
        TreeModelEvent event = new TreeModelEvent(source, path);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(event);
        }
    }

    public Object getChild(Object parent, int index)
    {
        return getChild((TreeNode) parent, index);
    }


    public int getChildCount(Object parent)
    {
        return getChildCount((TreeNode) parent);
    }
}