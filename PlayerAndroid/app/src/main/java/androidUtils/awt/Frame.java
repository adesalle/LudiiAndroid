package androidUtils.awt;


import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import playerAndroid.app.StartAndroidApp;

public class Frame extends LinearLayout {

    private String title;
    private LinearLayout contentPane;

    public Frame() {
        super(StartAndroidApp.getAppContext());
        setOrientation(VERTICAL);

        // Initialiser le titre par défaut
        title = "Untitled Frame";

        // Ajouter une barre de titre
        TextView titleBar = new TextView(getContext());
        titleBar.setText(title);
        titleBar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        titleBar.setBackgroundColor(Color.DKGRAY);
        titleBar.setTextColor(Color.WHITE);
        titleBar.setPadding(16, 16, 16, 16);

        // Ajouter la barre de titre
        addView(titleBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // Initialiser le conteneur principal
        contentPane = new LinearLayout(getContext());
        contentPane.setOrientation(VERTICAL);
        contentPane.setBackgroundColor(Color.LTGRAY);

        // Ajouter le conteneur au `Frame`
            addView(contentPane, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
    }

    // Définir le titre
    public void setTitle(String title) {
        this.title = title;
        ((TextView) getChildAt(0)).setText(title); // Met à jour la barre de titre
    }

    public String getTitle() {
        return title;
    }

    // Obtenir le conteneur principal
    public LinearLayout getContentPane() {
        return contentPane;
    }

    // Ajouter des vues au conteneur principal
    public void add(Component component) {
        contentPane.addView(component);
    }

    public void remove(Component component) {
        contentPane.removeView(component);
    }

    public void removeAll() {
        contentPane.removeAllViews();
    }

}
