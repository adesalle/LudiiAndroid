package androidUtils.swing;

import static android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.event.KeyEvent;
import androidUtils.awt.event.KeyListener;
import androidUtils.swing.text.Document;
import androidUtils.swing.text.HTMLDocument;
import androidUtils.swing.text.Style;
import playerAndroid.app.StartAndroidApp;

public class JEditorPane extends AppCompatTextView implements ViewComponent {
    public static final Object HONOR_DISPLAY_PROPERTIES = new Object();


    private boolean isHtmlMode = false;
    private boolean isEditable = false;
    private final Map<String, Style> styles = new HashMap<>();
    private WebView webView;

    public JEditorPane() {
        super(StartAndroidApp.getAppContext());
        init();
    }

    public JEditorPane(AttributeSet attrs) {
        super(StartAndroidApp.getAppContext(), attrs);
        init();
    }

    public JEditorPane(String contentType, String text) {
        super(StartAndroidApp.getAppContext());
        setContentType(contentType);
        setText(text);
        init();
    }

    private void init() {

        putClientProperty(HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        setTextColor(Color.BLACK);
        setTypeface(Typeface.DEFAULT);
        setFocusable(true);
        setClickable(true);

        // Initialize WebView for handling HTML
        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }

    public void putClientProperty(Object key, Object value) {
        if (key == HONOR_DISPLAY_PROPERTIES && value instanceof Boolean) {
            boolean honor = (Boolean) value;
            if (honor) {
                // Enable Android's system UI visibility flags
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            } else {
                // Disable special display properties
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    // Helper method to mimic Swing's getClientProperty
    public Object getClientProperty(Object key) {
        if (key == HONOR_DISPLAY_PROPERTIES) {
            return (getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0;
        }
        return null;
    }



    // Set content type (text/plain or text/html)
    public void setContentType(String contentType) {
        isHtmlMode = "text/html".equalsIgnoreCase(contentType);
    }

    // Set text (handles both plain text and HTML)
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHtmlMode) {
            Spanned spanned = Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_LEGACY);
            super.setText(spanned, type);
        } else {
            super.setText(text, type);
        }
    }

    // Get text (returns HTML if in HTML mode)
    public String getTextContent() {

        return isHtmlMode ? Html.toHtml(charSequenceToSpanned(getText()),TO_HTML_PARAGRAPH_LINES_CONSECUTIVE) : getText().toString();
    }


    // Set editable mode
    public void setEditable(boolean editable) {
        this.isEditable = editable;
        setFocusable(editable);
        setFocusableInTouchMode(editable);
    }

    // Handle hyperlink clicks
    public void setHyperlinkListener(View.OnClickListener listener) {
        setOnClickListener(listener);
    }

    // Load HTML content using WebView (for full HTML support)
    public void loadHtmlContent(String html) {
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    public WebView getWebView() {
        return webView;
    }

    public static Spanned charSequenceToSpanned(CharSequence text) {
        return Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_LEGACY);
    }

    public Document getDocument() {
        HTMLDocument document = new HTMLDocument();
        document.setHTML(getText().toString());
        return document;
    }

    private List<KeyListener> keyListeners = new ArrayList<>();

    public void addKeyListener(KeyListener listener) {
        if (listener != null && !keyListeners.contains(listener)) {
            keyListeners.add(listener);
            setupKeyListener();
        }
    }

    private void setupKeyListener() {
        setOnKeyListener((v, keyCode, event) -> {
            for (KeyListener listener : keyListeners) {
                switch (event.getAction()) {
                    case KeyEvent.KEY_PRESSED:
                        listener.keyPressed(new KeyEvent(
                                event
                        ));
                        break;

                    case KeyEvent.KEY_RELEASED:
                        listener.keyReleased(new KeyEvent(
                                event
                        ));
                        break;
                }
            }
            return false;
        });
    }

    @Override
    public Dimension getPreferredSize() {
        // Mesure du texte pour déterminer la taille préférée
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // Si non mesuré, utiliser les dimensions actuelles
        if (width <= 0 || height <= 0) {
            width = getWidth();
            height = getHeight();
        }

        // Valeurs par défaut si toujours indéterminé
        if (width <= 0) width = 200; // Largeur par défaut
        if (height <= 0) height = 100; // Hauteur par défaut

        return new Dimension(width, height);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        if (dimension != null) {
            setMinimumWidth(dimension.width);
            setMinimumHeight(dimension.height);
        }
    }

    @Override
    public void setSize(Dimension dimension) {
        setRight(getLeft() + dimension.width);
        setBottom(getTop() + dimension.height);
    }

    @Override
    public void setFont(Font font) {
        if (font != null) {
            setTypeface(font.getFont());
            setTextSize(TypedValue.COMPLEX_UNIT_PX, font.getSize());

            // Mise à jour des styles si en mode HTML
            if (isHtmlMode) {
                String html = getTextContent();
                setText(html); // Re-appliquer le HTML avec nouvelle police
            }
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Font getFont() {
        return new Font(getTypeface(), (int)getTextSize());
    }
}
