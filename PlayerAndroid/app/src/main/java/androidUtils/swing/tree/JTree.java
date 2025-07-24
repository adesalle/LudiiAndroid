package androidUtils.swing.tree;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.FocusEvent;
import androidUtils.awt.event.FocusListener;
import androidUtils.awt.event.KeyEvent;
import androidUtils.awt.event.KeyListener;
import androidUtils.swing.ViewComponent;
import androidUtils.swing.text.Position;
import playerAndroid.app.StartAndroidApp;

public class JTree extends ExpandableListView implements ViewComponent {

    private TreeModel model;
    private TreeSelectionModel selectionModel;
    private TreeCellRenderer cellRenderer;
    private boolean rootVisible = true;
    private boolean showsRootHandles = true;
    private boolean editable = false;
    private TreeCellEditor cellEditor;
    private Font currentFont;

    public JTree(TreeNode root) {
        this(new DefaultTreeModel(root, false));
    }

    public JTree(TreeModel model) {
        this(model, StartAndroidApp.getAppContext());
    }

    public JTree(TreeNode root, boolean asksAllowChildren) {
        this(new DefaultTreeModel(root, asksAllowChildren));
    }

    private JTree(TreeModel model, Context context) {
        super(context);
        this.model = model;
        this.selectionModel = new DefaultTreeSelectionModel();
        this.cellRenderer = new DefaultTreeCellRenderer(context);
        initAdapter();
    }

