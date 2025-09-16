package androidUtils.swing.tree;

import java.util.Enumeration;

/**
 * A tree node interface adapted for Android.
 */
public interface TreeNode{
    TreeNode getParent();
    TreeNode getChildAt(int index);
    int getChildCount();
    int getIndex(TreeNode node);
    boolean getAllowsChildren();
    boolean isLeaf();
    Enumeration<TreeNode> children();

    Object getUserObject();

}
