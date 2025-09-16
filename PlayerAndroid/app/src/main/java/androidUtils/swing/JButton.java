package androidUtils.swing;

import static androidUtils.awt.Component.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.action.TouchListener;
import androidUtils.swing.border.Border;
import androidUtils.swing.border.EmptyBorder;
import playerAndroid.app.StartAndroidApp;

public class JButton extends androidx.appcompat.widget.AppCompatButton implements ViewComponent {
    private Bitmap backBuffer;
    private Graphics backG;
    private String actionCommand;
    private String toolTipText;
    private Font currentFont;
    private Border border;
    private boolean focusPainted = true;
    private boolean opaque = true;
    private boolean contentAreaFilled = true;
    private boolean borderPainted = true;
    private boolean isDefaultButton = false;
    private ImageIcon icon;

    private List<OnTouchListener> mouseListeners = new ArrayList<>();
    private List<ActionListener> actionListeners = new ArrayList<>();

    protected int alignment = CENTER_CENTER;

    public JButton() {
        this(StartAndroidApp.getAppContext());
    }
    public JButton(Context context) {
        super(context);
        init();
    }

    public JButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public JButton(String text) {
        super(StartAndroidApp.getAppContext());
        setText(text);
        init();
    }

    public JButton(ImageIcon image) {
        super(StartAndroidApp.getAppContext());
        setIcon(image);
        init();
    }

    private void init() {
        setMinWidth(0);
        setMinHeight(0);
        setPadding(4, 4, 4, 4);
        backG = new Graphics();
        setAllCaps(false);
        setupListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        super.setOnClickListener(v -> {
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(new ActionEvent(this));
            }
        });

