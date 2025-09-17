package playerAndroid.app.util;

import androidUtils.awt.Toolkit;
import androidUtils.swing.JDialog;

// import javax.swing.JDialog;

/**
 * Desktop specific settings
 * 
 * @author Matthew.Stephenson
 */
public class SettingsDesktop
{

	/** Default display width for the program (in pixels). */
	public static int defaultWidth;

	/** Default display height for the program (in pixels). */
	public static int defaultHeight;

    static {
        defaultWidth = (int) Math.max((Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
				(Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		defaultHeight = (int) Math.min((Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
				(Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
    }



	
	/** Whether a separate dialog (settings, puzzle, etc.) is open. */
	public static JDialog openDialog = null;
	
}
