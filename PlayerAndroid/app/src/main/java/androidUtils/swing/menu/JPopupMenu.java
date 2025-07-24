package androidUtils.swing.menu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidUtils.awt.Color;
import androidUtils.swing.JButton;
import androidUtils.swing.event.PopupMenuEvent;
import androidUtils.swing.event.PopupMenuListener;
import playerAndroid.app.StartAndroidApp;

public class JPopupMenu extends PopupWindow {
    private final Context context;
    private JMenu menu;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final List<PopupMenuListener> listeners = new CopyOnWriteArrayList<>();
    private final List<JMenuItem> components = new ArrayList<>();
    private View anchorView;

    public JPopupMenu(Context context, JMenu menu) {
        super(context);
        this.context = context;
        this.menu = menu;
        initialize();
    }

    public JPopupMenu()
    {
        this(StartAndroidApp.getAppContext(), new JMenu());
    }

    private void initialize() {
        // Basic setup
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT.toArgb()));
        setElevation(dpToPx(8, context));

        // Create and configure ListView
        listView = new ListView(context);
        listView.setBackgroundColor(Color.parseColor("#B3B3B3"));
        listView.setDivider(new ColorDrawable(Color.parseColor("#555555")));
        listView.setDividerHeight(1);

        // Set as content view
        setContentView(listView);
        updateMenuItems();
    }

    private void updateMenuItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            items.add(menu.getItem(i).getTitle().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                items
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            JMenuItem item = (JMenuItem) menu.getItem(position);
            if (item.hasSubMenu()) {
                JPopupMenu subMenu = new JPopupMenu(context, (JMenu) item.getSubMenu());
                subMenu.showAsDropDown(view);
            } else {
                if (item.performClick()) {
                    dismiss();
                }
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor) {
        this.anchorView = anchor;
        updateMenuItems(); // Refresh items before showing

        // Calculate proper width
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        setWidth(width);
        setHeight(height);

        // Show the popup
        super.showAsDropDown(anchor);
        notifyPopupMenuWillBecomeVisible(this);
    }

    @Override
    public void dismiss() {
        notifyPopupMenuWillBecomeInvisible();
        super.dismiss();
    }
    public void setMenu(JMenu newMenu) {
        this.menu = newMenu;
        updateMenuItems();
    }

    public void addPopupMenuListener(PopupMenuListener menuListener) {
        if (menuListener != null && !listeners.contains(menuListener)) {
            listeners.add(menuListener);
        }
    }

    public void removePopupMenuListener(PopupMenuListener menuListener) {
        listeners.remove(menuListener);
    }

    public void removeAll() {
        menu.clear();
        updateMenuItems();
    }

    public void add(JMenuItem item) {
        menu.add(item);
        updateMenuItems();
    }

    public void add(JMenu menu) {
        JMenuItem item = new JMenuItem();
        item.setSubMenu(new JSubMenu(menu, item));
    }

    public void addSeparator() {
        JMenuItem separator = new JMenuItem(context);
        separator.setTitle("──────────");
        separator.setEnabled(false);
        add(separator);
    }

    public int getComponentIndex(JMenuItem item) {
        return components.indexOf(item);
    }

    public JMenuItem[] getComponents() {
        return components.toArray(new JMenuItem[0]);
    }



    private void notifyPopupMenuWillBecomeVisible(JPopupMenu menu) {
        for (PopupMenuListener listener : listeners) {
            listener.popupMenuWillBecomeVisible(new PopupMenuEvent(this));
        }
    }

    private void notifyPopupMenuWillBecomeInvisible() {
        for (PopupMenuListener listener : listeners) {
            listener.popupMenuWillBecomeInvisible(new PopupMenuEvent(this));
        }
    }

    private static int dpToPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public void show(View anchor, int xOffset, int yOffset) {
        showAsDropDown(anchor, xOffset, yOffset);
    }

    public String getText()
    {
        return menu.getTitle();
    }

    public Object getInvoker() {
        return menu;
    }
}