    private void initAdapter() {
        setAdapter(new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                if (model == null || model.getRoot() == null) return 0;
                if (!rootVisible) {
                    return model.getChildCount(model.getRoot());
                }
                return 1;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                TreeNode group = getGroupNode(groupPosition);
                return group != null ? group.getChildCount() : 0;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return getGroupNode(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                TreeNode group = getGroupNode(groupPosition);
                return group != null ? group.getChildAt(childPosition) : null;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent) {
                TreeNode node = getGroupNode(groupPosition);
                if (node == null) return new TextView(getContext());

                if (cellRenderer != null) {
                    return cellRenderer.getTreeCellView(JTree.this, node,
                            selectionModel.isPathSelected(new TreePath(node)),
                            isExpanded, node.isLeaf(), 0);
                }

                return createTextView(node.toString(), 18, Color.BLACK, 50);
            }

            @Override
            public View getChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
                TreeNode node = getChildNode(groupPosition, childPosition);
                if (node == null) return new TextView(getContext());

                TreePath path = buildTreePath(groupPosition, childPosition);
                if (cellRenderer != null) {
                    return cellRenderer.getTreeCellView(JTree.this, node,
                            selectionModel.isPathSelected(path),
                            isExpanded(path), node.isLeaf(), 1);
                }

                return createTextView(node.toString(), 16, Color.DKGRAY, 100);
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            private TreeNode getGroupNode(int groupPosition) {
                if (model == null || model.getRoot() == null) return null;
                if (!rootVisible) {
                    return model.getChild(model.getRoot(), groupPosition);
                }
                return (TreeNode) model.getRoot();
            }

            private TreeNode getChildNode(int groupPosition, int childPosition) {
                TreeNode group = getGroupNode(groupPosition);
                return group != null ? group.getChildAt(childPosition) : null;
            }

            private TreePath buildTreePath(int groupPosition, int childPosition) {
                TreeNode group = getGroupNode(groupPosition);
                TreeNode child = getChildNode(groupPosition, childPosition);

                if (!rootVisible) {
                    return new TreePath(new TreeNode[]{model.getRoot(), group, child});
                } else {
                    return new TreePath(new TreeNode[]{group, child});
                }
            }
        });
    }

    private TextView createTextView(String text, int textSize, int color, int paddingLeft) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(color);
        textView.setPadding(paddingLeft, 20, 20, 20);

        if (currentFont != null) {
            textView.setTypeface(currentFont.getFont());
            textView.setTextSize(currentFont.getSize());
        }

        return textView;
    }

    public TreePath getSelectionPath() {
        return selectionModel.getSelectionPath();
    }

    public void setSelectionPath(TreePath path) {
        selectionModel.setSelectionPath(path);
        if (path != null) {
            expandPath(path);
        }
    }

    public void expandPath(TreePath path) {
        if (path != null) {
            Object[] pathArray = path.getPath();
            int groupPos = findGroupPosition(pathArray[0]);

            if (groupPos >= 0) {
                expandGroup(groupPos);

                if (pathArray.length > 1) {
                    int childPos = findChildPosition(groupPos, (TreeNode) pathArray[1]);
                    if (childPos >= 0) {
                        setSelectedChild(groupPos, childPos, true);
                    }
                }
            }
        }
    }

    public void collapsePath(TreePath path) {
        if (path != null) {
            Object[] pathArray = path.getPath();
            int groupPos = findGroupPosition(pathArray[0]);
            if (groupPos >= 0) {
                collapseGroup(groupPos);
            }
        }
    }

    private int findGroupPosition(Object node) {
        for (int i = 0; i < getExpandableListAdapter().getGroupCount(); i++) {
            if (getExpandableListAdapter().getGroup(i).equals(node)) {
                return i;
            }
        }
        return -1;
    }

    private int findChildPosition(int groupPosition, TreeNode childNode) {
        for (int i = 0; i < getExpandableListAdapter().getChildrenCount(groupPosition); i++) {
            if (getExpandableListAdapter().getChild(groupPosition, i).equals(childNode)) {
                return i;
            }
        }
        return -1;
    }

    public TreeModel getModel() {
        return model;
    }

    public void setModel(TreeModel newModel) {
        this.model = newModel;
        ((BaseExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
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
        ((BaseExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
    }

    public boolean isRootVisible() {
        return rootVisible;
    }

    public void setRootVisible(boolean visible) {
        this.rootVisible = visible;
        ((BaseExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
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

    @Override
    public Dimension getPreferredSize() {
        int width = measureMaxTextWidth() + getPaddingLeft() + getPaddingRight();
        int height = calculateTotalHeight();
        return new Dimension(width, height);
    }

    private int measureMaxTextWidth() {
        int maxWidth = 0;
        Paint paint = new Paint();
        paint.setTextSize(getFont().getSize());

        for (int i = 0; i < model.getChildCount(getModel().getRoot()); i++) {
            TreeNode group = model.getChild(getModel().getRoot(), i);
            maxWidth = getWidthGroup(group, paint, maxWidth);
        }

        return maxWidth + 100;
    }

    private int getWidthGroup(TreeNode group, Paint paint, int maxWidth) {
        if (group.isLeaf()) return Math.max(maxWidth, (int) paint.measureText(group.toString()));

        for (int i = 0; i < model.getChildCount(group); i++) {
            TreeNode child = model.getChild(group, i);
            maxWidth = getWidthGroup(child, paint, maxWidth);
        }
        return maxWidth;
    }

    private int calculateTotalHeight() {
        int totalHeight = 0;
        BaseExpandableListAdapter adapter = (BaseExpandableListAdapter) getExpandableListAdapter();

        for (int i = 0; i < adapter.getGroupCount(); i++) {
            View groupView = adapter.getGroupView(i, false, null, this);
            groupView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );
            totalHeight += groupView.getMeasuredHeight();

            if (isGroupExpanded(i)) {
                for (int j = 0; j < adapter.getChildrenCount(i); j++) {
                    View childView = adapter.getChildView(i, j, false, null, this);
                    childView.measure(
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    );
                    totalHeight += childView.getMeasuredHeight();
                }
            }
        }

        int dividerHeight = getDividerHeight();
        totalHeight += dividerHeight * (adapter.getGroupCount() - 1);
        totalHeight += getPaddingTop() + getPaddingBottom();

        return totalHeight;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
        requestLayout();
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        this.currentFont = font;
        if (cellRenderer instanceof DefaultTreeCellRenderer) {
            ((DefaultTreeCellRenderer)cellRenderer).setFont(font);
        }
        ((BaseExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
    }

    @Override
    public Font getFont() {
        if (getChildCount() > 0) {
            View firstChild = getChildAt(0);
            if (firstChild instanceof TextView) {
                TextView tv = (TextView) firstChild;
                return new Font(tv.getTypeface(), (int) tv.getTextSize());
            }
        }
        return new Font(Typeface.DEFAULT, 14);
    }

    public int getRowCount() {
        BaseExpandableListAdapter adapter = (BaseExpandableListAdapter) getExpandableListAdapter();
        int count = adapter.getGroupCount();

        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (isGroupExpanded(i)) {
                count += adapter.getChildrenCount(i);
            }
        }

        return count;
    }

    public TreePath getPathForRow(int row) {
        BaseExpandableListAdapter adapter = (BaseExpandableListAdapter) getExpandableListAdapter();
        int currentRow = 0;

        for (int groupPos = 0; groupPos < adapter.getGroupCount(); groupPos++) {
            if (currentRow == row) {
                return new TreePath(new TreeNode[]{(TreeNode) adapter.getGroup(groupPos)});
            }
            currentRow++;

            if (isGroupExpanded(groupPos)) {
                for (int childPos = 0; childPos < adapter.getChildrenCount(groupPos); childPos++) {
                    if (currentRow == row) {
                        return new TreePath(new TreeNode[]{
                                (TreeNode) adapter.getGroup(groupPos),
                                (TreeNode) adapter.getChild(groupPos, childPos)
                        });
                    }
                    currentRow++;
                }
            }
        }

        return null;
    }

    public int getRowForPath(TreePath path) {
        if (path == null) return -1;

        BaseExpandableListAdapter adapter = (BaseExpandableListAdapter) getExpandableListAdapter();
        int row = 0;

        for (int groupPos = 0; groupPos < adapter.getGroupCount(); groupPos++) {
            TreeNode group = (TreeNode) adapter.getGroup(groupPos);
            if (path.getPath().length > 0 && path.getPath()[0].equals(group)) {
                if (path.getPath().length == 1) {
                    return row;
                }
            }
            row++;

            if (isGroupExpanded(groupPos)) {
                for (int childPos = 0; childPos < adapter.getChildrenCount(groupPos); childPos++) {
                    TreeNode child = (TreeNode) adapter.getChild(groupPos, childPos);
                    if (path.getPath().length > 1 && path.getPath()[1].equals(child)) {
                        return row;
                    }
                    row++;
                }
            }
        }

        return -1;
    }

    public int getLeadSelectionRow() {
        TreePath path = getSelectionPath();
        return path != null ? getRowForPath(path) : -1;
    }

    public void setSelectionRow(int row) {
        TreePath path = getPathForRow(row);
        if (path != null) {
            setSelectionPath(path);
        }
    }

    public void addMouseListener(Object mouseListener) {
        setOnItemClickListener((parent, view, position, id) -> {
            // Implémentation à compléter selon les besoins
        });
    }

    public boolean hasFocus() {
        return super.hasFocus();
    }

    public void addKeyListener(KeyListener listener) {
        setOnKeyListener((v, keyCode, event) -> {
            listener.keyPressed(new KeyEvent(event, keyCode));
            return false;
        });
    }

    public void addFocusListener(FocusListener listener) {
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                listener.focusGained(new FocusEvent(this));
            } else {
                listener.focusLost(new FocusEvent(this));
            }
        });
    }

    public void scrollRowToVisible(int row) {
        TreePath path = getPathForRow(row);
        if (path != null) {
            expandPath(path);
            smoothScrollToPosition(row);
        }
    }

    public void revalidate() {
        invalidate();
    }

    public void expandRow(int row) {
        TreePath path = getPathForRow(row);
        if (path != null) {
            expandPath(path);
        }
    }

    public TreePath getNextMatch(final String prefix, final int startingRow, final Position.Bias bias) {
        if (prefix == null || startingRow < 0) {
            return null;
        }

        String searchStr = prefix.toLowerCase();
        int increment = (bias == Position.Bias.Forward) ? 1 : -1;
        int max = getRowCount();
        int row = startingRow;

        do {
            TreePath path = getPathForRow(row);
            if (path != null) {
                TreeNode node = (TreeNode) path.getLastPathComponent();
                if (node.toString().toLowerCase().startsWith(searchStr)) {
                    return path;
                }
            }
            row = (row + increment + max) % max;
        } while (row != startingRow);

        return null;
    }

    public TreePath getPathForLocation(int x, int y) {

        return null;
    }

    private boolean isExpanded(TreePath path) {
        if (path == null) return false;
        Object[] pathArray = path.getPath();
        if (pathArray.length < 2) return false;

        TreeNode node = (TreeNode) pathArray[pathArray.length - 2];
        int groupPos = findGroupPosition(node);
        return groupPos >= 0 && isGroupExpanded(groupPos);
    }
}