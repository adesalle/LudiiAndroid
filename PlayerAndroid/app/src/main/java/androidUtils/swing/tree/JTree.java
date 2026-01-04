package androidUtils.swing.tree;

import android.content.Context;
import android.view.View;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.FocusListener;
import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.MouseAdapter;
import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.event.TreeModelListener;
import androidUtils.swing.text.Position;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.display.dialogs.GameLoaderDialog;

public class JTree extends AndroidTreeView {

    private TreeModel model;
    private TreeSelectionModel selectionModel;
    private TreeCellRenderer cellRenderer;
    private TreeCellEditor cellEditor;
    private boolean rootVisible = true;
    private boolean editable = false;
    private Font font;
    private Dimension preferredSize;
    private final List<TreeNode> nodeList = new ArrayList<>();

    private final Map<Object, androidUtils.swing.tree.TreeNode> nodeMapping = new HashMap<>();


    public JTree(androidUtils.swing.tree.TreeNode root) {
        this(new DefaultTreeModel(root));
    }

    public JTree(TreeModel model) {
        super(StartAndroidApp.getAppContext());
        setRoot(convertToAndroidTreeNode(model.getRoot()));

        this.model = model;
        this.selectionModel = new DefaultTreeSelectionModel();
        this.cellRenderer = new DefaultTreeCellRenderer();
        this.cellEditor = new DefaultTreeCellEditor();

        setDefaultAnimation(true);
        rebuildTree();
    }

    private TreeNode convertToAndroidTreeNode(androidUtils.swing.tree.TreeNode swingNode) {
        TreeNode androidNode = new TreeNode(swingNode.getUserObject());

        androidNode.setViewHolder(new StyledNodeViewHolder(StartAndroidApp.getAppContext()));
        for (int i = 0; i < swingNode.getChildCount(); i++) {
            TreeNode child = convertToAndroidTreeNode(swingNode.getChildAt(i));
            androidNode.addChild(child);
        }


        nodeMapping.put(androidNode.getValue(), swingNode);
        return androidNode;
    }

    public void rebuildTree() {
        TreeNode root = convertToAndroidTreeNode(model.getRoot());
        nodeMapping.put(root.getPath(), model.getRoot());
        setRoot(root);
        nodeList.clear();
        populateNodeList(root);
    }

    private void populateNodeList(TreeNode node) {
        nodeList.add(node);
        for (TreeNode child : node.getChildren()) {
            populateNodeList(child);
        }
    }

    public TreePath getSelectionPath() {
        return selectionModel.getSelectionPath();
    }

    public void setSelectionPath(TreePath path) {
        selectionModel.setSelectionPath(path);
    }

    public void expandPath(TreePath path) {
        TreeNode node = findAndroidNode(path);
        expandNode(node);

    }


    public void collapsePath(TreePath path) {
        collapseNode(findAndroidNode(path));
    }

    private TreeNode findAndroidNode(TreePath path) {
        Object[] nodes = path.getPath();
        androidUtils.swing.tree.TreeNode current = model.getRoot();
        for (int i = 1; i < nodes.length; i++) {
            Object target = nodes[i];
            boolean found = false;

            for (int j = 0; j < current.getChildCount(); j++) {
                androidUtils.swing.tree.TreeNode child = current.getChildAt(j);

                if (child.getUserObject().toString().equals(target.toString())) {
                    current = child;
                    found = true;
                    break;
                }
            }
            if (!found) return null;
        }
        return convertToAndroidTreeNode(current);
    }

    public TreeModel getModel() {
        return model;
    }

    public void setModel(TreeModel newModel) {
        this.model = newModel;
        rebuildTree();
    }

    public TreeSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void setSelectionModel(TreeSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
    }

    public TreeCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(TreeCellRenderer renderer) {
        this.cellRenderer = renderer;
        rebuildTree();
    }

    public boolean isRootVisible() {
        return rootVisible;
    }

