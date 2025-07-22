package androidUtils.swing;

import android.view.ViewGroup;
import android.widget.SeekBar;
import androidUtils.swing.event.ChangeEvent;
import androidUtils.swing.event.ChangeListener;
import playerAndroid.app.StartAndroidApp;

import java.util.ArrayList;
import java.util.List;

public class JSlider extends androidx.appcompat.widget.AppCompatSeekBar {
    private int minorTickSpacing = 1;
    private int majorTickSpacing = 10;
    private int minimum = 0;
    private int maximum = 100;
    private List<ChangeListener> changeListeners = new ArrayList<>();

    public JSlider() {
        super(StartAndroidApp.getAppContext());
        init();
    }
    public JSlider(int min, int max) {
        super(StartAndroidApp.getAppContext());
        minimum = min;
        maximum = max;
        init();
    }


    private void init() {
        setMax(maximum - minimum);
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fireStateChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void setMinorTickSpacing(int spacing) {
        this.minorTickSpacing = spacing;
    }

    public void setMajorTickSpacing(int spacing) {
        this.majorTickSpacing = spacing;
    }

    public void setMinimum(int min) {
        this.minimum = min;
        setMax(maximum - minimum);
    }

    public void setMaximum(int max) {
        this.maximum = max;
        setMax(maximum - minimum);
    }

    public int getValue() {
        return getProgress() + minimum;
    }

    public void setValue(int value) {
        setProgress(value - minimum);
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    private void fireStateChanged() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(event);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }
}