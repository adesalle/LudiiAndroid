package playerAndroid.app.display.views.tabs;


import android.graphics.drawable.ColorDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;

import androidUtils.awt.Color;
import androidUtils.awt.Font;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.swing.Caret;
import androidUtils.swing.JEditorPane;
import androidUtils.swing.JScrollPane;
import androidUtils.swing.JTextPane;
import androidUtils.swing.ScrollPaneConstants;
import androidUtils.swing.text.BadLocationException;
import androidUtils.swing.text.HTMLDocument;
import androidUtils.swing.text.HTMLEditorKit;
import androidUtils.swing.text.Style;
import androidUtils.swing.text.StyleConstants;
import app.PlayerApp;
import app.utils.SettingsExhibition;
import app.views.View;
import other.context.Context;
import playerAndroid.app.AndroidApp;
import playerAndroid.app.display.views.tabs.pages.InfoPage;
import playerAndroid.app.display.views.tabs.pages.RulesPage;

//-----------------------------------------------------------------------------

/**
 * View showing a single tab page.
 *
 * @author Matthew.Stephenson and cambolbro
 */
public abstract class TabPage extends View
{
    /** Tab title. */
    protected String title = "Tab";

    /** Text area. */
    protected JTextPane textArea = new JTextPane();
    {
        textArea.setContentType("text/html");
    }

    protected HTMLDocument doc = (HTMLDocument)textArea.getDocument();
    protected Style textstyle = textArea.addStyle("text style", null);

    /** Font colour. */
    protected Color fontColour;

    /** Faded font colour. */
    protected Color fadedFontColour;

    /** Scroll pane for the text area. */
    protected JScrollPane scrollPane = new JScrollPane(textArea);

    /** Solid text to show on the text area. */
    public String solidText = "";

    /** Faded text to show on the text area. */
    public String fadedText = "";

    /** Rectangle bounding box for title. */
    public Rectangle titleRect = null;  //new Rectangle();

    /** Whether or not a mouse is over the title. */
    protected boolean mouseOverTitle = false;

    /** Tab page index. */
    public final int pageIndex;

    /** Tab view that holds all tab pages. */
    private final TabView parent;

    //-------------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param rect
     * @param title
     * @param text
     */
    public TabPage
    (
            final PlayerApp app, final Rectangle rect, final String title, final String text, final int pageIndex, final TabView parent
    )
    {
        super(app);
        this.parent = parent;

        placement = rect;

        this.title = new String(title);
        this.pageIndex = pageIndex;

        final int charWidth = 9;  // approximate char width for spacing tab page headers
        final int wd = charWidth * this.title.length();
        final int ht = TabView.fontSize;

        titleRect = new Rectangle(rect.x, rect.y, wd, ht);

        scrollPane.setBounds(placement);
        scrollPane.setBorder(null);
        scrollPane.setVisible(false);
        scrollPane.setFocusable(false);
        textArea.setFocusable(true);

        textArea.setEditable(false);
        textArea.setBackground(new ColorDrawable(new Color(255, 255, 255).toArgb()));
        final Caret caret = (Caret) textArea.getCaret();
        caret.setUpdatePolicy(Caret.ALWAYS_UPDATE);

        textArea.setFont(new Font("Arial", Font.PLAIN, app.settingsPlayer().tabFontSize()));
        textArea.setContentType("text/html");
        textArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.valueOf(true));

        fontColour = new Color(50, 50, 50);
        textArea.setBackground(new ColorDrawable(Color.white.toArgb()));

        if (SettingsExhibition.exhibitionVersion)
        {

            try(InputStream in = getClass().getResourceAsStream("/National-Regular.ttf"))
            {
                textArea.setFont(Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(32f));
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }

            textArea.setBackground(new ColorDrawable(Color.black.toArgb()));
            fontColour = Color.white;
            textArea.setForeground(new ColorDrawable(fontColour.toArgb()));
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }

        fadedFontColour = new Color(fontColour.getRed() + (int) ((255 - fontColour.getRed()) * 0.75),
                fontColour.getGreen() + (int) ((255 - fontColour.getGreen()) * 0.75),
                fontColour.getBlue() + (int) ((255 - fontColour.getBlue()) * 0.75));

