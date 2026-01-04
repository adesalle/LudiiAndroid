package androidUtils.swing;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidUtils.awt.Color;
import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.ActionListener;
import playerAndroid.app.StartAndroidApp;

public class JCheckBox extends androidx.appcompat.widget.AppCompatCheckBox implements ViewComponent {
    protected Font currentFont;
    protected Dimension preferredSize;

    public JCheckBox() {
        super(StartAndroidApp.getAppContext());
        init(null);
    }

    public JCheckBox(String text) {
        super(StartAndroidApp.getAppContext());
        init(text);
    }

    public JCheckBox(String text, Boolean selected) {
        super(StartAndroidApp.getAppContext());
        init(text);
        setSelected(selected);
    }

    protected void init(String text) {
        setTextColor(Color.WHITE.toArgb());
        setButtonTintList(ColorStateList.valueOf(Color.WHITE.toArgb()));

        if (text != null) {
            setText(text);
        }
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setSelected(boolean selected) {
        super.setChecked(selected);
    }

    public boolean isSelected() {
        return super.isChecked();
    }

    public void addActionListener(ActionListener listener) {
        setOnCheckedChangeListener((buttonView, isChecked) -> listener.actionPerformed(null));
    }

    public void removeActionListener() {
        setOnCheckedChangeListener(null);
    }

    public void setBounds(int x, int y, int width, int height) {
        layout(x, y, x + width, y + height);
    }

    public void setHorizontalAlignment(int alignment) {
        switch (alignment) {
            case Component.LEFT_ALIGNMENT:
                setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case Component.RIGHT_ALIGNMENT:
                setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            case Component.CENTER_ALIGNMENT:
                setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                break;
            default:
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize != null) {
            return preferredSize;
        }
        measure(0, 0);
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
        requestLayout();
    }

    @Override
    public void setSize(Dimension dimension) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(dimension.width, dimension.height);
        } else {
            params.width = dimension.width;
            params.height = dimension.height;
        }
        setLayoutParams(params);
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            Typeface typeface = font.getFont();
            setTypeface(typeface);
            setTextSize(font.getSize());
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        if (currentFont == null) {
            currentFont = new Font(getTypeface().toString(), Typeface.NORMAL, (int) getTextSize());
        }
        return currentFont;
    }

    public void addChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        setOnCheckedChangeListener(listener);
    }
}