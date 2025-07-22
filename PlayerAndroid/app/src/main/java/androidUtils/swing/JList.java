package androidUtils.swing;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Point;
import androidUtils.awt.event.KeyEvent;
import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;
import androidUtils.awt.event.MouseMotionListener;
import androidUtils.swing.event.ListSelectionEvent;
import androidUtils.swing.event.ListSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import playerAndroid.app.StartAndroidApp;

public class JList<E> extends ListView implements ViewComponent{
    private ListModel<E> dataModel;
    private ListCellRenderer<? super E> cellRenderer;
    private ListSelectionModel selectionModel;
    private final List<ListSelectionListener> selectionListeners = new ArrayList<>();

    private final List<MouseMotionListener> mouseMotionListeners = new ArrayList<>();
    private final List<KeyListener> keyListeners = new ArrayList<>();
    private Font currentFont;

    private boolean opaque;

    public JList() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JList(ListModel<E> dataModel) {
        super(StartAndroidApp.getAppContext());
        this.dataModel = dataModel;
        init();
        updateFromModel();
    }

    public JList(E[] listData) {
        super(StartAndroidApp.getAppContext());
        this.dataModel = new DefaultListModel<>();
        for (E item : listData) {
            ((DefaultListModel<E>) dataModel).addElement(item);
        }
        init();
        updateFromModel();
    }

    public JList(Vector<E> listData) {
        super(StartAndroidApp.getAppContext());
        this.dataModel = new DefaultListModel<>();
        for (E item : listData) {
            ((DefaultListModel<E>) dataModel).addElement(item);
        }
        init();
        updateFromModel();
    }

