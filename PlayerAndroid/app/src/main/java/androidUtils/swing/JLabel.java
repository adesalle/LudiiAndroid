package androidUtils.swing;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.MouseAdapter;
import playerAndroid.app.StartAndroidApp;


import static androidUtils.swing.SwingConstants.*;

import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class JLabel extends androidx.appcompat.widget.AppCompatTextView implements ViewComponent{

    List<MouseAdapter> listeners = new ArrayList<>();

    public JLabel(ImageIcon icon)
    {
        this();
        setBackground(IconToDrawable.convertIconToDrawable(icon, this));
    }
    public JLabel(String text) {
        super(StartAndroidApp.getAppContext());
        setText(text);
    }
    public JLabel() {
        super(StartAndroidApp.getAppContext());
        setText("");
    }

    private void setupListener()
    {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isUsed = false;
                for (MouseAdapter listener : listeners) {
                    if (listener.onTouch(v, event)) {
                        isUsed = true;
                    }

                }
                return isUsed;
            }
        });
    }

    public void setHorizontalAlignment(int alignment) {
        switch (alignment) {
            case LEFT:
                setGravity(android.view.Gravity.LEFT);
                break;
            case CENTER:
                setGravity(android.view.Gravity.CENTER);
                break;
            case RIGHT:
                setGravity(android.view.Gravity.RIGHT);
                break;
        }
    }

    public void setBounds(int x, int y, int w, int h) {
        setLeft(x);
        setTop(y);
        setSize(w, h);
    }

    public void setLabelFor(View view) {
        // Pour Android, nous utilisons setLabelFor avec l'ID de la vue
        if (view != null && view.getId() != View.NO_ID) {
            setLabelFor(view.getId());
        } else {
            // Si la vue n'a pas d'ID, on lui en attribue un
            if (view != null && view.getId() == View.NO_ID) {
                view.setId(View.generateViewId());
            }
            if (view != null) {
                setLabelFor(view.getId());
            }
        }
    }

    public void setFont(Font font) {
        if (font != null) {
            setTypeface(font.getFont());
            setTextSize(font.getSize());
            int style = Typeface.NORMAL;
            if (font.isBold() && font.isItalic()) {
                style = Typeface.BOLD_ITALIC;
            } else if (font.isBold()) {
                style = Typeface.BOLD;
            } else if (font.isItalic()) {
                style = Typeface.ITALIC;
            }
            setTypeface(font.getFont(), style);
        }
    }

    public Font getFontMetrics()
    {
        return new Font(getTypeface());
    }



    public void setVisible(boolean b) {
        if(b) setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);
    }

    public void setIcon(ImageIcon icon) {
        super.setBackgroundDrawable(icon.getDrawable());
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        return new Font(getTypeface(), (int) getTextSize());
    }

    public List<MouseAdapter> getMouseListeners() {
        return listeners;
    }

    public void addMouseListener(MouseAdapter listener) {
        // Implémentation basique de l'écouteur souris
        listeners.add(listener);
    }

    @Override
    public Dimension getPreferredSize() {
        // Calcule la taille préférée basée sur le texte
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        // Pour Android, on utilise setMinimumWidth/Height comme approximation
        if (dimension != null) {
            setMinimumWidth(dimension.width);
            setMinimumHeight(dimension.height);
        }
    }

    public void setSize(int width, int height)
    {
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }

    @Override
    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setToolTipText(String s) {
        super.setTooltipText(s);
    }
}