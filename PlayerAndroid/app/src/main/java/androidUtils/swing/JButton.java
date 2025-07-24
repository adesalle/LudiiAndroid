package androidUtils.swing;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;
import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.border.Border;
import androidUtils.swing.border.EmptyBorder;
import playerAndroid.app.StartAndroidApp;

public class JButton extends Component implements ViewComponent {
    private final Graphics2DView nativeButton;
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

    private List<View.OnTouchListener> touchListeners = new ArrayList<>();
    private List<MouseListener> mouseListeners = new ArrayList<>();
    private List<ActionListener> actionListeners = new ArrayList<>();

    private ImageIcon icon;
    private boolean isDefaultButton = false;


    public JButton(String text) {
        super(text);
        nativeButton = new Graphics2DView();
        nativeButton.setText(text);
        setupListeners();
    }

    public JButton(ImageIcon image) {
        super();
        nativeButton = new Graphics2DView();
        nativeButton.draw(new Canvas());
        nativeButton.setBackground(image.getDrawable());
        setupListeners();
    }

    public JButton() {
        super();
        nativeButton = new Graphics2DView();
        setupListeners();
    }

    public void setAsDefaultButton(boolean isDefault) {
        this.isDefaultButton = isDefault;
        refreshDefaultState();
    }

    public void refreshDefaultState() {
        if (isDefaultButton) {
            // Mettre en valeur le bouton par défaut
            setBackgroundColor(Color.parseColor("#2E86C1")); // Bleu plus foncé
            nativeButton.setTextColor(Color.WHITE.toArgb());
        } else {
            // Apparence normale
            setBackgroundColor(Color.parseColor("#3498DB")); // Bleu standard
            nativeButton.setTextColor(Color.WHITE.toArgb());
        }
    }

    public boolean performClick() {
        super.performClick();
        // Gérer l'effet visuel du clic
        animate().scaleX(0.95f).scaleY(0.95f).setDuration(50)
                .withEndAction(() -> animate().scaleX(1f).scaleY(1f).setDuration(50));
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        nativeButton.setOnClickListener(v -> {
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(new ActionEvent(this));
            }
        });

