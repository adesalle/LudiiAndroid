package playerAndroid.app.loading;

import androidUtils.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


import androidUtils.swing.Action;
import androidUtils.swing.FileFilter;
import androidUtils.swing.FileNameExtensionFilter;
import androidUtils.swing.JFileChooser;
import androidUtils.swing.JFrame;


import playerAndroid.app.AndroidApp;
import playerAndroid.app.display.MainWindowDesktop;
import playerAndroid.app.util.SettingsDesktop;

/**
 * Functions for loading external resources.
 * 
 * @author Matthew Stephenson
 */
public class FileLoading
{

	//-------------------------------------------------------------------------

	/**
	 * Select a file to load (used when loading .lud and .svg)
	 * @return Filepath of selected file.
	 */
	public static final String selectFile(final JFrame parent, final boolean isOpen, final String relativePath,
			final String description, final MainWindowDesktop view, final String... extensions)
	{
		final String baseFolder = System.getProperty("user.dir");

		String folder = baseFolder + relativePath;
		final File testFile = new File(folder);
		if (!testFile.exists())
			folder = baseFolder; // no suitable subfolder - let user find them

		final JFileChooser dlg = new JFileChooser(folder);
		dlg.setPreferredSize(new Dimension(500, 500));

		// Set the file filter to show mgl files only
		final FileFilter filter = new FileNameExtensionFilter(description, extensions);
		dlg.setFileFilter(filter);

		int response;
		if (isOpen)
			response = dlg.showOpenDialog(parent);
		else
			response = dlg.showSaveDialog(parent);

		if (response == JFileChooser.APPROVE_OPTION)
			return dlg.getSelectedFile().getAbsolutePath();

		return null;
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Instantiates a few File Chooser objects
	 */
	public static void createFileChoosers()
	{
		AndroidApp.setJsonFileChooser(createFileChooser(AndroidApp.lastSelectedJsonPath(), ".json", "JSON files (.json)"));
		AndroidApp.setJarFileChooser(createFileChooser(AndroidApp.lastSelectedJarPath(), ".jar", "JAR files (.jar)"));
		AndroidApp.setGameFileChooser(createFileChooser(AndroidApp.lastSelectedGamePath(), ".lud", "LUD files (.lud)"));
		AndroidApp.setAiDefFileChooser(createFileChooser(AndroidApp.lastSelectedAiDefPath(), "ai.def", "AI.DEF files (ai.def)"));

		// Also create file chooser for saving played games
		AndroidApp.setSaveGameFileChooser(new JFileChooser(AndroidApp.lastSelectedSaveGamePath()));
		AndroidApp.saveGameFileChooser().setPreferredSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));

		AndroidApp.setLoadTrialFileChooser(new JFileChooser(AndroidApp.lastSelectedLoadTrialPath()));
		AndroidApp.loadTrialFileChooser().setPreferredSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));

		AndroidApp.setLoadTournamentFileChooser(new JFileChooser(AndroidApp.lastSelectedLoadTournamentPath()));
		AndroidApp.loadTournamentFileChooser().setPreferredSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Creates a File Chooser at a specified directory looking for a specific file extension.
	 */
	public static JFileChooser createFileChooser(final String defaultDir, final String extension, final String description)
	{
		final JFileChooser fileChooser;

		// Try to set a useful default directory
		if (defaultDir != null && defaultDir.length() > 0 && new File(defaultDir).exists())
		{
			fileChooser = new JFileChooser(defaultDir);
		}
		else
		{
			fileChooser = new JFileChooser("");
		}
			

		final FileFilter filter = new FileFilter()
		{
			@Override
			public boolean accept(final File f)
			{
				return f.isDirectory() || f.getName().endsWith(extension);
			}

			@Override
			public String getDescription()
			{
				return description;
			}

		};
		fileChooser.setFileFilter(filter);
		fileChooser.setPreferredSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));

		// Automatically try to switch to details view in file chooser
		final Action details = fileChooser.getActionMap().get("viewTypeDetails");
		if (details != null)
			details.actionPerformed(null);

		return fileChooser;
	}

	//-------------------------------------------------------------------------
	
	/**
	 * Writes specified text to a specified file, at the root directory. 
	 */
	public static void writeTextToFile(final String fileName, final String text)
	{
		final File file = new File("." + File.separator + fileName);
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (final IOException e2)
			{
				e2.printStackTrace();
			}
		}
		
		try (FileWriter writer = new FileWriter(file);)
		{
			writer.write(text + "\n");
			writer.close();
			AndroidApp.view().setTemporaryMessage("Log file created.");
		}
		catch (final Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Writes specified exception message to a specified file, at the root directory.
	 */
	public static void writeErrorFile(final String fileName, final Exception e)
	{
		final File file = new File("." + File.separator + fileName);
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (final IOException e2)
			{
				e2.printStackTrace();
			}
		}
		
		try (final PrintWriter writer = new PrintWriter(file))
		{
			final StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			writer.println(errors.toString() + "\n");
			writer.close();
			AndroidApp.view().setTemporaryMessage("Error report file created.");
		}
		catch (final Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------

}
