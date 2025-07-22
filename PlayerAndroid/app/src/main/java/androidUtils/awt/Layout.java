package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;


public interface Layout{
    void applyLayout(ViewGroup group);

    void addLayoutComponent(View comp, Object constraints);

    void removeLayoutComponent(View comp);

}