        super.setOnTouchListener((v, event) -> {
            for (OnTouchListener listener : mouseListeners) {
                listener.onTouch(v, event);
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                performClick();
            }
            return false;
        });
    }


    public Dimension getTextSize(String text, Font font) {
        // Créer un objet Paint avec les propriétés de la police

        Paint paint = new Paint();

        // Configurer le Paint avec les propriétés de la police
        if (font != null) {
            paint.setTypeface(font.getFont());
            paint.setTextSize(font.getSize());
        } else {
            // Utiliser les valeurs par défaut si la police n'est pas spécifiée
            paint.setTextSize(getTextSize()); // Taille de texte par défaut du bouton
            paint.setTypeface(getTypeface()); // Typeface par défaut du bouton
        }

        // Obtenir les limites du texte
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Retourner la dimension (la hauteur peut être ajustée selon les besoins)
        return new Dimension(bounds.width(), bounds.height());
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Animation du clic
        animate().scaleX(0.95f).scaleY(0.95f).setDuration(50)
                .withEndAction(() -> animate().scaleX(1f).scaleY(1f).setDuration(50));
        return true;
    }

    // Méthodes pour la gestion des bordures et de l'apparence
    public void setBorder(Border border) {
        this.border = border;

        invalidate();
    }

    public Border getBorder() {
        return border;
    }

    public void setFocusPainted(boolean focusPainted) {
        this.focusPainted = focusPainted;
        setFocusable(focusPainted);
        setFocusableInTouchMode(focusPainted);
    }

    public boolean isFocusPainted() {
        return focusPainted;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        if (!opaque) {
            setAlpha(0.3f);
        }
        invalidate();
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setContentAreaFilled(boolean contentAreaFilled) {
        this.contentAreaFilled = contentAreaFilled;
        if (!contentAreaFilled) {
            setAlpha(0.0f);
        }
        invalidate();
    }

    public boolean isContentAreaFilled() {
        return contentAreaFilled;
    }

    public void setBorderPainted(boolean borderPainted) {
        this.borderPainted = borderPainted;
        invalidate();
    }

    public boolean isBorderPainted() {
        return borderPainted;
    }

    public void setAsDefaultButton(boolean isDefault) {
        this.isDefaultButton = isDefault;
        refreshDefaultState();
    }

    public void refreshDefaultState() {
        if (isDefaultButton) {
            setBackgroundColor(Color.parseColor("#2E86C1")); // Bleu plus foncé
            setTextColor(Color.WHITE.toArgb());
        } else {
            setBackgroundColor(Color.parseColor("#3498DB")); // Bleu standard
            setTextColor(Color.WHITE.toArgb());
        }
    }

    // Gestion des listeners
    public void addActionListener(ActionListener listener) {
        this.actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        this.actionListeners.remove(listener);
    }

    public List<ActionListener> getActionListeners() {
        return actionListeners;
    }

    // Méthodes graphiques
    public Graphics getGraphics() {
        if (backBuffer == null)
            createBuffer();
        return backG;
    }

    private void createBuffer() {
        int w = Math.max(1, getWidth());
        int h = Math.max(1, getHeight());
        backBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        backG = new Graphics(backBuffer);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        backBuffer = null;
    }


    protected void paintComponent(Graphics g) {
        // À implémenter par les sous-classes si nécessaire
    }

    // Gestion des tailles
    public Dimension getPreferredSize() {
        return new Dimension(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setPreferredSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
    }

    public void setSize(Dimension dimension) {
        setWidth(dimension.width);
        setHeight(dimension.height);
    }

    // Autres méthodes importantes
    public void setBackground(Color color) {
        setBackground(new ColorDrawable(color.toArgb()));
    }

    public void setForeground(Color color) {
        setTextColor(color.toArgb());
    }

    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            setTypeface(font.getFont());
            setTextSize(font.getSize());

            int padding = (int) (font.getSize() * 0.5f);
            setPadding(padding, padding, padding, padding);
        }
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    public Font getFont() {
        if (currentFont != null) {
            return currentFont;
        }
        return new Font(getTypeface(), (int) getTextSize());
    }

    public void setIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            this.icon = imageIcon;
            setCompoundDrawablesWithIntrinsicBounds(imageIcon.getDrawable(), null, null, null);
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setToolTipText(String text) {
        this.toolTipText = text;
        setTooltipText(text);
    }

    public void setActionCommand(String command) {
        this.actionCommand = command;
        setTag(command);
    }

    public String getActionCommand() {
        return actionCommand != null ? actionCommand : (String) getTag();
    }

    // Gestion de l'alignement
    public void setAlignment(int align) {
        this.alignment = align;
        updateLayoutGravity();
    }

    private void updateLayoutGravity() {
        int gravity = 0;

        switch (alignment & 0x3) { // Horizontal
            case LEFT_ALIGNMENT: gravity |= android.view.Gravity.LEFT; break;
            case CENTER_ALIGNMENT: gravity |= android.view.Gravity.CENTER_HORIZONTAL; break;
            case RIGHT_ALIGNMENT: gravity |= android.view.Gravity.RIGHT; break;
        }

        switch (alignment & 0xC) { // Vertical
            case TOP_ALIGNMENT: gravity |= android.view.Gravity.TOP; break;
            case BOTTOM_ALIGNMENT: gravity |= android.view.Gravity.BOTTOM; break;
            default: gravity |= android.view.Gravity.CENTER_VERTICAL; break;
        }

        setGravity(gravity);
    }

    public void setBounds(int x, int y, int width, int height) {
        // Positionne et dimensionne le bouton
        layout(x, y, x + width, y + height);

        invalidate();
    }

    public void addMouseListener(View.OnTouchListener touchListener) {
        mouseListeners.add(touchListener);
    }

    public void repaint() {
        // Force le redessin immédiat
        invalidate();

        // Si vous utilisez un buffer, le recréer
        if (backBuffer != null) {
            createBuffer();
        }
    }

    public void revalidate() {
        // Demande un nouveau calcul du layout
        requestLayout();

        // Force le redessin
        invalidate();
    }

    public void setAlignmentX(int alignment) {
        // Conserve l'alignement vertical existant
        this.alignment = (this.alignment & 0xC) | (alignment & 0x3);
        updateLayoutGravity();
    }

    /**
     * Définit la taille minimale du bouton
     * @param dimension Dimension minimale
     */
    public void setMinimumSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
        requestLayout();
    }

    /**
     * Définit la taille maximale du bouton
     * @param dimension Dimension maximale
     */
    public void setMaximumSize(Dimension dimension) {
        setMaxWidth(dimension.width);
        setMaxHeight(dimension.height);
        requestLayout();
    }

    /**
     * Définit la taille du bouton
     * @param width Largeur en pixels
     * @param height Hauteur en pixels
     */
    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        setLayoutParams(params);
        requestLayout();
        invalidate();
    }

}