package playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.inputs;

import android.graphics.Canvas;
import android.view.ViewGroup;

import androidUtils.awt.BoxLayout;
import androidUtils.awt.Component;
import androidUtils.awt.Dimension;
import androidUtils.awt.Graphics;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Point;
import androidUtils.awt.RenderingHints;
import androidUtils.swing.JComponent;
import playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.ImmutablePoint;
import playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.LHeader;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;

public class LIngoingConnectionComponent extends JComponent
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4899501888400568564L;
	private final LHeader lHeader;
    private boolean fill;
    int RADIUS;
    private final ConnectionPointComponent connectionPointComponent;
    private ImmutablePoint connectionPointPosition = new ImmutablePoint(0, 0);

    public LIngoingConnectionComponent(LHeader header, boolean fill)
    {
        this.lHeader = header;
        int height = lHeader.title().getSize().height;
        RADIUS = (int) (lHeader.title().getSize().height * 0.4);
        this.fill = fill;

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(height, height));
            setSize(getPreferredSize());

            connectionPointComponent = new ConnectionPointComponent(fill);

            connectionPointComponent.repaint();
            add(connectionPointComponent);
            setAlignmentX(Component.CENTER_ALIGNMENT);

            revalidate();
            repaint();
            setVisible(true);

        }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        RADIUS = (int) (lHeader.title().getSize().height * 0.4);
    }

    private LInputField connectionFrom;

    public void setInputField(LInputField inputField)
    {
        connectionFrom = inputField;
    }

    // lif that connects to it
    public LInputField inputField()
    {
        return connectionFrom;
    }

    public void updatePosition()
    {
        int x = (int) (connectionPointComponent.getX() + this.getX() + ((ViewGroup) this.getParent()).getX() + ((ViewGroup) this.getParent().getParent()).getX() + ((ViewGroup) this.getParent().getParent().getParent()).getX() + RADIUS);
        int y = (int) (connectionPointComponent.getY() + this.getY() + ((ViewGroup) this.getParent()).getY() + ((ViewGroup) this.getParent().getParent()).getY() + ((ViewGroup) this.getParent().getParent().getParent()).getY() + RADIUS);
        Point p = new Point(x,y);
        if(connectionPointPosition == null)
            connectionPointPosition = new ImmutablePoint(p);
        connectionPointPosition.update(p);
    }

    public ImmutablePoint getConnectionPointPosition()
    {
        updatePosition();
        return connectionPointPosition;
    }

    public void setFill(boolean fill)
    {
        this.fill = fill;
        connectionPointComponent.filled = fill;
        connectionPointComponent.repaint();
        connectionPointComponent.revalidate();
    }

    public LHeader getHeader(){
        return lHeader;
    }

    public boolean isFilled()
    {
        return fill;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintComponent(new Graphics(canvas));
    }


    class ConnectionPointComponent extends JComponent
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 2683408596975730858L;
		public boolean filled;
        private final int x = 0;
        private final int y = 0;

        public ConnectionPointComponent(boolean fill)
        {
            this.filled = fill;
            setSize(getRadius()*2,getRadius()*2);
            revalidate();
            repaint();
            setVisible(true);
        }

        private int getRadius(){
            return RADIUS;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // if fill = true, draw a filled circle. otherwise, the contour only
            if(filled)
            {
                g2.setColor(DesignPalette.LUDEME_CONNECTION_POINT());
                g2.fillOval(x, y, getRadius()*2, getRadius()*2);
            }
            else
            {

                g2.setColor(DesignPalette.LUDEME_CONNECTION_POINT_INACTIVE());
                g2.fillOval(x, y, getRadius()*2, getRadius()*2);
                // make white hole to create stroke effect
                g2.setColor(DesignPalette.BACKGROUND_LUDEME_BODY());
                g2.fillOval(x+getRadius()/2, y+getRadius()/2, getRadius(), getRadius());

            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paintComponent(new Graphics(canvas));
        }
    }
}
