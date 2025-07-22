package androidUtils.swing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import playerAndroid.app.StartAndroidApp;

public class Box extends LinearLayout implements ViewComponent{
    private Box() {
        super(StartAndroidApp.getAppContext());
    } // Classe utilitaire, pas d'instanciation

    public static Box createHorizontalStrut(int width) {
        return new Strut(width, 0);
    }

    public static View createVerticalStrut(int height) {
        return new Strut(0, height);
    }

    public static LinearLayout createHorizontalBox() {
        LinearLayout box = new LinearLayout(StartAndroidApp.getAppContext());
        box.setOrientation(LinearLayout.HORIZONTAL);
        return box;
    }

    public static Box createVerticalBox() {
        Box box = new Box();
        box.setOrientation(LinearLayout.VERTICAL);
        return box;
    }

    @Override
    public Dimension getPreferredSize() {
        return null;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {

    }

    @Override
    public void setSize(Dimension dimension) {

    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Font getFont() {
        return null;
    }

    public void add(View offsetText) {
        addView(offsetText);
    }

    public static class Filler extends View {
        private final Dimension minSize;
        private final Dimension prefSize;
        private final Dimension maxSize;

        public Filler(Context context, Dimension min, Dimension pref, Dimension max) {
            super(context);
            this.minSize = min;
            this.prefSize = pref;
            this.maxSize = max;
        }


    }

    private static class Strut extends Box {
        public Strut(int width, int height) {
            super();
            setLayoutParams(new LinearLayout.LayoutParams(
                    width > 0 ? width : LinearLayout.LayoutParams.WRAP_CONTENT,
                    height > 0 ? height : LinearLayout.LayoutParams.WRAP_CONTENT
            ));
        }
    }

    // Pour la compatibilité avec votre code existant
    public static View createRigidArea(Dimension d) {
        return new Strut(d.width, d.height);
    }
}