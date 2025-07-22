package androidUtils.swing.event;


import androidUtils.swing.tree.TreePath;

/**
 * Encapsulates information describing changes to a tree model.
 */
public class TreeModelEvent extends java.util.EventObject {
    protected TreePath path;
    protected int[] childIndices;
    protected Object[] children;

    /**
     * Creates a TreeModelEvent with the specified source and path.
     */
    public TreeModelEvent(Object source, TreePath path) {
        this(source, path, null, null);
    }

    /**
     * Creates a TreeModelEvent with the specified source, path, child indices,
     * and children.
     */
    public TreeModelEvent(Object source, TreePath path,
                          int[] childIndices, Object[] children) {
        super(source);
        this.path = path;
        this.childIndices = childIndices;
        this.children = children;
    }

    /**
     * Returns the path to the parent of the changed nodes.
     */
    public TreePath getTreePath() {
        return path;
    }

    /**
     * Returns the indices of the changed elements.
     */
    public int[] getChildIndices() {
        if (childIndices != null) {
            int[] returnValue = new int[childIndices.length];
            System.arraycopy(childIndices, 0, returnValue, 0, childIndices.length);
            return returnValue;
        }
        return null;
    }

    /**
     * Returns the objects representing the changed elements.
     */
    public Object[] getChildren() {
        if (children != null) {
            Object[] returnValue = new Object[children.length];
            System.arraycopy(children, 0, returnValue, 0, children.length);
            return returnValue;
        }
        return null;
    }
}