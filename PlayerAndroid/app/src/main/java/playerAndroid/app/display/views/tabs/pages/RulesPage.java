package playerAndroid.app.display.views.tabs.pages;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
						// Extraire l'URL et le texte du lien
						String url = extractUrlFromHtml(s);
						String linkText = extractTextFromHtml(s);


						if (url != null && linkText != null) {
							addClickableText(linkText, url);
						} else {
							// Fallback si ce n'est pas un lien HTML
							Spanned spannedText;
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								spannedText = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY);
							} else {
								spannedText = Html.fromHtml(s);
							}
							addText(String.valueOf(spannedText));
						}
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
				
				//clear();
				//addText(solidText);
			}
		}
		catch (final Exception e)
		{
			// one of the above was not defined properly
			e.printStackTrace();
		}
	}

	// Méthodes utilitaires
	private String extractUrlFromHtml(String html) {
		try {
			Pattern pattern = Pattern.compile("href=\"(.*?)\"");
			Matcher matcher = pattern.matcher(html);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String extractTextFromHtml(String html) {
		try {
			Pattern pattern = Pattern.compile(">(.*?)<");
			Matcher matcher = pattern.matcher(html);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addClickableText(String text, final String url) {
		SpannableString spannableString = new SpannableString(text);
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				try {
					widget.getContext().startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(widget.getContext(), "Aucune application pour ouvrir le lien", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setColor(Color.BLUE);
				ds.setUnderlineText(true);
			}
		};

		spannableString.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		addText(String.valueOf(spannableString));
	}
	
	//-------------------------------------------------------------------------

}
