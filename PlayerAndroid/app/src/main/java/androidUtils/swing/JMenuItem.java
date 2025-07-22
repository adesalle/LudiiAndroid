package androidUtils.swing;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import androidUtils.awt.Component;
import androidUtils.awt.Container;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.InputEvent;
import androidUtils.swing.event.ChangeEvent;
import androidUtils.swing.event.ChangeListener;
import playerAndroid.app.StartAndroidApp;

import java.util.ArrayList;
import java.util.List;

public class JMenuItem extends AppCompatButton implements ViewComponent{
    private String text;
    private KeyStroke accelerator;
    List<ActionListener> actionListeners = new ArrayList<>();
    private Icon icon;
    private Icon disabledIcon;
    private boolean armed = false;
    private final List<ChangeListener> changeListeners = new ArrayList<>();
    private Font currentFont = new Font(Typeface.DEFAULT, 14); // Default font

    public JMenuItem(String text) {
        super(StartAndroidApp.getAppContext());
        this.text = text;

        setOnClickListener(v -> fireActionEvent());

    }

    public JMenuItem() {
        this("");
    }


    // Gestion des événements
    private void fireActionEvent() {
        System.out.println("click performed");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(e);
        }
    }

    protected void fireStateChanged() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(event);
        }
    }

    public void setAccelerator(KeyStroke keyStroke) {
        this.accelerator = keyStroke;

    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setDisabledIcon(Icon icon) {
        this.disabledIcon = icon;
    }


    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }


    public boolean isArmed() {
        return armed;
    }

    protected void setArmed(boolean armed) {
        if (this.armed != armed) {
            this.armed = armed;
            fireStateChanged();
        }
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }


    private int convertSwingModifiers(int swingModifiers) {
        int androidModifiers = 0;
        if ((swingModifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
            androidModifiers |= KeyEvent.META_SHIFT_ON;
        }
        if ((swingModifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
            androidModifiers |= KeyEvent.META_CTRL_ON;
        }
        if ((swingModifiers & InputEvent.ALT_DOWN_MASK) != 0) {
            androidModifiers |= KeyEvent.META_ALT_ON;
        }
        return androidModifiers;
    }

    private String getShortcutString() {
        if (accelerator == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if ((accelerator.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) {
            sb.append("Shift+");
        }
        if ((accelerator.getModifiers() & InputEvent.CTRL_DOWN_MASK) != 0) {
            sb.append("Ctrl+");
        }
        if ((accelerator.getModifiers() & InputEvent.ALT_DOWN_MASK) != 0) {
            sb.append("Alt+");
        }
        sb.append((char)accelerator.getKeyCode());
        return sb.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        super.setText(text);
    }

    public MenuItem.OnMenuItemClickListener getMenuItemClickListener() {
        return item -> {
            ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(e);
            }
            return true;
        };
    }



    public KeyStroke getAccelerator() {
        return accelerator;
    }

    public void setToolTipText(String s) {
        super.setTooltipText(s);

    }

    public MenuItem.OnMenuItemClickListener getOnMenuItemClickListener() {
        return getMenuItemClickListener();
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);

    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            updateAndroidFont();
        }
    }

    private void updateAndroidFont() {
            // Find the TextView in the menu item
            View actionView = this;
            if (actionView instanceof ViewGroup) {
                TextView textView = findTextView((ViewGroup) actionView);
                if (textView != null) {
                    textView.setTypeface(currentFont.getFont());
                    textView.setTextSize(currentFont.getSize());
                }
            }

    }

    private TextView findTextView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                return (TextView) child;
            } else if (child instanceof ViewGroup) {
                TextView textView = findTextView((ViewGroup) child);
                if (textView != null) {
                    return textView;
                }
            }
            else if(child instanceof Container)
            {
                TextView textView = findTextView(((Container) child).getView());
                if (textView != null) {
                    return textView;
                }
            }
        }
        return null;
    }

    @Override
    public Font getFont() {
        return currentFont;
    }

    @Override
    public Dimension getPreferredSize() {
        // Crée un TextView temporaire pour mesurer la taille du texte
        TextView tempView = new TextView(StartAndroidApp.getAppContext());
        tempView.setTypeface(currentFont.getFont());
        tempView.setTextSize(currentFont.getSize());
        tempView.setText(text);

        // Mesure la taille requise
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tempView.measure(widthMeasureSpec, heightMeasureSpec);

        // Ajoute un padding pour l'icône si présent
        int iconWidth = icon != null ? icon.getIconWidth() + 10 : 0;

        return new Dimension(
                tempView.getMeasuredWidth() + iconWidth + 40, // 40px de padding supplémentaire
                Math.max(tempView.getMeasuredHeight(), icon != null ? icon.getIconHeight() : 0) + 20
        );
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
            View actionView = this;
            actionView.setMinimumWidth(dimension.width);
            actionView.setMinimumHeight(dimension.height);

    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public Dimension getSize() {
        View actionView = this;
        return new Dimension(actionView.getWidth(), actionView.getHeight());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}