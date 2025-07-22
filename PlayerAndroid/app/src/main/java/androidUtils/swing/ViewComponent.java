package androidUtils.swing;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.swing.border.Border;

public interface ViewComponent{
    Dimension getPreferredSize();
    void setPreferredSize(Dimension dimension);
    void setSize(Dimension dimension);
    void setFont(Font font);

    Dimension getSize();
    Font getFont();


}
