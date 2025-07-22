package androidUtils.swing;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.border.Border;
import playerAndroid.app.StartAndroidApp;

public class JComboBox<E> extends androidx.appcompat.widget.AppCompatSpinner implements ViewComponent {
    private ComboBoxModel<E> model;
    private ListCellRenderer<? super E> renderer;
    private final List<E> items = new ArrayList<>();
    private final List<ActionListener> actionListeners = new ArrayList<>();
    private boolean opaque;
    private Rect bounds;
    private Font currentFont = new Font(Typeface.DEFAULT, 14);
    private Dimension preferredSize;
    private Color background;
    private Color foreground;
    private Border border;
    private boolean editable = false;
    private ComboBoxEditor editor;

    public JComboBox() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JComboBox(ComboBoxModel<E> model) {
        super(StartAndroidApp.getAppContext());
        this.model = model;
        init();
        updateFromModel();
    }

    public JComboBox(E[] items) {
        super(StartAndroidApp.getAppContext());
        this.items.addAll(Arrays.asList(items));
        init();
        updateAdapter();
    }

    public JComboBox(Vector<E> items) {
        super(StartAndroidApp.getAppContext());
        this.items.addAll(items);
        init();
        updateAdapter();
    }

    private void init() {
        setAdapter(new CustomAdapter(getContext(), android.R.layout.simple_spinner_item, items));
        this.bounds = new Rect(0, 0, 0, 0);
        this.editor = new BasicComboBoxEditor();

        setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fireActionEvent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    public void addActionListener(ActionListener listener) {
        if (listener != null && !actionListeners.contains(listener)) {
            actionListeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public ActionListener[] getActionListeners() {
        return actionListeners.toArray(new ActionListener[0]);
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectionChanged");
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }

    private void updateFromModel() {
        if (model != null) {
            items.clear();
            for (int i = 0; i < model.getSize(); i++) {
                items.add(model.getElementAt(i));
            }
            updateAdapter();
        }
    }

    private void updateAdapter() {
        setAdapter(new CustomAdapter(getContext(), android.R.layout.simple_spinner_item, items));
    }

    public void setModel(ComboBoxModel<E> model) {
        this.model = model;
        updateFromModel();
    }

    public ComboBoxModel<E> getModel() {
        return model;
    }

    public void setRenderer(ListCellRenderer<? super E> renderer) {
        this.renderer = renderer;
        updateAdapter();
    }

    public ListCellRenderer<? super E> getRenderer() {
        return renderer;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        setBackgroundColor(opaque ? Color.WHITE.toArgb() : Color.TRANSPARENT.toArgb());
    }

    public boolean isOpaque() {
        return opaque;
    }

    public int getSelectedIndex() {
        return getSelectedItemPosition();
    }

    public E getSelectedItem() {
        if (model != null) {
            return model.getSelectedItem();
        }
        int position = getSelectedItemPosition();
        return position >= 0 && position < items.size() ? items.get(position) : null;
    }



    public void setSelectedItem(E item) {
        if (model != null) {
            model.setSelectedItem(item);
        } else {
            int position = items.indexOf(item);
            if (position >= 0) {
                setSelection(position);
            }
        }
    }

    public void setSelectedIndex(int i) {
        if (model != null) {
            model.setSelectedItem(model.getElementAt(i));
        } else {
            if (i >= 0) setSelection(i);
        }
    }

    public void addItem(E item) {
        items.add(item);
        updateAdapter();
    }

    public void insertItemAt(E item, int index) {
        if (index >= 0 && index <= items.size()) {
            items.add(index, item);
            updateAdapter();
        }
    }

    public void removeItem(E item) {
        items.remove(item);
        updateAdapter();
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            updateAdapter();
        }
    }

    public void removeAllItems() {
        items.clear();
        updateAdapter();
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds = new Rect(x, y, x + width, y + height);
        setLeft(x);
        setLeft(y);
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }

    public int getItemCount() {
        return items.size();
    }

    public E getItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public Rect getBounds() {
        return new Rect(bounds);
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize != null) {
            return preferredSize;
        }

        // Calculate based on content
        int width = measureMaxItemWidth() + getPaddingLeft() + getPaddingRight();
        int height = measureItemHeight() + getPaddingTop() + getPaddingBottom();

        return new Dimension(width, height);
    }

    private int measureMaxItemWidth() {
        int maxWidth = 0;
        Paint paint = new Paint();
        paint.setTextSize(currentFont.getSize());

        for (E item : items) {
            String text = item != null ? item.toString() : "";
            maxWidth = Math.max(maxWidth, (int) paint.measureText(text));
        }

        return maxWidth + 50; // Add some padding for the dropdown arrow
    }

    private int measureItemHeight() {
        TextView textView = new TextView(getContext());
        textView.setText("Test");
        textView.setTextSize(currentFont.getSize());
        textView.measure(0, 0);
        return textView.getMeasuredHeight() + 20; // Add some padding
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
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
    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            updateAdapter();
        }
    }

