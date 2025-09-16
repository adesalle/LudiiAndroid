package androidUtils.swing.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTreeSelectionModel implements TreeSelectionModel {

    private final List<TreeSelectionListener> listeners = new ArrayList<>();
    private final List<TreePath> selectedPaths = new ArrayList<>();
    private int selectionMode = SINGLE_TREE_SELECTION;

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
        clearSelection();
        if (path != null) {
            selectedPaths.add(path);
            fireValueChanged(path);
        }
    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {
        clearSelection();
        if (paths != null) {
            selectedPaths.addAll(Arrays.asList(paths));
            fireValueChanged(paths);
        }
    }

    @Override
    public void addSelectionPath(TreePath path) {
        if (path != null && !selectedPaths.contains(path)) {
            selectedPaths.add(path);
            fireValueChanged(path);
        }
    }

    @Override
    public void addSelectionPaths(TreePath[] paths) {
        List<TreePath> added = new ArrayList<>();
        for (TreePath path : paths) {
            if (!selectedPaths.contains(path)) {
                selectedPaths.add(path);
                added.add(path);
            }
        }
        if (!added.isEmpty()) {
            fireValueChanged(added.toArray(new TreePath[0]));
        }
    }

    @Override
    public void removeSelectionPath(TreePath path) {
        if (selectedPaths.remove(path)) {
            fireValueChanged(path);
        }
    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {
        boolean changed = false;
        for (TreePath path : paths) {
            changed |= selectedPaths.remove(path);
        }
        if (changed) {
            fireValueChanged(paths);
        }
    }

    @Override
    public TreePath getSelectionPath() {
        return selectedPaths.isEmpty() ? null : selectedPaths.get(0);
    }

    @Override
    public TreePath[] getSelectionPaths() {
        return selectedPaths.toArray(new TreePath[0]);
    }

    @Override
    public int getSelectionCount() {
        return selectedPaths.size();
    }

    @Override
    public boolean isPathSelected(TreePath path) {
        return selectedPaths.contains(path);
    }

    @Override
    public void clearSelection() {
        if (!selectedPaths.isEmpty()) {
            TreePath[] removed = getSelectionPaths();
            selectedPaths.clear();
            fireValueChanged(removed);
        }
    }

    @Override
    public void addTreeSelectionListener(TreeSelectionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTreeSelectionListener(TreeSelectionListener listener) {
        listeners.remove(listener);
    }

    private void fireValueChanged(TreePath... changedPaths) {
        TreeSelectionEvent event = new TreeSelectionEvent(this, changedPaths);
        for (TreeSelectionListener l : listeners) {
            l.valueChanged(event);
        }
    }
}
