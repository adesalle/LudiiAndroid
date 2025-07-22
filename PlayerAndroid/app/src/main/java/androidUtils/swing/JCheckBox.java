package androidUtils.swing;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.ActionListener;
import playerAndroid.app.StartAndroidApp;

public class JCheckBox extends androidx.appcompat.widget.AppCompatCheckBox implements ViewComponent {
    private Font currentFont;
    private Dimension preferredSize;

    public JCheckBox() {
        super(StartAndroidApp.getAppContext());
        init("");
        setSelected(false);
    }

    public JCheckBox(String text) {
        super(StartAndroidApp.getAppContext());
        init(text);
        setSelected(false);
    }

    public JCheckBox(String text, Boolean selected) {
        super(StartAndroidApp.getAppContext());
        init(text);
        setSelected(selected);
    }

    private void init(String text) {
        setText(text);
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        currentFont = new Font(Typeface.DEFAULT, 14);
    }

    public void setSelected(boolean selected) {
        setChecked(selected);
    }

    public boolean isSelected() {
        return isChecked();
    }

    public void addActionListener(ActionListener listener) {
        setOnCheckedChangeListener((buttonView, isChecked) -> listener.run());
    }

    public void removeActionListener() {
        setOnCheckedChangeListener(null);
    }

    public void setBounds(int x, int y, int width, int height) {
        setX(x);
        setY(y);

        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }

    public void setHorizontalAlignment(int alignment) {
        int gravity;
        switch (alignment) {
            case SwingConstants.LEFT:
                gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case SwingConstants.RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
            case SwingConstants.CENTER:
                gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                break;
            default:
                gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        }
        setGravity(gravity);
    }



    @Override
    public Dimension getPreferredSize() {
        if (preferredSize != null) {
            return preferredSize;
        }

        // Calculate based on text and checkbox size
        measure(0, 0);
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
        requestLayout();
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            setTypeface(font.getFont());
            setTextSize(font.getSize());
        }
    }

    @Override
    public Dimension getSize() {

        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        return currentFont;
    }



    public void addChangeListener(Object o) {

    }
}