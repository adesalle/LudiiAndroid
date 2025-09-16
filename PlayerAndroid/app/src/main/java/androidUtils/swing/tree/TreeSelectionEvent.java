package androidUtils.swing.tree;

public class TreeSelectionEvent {
    private final Object source;
    private final TreePath[] paths;

    public TreeSelectionEvent(Object source, TreePath... paths) {
        this.source = source;
        this.paths = paths;
    }

    public Object getSource() {
        return source;
    }

    public TreePath[] getPaths() {
        return paths;
    }
}
