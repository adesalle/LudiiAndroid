package androidUtils.awt;

import android.content.Context;
import android.view.View;

import playerAndroid.app.AndroidApp;

public class GridLayout {

    private android.widget.GridLayout layout;
    private Context context;

    public GridLayout()
    {
        context = AndroidApp.getAppContext();
        layout = new android.widget.GridLayout(context);

    }

    public Context getContext() {
        return context;
    }

    public void setRowCount(int rows) {
        layout.setRowCount(rows);
    }

    public void setColumnCount(int columns) {
        layout.setColumnCount(columns);
    }

    public void addView(View component) {
        layout.addView(component);
    }

    public android.widget.GridLayout getLayout() {
        return layout;
    }
}
