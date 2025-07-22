package androidUtils.swing.menu;

import android.content.Context;
import android.widget.Button;

public class JMenuButton extends androidx.appcompat.widget.AppCompatButton {
    public JMenuButton(Context context) {
        super(context);
        setAllCaps(false);
        setTextSize(16);
    }
}