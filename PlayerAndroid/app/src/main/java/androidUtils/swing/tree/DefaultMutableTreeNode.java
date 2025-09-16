package androidUtils.swing.tree;

import java.io.Serializable;
import java.util.*;

public class DefaultMutableTreeNode implements Cloneable, MutableTreeNode, Serializable {

    private MutableTreeNode parent;
    protected final Vector<TreeNode> children = new Vector<>();
    private boolean allowsChildren;
    private Object userObject;

    public DefaultMutableTreeNode() {
        this(null, true);
    }

    public DefaultMutableTreeNode(Object userObject) {
        this(userObject, true);
    }

    public DefaultMutableTreeNode(Object userObject, boolean allowsChildren) {
        this.userObject = userObject;
        this.allowsChildren = allowsChildren;
    }

    @Override
    public Object clone() {
        try {
            DefaultMutableTreeNode copy = (DefaultMutableTreeNode) super.clone();
            copy.parent = null;
            copy.children.clear();
            for (TreeNode child : this.children) {
                MutableTreeNode childClone = (MutableTreeNode) ((DefaultMutableTreeNode) child).clone();
                childClone.setParent(copy);
                copy.children.add(childClone);
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }

    @Override
    public String toString() {
        return userObject != null ? userObject.toString() : "null";
    }

    @Override
    public void add(MutableTreeNode child) {
        insert(child, getChildCount());
    }

    @Override
    public void insert(MutableTreeNode node, int index) {
        if (!allowsChildren)
            throw new IllegalStateException("Node does not allow children");

        if (node == null)
            throw new NullPointerException("Child is null");

        node.setParent(this);
        children.add(index, node);
    }

    @Override
    public void remove(int index) {
        MutableTreeNode child = (MutableTreeNode) children.remove(index);
        child.setParent(null);
    }

    @Override
    public void remove(MutableTreeNode node) {
        if (children.remove(node)) {
            node.setParent(null);
        }
    }

    @Override
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }

    @Override
    public void removeAllChildren() {
        for (TreeNode child : children) {
            ((MutableTreeNode)child).setParent(null);
        }
        children.clear();
    }

    @Override
    public TreeNode getChildAt(int index) {
        return children.get(index);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(MutableTreeNode node) {
        this.parent = node;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    @Override
    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public Object getUserObject() {
        return userObject;
    }


    public boolean isNodeAncestor(TreeNode node) {
        TreeNode current = this;
        while (current != null) {
            if (current == node) return true;
            current = current.getParent();
        }
        return false;
    }

    public boolean isNodeDescendant(DefaultMutableTreeNode node) {
        return node.isNodeAncestor(this);
    }

    public TreeNode getSharedAncestor(DefaultMutableTreeNode node) {
        Set<TreeNode> ancestors = new HashSet<>();
        TreeNode current = this;
        while (current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        current = node;
        while (current != null) {
            if (ancestors.contains(current))
                return current;
            current = current.getParent();
        }

        return null;
    }

    public boolean isNodeRelated(DefaultMutableTreeNode node) {
        return isNodeAncestor(node) || isNodeDescendant(node);
    }

    public TreeNode getRoot() {
        TreeNode current = this;
        TreeNode parent;
        while ((parent = current.getParent()) != null) {
            current = parent;
        }
        return current;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public int getLevel() {
        int level = 0;
        TreeNode current = this;
        while ((current = current.getParent()) != null) {
            level++;
        }
        return level;
    }

    public int getDepth() {
        int max = 0;
        for (TreeNode child : children) {
            if (child instanceof DefaultMutableTreeNode) {
                max = Math.max(max, ((DefaultMutableTreeNode) child).getDepth());
            }
        }
        return max + 1;
    }

    public TreeNode[] getPath() {
        List<TreeNode> list = new ArrayList<>();
        TreeNode current = this;
        while (current != null) {
            list.add(0, current);
            current = current.getParent();
        }
        return list.toArray(new TreeNode[0]);
    }

    public Object[] getUserObjectPath() {
        TreeNode[] nodes = getPath();
        Object[] path = new Object[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof DefaultMutableTreeNode) {
                path[i] = ((DefaultMutableTreeNode) nodes[i]).getUserObject();
            }
        }
        return path;
    }

    public Enumeration<TreeNode> children() {
        return Collections.enumeration(children);
    }


    public Enumeration<TreeNode> depthFirstEnumeration() {
        Vector<TreeNode> list = new Vector<>();
        collectDepthFirst(this, list);
        return list.elements();
    }

    private void collectDepthFirst(TreeNode node, Vector<TreeNode> list) {
        list.add(node);
        for (int i = 0; i < node.getChildCount(); i++) {
            collectDepthFirst(node.getChildAt(i), list);
        }
    }

    public Enumeration<TreeNode> breadthFirstEnumeration() {
        Vector<TreeNode> list = new Vector<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            list.add(node);
            for (int i = 0; i < node.getChildCount(); i++) {
                queue.add(node.getChildAt(i));
            }
        }
        return list.elements();
    }

    public Enumeration<TreeNode> preorderEnumeration() {
        return depthFirstEnumeration(); // Pre-order == DFS
    }

    public Enumeration<TreeNode> postorderEnumeration() {
        Vector<TreeNode> list = new Vector<>();
        collectPostOrder(this, list);
        return list.elements();
    }

    private void collectPostOrder(TreeNode node, Vector<TreeNode> list) {
        for (int i = 0; i < node.getChildCount(); i++) {
            collectPostOrder(node.getChildAt(i), list);
        }
        list.add(node);
    }
}