    @Override
    public Dimension getSize() {

        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        return currentFont;
    }


    public void setBackground(Color color) {
        this.background = color;
        if (color != null) {
            setBackgroundColor(color.toArgb());
        }
    }

    public void setForeground(Color color) {
        this.foreground = color;
        if (color != null) {
            // For spinner, we need to set text color in the adapter
            updateAdapter();
        }
    }

    public void setVisible(boolean b) {
        setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    public void setEditable(boolean b) {
        this.editable = b;
        // In Android, Spinner is not directly editable, but we can simulate it
    }

    public boolean isEditable() {
        return editable;
    }

    public ComboBoxEditor getEditor() {
        return editor;
    }

    public void setEditor(ComboBoxEditor editor) {
        this.editor = editor;
    }

    public void addItemListener(Object o) {

    }

    private class CustomAdapter extends ArrayAdapter<E> {
        public CustomAdapter(Context context, int resource, List<E> objects) {
            super(context, resource, objects);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (renderer != null) {
                view = renderer.getListCellRendererComponent(
                        JComboBox.this,
                        getItem(position),
                        position,
                        false,
                        hasFocus()
                );
            } else {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(getItem(position) != null ? getItem(position).toString() : "");
                textView.setTypeface(currentFont.getFont());
                textView.setTextSize(currentFont.getSize());
                if (foreground != null) {
                    textView.setTextColor(foreground.toArgb());
                }
                view = textView;
            }
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;
            if (renderer != null) {
                view = renderer.getListCellRendererComponent(
                        JComboBox.this,
                        getItem(position),
                        position,
                        true,
                        true
                );
            } else {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(getItem(position) != null ? getItem(position).toString() : "");
                textView.setTypeface(currentFont.getFont());
                textView.setTextSize(currentFont.getSize());
                if (foreground != null) {
                    textView.setTextColor(foreground.toArgb());
                }
                view = textView;
            }
            return view;
        }
    }


    public static class BasicComboBoxEditor implements ComboBoxEditor {
        private final JTextField editor;
        private final List<ActionListener> actionListeners = new ArrayList<>();

        public BasicComboBoxEditor() {
            editor = new JTextField();
            editor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    fireActionEvent();
                }
            });
        }

        @Override
        public View getEditorComponent() {
            return editor;
        }

        @Override
        public void setItem(Object anObject) {
            editor.setText(anObject != null ? anObject.toString() : "");
        }

        @Override
        public Object getItem() {
            return editor.getText();
        }

        @Override
        public void selectAll() {
            editor.selectAll();
        }

        @Override
        public void addActionListener(ActionListener l) {
            if (l != null && !actionListeners.contains(l)) {
                actionListeners.add(l);
            }
        }

        @Override
        public void removeActionListener(ActionListener l) {
            actionListeners.remove(l);
        }

        private void fireActionEvent() {
            ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "editingStopped");
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(e);
            }
        }
    }
}