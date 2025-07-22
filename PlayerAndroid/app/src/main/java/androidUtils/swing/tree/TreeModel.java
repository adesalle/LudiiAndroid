
package androidUtils.swing.tree;

import androidUtils.swing.event.TreeModelListener;

public interface TreeModel {
    TreeNode getRoot();
    TreeNode getChild(TreeNode parent, int index);
    int getChildCount(TreeNode parent);
    boolean isLeaf(TreeNode node);
    void valueForPathChanged(TreePath path, TreeNode newValue);
    int getIndexOfChild(TreeNode parent, TreeNode child);
    void addTreeModelListener(TreeModelListener l);
    void removeTreeModelListener(TreeModelListener l);

    void reload();
}


