package androidUtils.swing.tree;

import java.util.Enumeration;

/**
 * A tree node interface adapted for Android.
 */
public interface TreeNode {
    /**
     * Returns the parent node for this tree node, or null if this
     * node has no parent.
     */
    TreeNode getParent();

    /**
     * Returns the index of the specified child node, or -1 if the node is not
     * a child of this node.
     */
    int getIndex(TreeNode node);

    /**
     * Returns the child node at the given index.
     */
    TreeNode getChildAt(int index);

    /**
     * Returns the number of children for this node.
     */
    int getChildCount();

    /**
     * Returns true if this node allows children, and false otherwise.
     */
    boolean getAllowsChildren();

    /**
     * Returns true if this node is a leaf node, and false otherwise.
     */
    boolean isLeaf();

    /**
     * Returns an enumeration of the children of this node.
     */
    Enumeration<TreeNode> children();

    Enumeration<?> depthFirstEnumeration();
}