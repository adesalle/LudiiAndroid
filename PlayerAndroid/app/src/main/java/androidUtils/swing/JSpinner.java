package androidUtils.swing;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatButton;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.swing.event.ChangeEvent;
import androidUtils.swing.event.ChangeListener;
import playerAndroid.app.StartAndroidApp;

public class JSpinner extends LinearLayout implements ViewComponent {
    private SpinnerModel model;
    private Editor editor;
    private AppCompatButton nextButton;
    private AppCompatButton prevButton;
    private List<ChangeListener> changeListeners = new ArrayList<>();
    private boolean editable = true;

    // Constructeurs
    public JSpinner() {
        this(new SpinnerNumberModel(0, null, null, 1));
    }

    public JSpinner(SpinnerModel model) {
        super(StartAndroidApp.getAppContext());
        this.model = model;
        initComponents();
    }

    private void initComponents() {
        setOrientation(HORIZONTAL);

        // Bouton précédent
        prevButton = new AppCompatButton(getContext());
        prevButton.setText("◀");
        prevButton.setOnClickListener(v -> decrement());
        addView(prevButton);

        // Éditeur
        editor = new Editor(StartAndroidApp.getAppContext());
        editor.setFocusable(editable);
        editor.setEditable(editable);
        updateEditorFormat();
        addView(editor);

        // Bouton suivant
        nextButton = new AppCompatButton(getContext());
        nextButton.setText("▶");
        nextButton.setOnClickListener(v -> increment());
        addView(nextButton);

        updateFromModel();
    }

    // Méthodes principales
    public void setModel(SpinnerModel model) {
        this.model = model;
        updateEditorFormat();
        updateFromModel();
    }

    public SpinnerModel getModel() {
        return model;
    }

    public Object getValue() {
        try {
            commitEdit();
        } catch (ParseException e) {
            // Garder l'ancienne valeur
        }
        return model.getValue();
    }

    public void setValue(Object value) {
        model.setValue(value);
        updateFromModel();
    }

    public void commitEdit() throws ParseException {
        Object value = editor.getValue();
        model.setValue(value);
    }

    // Méthodes d'incrémentation
    private void increment() {
        Object next = model.getNextValue();
        if (next != null) {
            setValue(next);
            fireStateChanged();
        }
    }

    private void decrement() {
        Object prev = model.getPreviousValue();
        if (prev != null) {
            setValue(prev);
            fireStateChanged();
        }
    }

    // Gestion des événements
    public void addChangeListener(ChangeListener l) {
        changeListeners.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(l);
    }

    protected void fireStateChanged() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : changeListeners) {
            l.stateChanged(e);
        }
    }

    // Mise à jour de l'interface
    private void updateFromModel() {
        editor.setValue(model.getValue());
    }

    private void updateEditorFormat() {
        if (model instanceof SpinnerNumberModel) {
            editor.setFormatter(new NumberFormatter());
        } else {
            editor.setFormatter(new DefaultFormatter());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return null;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {

    }

    @Override
    public void setSize(Dimension dimension) {

    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Font getFont() {
        return null;
    }

    public ArrayList<ChangeListener> getChangeListeners() {
        return (ArrayList<ChangeListener>) changeListeners;
    }

    // Implémentation des interfaces
    public interface SpinnerModel {
        Object getValue();
        void setValue(Object value);
        Object getNextValue();
        Object getPreviousValue();
    }

    public static class SpinnerNumberModel implements SpinnerModel {
        private Number value;
        private Comparable min;
        private Comparable max;
        private Number step;

        public SpinnerNumberModel(Number value, Comparable min, Comparable max, Number step) {
            this.value = value;
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public void setMinimum(Comparable min) { this.min = min; }
        public void setMaximum(Comparable max) { this.max = max; }
        public void setStepSize(Number step) { this.step = step; }

        @Override
        public Object getValue() { return value; }

        @Override
        public void setValue(Object value) {
            if (((min != null) && (min.compareTo(value) > 0)) ||
                    ((max != null) && (max.compareTo(value) < 0))){
                throw new IllegalArgumentException("value out of range");
            }
            this.value = (Number)value;
        }

        @Override
        public Object getNextValue() {
            double next = value.doubleValue() + step.doubleValue();
            return (max == null || max.compareTo(next) >= 0) ? next : null;
        }

        @Override
        public Object getPreviousValue() {
            double prev = value.doubleValue() - step.doubleValue();
            return (min == null || min.compareTo(prev) <= 0) ? prev : null;
        }
    }

    public class Editor extends LinearLayout {
        private JFormattedTextField textField;

        public Editor(Context context) {
            super(context);
            textField = new JFormattedTextField();
            addView(textField);
        }

        public View getComponent(int index) {
            if (index == 0) {
                return textField.getTextField();
            }
            throw new ArrayIndexOutOfBoundsException();
        }

        public JFormattedTextField getTextField() {
            return textField;
        }

        public void setEditable(boolean editable) {
            textField.setEditable(editable);
        }

        public void setFormatter(AbstractFormatter numberFormatter) {
            textField.setFormatter(numberFormatter);
        }

        public void setValue(Object value) {
            textField.setValue(value);
        }

        public Object getValue() {
            return textField.getValue();
        }
    }


    // Getters pour les composants internes
    public Editor getEditor() {
        return editor;
    }

}