package androidUtils.swing.tree;

import androidUtils.swing.event.TreeModelListener;

public class DefaultTreeModel implements TreeModel {

    protected TreeNode root;

    protected TreeNode originalNode;

    public DefaultTreeModel(TreeNode root) {
        this.root = root;
        originalNode = root;
    }

    @Override
    public TreeNode getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) parent).getChildAt(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) parent).getChildCount();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(TreeNode node) {
        if (node instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) node).isLeaf();
        }
        return true;
    }

    @Override
    public void valueForPathChanged(TreePath path, TreeNode newValue) {

    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        Object node = path.getLastPathComponent();
        if (node instanceof DefaultMutableTreeNode) {
            ((DefaultMutableTreeNode) node).setUserObject(newValue);

        }
    }

    @Override
    public int getIndexOfChild(TreeNode parent, TreeNode child) {
        if (parent instanceof DefaultMutableTreeNode && child instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) parent).getIndex((DefaultMutableTreeNode) child);
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void reload() {

    }


    public void setRoot(DefaultMutableTreeNode root) {
        this.root = root;
    }


}
