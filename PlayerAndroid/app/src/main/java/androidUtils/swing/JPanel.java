package androidUtils.swing;

import android.widget.LinearLayout;
import androidUtils.awt.GridLayout;

import playerAndroid.app.AndroidApp;

public class JPanel extends LinearLayout {

    private final GridLayout gridLayout;

    public JPanel(GridLayout layout) {
        super(AndroidApp.getAppContext());

        this.gridLayout = layout;

        this.setOrientation(VERTICAL);
        this.addView(gridLayout.getLayout());
    }


    public void add(Component child) {
        android.widget.GridLayout.LayoutParams params = new android.widget.GridLayout.LayoutParams();
        gridLayout.getLayout().addView(child, params);
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }
}