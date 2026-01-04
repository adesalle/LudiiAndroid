package androidUtils.swing.menu;

import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;

public class JMenuButton extends AppCompatButton {
    private JMenu menu;
    private JMenuItem item;

    private ActionListener listener;

    public JMenuButton(Context context, JMenu menu) {
        super(context);
        this.menu = menu;
        item = new JMenuItem(menu.getTitle());

        setMinWidth(0);
        setMinHeight(0);
        setPadding(4, 4, 4, 4);
        init();

    }

    private void init() {
        JMenuButton button = this;
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (menu != null && menu.getPopupMenu() != null) {
                    menu.getPopupMenu().showAsDropDown(button);
                }
            }
        };
        setOnClickListener(v -> {
            // Affiche le menu contextuel attaché

            listener.actionPerformed(new ActionEvent(button));
        });
    }

    public ActionListener getListener() {
        return listener;
    }

    public void addActionListener(ActionListener listener) {

        this.listener = listener;
        setOnClickListener(v -> {
            listener.actionPerformed(new ActionEvent(item));
        });
    }
}