    private void init() {
        this.selectionModel = new DefaultListSelectionModel();
        setAdapter(new ListAdapter());

        setOnItemClickListener((parent, view, position, id) -> {
            selectionModel.setSelectionInterval(position, position);
            fireSelectionEvent();
        });
    }
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        setBackgroundColor(opaque ? 0xFF000000 : 0x00000000);

    }

    public boolean isOpaque()
    {
        return opaque;
    }

    private void updateFromModel() {
        if (dataModel != null) {
            setAdapter(new ListAdapter());
        }
    }

    public void setModel(ListModel<E> model) {
        this.dataModel = model;
        updateFromModel();
    }

    public ListModel<E> getModel() {
        return dataModel;
    }

    public void setCellRenderer(ListCellRenderer<? super E> renderer) {
        this.cellRenderer = renderer;
        updateFromModel();
    }

    public void setSelectionMode(int selectionMode) {
        selectionModel.setSelectionMode(selectionMode);
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        if (listener != null && !selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    public void addMouseListener(MouseListener listener)
    {

    }

    private void fireSelectionEvent() {
        ListSelectionEvent event = new ListSelectionEvent(
                this,
                selectionModel.getMinSelectionIndex(),
                selectionModel.getMaxSelectionIndex(),
                false
        );

        for (ListSelectionListener listener : selectionListeners) {
            listener.valueChanged(event);
        }
    }

    public E getSelectedValue() {
        int index = selectionModel.getMinSelectionIndex();
        if (index >= 0 && dataModel != null && index < dataModel.getSize()) {
            return dataModel.getElementAt(index);
        }
        return null;
    }

    public int[] getSelectedIndices() {
        return selectionModel.getSelectedIndices();
    }

    public List<E> getSelectedValues() {
        List<E> selected = new ArrayList<>();
        for (int index : selectionModel.getSelectedIndices()) {
            if (dataModel != null && index < dataModel.getSize()) {
                selected.add(dataModel.getElementAt(index));
            }
        }
        return selected;
    }

    public int getSelectedIndex() {
        return selectionModel.getMinSelectionIndex();
    }

    public int locationToIndex(Point point) {
        if (point == null) {
            return -1;
        }

        if (getVisibility() != View.VISIBLE) {
            return -1;
        }

        int[] listViewCoords = new int[2];
        getLocationOnScreen(listViewCoords);
        int relativeX = point.x - listViewCoords[0];
        int relativeY = point.y - listViewCoords[1];

        relativeX -= getPaddingLeft() - getScrollX();
        relativeY -= getPaddingTop() - getScrollY();

        if (relativeX < 0 || relativeX > (getWidth() - getPaddingLeft() - getPaddingRight()) ||
                relativeY < 0 || relativeY > (getHeight() - getPaddingTop() - getPaddingBottom())) {
            return -1;
        }

        int position = pointToPosition(relativeX, relativeY);


        if (position != AdapterView.INVALID_POSITION) {
            View child = getChildAt(position - getFirstVisiblePosition());
            if (child != null) {
                int childTop = child.getTop();
                int childBottom = child.getBottom();
                if (relativeY >= childTop && relativeY <= childBottom) {
                    return position;
                }
            }
        }

        return -1;
    }

    public void setSelectedIndex(int index) {
        selectionModel.setSelectionInterval(index, index);
    }

    public void removeAll() {
        dataModel.clear();
    }

    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        if (listener != null && !mouseMotionListeners.contains(listener)) {
            mouseMotionListeners.add(listener);
            setupMouseMotionDetection();
        }
    }

    public void removeMouseMotionListener(MouseMotionListener listener) {
        mouseMotionListeners.remove(listener);
        if (mouseMotionListeners.isEmpty()) {
            // Optionnel: désactiver la détection si plus de listeners
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupMouseMotionDetection() {
        setOnTouchListener((v, event) -> {
            Point point = new Point((int) event.getX(), (int) event.getY());
            int index = locationToIndex(point);

            for (MouseMotionListener listener : mouseMotionListeners) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        listener.mouseMoved(new MouseEvent(
                                event, this
                        ));
                        break;
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        // Gérer d'autres événements si nécessaire
                        break;
                }
            }
            return false;
        });
    }



    public void setFont(Font font) {
        this.currentFont = font;
        if (font != null) {
            Typeface typeface = Typeface.create(font.getFont(), font.getStyle());


            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTypeface(typeface);
                    ((TextView) child).setTextSize(font.getSize());
                }
            }

            if (getAdapter() != null) {
                ((BaseAdapter)getAdapter()).notifyDataSetChanged();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculer la taille préférée basée sur le contenu
        int width = 0;
        int height = 0;

        // Si nous avons un modèle de données
        if (dataModel != null && dataModel.getSize() > 0) {
            // Mesurer un élément pour estimer la hauteur totale
            View itemView = getAdapter().getView(0, null, this);
            itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );

            // Hauteur totale = hauteur d'un item * nombre d'items (max 5 items visibles)
            int itemHeight = itemView.getMeasuredHeight();
            height = itemHeight * Math.min(5, dataModel.getSize());

            // Largeur = largeur de l'item + padding
            width = itemView.getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        }

        return new Dimension(width, height);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
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

    public Font getFont() {
        return currentFont;
    }

    public void addKeyListener(KeyListener listener) {
        if (listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);
            setupKeyDetection();
        }
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
        if (keyListeners.isEmpty()) {
            setOnKeyListener(null);
        }
    }

    private void setupKeyDetection() {
        setOnKeyListener((v, keyCode, event) -> {
            for (KeyListener listener : keyListeners) {
                switch (event.getAction()) {
                    case KeyEvent.KEY_PRESSED:
                        listener.keyPressed(new KeyEvent(
                                event, event.getDeviceId()
                        ));
                        break;
                    case KeyEvent.KEY_RELEASED:
                        listener.keyReleased(new KeyEvent(
                                event, event.getDeviceId()
                        ));
                        break;
                }
            }
            return false;
        });
    }

    public String getToolTipText(MouseEvent e) {
        // Trouver l'index de l'élément sous le pointeur
        int index = locationToIndex(e.getPoint());
        if (index >= 0 && index < dataModel.getSize()) {
            E item = dataModel.getElementAt(index);
            if (item != null) {
                // Si l'élément est un objet avec texte d'infobulle, retourner ce texte
                if (item instanceof ToolTipProvider) {
                    return ((ToolTipProvider)item).getToolTipText();
                }
                // Sinon retourner la représentation textuelle de l'élément
                return item.toString();
            }
        }
        return null;
    }

    public Point getToolTipLocation(MouseEvent e) {
        // Retourne la position du pointeur ajustée pour l'affichage de l'infobulle
        Point point = e.getPoint();

        // Ajuster la position pour éviter que l'infobulle ne cache l'élément
        int yOffset = 20; // Décalage vertical

        // Convertir les coordonnées relatives en coordonnées absolues
        int[] location = new int[2];
        getLocationOnScreen(location);

        return new Point(
                location[0] + point.x,
                location[1] + point.y - yOffset
        );
    }

    public void setListData(E[] clauseStrings) {
        dataModel.setElements(clauseStrings);
    }


    private class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataModel != null ? dataModel.getSize() : 0;
        }

        @Override
        public E getItem(int position) {
            return dataModel != null ? dataModel.getElementAt(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (cellRenderer != null) {
                return cellRenderer.getListCellRendererComponent(
                        JList.this,
                        getItem(position),
                        position,
                        selectionModel.isSelectedIndex(position),
                        hasFocus()
                );
            }

            // Default rendering
            TextView textView = new TextView(getContext());
            E item = getItem(position);
            textView.setText(item != null ? item.toString() : "");
            return textView;
        }
    }
}