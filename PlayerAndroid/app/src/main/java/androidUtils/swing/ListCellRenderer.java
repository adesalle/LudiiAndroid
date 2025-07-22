package androidUtils.swing;

import android.view.View;


public interface ListCellRenderer<E> {

    View getListCellRendererComponent(
            Object component,
            E value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    );
}