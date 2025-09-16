package androidUtils.swing.tree;

public interface TreeSelectionModel {
    int SINGLE_TREE_SELECTION = 1;
    int CONTIGUOUS_TREE_SELECTION = 2;
    int DISCONTIGUOUS_TREE_SELECTION = 4;

    void setSelectionMode(int mode);
    int getSelectionMode();

    void setSelectionPath(TreePath path);
    void setSelectionPaths(TreePath[] paths);
    void addSelectionPath(TreePath path);
    void addSelectionPaths(TreePath[] paths);
    void removeSelectionPath(TreePath path);
    void removeSelectionPaths(TreePath[] paths);

    TreePath getSelectionPath();
    TreePath[] getSelectionPaths();
    int getSelectionCount();

    boolean isPathSelected(TreePath path);
    void clearSelection();

    void addTreeSelectionListener(TreeSelectionListener listener);
    void removeTreeSelectionListener(TreeSelectionListener listener);
}
