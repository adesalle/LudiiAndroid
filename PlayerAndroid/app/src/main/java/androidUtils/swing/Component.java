package androidUtils.swing;

import android.view.View;
import playerAndroid.app.AndroidApp;

public abstract class Component extends View {


    private String name;


    public Component(String name) {
        super(AndroidApp.getAppContext());
        this.name = name;
    }


    public String getName() {
        return name;
    }
}