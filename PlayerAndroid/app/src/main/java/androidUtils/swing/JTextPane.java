package androidUtils.swing;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidUtils.awt.Dimension;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import androidUtils.awt.event.FocusListener;
import androidUtils.awt.event.KeyEvent;
import androidUtils.awt.event.KeyListener;
import androidUtils.awt.event.MouseAdapter;
import androidUtils.awt.event.MouseEvent;
import androidUtils.swing.event.DocumentListener;
import androidUtils.swing.text.AttributeSet;
import androidUtils.swing.text.BadLocationException;
import androidUtils.swing.text.DefaultHighlighter;
import androidUtils.swing.text.Document;
import androidUtils.swing.text.DocumentFilter;
import androidUtils.swing.text.HTMLDocument;
import androidUtils.swing.text.Highlighter;
import androidUtils.swing.text.JTextComponent;
import androidUtils.swing.text.Style;
import androidUtils.swing.text.StyledDocument;
import playerAndroid.app.StartAndroidApp;

public class JTextPane extends JTextComponent {
    private final List<KeyListener> keyListeners = new ArrayList<>();
    private final List<FocusListener> focusListeners = new ArrayList<>();

    private final Caret caret;
    private StyledDocument styledDocument;
    private boolean focusTraversalKeysEnabled = true;

    private Highlighter highlighter;

    public JTextPane() {
        super(StartAndroidApp.getAppContext());
        caret = new Caret(this);
        setupKeyListeners();
        setupFocusListeners();
    }

