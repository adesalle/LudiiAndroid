package androidUtils.swing;

import android.annotation.SuppressLint;
import android.content.Context;
import androidUtils.awt.Color;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;


import androidUtils.awt.Font;
import androidUtils.awt.event.FocusEvent;
import androidUtils.awt.event.FocusListener;
import playerAndroid.app.StartAndroidApp;

@SuppressLint("ViewConstructor")
public class JTextArea extends androidx.appcompat.widget.AppCompatEditText{

    public JTextArea() {
        super(StartAndroidApp.getAppContext());
    }

    public JTextArea(String text) {
        super(StartAndroidApp.getAppContext());
        setText(text);
    }
    public void setFont(Font font)
    {
        setTypeface(font.getFont());
    }

    public void setForeground(Color color)
    {
        setTextColor(color.toArgb());
    }

    public void setBounds(int lm, int tm, int height, int width)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        params.setMargins(lm, tm, 0, 0);

        setLayoutParams(params);
    }

    public void setCaretColor(Color color) {}

    public void setOpaque(boolean opaque)
    {
        if (opaque) {

            setBackgroundColor(Color.GRAY.toArgb());
        } else {
            setBackground(null);
        }
    }

    public void setLineWrap(boolean wrapLine)
    {
        if (wrapLine) {
            setSingleLine(false);
            setEllipsize(null);
        } else {
            setSingleLine(true);
            setEllipsize(TextUtils.TruncateAt.END);
        }
    }


    public void setWrapStyleWord(boolean wrapStyleWord)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (wrapStyleWord) {
                setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED);
                setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_FULL);
            } else {
                setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE);
                setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NONE);
            }
        } else {
            if (wrapStyleWord) {

                setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_FULL);
            } else {

                setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NONE);
            }
        }
    }

    public void setText(String text)
    {
        super.setText(text);
    }

    public void setVisible(boolean visible)
    {
        if(visible)setVisibility(View.VISIBLE);
        else setVisibility(View.INVISIBLE);
    }


    public void setEditable(boolean editable) {
        setFocusable(editable);
        setFocusableInTouchMode(editable);
        setCursorVisible(editable);
        setLongClickable(editable);

        if (editable) {
            setKeyListener(new android.text.method.TextKeyListener(android.text.method.TextKeyListener.Capitalize.NONE, false));
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        } else {
            setKeyListener(null);
            setInputType(InputType.TYPE_NULL);
        }
    }
    public void addFocusListener(FocusListener focusListener) {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                FocusEvent event = new FocusEvent(JTextArea.this, hasFocus ? FocusEvent.FOCUS_GAINED : FocusEvent.FOCUS_LOST);
                if (hasFocus) {
                    focusListener.focusGained(event);
                } else {
                    focusListener.focusLost(event);
                }
            }
        });
    }
}

