package playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.inputs;


import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Image;
import androidUtils.awt.event.MouseAdapter;
import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.ImageIcon;
import androidUtils.swing.JButton;
import androidUtils.swing.border.BorderFactory;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;

public class LInputButton extends JButton {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6627034885726770235L;
	public Color ACTIVE_COLOR;
    private final Color HOVER_COLOR = new Color(127,191,255);

    public ImageIcon ACTIVE_ICON;
    private ImageIcon HOVER_ICON;
    private boolean active = true;

    public LInputButton(ImageIcon activeIcon, ImageIcon hoverIcon)
    {
        super(activeIcon);
        this.ACTIVE_COLOR = DesignPalette.FONT_LUDEME_INPUTS_COLOR();
        this.ACTIVE_ICON = activeIcon;
        this.HOVER_ICON = hoverIcon;

        setFont(new Font("Roboto Bold", 0, 12));

        // make transparent
        setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addMouseListener(hoverMouseListener);
    }

    public void updateDP()
    {
        if(active)
            setActive();
    }

    public void setActive(){
        active = true;
        setForeground(ACTIVE_COLOR);
        setIcon(ACTIVE_ICON);
        repaint();
    }


    public void setHover(){
        active = false;
        setForeground(HOVER_COLOR);
        setIcon(HOVER_ICON);
        repaint();
    }

    final MouseListener hoverMouseListener = new MouseAdapter() {
        @Override
		public void mouseEntered(MouseEvent e) {
            setHover();
        }
        @Override
		public void mouseExited(MouseEvent e) {
            setActive();
        }
    };

    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        ACTIVE_ICON = new ImageIcon(ACTIVE_ICON.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        HOVER_ICON = new ImageIcon(HOVER_ICON.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        if(active) setIcon(ACTIVE_ICON);
        else setIcon(HOVER_ICON);
    }


    public void setSize(Dimension dimension)
    {
        int width = dimension.width;
        int height = dimension.height;
        super.setSize(width, height);
        ACTIVE_ICON = new ImageIcon(ACTIVE_ICON.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        HOVER_ICON = new ImageIcon(HOVER_ICON.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        if(active) setIcon(ACTIVE_ICON);
        else setIcon(HOVER_ICON);
    }
}
