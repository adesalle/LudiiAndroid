package androidUtils.swing.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

import playerAndroid.app.StartAndroidApp;

public class JMenuBar extends LinearLayout {
    private final List<JMenu> menus = new ArrayList<>();
    private final Context context;

    public JMenuBar() {
        this(StartAndroidApp.getAppContext());
    }
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

    public JMenuButton add(JMenu menu)
    {
        menus.add(menu);
        menu.setParent(this);
        JMenuButton button = new JMenuButton(context, menu);
        button.setText(menu.getTitle());
        button.setOnClickListener(v -> {
            menu.showMenuPopup(button);
        });

        addView(button);
        return button;
    }


    public JMenuButton addMenu(String title) {
        JMenu menu = new JMenu(context);
        menu.setTitle(title);
        return add(menu);
    }



    public List<JMenu> getMenus() {
        return new ArrayList<>(menus);
    }

    public void setProportionalWeights(boolean b) {
    }
}