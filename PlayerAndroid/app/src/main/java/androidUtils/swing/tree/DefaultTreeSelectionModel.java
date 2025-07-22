package androidUtils.swing.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class DefaultTreeSelectionModel implements TreeSelectionModel {
    private int selectionMode = SINGLE_TREE_SELECTION;
    private List<TreePath> selectionPaths = new ArrayList<>();
    private List<TreeSelectionListener> listeners = new ArrayList<>();
    private TreePath leadPath;

    @Override
    public void setSelectionMode(int mode) {
        if (mode != SINGLE_TREE_SELECTION &&
                mode != CONTIGUOUS_TREE_SELECTION &&
                mode != DISCONTIGUOUS_TREE_SELECTION) {
            throw new IllegalArgumentException("Invalid selection mode");
        }
        this.selectionMode = mode;
    }

    @Override
    public int getSelectionMode() {
        return selectionMode;
    }

    @Override
    public void setSelectionPath(TreePath path) {
        selectionPaths.clear();
        if (path != null) {
            selectionPaths.add(path);
            leadPath = path;
        }
        notifyListeners();
    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {
        selectionPaths.clear();
        if (paths != null && paths.length > 0) {
            if (selectionMode == SINGLE_TREE_SELECTION) {
                selectionPaths.add(paths[0]);
                leadPath = paths[0];
            } else {
                for (TreePath path : paths) {
                    if (path != null) {
                        selectionPaths.add(path);
                    }
                }
                if (paths.length > 0) {
                    leadPath = paths[paths.length - 1];
                }
            }
        }
        notifyListeners();
    }

    @Override
    public void addSelectionPath(TreePath path) {
        if (path == null) return;

        if (selectionMode == SINGLE_TREE_SELECTION) {
            setSelectionPath(path);
        } else {
            if (!selectionPaths.contains(path)) {
                selectionPaths.add(path);
                leadPath = path;
                notifyListeners();
            }
        }
    }

    @Override
    public void addSelectionPaths(TreePath[] paths) {
        if (paths == null || paths.length == 0) return;

        if (selectionMode == SINGLE_TREE_SELECTION) {
            setSelectionPath(paths[0]);
        } else {
            boolean changed = false;
            for (TreePath path : paths) {
                if (path != null && !selectionPaths.contains(path)) {
                    selectionPaths.add(path);
                    changed = true;
                    leadPath = path;
                }
            }
            if (changed) {
                notifyListeners();
            }
        }
    }

    @Override
    public void removeSelectionPath(TreePath path) {
        if (path == null) return;

        if (selectionPaths.remove(path)) {
            if (path.equals(leadPath)) {
                leadPath = selectionPaths.isEmpty() ? null : selectionPaths.get(selectionPaths.size() - 1);
            }
            notifyListeners();
        }
    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {
        if (paths == null || paths.length == 0) return;

        boolean changed = false;
        for (TreePath path : paths) {
            if (path != null && selectionPaths.remove(path)) {
                changed = true;
                if (path.equals(leadPath)) {
                    leadPath = selectionPaths.isEmpty() ? null : selectionPaths.get(selectionPaths.size() - 1);
                }
            }
        }
        if (changed) {
            notifyListeners();
        }
    }

    @Override
    public TreePath getSelectionPath() {
        return selectionPaths.isEmpty() ? null : selectionPaths.get(0);
    }

    @Override
    public TreePath[] getSelectionPaths() {
        return selectionPaths.toArray(new TreePath[0]);
    }

    @Override
    public int getSelectionCount() {
        return selectionPaths.size();
    }

    @Override
    public boolean isPathSelected(TreePath path) {
        return selectionPaths.contains(path);
    }

    @Override
    public boolean isSelectionEmpty() {
        return selectionPaths.isEmpty();
    }

    @Override
    public void clearSelection() {
        if (!selectionPaths.isEmpty()) {
            selectionPaths.clear();
            leadPath = null;
            notifyListeners();
        }
    }

    @Override
    public void addTreeSelectionListener(TreeSelectionListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeTreeSelectionListener(TreeSelectionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Enumeration<TreePath> getSelectionPathsEnumeration() {
        return new Vector<>(selectionPaths).elements();
    }

    public TreePath getLeadSelectionPath() {
        return leadPath;
    }

    private void notifyListeners() {
        TreeSelectionEvent event = new TreeSelectionEvent(
                this,
                getSelectionPaths(),
                new TreePath[0],
                leadPath
        );

        for (TreeSelectionListener listener : listeners) {
            listener.valueChanged(event);
        }
    }
}