package androidUtils.swing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;




import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.ItemEvent;
import androidUtils.awt.event.ItemListener;
import androidUtils.swing.border.Border;
import androidUtils.swing.event.ListDataEvent;
import playerAndroid.app.StartAndroidApp;

public class JComboBox<E> extends androidx.appcompat.widget.AppCompatSpinner implements ViewComponent{
    private int chevronRightMargin = 4;

    protected ComboBoxModel<E> model;
    protected ListCellRenderer<? super E> renderer;
    protected List<E> items;
    protected List<ActionListener> actionListeners;
    protected boolean opaque;
    protected Rect bounds;
    protected Font currentFont;
    protected Dimension preferredSize;
    protected Color background = Color.GRAY;
    protected Color foreground;
    protected Color textColor = Color.WHITE;
    protected Border border;
    protected boolean editable;
    protected ComboBoxEditor editor;
    protected ArrayAdapter<E> adapter;
    private boolean isEditing = false;

    public JComboBox() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JComboBox(ComboBoxModel<E> model) {
        super(StartAndroidApp.getAppContext());
        this.model = model;
        init();
    }

    public JComboBox(E[] items) {
        super(StartAndroidApp.getAppContext());
        this.items = new ArrayList<>();
        for (E item : items) {
            this.items.add(item);
        }
        init();
    }

    public JComboBox(Vector<E> items) {
        super(StartAndroidApp.getAppContext());
        this.items = new ArrayList<>(items);
        init();
    }

