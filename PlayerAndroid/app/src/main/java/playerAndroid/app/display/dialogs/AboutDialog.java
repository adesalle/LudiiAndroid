package playerAndroid.app.display.dialogs;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import androidUtils.awt.image.BufferedImage;
import androidUtils.swing.ImageIcon;
import androidUtils.swing.JOptionPane;
import app.PlayerApp;
import game.equipment.component.Component;
import game.equipment.container.Container;
import main.Constants;
import playerAndroid.app.AndroidApp;
import playerAndroid.app.StartAndroidApp;

/**
 * Dialog for showing various information about Ludii and the current game.
 * 
 * @author Matthew.Stephenson
 */
public class AboutDialog
{

	//-------------------------------------------------------------------------
	
	/**
	 * Show the About dialog.
	 */
	public static void showAboutDialog(final PlayerApp app){
		AssetManager manager = StartAndroidApp.getAppContext().getAssets();
        final InputStream resource;
        try {
            resource = manager.open("img/all-logos-64.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final Bitmap btp = BitmapFactory.decodeStream(resource);
		final BufferedImage image = new BufferedImage(btp);
		final ImageIcon icon = new ImageIcon(image);

		// Description
		final StringBuilder sbDescription = new StringBuilder();
		sbDescription.append("Ludii General Game System");

		// Version
		final StringBuilder sbVersion = new StringBuilder();
		sbVersion.append(", version " + Constants.LUDEME_VERSION + " (" + Constants.DATE + ").\n\n");

		// Legal
		final StringBuilder sbLegal = new StringBuilder();
		//sbLegal.append("Cameron Browne (c) 2017-21.\n\n");
		sbLegal.append("Ludii is an initiative by Cameron Browne 2017-2022.\n\n");

		// Team
		final StringBuilder sbTeam = new StringBuilder();
		
		sbTeam.append("Code: Cameron Browne, Eric Piette, Matthew Stephenson, \n"
				+ "Dennis Soemers, Stephen Tavener, Markus Niebisch, \n"
				+ "Tahmina Begum, Coen Hacking and Lianne Hufkens.\n\n");

		sbTeam.append("Testing: Wijnand Engelkes\n\n");

		sbTeam.append("Historical Advice: Walter Crist\n\n");

		// Admin
		final StringBuilder sbAdmin = new StringBuilder();
		sbAdmin.append("Developed as part of the Digital Ludeme Project \n"
				+ "funded by ERC Consolidator Grant #771292 led by \n"
				+ "Cameron Browne at Maastricht University.\n\n");

		sbAdmin.append("The Ludii JAR is freely available for non-commercial use.\n\n");

		final StringBuilder sbURLs = new StringBuilder();
		sbURLs.append("http://ludii.games\n" + "http://ludeme.eu\n\n");
		sbURLs.append("Ludii source code available at: https://github.com/Ludeme/Ludii\n\n");

		// Credits
		final StringBuilder sbCredits = new StringBuilder();
		final Map<Integer, String> creditMap = new HashMap<Integer, String>();

		// Component credits
		for (final Component component : app.manager().ref().context().game().equipment().components())
			if (component != null && component.credit() != null)
			{
				final Integer key = Integer.valueOf(component.getNameWithoutNumber().hashCode());
				if (creditMap.get(key) == null)
				{
					sbCredits.append(component.credit() + "\n");
					creditMap.put(key, "Found"); 
				}
			}
		
		// Container credits
		for (final Container container : app.manager().ref().context().game().equipment().containers())
			if (container != null && container.credit() != null)
			{
				final Integer key = Integer.valueOf(container.name().hashCode());
				if (creditMap.get(key) == null)
				{
					sbCredits.append(container.credit() + "\n");
					creditMap.put(key, "Found");
				}
			}

		// Audio credits
		sbCredits.append("Pling audio file by KevanGC from http://soundbible.com/1645-Pling.html.\n");
				
		JOptionPane.showMessageDialog
		(
			AndroidApp.frame(),
			sbDescription.toString() + sbVersion.toString() + sbLegal.toString() + sbTeam.toString()
				+ sbAdmin.toString() + sbURLs.toString() + sbCredits.toString(),
			AndroidApp.AppName, 
			JOptionPane.PLAIN_MESSAGE, 
			icon
		);
	}
	
}
