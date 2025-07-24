package androidUtils.swing.text;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Arrays;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Point;
import androidUtils.swing.Caret;
import androidUtils.swing.ViewComponent;

public class JTextComponent extends AppCompatEditText implements ViewComponent{
    private HTMLDocument document;
    private Highlighter highlighter;
    private EditorKit editorKit;
    private boolean editable = true;
    private boolean isHtmlMode = false;

    private final Caret caret = new Caret(this);

    public JTextComponent(Context context) {
        super(context);
        init();
    }

    public JTextComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        this.document = new HTMLDocument();
        this.highlighter = new DefaultHighlighter();
        this.editorKit = new HTMLEditorKit();

        // Gestion des événements
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                document.setHTML(s.toString());
            }
        });
    }

    public String getText(int start, int end)
    {
        char[] result = new char[end - start];
        getEditableText().getChars(start, end, result, 0);
        return Arrays.toString(result);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void setDocument(HTMLDocument document) {
        this.document = document;
        setText(document.getDocument());
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    public EditorKit getEditorKit() {
        return editorKit;
    }

    public void setEditorKit(EditorKit editorKit) {
        this.editorKit = editorKit;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        setFocusable(editable);
        setFocusableInTouchMode(editable);
        setClickable(editable);
        setLongClickable(editable);
    }

    public boolean isEditable() {
        return editable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (highlighter != null) {
            highlighter.paint(canvas);
        }
    }

    public Caret getCaret() {
        return caret;
    }

    public void setCaretPosition(int newCaretPosition) {
        caret.setDot(newCaretPosition);
    }

    public int getCaretPosition() {
        return caret.getDot();
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

    public void replaceSelection(String text) {
        int position = caret.getDot();
        setText(text.toCharArray() , position, text.length());
    }

    public int viewToModel(Point pt) {
        if (pt == null) return -1;

        android.text.Layout layout = getLayout();
        if (layout == null) return -1;


        int x = pt.x - getTotalPaddingLeft() + getScrollX();
        int y = pt.y - getTotalPaddingTop()  + getScrollY();

        int line   = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        int len = getText() != null ? getText().length() : 0;
        return Math.max(0, Math.min(offset, len));
    }

    public String getSelectedText() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        return getText(start, end);
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate preferred size based on text content and font metrics
        int width = (int) getPaint().measureText(getText().toString())
                + getPaddingLeft() + getPaddingRight();
        int height = getLineHeight() * getLineCount()
                + getPaddingTop() + getPaddingBottom();
        return new Dimension(width, height);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        // Set minimum dimensions that the view will try to respect
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
        requestLayout(); // Request layout update
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            // Apply font properties
            setTypeface(font.getFont());
            setTextSize(font.getSize());

            // Update padding based on font size
            int padding = (int)(font.getSize() * 0.2f); // 20% of font size
            setPadding(padding, padding, padding, padding);
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        return new Font(getTypeface());
    }

    public void setContentType(String contentType) {
        isHtmlMode = "text/html".equalsIgnoreCase(contentType);
    }

    public Style addStyle(String name, Style parent) {
        return new DefaultStyle(name, parent);
    }

    public void setVisible(boolean visible) {
        setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void putClientProperty(Object key, Object value) {
        // Implémentation simplifiée - stocke les propriétés dans les tags
        //setTag(key.hashCode(), value);
    }

    public Object getClientProperty(Object key) {
        return getTag(key.hashCode());
    }
}