package androidUtils.swing.tree;

public class TreeSelectionEvent {
    private Object source;
    private TreePath[] paths;
    private TreePath[] oldPaths;
    private TreePath leadPath;

    public TreeSelectionEvent(Object source, TreePath[] paths,
                              TreePath[] oldPaths, TreePath leadPath) {
        this.source = source;
        this.paths = paths;
        this.oldPaths = oldPaths;
        this.leadPath = leadPath;
    }

    public Object getSource() {
        return source;
    }

    public TreePath[] getPaths() {
        return paths;
    }

    public TreePath[] getOldPaths() {
        return oldPaths;
    }

    public TreePath getLeadSelectionPath() {
        return leadPath;
    }
}