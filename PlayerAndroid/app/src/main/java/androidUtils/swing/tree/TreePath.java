
package androidUtils.swing.tree;

public class TreePath {
    private TreeNode[] path;

    public TreePath(TreeNode[] path) {
        this.path = path;
    }

    public TreePath(TreeNode path) {
        this.path = new TreeNode[1];
        this.path[0] = path;
    }

    public TreeNode getLastPathComponent() {
        return path[path.length-1];
    }

    public TreeNode[] getPath() {
        return path;
    }
}