    protected void init() {
        actionListeners = new ArrayList<>();
        opaque = true;
        editable = false;
        bounds = new Rect();
        preferredSize = new Dimension(100, 40); // Taille par défaut

        if (model != null) {
            updateFromModel();
        } else if (items != null) {
            updateAdapter();
        } else {
            items = new ArrayList<>();
            updateAdapter();
        }
        initChevronPosition();
    }
    private void initChevronPosition() {
        // Écouter les changements de layout
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        adjustChevron();
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void adjustChevron() {
        post(() -> {
            try {
                // Méthode plus sûre pour trouver le conteneur du chevron
                View childView = getChildAt(0);

                if (childView != null) {
                    // Le conteneur du chevron est souvent un AppCompatTextView enveloppé
                    ViewParent parent = childView.getParent();

                    if (parent instanceof ViewGroup) {
                        ViewGroup container = (ViewGroup) parent;

                        // Parcourir les vues pour trouver le chevron (ImageView)
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View view = container.getChildAt(i);

                            if (view instanceof ImageView) {
                                // Ajuster la position du chevron
                                ViewGroup.MarginLayoutParams params =
                                        (ViewGroup.MarginLayoutParams) view.getLayoutParams();

                                // Convertir dp en px
                                float density = getResources().getDisplayMetrics().density;
                                params.rightMargin = (int) (4 * density); // 4dp de marge

                                view.setLayoutParams(params);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("JComboBox", "Error adjusting chevron", e);
            }
        });
    }

    // Méthode alternative plus robuste
    private void findAndAdjustChevron() {
        post(() -> {
            // Parcourir récursivement la hiérarchie des vues
            findChevronView(this);
        });
    }

    private boolean findChevronView(View view) {
        if (view instanceof ImageView && view.getBackground() != null) {
            // Trouvé le chevron - ajuster sa position
            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.rightMargin = convertDpToPx(4); // 4dp
            view.setLayoutParams(params);
            return true;
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                if (findChevronView(group.getChildAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }



    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public ActionListener[] getActionListeners() {
        return actionListeners.toArray(new ActionListener[0]);
    }

    protected void fireActionEvent() {
        ActionEvent event = new ActionEvent(this);
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }

    protected void updateFromModel() {
        if (model != null) {
            items = new ArrayList<>();
            for (int i = 0; i < model.getSize(); i++) {
                items.add(model.getElementAt(i));
            }
            updateAdapter();
        }
    }

    protected void updateAdapter() {
        adapter = new ArrayAdapter<E>(getContext(), android.R.layout.simple_spinner_item, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                applyViewStyles(view);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                applyDropDownStyles(view);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(adapter);
    }

    private void applyViewStyles(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (currentFont != null) {
                textView.setTypeface(currentFont.getFont());
                textView.setTextSize(currentFont.getSize());
            }
            if (foreground != null) {
                textView.setTextColor(foreground.toArgb());
            }
            if (background != null) {
                view.setBackgroundColor(background.toArgb());
            }
            if (textColor != null) textView.setTextColor(textColor.toArgb());
        }
    }

    private void applyDropDownStyles(View view) {
        applyViewStyles(view);

        if (border != null && view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            group.setClipToPadding(false);

            // Appliquer le padding de la bordure
            Rect insets = border.getBorderInsets(this);
            group.setPadding(
                    insets.left,
                    insets.top,
                    insets.right,
                    insets.bottom
            );

            // Dessiner la bordure
            group.post(() -> {
                Canvas canvas = new Canvas();
                border.paintBorder(
                        JComboBox.this,
                        new Graphics(canvas),
                        0,
                        0,
                        group.getWidth(),
                        group.getHeight()
                );
            });
        }
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
        // Implémenter la logique de rendu personnalisé ici
    }

    public ListCellRenderer<? super E> getRenderer() {
        return renderer;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        if (opaque) {
            setBackgroundColor(background != null ? background.toArgb() : Color.WHITE.toArgb());
        } else {
            setBackgroundColor(Color.TRANSPARENT.toArgb());
        }
    }

    public boolean isOpaque() {
        return opaque;
    }

    public int getSelectedIndex() {
        return getSelectedItemPosition();
    }

    public E getSelectedItem() {
        return (E) super.getSelectedItem();
    }

    public void setSelectedItem(E item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            setSelection(index);
        }

    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < items.size()) {
            setSelection(index);
        }
    }

    public void addItem(E item) {
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    public void insertItemAt(E item, int index) {
        if (index >= 0 && index <= items.size()) {
            items.add(index, item);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeItem(E item) {
        items.remove(item);
        adapter.notifyDataSetChanged();
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeAllItems() {
        items.clear();
        adapter.notifyDataSetChanged();
    }

    public void setBounds(int x, int y, int width, int height) {
        bounds.set(x, y, x + width, y + height);
        //setX(x);
        //setY(y);
        //setLayoutParams(new ViewGroup.LayoutParams(width, height));
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
        return bounds;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
        requestLayout();
    }

    @Override
    public void setSize(Dimension dimension) {
        setLayoutParams(new ViewGroup.LayoutParams(dimension.width, dimension.height));
    }

    @Override
    public void setFont(Font font) {
        this.currentFont = font;
        adapter.notifyDataSetChanged();
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
        setBackgroundColor(color.toArgb());
    }

    public void setTextColor(Color color)
    {
        this.textColor = color;
    }

    public void setForeground(Color color) {
        this.foreground = color;
        adapter.notifyDataSetChanged();
    }

    public void setVisible(boolean visible) {
        setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        // Implémenter la logique d'édition ici
    }

    public boolean isEditable() {
        return editable;
    }

    public ComboBoxEditor getEditor() {
        return editor;
    }

    public void setEditor(ComboBoxEditor editor) {
        this.editor = editor;

        if (editor != null) {
            // Configurer le mode éditable
            setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP && isEnabled()) {
                    startEditing();
                    return true;
                }
                return false;
            });
        } else {
            setOnTouchListener(null);
        }
    }

    private void startEditing() {
        if (editor == null || !editable) return;

        isEditing = true;

        // Remplacer la vue actuelle par l'éditeur
        View editorComponent = editor.getEditorComponent();
        editor.setItem(getSelectedItem());

        // Sauvegarder l'ancienne vue
        View originalView = getSelectedView();

        // Configurer le conteneur d'édition
        FrameLayout editorContainer = new FrameLayout(getContext());
        editorContainer.addView(editorComponent);
        editorContainer.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Remplacer la vue
        setAdapter(new ArrayAdapter<E>(getContext(), 0, new ArrayList<E>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return editorContainer;
            }
        });

        // Focus sur l'éditeur
        editorComponent.post(() -> {
            editorComponent.requestFocus();
            if (editorComponent instanceof EditText) {
                ((EditText) editorComponent).selectAll();
            }
        });

        // Gérer la fin de l'édition
        editorComponent.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                stopEditing();
            }
        });
    }

    private void stopEditing() {
        if (!isEditing) return;

        isEditing = false;
        Object newValue = editor.getItem();

        // Restaurer l'adapter original
        updateAdapter();

        // Mettre à jour la valeur si modifiée
        if (newValue != null && !newValue.equals(getSelectedItem())) {
            setSelectedItem((E) newValue);
        }

        // Notifier les listeners
        fireActionEvent();
    }

    private List<ItemListener> itemListeners = new ArrayList<>();

    public void addItemListener(ItemListener listener) {
        if (listener != null && !itemListeners.contains(listener)) {
            itemListeners.add(listener);

            // Ajouter le listener natif si c'est le premier
            if (itemListeners.size() == 1) {
                super.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ItemEvent event = new ItemEvent(
                                JComboBox.this,
                                ItemEvent.ITEM_STATE_CHANGED,
                                items.get(position),
                                ItemEvent.SELECTED
                        );

                        for (ItemListener listener : itemListeners) {
                            listener.itemStateChanged(event);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ItemEvent event = new ItemEvent(
                                JComboBox.this,
                                ItemEvent.ITEM_STATE_CHANGED,
                                null,
                                ItemEvent.DESELECTED
                        );

                        for (ItemListener listener : itemListeners) {
                            listener.itemStateChanged(event);
                        }
                    }
                });
            }
        }
    }

    public void removeItemListener(ItemListener listener) {
        itemListeners.remove(listener);

        // Supprimer le listener natif si plus aucun listener
        if (itemListeners.isEmpty()) {
            super.setOnItemSelectedListener(null);
        }
    }

    public void addItems(List<E> strings) {
        for (E string: strings) {
            addItem(string);
        }
    }

    // Interfaces nécessaires
    public interface ComboBoxModel<E> {
        int getSize();
        E getElementAt(int index);
        void addListDataListener(ListDataListener l);
        void removeListDataListener(ListDataListener l);
    }

    public interface ListCellRenderer<E> {
        View getListCellRendererComponent(Object view, E value, int index, boolean isSelected, boolean cellHasFocus);
    }

    public interface ComboBoxEditor {
        View getEditorComponent();
        void setItem(Object item);
        Object getItem();
        void addActionListener(ActionListener l);
        void removeActionListener(ActionListener l);
    }

    public interface ListDataListener {
        void contentsChanged(ListDataEvent e);
        void intervalAdded(ListDataEvent e);
        void intervalRemoved(ListDataEvent e);
    }

    public interface ItemListener {
        void itemStateChanged(ItemEvent e);
    }
}