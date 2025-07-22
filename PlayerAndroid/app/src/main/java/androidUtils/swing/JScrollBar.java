package androidUtils.swing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.AdjustmentEvent;
import androidUtils.awt.event.AdjustmentListener;
import playerAndroid.app.StartAndroidApp;

import java.util.ArrayList;
import java.util.List;

public class JScrollBar extends LinearLayout implements ViewComponent {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private final SeekBar seekBar;
    private int orientation;
    private int value;
    private int extent;
    private int min;
    private int max;
    private boolean enabled = true;
    private final List<AdjustmentListener> listeners = new ArrayList<>();

    public JScrollBar() {
        this(StartAndroidApp.getAppContext(), VERTICAL, 0, 10, 0, 100);
    }

    public JScrollBar(int orientation) {
        this(StartAndroidApp.getAppContext(), orientation, 0, 10, 0, 100);
    }

    public JScrollBar(int orientation, int value, int extent, int min, int max) {
        this(StartAndroidApp.getAppContext(), orientation, value, extent, min, max);
    }

    public JScrollBar(Context context, int orientation, int value, int extent, int min, int max) {
        super(context);
        this.orientation = orientation;
        this.value = value;
        this.extent = extent;
        this.min = min;
        this.max = max;

        // Configuration de la SeekBar Android
        seekBar = new SeekBar(context);
        seekBar.setThumbOffset(0);
        seekBar.setProgressDrawable(new ColorDrawable(Color.TRANSPARENT));
        seekBar.setThumb(new ColorDrawable(Color.GRAY));

        if (orientation == HORIZONTAL) {
            setOrientation(LinearLayout.HORIZONTAL);
            seekBar.setRotation(0);
        } else {
            setOrientation(LinearLayout.VERTICAL);
            seekBar.setRotation(90); // Rotation pour un effet vertical
        }

        // Configuration des valeurs
        updateSeekBarRange();
        seekBar.setProgress(value);

        // Listener pour les changements
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setValue(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                fireAdjustmentEvent(AdjustmentEvent.TRACK);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fireAdjustmentEvent(AdjustmentEvent.BLOCK_DECREMENT);
            }
        });

        addView(seekBar, new LayoutParams(
                orientation == HORIZONTAL ? LayoutParams.MATCH_PARENT : 20,
                orientation == VERTICAL ? LayoutParams.MATCH_PARENT : 20
        ));
    }

    private void updateSeekBarRange() {
        seekBar.setMax(max - extent);
        seekBar.setProgress(value);
    }

    public int getOrientation() {
        return orientation;
    }

    public void setValue(int value) {
        if (value < min) value = min;
        if (value > max - extent) value = max - extent;

        if (this.value != value) {
            this.value = value;
            seekBar.setProgress(value);
            fireAdjustmentEvent(AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED);
        }
    }

    public int getValue() {
        return value;
    }

    public void setExtent(int extent) {
        this.extent = extent;
        updateSeekBarRange();
    }

    public int getExtent() {
        return extent;
    }

    public void setRange(int min, int max, int extent) {
        this.min = min;
        this.max = max;
        this.extent = extent;
        updateSeekBarRange();
    }

    public int getMinimum() {
        return min;
    }

    public int getMaximum() {
        return max;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        seekBar.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addAdjustmentListener(AdjustmentListener listener) {
        listeners.add(listener);
    }

    public void removeAdjustmentListener(AdjustmentListener listener) {
        listeners.remove(listener);
    }

    private void fireAdjustmentEvent(int type) {
        AdjustmentEvent e = new AdjustmentEvent(this, AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED, type, value);
        for (AdjustmentListener listener : listeners) {
            listener.adjustmentValueChanged(e);
        }
    }

    // Méthodes ViewComponent
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                orientation == HORIZONTAL ? 100 : 20,
                orientation == VERTICAL ? 100 : 20
        );
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        // Implémentation optionnelle pour forcer une taille
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        // Non applicable pour une scrollbar
    }

    @Override
    public Font getFont() {
        return null;
    }

}