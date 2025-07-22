package androidUtils.swing.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class JMenuBar extends LinearLayout {
    private final List<JMenu> menus = new ArrayList<>();
    private final Context context;

    public JMenuBar(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    public JMenu addMenu(String title) {
        JMenu menu = new JMenu(context);
        menus.add(menu);

        JMenuButton button = new JMenuButton(context);
        button.setText(title);
        button.setOnClickListener(v -> showMenuPopup(menu, button));

        addView(button);
        return menu;
    }

    private void showMenuPopup(JMenu menu, View anchor) {
        JPopupMenu popup = new JPopupMenu(context, menu);
        popup.showAsDropDown(anchor);
    }

    public List<JMenu> getMenus() {
        return new ArrayList<>(menus);
    }
}