    public void setRootVisible(boolean visible) {
        this.rootVisible = visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public TreeCellEditor getCellEditor() {
        return cellEditor;
    }

    public void setCellEditor(TreeCellEditor editor) {
        this.cellEditor = editor;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
    }

    public void setSize(Dimension dimension) {
        setPreferredSize(dimension);
    }

    public Dimension getSize() {
        return preferredSize;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getRowCount() {
        return nodeList.size();
    }

    public TreePath getPathForRow(int row) {
        if (row < 0 || row >= nodeList.size()) return null;
        TreeNode node = nodeList.get(row);
        List<Object> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.getValue());
            node = node.getParent();
        }
        return new TreePath(path.toArray());
    }


    public int getRowForPath(TreePath path) {
        TreeNode node = findAndroidNode(path);
        return nodeList.indexOf(node);
    }

    public void setSelectionRow(int row) {
        TreePath path = getPathForRow(row);
        setSelectionPath(path);
    }

    public int getLeadSelectionRow() {
        TreePath path = getSelectionPath();
        return getRowForPath(path);
    }

    public void addMouseListener(MouseAdapter mouseListener) {
        for (TreeNode node: nodeList) {
            node.setClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, Object value) {
                    View nodeView = node.getViewHolder().getView();

                    // Crée un faux MotionEvent
                    long time = System.currentTimeMillis();
                    float x = nodeView.getX();
                    float y = nodeView.getY();

                    android.view.MotionEvent motionEvent = android.view.MotionEvent.obtain(
                            time, time, android.view.MotionEvent.ACTION_UP, x, y, 0
                    );

                    MouseEvent swingEvent = new MouseEvent(motionEvent, nodeView, MouseEvent.MOUSE_CLICKED);

                    // Déclenche le listener Swing-style
                    mouseListener.mouseClicked(swingEvent);
                }
            });
        }
    }

    public boolean hasFocus() {
        return getView().hasFocus();
    }

    public void addKeyListener(KeyListener listener) {
        // Implémentation spécifique à Android
    }

    public void addFocusListener(FocusListener listener) {
        // Implémentation spécifique à Android
    }

    public void scrollRowToVisible(int row) {
        // Implémentation spécifique à Android
    }

    public void revalidate() {
        rebuildTree();
    }

    public void expandRow(int row) {
        expandPath(getPathForRow(row));
    }

    public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
        for (int i = startingRow; i < nodeList.size(); i++) {
            TreeNode node = nodeList.get(i);
            String s = node.getValue().toString();
            if (s.startsWith(prefix)) {
                return getPathForRow(i);
            }
        }
        return null;
    }



    public TreePath getPathForLocation(int x, int y) {
        for (TreeNode node : nodeList) {
            if(node.getViewHolder() != null && node.getViewHolder().getNodeView() != null)
            {
                View view = node.getViewHolder().getView();
                if (view != null && view.getVisibility() == View.VISIBLE) {
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    int viewX = location[0];
                    int viewY = location[1];
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();

                    if (x >= viewX && x <= viewX + viewWidth &&
                            y >= viewY && y <= viewY + viewHeight) {

                        // On construit le TreePath à partir du nœud cliqué
                        List<Object> pathList = new ArrayList<>();
                        TreeNode current = node;
                        androidUtils.swing.tree.TreeNode currentS;
                        while (current != null)
                        {
                            currentS = nodeMapping.get(current.getValue());
                            pathList.add(0, currentS);
                            current = current.getParent();

                        }
                        return new TreePath(pathList.toArray());
                    }
                }
            }

        }
        return null;
    }

    public TreePath getPathForLocation(View view) {
        for (TreeNode node : nodeList) {
            if (node.getViewHolder() != null && node.getViewHolder().getNodeView() != null && node.getViewHolder().getView() == view) {
                // Construire le chemin depuis ce noeud jusqu'à la racine
                List<Object> pathList = new ArrayList<>();
                TreeNode current = node;

                androidUtils.swing.tree.TreeNode currentS;
                while (current != null)
                {
                    currentS = nodeMapping.get(current.getValue());
                    pathList.add(0, currentS);
                    current = current.getParent();

                }
                if (pathList.size() == 0)return null;
                return new TreePath(pathList.toArray());
            }
        }
        return null;
    }

    public void requestFocus() {
        getView().requestFocus();
    }

}