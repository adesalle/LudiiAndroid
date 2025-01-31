package app.views.players;

import androidUtils.awt.Color;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.Rectangle;

import app.PlayerApp;
import other.context.Context;

//-----------------------------------------------------------------------------

/**
 * Panel showing the Shared pieces
 *
 * @author Matthew.Stephenson and cambolbro and Eric.Piette
 */
public class PlayerViewShared extends PlayerViewUser {

    /**
     * Constructor.
     */
    public PlayerViewShared(final PlayerApp app, final Rectangle rect, final int pid, final PlayerView playerView) {
        super(app, rect, pid, playerView);
    }

    //-------------------------------------------------------------------------

    @Override
    public void paint(final Graphics2D g2d) {
        // Add border around shared hand for exhibition app.
//		if (app.settingsPlayer().usingExhibitionApp() && app.manager().ref().context().board().numSites() > 1)
//		{
//			g2d.setColor(Color.WHITE);
//			g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
//			g2d.fillRoundRect(350, placement.y+30, placement.width+5, placement.height-60, 40, 40);
//		}

        if (hand != null) {
            final Context context = app.contextSnapshot().getContext(app);
            final Rectangle containerPlacement = new Rectangle((placement.x), placement.y - placement.height / 2, (placement.width), placement.height);
            playerView.paintHand(g2d, context, containerPlacement, hand.index());
        }

        paintDebug(g2d, Color.ORANGE);
    }

    //-------------------------------------------------------------------------

}