        nativeButton.setOnTouchListener((v, event) -> {
            for (View.OnTouchListener listener : touchListeners) {
                boolean handled = listener.onTouch(v, event);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                return handled;
            }
            return false;
        });
    }

    // Méthodes pour la gestion des bordures et de l'apparence
    public void setBorder(Border border) {
        this.border = border;
        if (border instanceof EmptyBorder) {
            nativeButton.setPadding(
                    border.getBorderInsets(this).left,
                    border.getBorderInsets(this).top,
                    border.getBorderInsets(this).right,
                    border.getBorderInsets(this).bottom
            );
        } else if (border != null) {
            // Implémentez d'autres types de bordures si nécessaire
        } else {
            nativeButton.setPadding(0, 0, 0, 0);
        }
    }

    public Border getBorder() {
        return border;
    }

    public void setFocusPainted(boolean focusPainted) {
        this.focusPainted = focusPainted;
        nativeButton.setFocusable(focusPainted);
        nativeButton.setFocusableInTouchMode(focusPainted);
    }

    public boolean isFocusPainted() {
        return focusPainted;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        if (!opaque) {
            nativeButton.setBackground(null);
        }
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setContentAreaFilled(boolean contentAreaFilled) {
        this.contentAreaFilled = contentAreaFilled;
        if (!contentAreaFilled) {
            nativeButton.setBackground(null);
        }
    }

    public boolean isContentAreaFilled() {
        return contentAreaFilled;
    }

    public void setBorderPainted(boolean borderPainted) {
        this.borderPainted = borderPainted;
        if (!borderPainted) {
            nativeButton.setBackground(null);
        }
    }

    public boolean isBorderPainted() {
        return borderPainted;
    }



    public void removeActionListener(ActionListener listener) {
        this.actionListeners.remove(listener);
    }



    public Graphics getGraphics() {
        if (backBuffer == null) createBuffer();
        return backG;
    }

    private void createBuffer() {
        int w = Math.max(1, nativeButton.getWidth());
        int h = Math.max(1, nativeButton.getHeight());
        backBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(backBuffer);
        backG = new Graphics(c);
    }

    public Dimension getPreferredSize() {
        return new Dimension(nativeButton.getMeasuredWidth(), nativeButton.getMeasuredHeight());
    }

    public void setBackground(Color boardColour) {
        nativeButton.setBackground(new ColorDrawable(boardColour.toArgb()));
    }

    public void setForeground(Color contrastColorFavourDark) {
        nativeButton.setForeground(new ColorDrawable(contrastColorFavourDark.toArgb()));
    }

    public void setText(String wrappedText) {
        nativeButton.setText(wrappedText);
    }

    public String getText() {
        return nativeButton.getText().toString();
    }



    public void addMouseListener(View.OnTouchListener mouseListener) {
        this.touchListeners.add(mouseListener);
    }

    public void addActionListener(ActionListener listener) {
        this.actionListeners.add(listener);
    }

    public void removeActionListener() {
        this.actionListeners = null;
    }

    public void setEnabled(boolean enabled) {
        nativeButton.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return nativeButton.isEnabled();
    }

    public List<ActionListener> getActionListeners() {
        return actionListeners;
    }

    public void setToolTipText(String text) {
        this.toolTipText = text;
        // Pour API 26+ (Android 8.0+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nativeButton.setTooltipText(text);
        } else {
            // Solution de repli pour les versions antérieures
            nativeButton.setOnLongClickListener(v -> {
                if (toolTipText != null) {
                    Toast.makeText(v.getContext(), toolTipText, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
        }
    }

    public void setActionCommand(String command) {
        this.actionCommand = command;
        //nativeButton.setTag(command);
    }

    public String getActionCommand() {
        return actionCommand != null ? actionCommand : (String) nativeButton.getTag();
    }

    public void setFont(Font font) {
        if (font != null) {
            this.currentFont = font;
            nativeButton.setTypeface(font.getFont());
            nativeButton.setTextSize(font.getSize());
            nativeButton.setTypeface(font.getFont(), font.getStyle());

            // Mise à jour du padding si nécessaire
            int padding = (int) (font.getSize() * 0.5f); // Padding relatif à la taille de police
            nativeButton.setPadding(padding, padding, padding, padding);
        }
    }

    public Font getFont() {
        if (currentFont != null) {
            return currentFont;
        }
        return new Font(nativeButton.getTypeface(), (int) nativeButton.getTextSize());
    }

    public void setSize(int width, int height) {
        nativeButton.getLayoutParams().width = width;
        nativeButton.getLayoutParams().height = height;
        nativeButton.requestLayout();

        if (backBuffer != null &&
                (backBuffer.getWidth() != width || backBuffer.getHeight() != height)) {
            createBuffer();
        }

        // Redessiner le composant
        nativeButton.invalidate();
    }

    public ImageIcon getIcon() {
        if(icon != null)return icon;
        if (nativeButton.getBackground() instanceof ColorDrawable) {
            return null;
        }

        if (nativeButton.getBackground() != null) {
            return new ImageIcon(nativeButton.getBackground());
        }

        return null;
    }

    public void setIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            icon = imageIcon;
            // Implémentation de base - à adapter selon le type de composant
            nativeButton.setBackground(imageIcon.getDrawable());
        }
    }

    public void setMinimumSize(Dimension dimension) {
        setMinimumWidth(dimension.width);
        setMinimumHeight(dimension.height);
    }

    public void setMaximumSize(Dimension dimension) {
        setSize(dimension);
    }


    /** Vue Android réelle qui sait recopier le buffer dans son onDraw */
    private class Graphics2DView extends androidx.appcompat.widget.AppCompatButton {
        public Graphics2DView() {
            super(StartAndroidApp.getAppContext());
            setAllCaps(false);
            setBackground(null); // Pour permettre la personnalisation complète
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            backBuffer = null;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Dessiner le fond seulement si contentAreaFilled est true
            if (contentAreaFilled && opaque) {
                super.onDraw(canvas);
            }

            // Dessiner la bordure si nécessaire
            if (borderPainted && border != null) {
                border.paintBorder(this, getGraphics(), 0, 0, getWidth(), getHeight());
            }

            // Dessiner le contenu du buffer
            if (backBuffer != null) {
                canvas.drawBitmap(backBuffer, 0, 0, null);
            }
        }
    }
}