        StyleConstants.setForeground(textstyle, fontColour);

        textArea.setVisible(false);
        textArea.setText(text);

        AndroidApp.view().setLayout(null);
        AndroidApp.view().add(scrollPane());

    }

    //-------------------------------------------------------------------------

    public abstract void updatePage(final Context context);
    public abstract void reset();

    //-------------------------------------------------------------------------

    public String title()
    {
        return title;
    }

    public Rectangle titleRect()
    {
        return titleRect;
    }

    public void setTitleRect(final int x, final int y, final int wd, final int ht)
    {
        titleRect = new Rectangle(x, y, wd, ht);
    }

    public JScrollPane scrollPane()
    {
        return scrollPane;
    }

    //-------------------------------------------------------------------------

    /**
     * Show/hide tab page.
     *
     * @param show
     */
    public void show(final boolean show)
    {
        textArea.setVisible(show);
        scrollPane.setVisible(show);
    }

    //-------------------------------------------------------------------------

    /**
     * Clear the console text.
     */
    public void clear()
    {
        textArea.setText("");
    }

    //-------------------------------------------------------------------------

    /**
     * Add text to tab page.
     */
    public void addText(final String str)
    {
       textArea.setText(textArea.getText() + str);

    }

    //-------------------------------------------------------------------------

    /**
     * Add faded text to tab page.
     */
    protected void addFadedText(final String str)
    {
        StyleConstants.setForeground(textstyle, fadedFontColour);
        try
        {
            doc.insertString(doc.getLength(), str, textstyle);
            fadedText = doc.getText(solidText.length(), doc.getLength()-solidText.length());
            Rectangle r = null;
            textArea.setCaretPosition(solidText.length());
            r = textArea.modelToView(textArea.getCaretPosition());
            textArea.scrollRectToVisible(r);
        }
        catch (final Exception e)
        {
            // carry on
        }
    }

    //-------------------------------------------------------------------------

    /**
     * @return Current text.
     */
    public String text()
    {
        try
        {
            return doc.getText(0, doc.getLength());
        }
        catch (final BadLocationException e)
        {
            e.printStackTrace();
            return Objects.requireNonNull(textArea.getText()).toString();
        }
    }

    //-------------------------------------------------------------------------

    /**
     * Draw player details.
     *
     * @param g2d
     */
    @Override
    public void paint(final Graphics2D g2d)
    {
        if (!parent.titlesSet())
            parent.setTitleRects();

        drawTabPageTitle(g2d);

        paintDebug(g2d, Color.YELLOW);
    }

    //-------------------------------------------------------------------------

    /**
     * Draw title of the tab page.
     */
    private void drawTabPageTitle(final Graphics2D g2d)
    {
        if (SettingsExhibition.exhibitionVersion)
            return;

        final Font oldFont = g2d.getFont();
        final Font font = new Font("Arial", Font.BOLD, TabView.fontSize);
        g2d.setFont(font);

        final Color dark = new Color(50, 50, 50);
        final Color light = new Color(255, 255, 255);
        final Color mouseOver = new Color(150,150,150);

        if (pageIndex == app.settingsPlayer().tabSelected())
            g2d.setColor(dark);
        else if (mouseOverTitle)
            g2d.setColor(mouseOver);
        else
            g2d.setColor(light);

        final String str = title();

        final Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(str, g2d);

        final int tx = titleRect.x + (int)((double) titleRect.width / 2 - bounds.getWidth()/2);
        final int ty = titleRect.y + titleRect.height / 2 + 5;
        g2d.drawString(str, tx, ty);

        g2d.setFont(oldFont);
    }

    //-------------------------------------------------------------------------

    @Override
    public void mouseOverAt(final Point pixel)
    {
        // See if mouse is over any of the tabs titles	
        if (titleRect.contains(pixel.x, pixel.y))
        {
            if (!mouseOverTitle)
            {
                mouseOverTitle = true;
                AndroidApp.view().repaint(titleRect);
            }
        }
        else
        {
            if (mouseOverTitle)
            {
                mouseOverTitle = false;
                AndroidApp.view().repaint(titleRect);
            }
        }
    }

    //-------------------------------------------------------------------------

}
