
package androidUtils.swing.tree;

import androidUtils.swing.event.TreeModelListener;

public interface TreeModel {
    TreeNode getRoot();
     Object getChild(Object parent, int index);
    int getChildCount(Object parent);
    boolean isLeaf(TreeNode node);
    void valueForPathChanged(TreePath path, TreeNode newValue);

    void valueForPathChanged(TreePath path, Object newValue);

    int getIndexOfChild(TreeNode parent, TreeNode child);
    void addTreeModelListener(TreeModelListener l);
    void removeTreeModelListener(TreeModelListener l);

    void reload();
}

