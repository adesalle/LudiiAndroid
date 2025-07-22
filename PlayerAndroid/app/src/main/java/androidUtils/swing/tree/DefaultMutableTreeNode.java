package androidUtils.swing.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;

import androidUtils.swing.MutableTreeNode;


public class DefaultMutableTreeNode implements Cloneable, MutableTreeNode, Serializable {
    private static final long serialVersionUID = -4298474751201349152L;

    /**
     * An empty enumeration, returned by {@link #children()} if a node has no children.
     */
    public static final Enumeration<TreeNode> EMPTY_ENUMERATION = new EmptyEnumeration();

    protected MutableTreeNode parent;
    protected Vector<MutableTreeNode> children = new Vector<>();
    protected transient Object userObject;
    protected boolean allowsChildren;

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
        return new DefaultMutableTreeNode(this.userObject, this.allowsChildren);
    }

    @Override
    public String toString() {
        return userObject == null ? null : userObject.toString();
    }

    public void add(MutableTreeNode child) {
        if (!allowsChildren)
            throw new IllegalStateException("Node does not allow children");

        if (child == null)
            throw new IllegalArgumentException("Child cannot be null");

        if (isNodeAncestor(child))
            throw new IllegalArgumentException("Cannot add ancestor as child");

        children.add(child);
        child.setParent(this);
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void remove(int index) {
        MutableTreeNode child = children.remove(index);
        child.setParent(null);
    }

    @Override
    public void remove(MutableTreeNode node) {
        if (node == null)
            throw new IllegalArgumentException("Node cannot be null");
        if (node.getParent() != this)
            throw new IllegalArgumentException("Node is not a child of this node");

        children.remove(node);
        node.setParent(null);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    @Override
    public void insert(MutableTreeNode node, int index) {
        if (!allowsChildren)
            throw new IllegalStateException("Node does not allow children");

        if (node == null)
            throw new IllegalArgumentException("Node cannot be null");

        if (isNodeAncestor(node))
            throw new IllegalArgumentException("Cannot insert ancestor node");

        children.insertElementAt(node, index);
        node.setParent(this);
    }

    public TreeNode[] getPath() {
        return getPathToRoot(this, 0);
    }

    @Override
    public Enumeration<TreeNode> children() {
        return children.size() == 0 ? EMPTY_ENUMERATION : new VectorEnumeration(children);
    }

    @Override
    public Enumeration<?> depthFirstEnumeration() {
        return new DepthFirstEnumeration(this);
    }

    private static final class DepthFirstEnumeration implements Enumeration<TreeNode> {
        private final Stack<Enumeration<TreeNode>> stack = new Stack<>();

        DepthFirstEnumeration(TreeNode rootNode) {
            super();
            Vector<TreeNode> v = new Vector<>(1);
            v.addElement(rootNode);
            stack.push(v.elements());
        }

        @Override
        public boolean hasMoreElements() {
            return (!stack.empty() && stack.peek().hasMoreElements());
        }

        @Override
        public TreeNode nextElement() {
            Enumeration<TreeNode> enumer = stack.peek();
            TreeNode node = enumer.nextElement();
            Enumeration<TreeNode> children = node.children();

            if (!enumer.hasMoreElements()) {
                stack.pop();
            }
            if (children.hasMoreElements()) {
                stack.push(children);
            }
            return node;
        }
    }

    public Enumeration<?> preorderEnumeration() {
        return new PreorderEnumeration(this);
    }

    private static final class PreorderEnumeration implements Enumeration<TreeNode> {
        private final Stack<Enumeration<TreeNode>> stack = new Stack<>();

        PreorderEnumeration(TreeNode rootNode) {
            super();
            Vector<TreeNode> v = new Vector<>(1);
            v.addElement(rootNode);
            stack.push(v.elements());
        }

        @Override
        public boolean hasMoreElements() {
            return (!stack.empty() && stack.peek().hasMoreElements());
        }

        @Override
        public TreeNode nextElement() {
            Enumeration<TreeNode> enumer = stack.peek();
            TreeNode node = enumer.nextElement();
            Enumeration<TreeNode> children = node.children();

            if (!enumer.hasMoreElements()) {
                stack.pop();
            }
            if (children.hasMoreElements()) {
                stack.push(children);
            }
            return node;
        }
    }

    public Enumeration<?> postorderEnumeration() {
        return new PostorderEnumeration(this);
    }

    private static final class PostorderEnumeration implements Enumeration<TreeNode> {
        protected TreeNode root;
        protected Enumeration<TreeNode> children;
        protected Enumeration<TreeNode> subtree;

        PostorderEnumeration(TreeNode rootNode) {
            super();
            root = rootNode;
            children = root.children();
            subtree = EMPTY_ENUMERATION;
        }

        @Override
        public boolean hasMoreElements() {
            return root != null;
        }

        @Override
        public TreeNode nextElement() {
            TreeNode retval;

            if (subtree.hasMoreElements()) {
                retval = subtree.nextElement();
            } else if (children.hasMoreElements()) {
                subtree = new PostorderEnumeration(children.nextElement());
                retval = subtree.nextElement();
            } else {
                retval = root;
                root = null;
            }

            return retval;
        }
    }


    @Override
    public void setParent(MutableTreeNode node) {
        parent = node;
    }

    @Override
    public TreeNode getChildAt(int index) {
        return children.elementAt(index);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        if (node == null)
            throw new IllegalArgumentException("Node cannot be null");
        return children.indexOf(node);
    }

    public void setAllowsChildren(boolean allowsChildren) {
        if (!allowsChildren)
            removeAllChildren();
        this.allowsChildren = allowsChildren;
    }

    @Override
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    @Override
    public boolean isLeaf() {
        return !getAllowsChildren() && getChildCount() == 0;
    }

    @Override
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public Object getUserObject() {
        return userObject;
    }

    @Override
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }

    public void removeAllChildren() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            remove(i);
        }
    }

    public boolean isNodeAncestor(TreeNode node) {
        if (node == null) return false;

        TreeNode current = this;
        while (current != null && current != node) {
            current = current.getParent();
        }
        return current == node;
    }

    public boolean isNodeDescendant(DefaultMutableTreeNode node) {
        if (node == null) return false;

        TreeNode current = node;
        while (current != null && current != this) {
            current = current.getParent();
        }
        return current == this;
    }

    public TreeNode getSharedAncestor(DefaultMutableTreeNode node) {
        if (node == null) return null;

        ArrayList<TreeNode> path = new ArrayList<>();
        TreeNode current = this;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        current = node;
        while (current != null) {
            if (path.contains(current)) {
                return current;
            }
            current = current.getParent();
        }

        return null;
    }

    public boolean isNodeRelated(DefaultMutableTreeNode node) {
        return node != null && node.getRoot() == getRoot();
    }

    public int getDepth() {
        if (!allowsChildren || children.isEmpty()) {
            return 0;
        }

        int depth = 0;
        Enumeration<TreeNode> e = breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            TreeNode node = e.nextElement();
            int level = getLevel(node);
            if (level > depth) {
                depth = level;
            }
        }
        return depth;
    }


    public int getLevel() {
        return getLevel(this);
    }

    private int getLevel(TreeNode node) {
        int level = 0;
        TreeNode current = node;
        while ((current = current.getParent()) != null) {
            level++;
        }
        return level;
    }

    protected TreeNode[] getPathToRoot(TreeNode node, int depth) {
        if (node == null) {
            return depth == 0 ? null : new TreeNode[depth];
        }

        TreeNode[] path = getPathToRoot(node.getParent(), depth + 1);
        if (path != null) {
            path[path.length - depth - 1] = node;
        }
        return path;
    }

    public Object[] getUserObjectPath() {
        TreeNode[] path = getPath();
        Object[] result = new Object[path.length];
        for (int i = 0; i < path.length; i++) {
            result[i] = ((DefaultMutableTreeNode) path[i]).getUserObject();
        }
        return result;
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

    public Enumeration<TreeNode> breadthFirstEnumeration() {
        return new BreadthFirstEnumeration(this);
    }

    private static class EmptyEnumeration implements Enumeration<TreeNode> {
        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public TreeNode nextElement() {
            throw new NoSuchElementException("No more elements");
        }
    }

    private static class VectorEnumeration implements Enumeration<TreeNode> {
        private final Enumeration<MutableTreeNode> enumeration;

        public VectorEnumeration(Vector<MutableTreeNode> v) {
            this.enumeration = v.elements();
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public TreeNode nextElement() {
            return enumeration.nextElement();
        }
    }

    private static final class BreadthFirstEnumeration implements Enumeration<TreeNode> {
        private final LinkedList<TreeNode> queue = new LinkedList<>();

        BreadthFirstEnumeration(TreeNode rootNode) {
            queue.add(rootNode);
        }

        @Override
        public boolean hasMoreElements() {
            return !queue.isEmpty();
        }

        @Override
        public TreeNode nextElement() {
            if (queue.isEmpty()) {
                throw new NoSuchElementException("No more elements");
            }

            TreeNode node = queue.removeFirst();

            Enumeration<? extends TreeNode> children = node.children();
            while (children.hasMoreElements()) {
                queue.add(children.nextElement());
            }

            return node;
        }
    }
}