    private void setupKeyListeners() {
        // Handle physical key presses
        setOnKeyListener((v, keyCode, androidEvent) -> {
            int swingEventId = androidEvent.getAction() == android.view.KeyEvent.ACTION_DOWN
                    ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED;

            KeyEvent swingEvent = new KeyEvent(androidEvent, swingEventId);

            for (KeyListener listener : keyListeners) {
                if (swingEventId == KeyEvent.KEY_PRESSED) {
                    listener.keyPressed(swingEvent);
                } else {
                    listener.keyReleased(swingEvent);
                }
            }
            return false;
        });


        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && before == 0) { // Character typed
                    KeyEvent swingEvent = new KeyEvent(
                            new android.view.KeyEvent(KeyEvent.VK_DOWN, 0),
                            KeyEvent.KEY_TYPED);
                    swingEvent.setKeyChar(s.charAt(start));

                    for (KeyListener listener : keyListeners) {
                        listener.keyTyped(swingEvent);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public String getText(int start, int end)
    {
        char[] result = new char[end - start];
        getEditableText().getChars(start, end, result, 0);
        return Arrays.toString(result);
    }
    private void setupFocusListeners() {
        setOnFocusChangeListener((v, hasFocus) -> {
            for (FocusListener listener : focusListeners) {
                if (hasFocus) {
                    listener.focusGained(null); // Passer un FocusEvent approprié si disponible
                } else {
                    listener.focusLost(null);
                }
            }
        });
    }

    public void addKeyListener(KeyListener listener) {
        if (listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);
        }
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void setPreferredSize(Dimension dimension) {
        setMeasuredDimension(dimension.width, dimension.height);
    }

    public Caret getCaret() {
        return caret;
    }

    public void setCaretPosition(int newCaretPosition) {
        caret.setDot(newCaretPosition);
    }


    public void setSelectionStart(int selectionStart) {
        if (selectionStart >= 0 && selectionStart <= getText().length()) {
            int end = getSelectionEnd();
            setSelection(selectionStart, end);
        }
    }

    public void setSelectionEnd(int selectionEnd) {
        if (selectionEnd >= 0 && selectionEnd <= getText().length()) {
            int start = getSelectionStart();
            setSelection(start, selectionEnd);
        }
    }


    public HTMLDocument getDocument() {
        return super.getDocument();
    }


    public StyledDocument getStyledDocument() {
        if (styledDocument == null) {
            styledDocument = new StyledDocument() {

                private final SpannableStringBuilder editable() {
                    return (SpannableStringBuilder) getEditableText();
                }


                @Override
                public int getLength() {
                    return editable().length();
                }

                @Override
                public String getText(int offset, int length) throws BadLocationException {
                    if (offset < 0 || offset + length > editable().length())
                        throw new BadLocationException("Out of bounds", offset);

                    return editable().subSequence(offset, offset + length).toString();
                }

                @Override
                public SpannableStringBuilder getDocument() {
                    return editable();
                }

                @Override
                public String getText() {
                    return editable().toString();
                }

                @Override
                public StyleSheet getStyleSheet() {
                    return null;
                }

                @Override
                public void addDocumentListener(DocumentListener listenerText1) {

                }

                @Override
                public void removeDocumentListener(DocumentListener listener) {

                }


                @Override
                public void insertString(int offset, String str, AttributeSet attr)
                        throws BadLocationException {

                    if (offset < 0 || offset > editable().length())
                        throw new BadLocationException("Out of bounds", offset);

                    editable().insert(offset, str);

                    if (attr instanceof Style && attr != null) {
                        ((Style) attr).apply(editable(), offset, offset + str.length());
                    }
                }

                @Override
                public void remove(int offset, int length) throws BadLocationException {
                    if (offset < 0 || offset + length > editable().length())
                        throw new BadLocationException("Out of bounds", offset);

                    editable().delete(offset, offset + length);
                }


                @Override public void setLogicalStyle(int pos, Style s) { }
                @Override public Style addStyle(String name, Style parent) { return null; }
                @Override public void setHTML(String htmlText) { }


                private final java.util.Map<Object,Object> properties = new java.util.HashMap<>();
                private DocumentFilter filter;

                @Override public void putProperty(Object k, Object v) { properties.put(k, v); }
                @Override public Object getProperty(Object k) { return properties.get(k); }
                @Override public void setDocumentFilter(DocumentFilter df) { filter = df; }
            };
        }
        return styledDocument;
    }


    public void setFocusTraversalKeysEnabled(boolean enabled) {
        this.focusTraversalKeysEnabled = enabled;
        if (!enabled) {
            setFocusableInTouchMode(true);
        }
    }

    public void addFocusListener(FocusListener focusListener) {
        if (focusListener != null && !focusListeners.contains(focusListener)) {
            focusListeners.add(focusListener);
        }
    }

    public void removeFocusListener(FocusListener focusListener) {
        focusListeners.remove(focusListener);
    }

    public int getCaretPosition() {
        return caret.getDot();
    }

    public void select(int pos, int i) {
        setSelection(pos, i);
    }

    public void replaceSelection(String content) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        getEditableText().replace(start, end, content);
    }

    public Rectangle modelToView(int caretPosition) {
        if (caretPosition < 0 || caretPosition > length()) {
            return null;
        }

        android.text.Layout layout = getLayout();
        if (layout == null) {

            return null;
        }

        int line = layout.getLineForOffset(caretPosition);

        float x = layout.getPrimaryHorizontal(caretPosition);


        int lineTop    = layout.getLineTop(line);
        int lineBottom = layout.getLineBottom(line);


        int left = (int) (x + getTotalPaddingLeft() - getScrollX());
        int top  = lineTop    + getTotalPaddingTop()  - getScrollY();
        int h    = lineBottom - lineTop;

        return new Rectangle(left, top, 1, h);
    }

    public int viewToModel(Point point) {
        if (point == null) return -1;

        android.text.Layout layout = getLayout();
        if (layout == null) {

            return -1;
        }

        int x = point.x - getTotalPaddingLeft() + getScrollX();
        int y = point.y - getTotalPaddingTop()  + getScrollY();

        int line   = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);


        int len = getText() != null ? getText().length() : 0;
        if (offset < 0)       offset = 0;
        else if (offset > len) offset = len;

        return offset;
    }
    public Highlighter getHighlighter() {
        if (highlighter == null) {
            highlighter = new DefaultHighlighter();
        }
        return highlighter;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addMouseListener(MouseAdapter mouseAdapter) {
        setOnTouchListener((v, event) -> {
            MouseEvent mouseEvent = new MouseEvent(event, this);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mouseAdapter.mousePressed(mouseEvent);
                    break;
                case MotionEvent.ACTION_UP:
                    mouseAdapter.mouseReleased(mouseEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mouseAdapter.mouseDragged(mouseEvent);
                    break;
            }
            return false;
        });
    }

    public void setCharacterAttributes(AttributeSet attr, boolean replace) {
        int start = getSelectionStart();
        int end = getSelectionEnd();

        if (start != end && attr instanceof Style) {
            ((Style) attr).apply(getEditableText(), start, end);
        }
    }

    public void scrollRectToVisible(Rectangle r) {

    }
}