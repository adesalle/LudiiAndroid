package playerAndroid.app.display.dialogs.util;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.IOException;
import java.io.InputStream;

import androidUtils.awt.FontRenderContext;
import androidUtils.awt.Graphics;
import androidUtils.awt.Rectangle;
import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.image.BufferedImage;
import androidUtils.swing.JButton;
import androidUtils.swing.JDialog;
import androidUtils.swing.WindowConstants;
import playerAndroid.app.AndroidApp;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.util.SettingsDesktop;


public class DialogUtil
{

	/**
	 * Initialise a Dialog.
	 * @param dialog
	 * @param title
	 */
	public static void initialiseDialog(final JDialog dialog, final String title, final Rectangle bounds)
	{
		sharedInitialisation(dialog, title, bounds);
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Initialise a Dialog on top of any existing ones, but with forced focus.
	 * @param dialog
	 * @param title
	 */
	public static void initialiseForcedDialog(final JDialog dialog, final String title, final Rectangle bounds)
	{
		dialog.setModal(true);
		sharedInitialisation(dialog, title, bounds);
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Initialise a Dialog and close any existing ones.
	 * @param dialog
	 * @param title
	 */
	public static void initialiseSingletonDialog(final JDialog dialog, final String title, final Rectangle bounds)
	{
		if (SettingsDesktop.openDialog != null)
		{
			SettingsDesktop.openDialog.setVisible(false);
			SettingsDesktop.openDialog.dispose();
		}
		SettingsDesktop.openDialog = dialog;
		sharedInitialisation(dialog, title, bounds);
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Shared initialisation calls between both above cases.
	 * @param dialog
	 * @param title
	 */
	private static void sharedInitialisation(final JDialog dialog, final String title, final Rectangle bounds)
	{
		// Logo and title
		try
		{

			AssetManager manager = StartAndroidApp.getAppContext().getAssets();
			final InputStream resource = manager.open("img/ludii-logo-100x100.png");
			final Bitmap btp = BitmapFactory.decodeStream(resource);
			final BufferedImage image = new BufferedImage(btp);
			dialog.setIconImage(image);
			dialog.setTitle(title);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		if (bounds == null)
			dialog.setLocationRelativeTo(AndroidApp.frame());
		else
		{
			dialog.setLocation(bounds.getLocation());
			if (bounds.width > 0 && bounds.height > 0)
			{
				dialog.setSize(bounds.width, bounds.height);
			}
		}
		
		try
		{
			dialog.setVisible(true);
		}
		catch (final Exception e)
		{
			// Sometimes this just fails. Don't really know why but seems harmless.
		}
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Code used for nicely formatting and wrapping text (in HTML form) for JButtons.
	 * Code obtained from https://stackoverflow.com/questions/32603544/wrap-dynamic-text-automatically-in-jbutton
	 * @param graphics
	 * @param button
	 * @param str
	 * @return Formatted text string (in HTML)
	 */
	public static String getWrappedText(final Graphics graphics, final JButton button, final String str)
	{
		final String STR_NEWLINE = "<br />";
		final FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
	    if( str != null ) 
	    {
	        final String text = str.replaceAll("<html><center>", "").replaceAll("</center></html>", "");
	        final int width = button.getWidth();
	        final Rectangle2D stringBounds = button.getFont().getStringBounds(text, fontRenderContext);
	        if ( !str.contains(STR_NEWLINE) && (width-5) < (Double.valueOf(stringBounds.getWidth())).intValue()) 
	        {
	            String newStr;
	            if( str.contains(" ") ) 
	            {
	                final int lastIndex = str.lastIndexOf(" ");
	                newStr = str.substring(0, lastIndex)+STR_NEWLINE+str.substring(lastIndex);
	            } else 
	            {
	                final int strLength = ((str.length()/3)*2);
	                newStr = str.substring(0, strLength)+STR_NEWLINE+str.substring(strLength);
	            }
	            return newStr;
	        }
	    }
	    return str;
	}
	
}
