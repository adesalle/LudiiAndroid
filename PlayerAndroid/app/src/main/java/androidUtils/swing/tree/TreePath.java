package androidUtils.swing.tree;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class TreePath implements Serializable {
    private final Object[] path;

    public TreePath(Object singlePath) {
        this.path = new Object[]{singlePath};
    }

    public TreePath(Object[] path) {
        if (path == null || path.length == 0) this.path = null;
        else this.path = Arrays.copyOf(path, path.length);
    }

    public TreePath(TreePath parent, Object lastPathComponent) {
        if (parent == null) {
            this.path = new Object[]{lastPathComponent};
        } else {
            Object[] parentPath = parent.getPath();
            this.path = Arrays.copyOf(parentPath, parentPath.length + 1);
            this.path[parentPath.length] = lastPathComponent;
        }
    }

    public Object[] getPath() {
        return Arrays.copyOf(path, path.length);
    }

    public Object getLastPathComponent() {
        if(path == null || path.length == 0)return null;
        return path[path.length - 1];
    }

    public int getPathCount() {
        return path.length;
    }

    public Object getPathComponent(int index) {
        return path[index];
    }

    public TreePath getParentPath() {
        if (path.length <= 1) {
            return null;
        }
        Object[] parentPath = Arrays.copyOf(path, path.length - 1);
        return new TreePath(parentPath);
    }

    public boolean isDescendant(TreePath potentialAncestor) {
        if (potentialAncestor == null) return false;
        if (potentialAncestor.path.length > this.path.length) return false;

        for (int i = 0; i < potentialAncestor.path.length; i++) {
            if (!Objects.equals(this.path[i], potentialAncestor.path[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TreePath)) return false;
        TreePath other = (TreePath) o;
        return Arrays.equals(this.path, other.path);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(path);
    }

    @Override
    public String toString() {
        return Arrays.toString(path);
    }
}
