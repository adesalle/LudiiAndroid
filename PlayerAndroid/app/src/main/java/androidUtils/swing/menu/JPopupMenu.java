package androidUtils.swing.menu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;

public class JPopupMenu extends PopupWindow {
    public JPopupMenu(Context context, JMenu menu) {
        super(context);

        ListView listView = new ListView(context);

        listView.setBackgroundColor(Color.parseColor("#B3B3B3"));

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
                new JPopupMenu(context, (JMenu) item.getSubMenu()).showAsDropDown(view);
            } else {
                item.performClick();
                dismiss();
            }
        });

        listView.setDivider(new ColorDrawable(Color.parseColor("#555555"))); // Couleur du séparateur
        listView.setDividerHeight(1);

        setContentView(listView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT.toArgb()));
        setElevation(dpToPx(8, context));
    }

    private static int dpToPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}