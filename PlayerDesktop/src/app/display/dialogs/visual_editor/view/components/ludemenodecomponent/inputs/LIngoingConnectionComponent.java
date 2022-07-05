package app.display.dialogs.visual_editor.view.components.ludemenodecomponent.inputs;

import app.display.dialogs.visual_editor.handler.Handler;
import app.display.dialogs.visual_editor.view.components.ludemenodecomponent.ImmutablePoint;
import app.display.dialogs.visual_editor.view.components.ludemenodecomponent.LHeader;

import javax.swing.*;
import java.awt.*;

public class LIngoingConnectionComponent extends JComponent {

    private final LHeader lHeader;
    private boolean fill;
    private int RADIUS;
    private final ConnectionPointComponent connectionPointComponent;
    private ImmutablePoint connectionPointPosition = new ImmutablePoint(0, 0);

    public LIngoingConnectionComponent(LHeader header, boolean fill){
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

        public void updatePosition(){
            int x = connectionPointComponent.getX() + this.getX() + this.getParent().getX() + this.getParent().getParent().getX() + this.getParent().getParent().getParent().getX() + RADIUS;
            int y = connectionPointComponent.getY() + this.getY() + this.getParent().getY() + this.getParent().getParent().getY() + this.getParent().getParent().getParent().getY() + RADIUS;
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
            connectionPointComponent.fill = fill;
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



    class ConnectionPointComponent extends JComponent
    {
        public boolean fill;
        public int x,y = 0;

        public ConnectionPointComponent(boolean fill)
        {
            this.fill = fill;
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
            if(fill)
            {
                g2.setColor(Handler.currentPalette().LUDEME_CONNECTION_POINT());
                g2.fillOval(x, y, getRadius()*2, getRadius()*2);
            }
            else
            {

                g2.setColor(Handler.currentPalette().LUDEME_CONNECTION_POINT_INACTIVE());
                g2.fillOval(x, y, getRadius()*2, getRadius()*2);
                // make white hole to create stroke effect
                g2.setColor(Handler.currentPalette().BACKGROUND_LUDEME_BODY());
                g2.fillOval(x+getRadius()/2, y+getRadius()/2, getRadius(), getRadius());

            }
        }
    }
}
