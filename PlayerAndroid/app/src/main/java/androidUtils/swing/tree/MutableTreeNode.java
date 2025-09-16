package androidUtils.swing.tree;


public interface MutableTreeNode extends TreeNode {
    void add(MutableTreeNode child);
    void insert(MutableTreeNode child, int index);
    void remove(int index);
    void remove(MutableTreeNode node);
    void removeAllChildren();
    void setParent(MutableTreeNode newParent);
    void setUserObject(Object object);
    void removeFromParent();
    void setAllowsChildren(boolean allowsChildren);
}
