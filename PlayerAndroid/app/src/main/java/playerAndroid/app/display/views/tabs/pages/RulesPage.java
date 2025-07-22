package playerAndroid.app.display.views.tabs.pages;

import java.util.List;

import androidUtils.awt.Rectangle;
import app.PlayerApp;
import game.Game;
import other.context.Context;
import playerAndroid.app.display.views.tabs.TabPage;
import playerAndroid.app.display.views.tabs.TabView;

import app.utils.SettingsExhibition;

import main.Constants;
import main.options.Option;
import metadata.Metadata;

/**
 * Tab for displaying the rules of the current game.
 * 
 * @author Matthew.Stephenson
 */
public class RulesPage extends TabPage
{

	//-------------------------------------------------------------------------
	
	public RulesPage(final PlayerApp app, final Rectangle rect, final String title, final String text, final int pageIndex, final TabView parent)
	{
		super(app, rect, title, text, pageIndex, parent);
	}
	
	//-------------------------------------------------------------------------

	@Override
	public void updatePage(final Context context)
	{
		//reset();
	}
	
	//-------------------------------------------------------------------------

	@Override
	public void reset()
	{
		clear();
		final Game game = app.contextSnapshot().getContext(app).game();
		try
		{
			final Metadata metadata = game.metadata();
			if (metadata != null)
			{				
				// rules tab
				if (metadata.info().getRules().size() > 0)
				{
					if (!SettingsExhibition.exhibitionVersion)
						addText("Rules:\n");
					for (final String s : metadata.info().getRules())
					{
						if (SettingsExhibition.exhibitionVersion)
						{
							for (final String line : s.split(">"))
							{
								if (line.trim().length() > 1)
									addText(line.trim() + "\n\n");
							}
						}
						else
						{
							addText(s.trim());
							addText("\n\n");
						}
					}
				}
				if (metadata.info().getSource().size() > 0)
				{
					addText("Source:\n");
					for (final String s : metadata.info().getSource())
					{
						addText(s);
						addText("\n");
					}
					addText("\n");
				}			
				if (app.manager().settingsManager().userSelections().ruleset() == Constants.UNDEFINED && !SettingsExhibition.exhibitionVersion)
				{
					final List<Option> activeOptions =
							game.description().gameOptions().activeOptionObjects
							(
								app.manager().settingsManager().userSelections().selectedOptionStrings()
							);
					if (activeOptions.size() > 0)
					{
						addText("Options:\n");
						for (final Option option : activeOptions)
						{
							addText(option.description() + "\n");
						}
					}
				}
				
				clear();
				addText(solidText);
			}
		}
		catch (final Exception e)
		{
			// one of the above was not defined properly
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------

}
