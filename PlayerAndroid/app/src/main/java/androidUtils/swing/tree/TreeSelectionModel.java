package androidUtils.swing.tree;

import java.util.Enumeration;

public interface TreeSelectionModel {
    // Modes de sélection
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
    boolean isSelectionEmpty();

    void clearSelection();

    void addTreeSelectionListener(TreeSelectionListener listener);
    void removeTreeSelectionListener(TreeSelectionListener listener);

    Enumeration<TreePath> getSelectionPathsEnumeration();
}