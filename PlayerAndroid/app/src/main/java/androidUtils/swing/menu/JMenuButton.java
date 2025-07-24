package androidUtils.swing.menu;

import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

public class JMenuButton extends AppCompatButton {
    private JMenu menu;

    public JMenuButton(Context context, JMenu menu) {
        super(context);
        this.menu = menu;
        setMinWidth(0);
        setMinHeight(0);
        setPadding(4, 4, 4, 4);
        init();
    }

    private void init() {
        setOnClickListener(v -> {
            // Affiche le menu contextuel attaché
            if (menu != null && menu.getPopupMenu() != null) {
                menu.getPopupMenu().showAsDropDown(this);
            }
        });
    }
}