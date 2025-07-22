package androidUtils.swing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.FocusAdapter;
import androidUtils.awt.event.FocusEvent;
import androidUtils.awt.event.KeyEvent;
import androidUtils.awt.event.KeyListener;
import androidUtils.swing.border.Border;
import androidUtils.swing.event.DocumentEvent;
import androidUtils.swing.event.DocumentListener;
import androidUtils.swing.event.EventType;
import androidUtils.swing.text.Document;
import androidUtils.swing.text.HTMLDocument;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.display.dialogs.util.MaxLengthTextDocument;

public class JTextField extends androidx.appcompat.widget.AppCompatEditText implements ViewComponent {
    private static final float DEFAULT_CHAR_WIDTH = 10f;
    private int columns = 0;
    private Rect bounds = new Rect();
    private DocumentListener documentListener;
    private Border border;
    private Paint paint;

    private ArrayList<KeyListener> keyListeners = new ArrayList<>();

    public JTextField() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JTextField(String text)
    {
        this();
        setText(text);
    }

    public JTextField(int columns) {
        super(StartAndroidApp.getAppContext());
        this.columns = columns;
        init();
        updateWidth();
    }

    private void init() {
        setSingleLine(true);
        setMaxLines(1);
        paint = new Paint();
        paint.setColor(0xFFFFFFFF); // White background by default
        paint.setStyle(Paint.Style.FILL);
    }

    public void addActionListener(Runnable action) {
        setOnEditorActionListener((v, actionId, event) -> {
            action.run();
            return true;
        });
    }

    public void addDocumentListener(DocumentListener listener) {
        this.documentListener = listener;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (documentListener != null) {
                    documentListener.insertUpdate(new DocumentEvent(JTextField.this.getDocument(), 0, s.length(), EventType.CHANGE));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (documentListener != null) {
                    documentListener.changedUpdate(new DocumentEvent(JTextField.this.getDocument(),0, s.length(), EventType.CHANGE));
                }
            }
        });
    }

    public void setColumns(int columns) {
        this.columns = columns;
        updateWidth();
    }

    public int getColumns() {
        return columns;
    }

    private void updateWidth() {
        if (columns > 0) {
            float charWidth = getPaint().measureText("M");
            setMinWidth((int) (columns * charWidth));
            setWidth((int) (columns * charWidth));
        }
    }

    public void setText(String text) {
        super.setText(text);
    }


    public String getTextContent() {
        return getText().toString();
    }

    public Document getDocument() {
        HTMLDocument doc= new HTMLDocument();
        doc.setHTML(getTextContent());
        return doc;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds = new Rect(x, y, x + width, y + height);
        setX(x);
        setY(y);
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }

    public Rect getBounds() {
        return new Rect(bounds);
    }
    

    public void setPreferredSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
    }
    @Override
    public void setSize(Dimension dimension) {
        if (dimension != null) {
            setSize(dimension.width, dimension.height);
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            setTypeface(font.getFont());
            setTextSize(font.getSize());
        }
    }

    @Override
    public Font getFont() {
        return new Font(getTypeface(), (int) getTextSize());
    }

    @Override
    public Dimension getPreferredSize() {
        // Calcul basé sur le nombre de colonnes et la taille de police
        float charWidth = getPaint().measureText("M");
        int width = columns > 0 ? (int)(columns * charWidth) : (int)(DEFAULT_CHAR_WIDTH * 20); // Valeur par défaut pour 20 caractères
        int height = (int)(getTextSize() * 1.5); // Hauteur basée sur la taille de police

        return new Dimension(width, height);
    }

    public void setSize(int width, int height) {
        setRight(getLeft() + width);
        setBottom(getTop() + height);
    }

    public void setEditable(boolean editable) {
        setEnabled(editable);
        setFocusable(editable);
        setFocusableInTouchMode(editable);
    }

    public void setCaretPosition(int position) {
        if (position >= 0 && position <= getText().length()) {
            setSelection(position);
        }
    }

    public int getCaretPosition() {
        return getSelectionStart();
    }


    public Font getFontMetrics(Font font) {
        return font;
    }

    public void addFocusListener(FocusAdapter listener) {
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                listener.focusGained(new FocusEvent(this));
            } else {
                listener.focusLost(new FocusEvent(this));
            }
        });
    }

    public void setDocument(MaxLengthTextDocument maxLength1) {

    }

    public void setVisible(boolean b) {
        setVisibility(b ? VISIBLE : INVISIBLE);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        // Draw background
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw border
        if (border != null) {
            border.paintBorder(this, new Graphics(canvas));
        }


        super.dispatchDraw(canvas);
    }
    public void setBorder(Border b) {
        border = b;
    }

    public ArrayList<KeyListener> getKeyListeners() {
        return new ArrayList<>(keyListeners);
    }

    public void removeKeyListener(KeyListener listener) {
        if (listener != null) {
            keyListeners.remove(listener);
        }
    }

    public void addKeyListener(KeyListener listener) {
        if (listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);

            // Configurer l'écouteur de touches seulement si c'est le premier
            if (keyListeners.size() == 1) {
                setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
                        // Convertir l'événement Android en événement AWT
                        KeyEvent awtEvent = new KeyEvent(
                                event
                        );

                        // Notifier tous les écouteurs
                        for (KeyListener kl : keyListeners) {
                            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                                kl.keyPressed(awtEvent);
                            } else if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                                kl.keyReleased(awtEvent);
                            }

                            // Pour les événements de frappe (combinaison pressé+relâché)
                            if (event.getAction() == android.view.KeyEvent.ACTION_UP &&
                                    event.getUnicodeChar() != 0) {
                                kl.keyTyped(awtEvent);
                            }
                        }

                        return false; // Ne pas consommer l'événement
                    }
                });
            }
        }
    }

}