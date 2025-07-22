package playerAndroid.app.display.views.tabs.pages;

import androidUtils.awt.Rectangle;
import app.PlayerApp;
import other.context.Context;
import playerAndroid.app.display.views.tabs.TabPage;
import playerAndroid.app.display.views.tabs.TabView;

/**
 * Tab for displaying the results of analytical experiments.
 * 
 * @author Matthew.Stephenson
 */
public class AnalysisPage extends TabPage
{

	//-------------------------------------------------------------------------
	
	public AnalysisPage(final PlayerApp app, final Rectangle rect, final String title, final String text, final int pageIndex, final TabView parent)
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
	}
	
	//-------------------------------------------------------------------------

}
