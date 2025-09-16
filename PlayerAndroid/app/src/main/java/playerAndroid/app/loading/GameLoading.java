package playerAndroid.app.loading;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

//import javax.swing.JFileChooser;

import androidUtils.UriToFileConverter;
import androidUtils.swing.JFileChooser;
import playerAndroid.app.AndroidApp;
import app.PlayerApp;
//import app.display.dialogs.GameLoaderDialog;
import app.utils.GameSetup;
import main.Constants;
import main.FileHandling;
import main.GameNames;
import other.GameLoader;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.display.dialogs.GameLoaderDialog;

public class GameLoading {

    private static int fcReturnVal = JFileChooser.CANCEL_OPTION;
    public static PlayerApp app = null;

    public static boolean debug = false;

    //-------------------------------------------------------------------------

    /**
     * Load a game from an external .lud file.
     */
    public static void loadGameFromFile(final PlayerApp app) {
        System.out.println("before return");
        GameLoading.app = app;
        fcReturnVal = AndroidApp.gameFileChooser().showOpenDialog(AndroidApp.frame());
    }

    public static void loadGameFromFileAfterResponse() {
        System.out.println("-----------------------------------------------------------");
        if (fcReturnVal == JFileChooser.APPROVE_OPTION && app != null) {
            System.out.println("ok");
            fcReturnVal = JFileChooser.CANCEL_OPTION;
            String extension = UriToFileConverter.getFileExtensionFromUri(StartAndroidApp.getAppContext(), AndroidApp.gameFileChooser().getSelectedFileUri());


            if (!"lud".equals(extension))return;

            String desc = UriToFileConverter.readTextFromUri(StartAndroidApp.getAppContext(), AndroidApp.gameFileChooser().getSelectedFileUri());

            if (desc != null) {
                System.out.println(desc);

                // TODO if we want to preserve per-game last-selected-options in preferences, load them here
                app.manager().settingsManager().userSelections().setRuleset(Constants.UNDEFINED);
                app.manager().settingsManager().userSelections().setSelectOptionStrings(new ArrayList<>());

                loadGameFromFileDesc(app, desc);
            }
            GameLoading.app = null;
        }
    }

    //-------------------------------------------------------------------------

    /**
     * Load a specified external .lud file.
     */
    public static void loadGameFromFilePath(final PlayerApp app, final String filePath) {
        System.out.println("loaded in " + filePath);
        if (filePath != null) {

            app.manager().setSavedLudName(filePath);

            String desc = "";
            try {
                app.settingsPlayer().setLoadedFromMemory(false);
                desc = FileHandling.loadTextContentsFromFile(filePath);
                GameSetup.compileAndShowGame(app, desc, false);
            } catch (final FileNotFoundException ex) {
                System.out.println("Unable to open file '" + filePath + "'");
            } catch (final IOException ex) {
                System.out.println("Error reading file '" + filePath + "'");
            }
        }
    }

    public static void loadGameFromFileDesc(final PlayerApp app, final String desc)
    {
        GameSetup.compileAndShowGame(app, desc, false);
    }

    //-------------------------------------------------------------------------

    /**
     * Load an internal .lud game description from memory
     */
    public static void loadGameFromMemory(final PlayerApp app, final boolean debug)
    {
        GameLoading.debug = debug;
        GameLoading.app = app;
        final String[] choices = FileHandling.listGames();

        String initialChoice = choices[0];
        for (final String choice : choices)
        {
            if (app.manager().savedLudName() != null && app.manager().savedLudName().endsWith(choice.replaceAll(Pattern.quote("\\"), "/")))
            {
                initialChoice = choice;
                break;
            }
        }
        final String choice = GameLoaderDialog.showDialog(AndroidApp.frame(), choices, initialChoice);


    }

    //-------------------------------------------------------------------------

    /**
     * Load a selected internal .lud game description.
     */
    public static void loadGameFromMemory(final PlayerApp app, final String gamePath, final boolean debug)
    {
        // Get game description from resource
        final StringBuilder sb = new StringBuilder();

        Context context = StartAndroidApp.getAppContext();


        if (gamePath != null)
        {
            String path = gamePath.replaceAll(Pattern.quote("\\"), "/");
            path = path.substring(path.indexOf("lud/"));

            app.manager().setSavedLudName(path);

            try (InputStream in = context.getAssets().open(path);
                 BufferedReader rdr = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                String line;
                while ((line = rdr.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                app.settingsPlayer().setLoadedFromMemory(true);
                GameSetup.compileAndShowGame(app, sb.toString(), debug);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    //-------------------------------------------------------------------------

    /**
     * Load game from name.
     *
     * @param name Filename + .lud extension.
     * @param options List of options to select
     */
    public static void loadGameFromName(final PlayerApp app, final String name, final List<String> options, final boolean debug)
    {
        try
        {
            final String gameDescriptionString = getGameDescriptionRawFromName(app, name);

            if (gameDescriptionString == null)
            {
                loadGameFromFilePath(app, name.substring(0, name.length()));
            }
            else
            {
                app.settingsPlayer().setLoadedFromMemory(true);
                app.manager().settingsManager().userSelections().setRuleset(Constants.UNDEFINED);
                app.manager().settingsManager().userSelections().setSelectOptionStrings(options);
                GameSetup.compileAndShowGame(app, gameDescriptionString, false);
            }
        }
        catch (final Exception e)
        {
            // used if a recent game was selected from an external file.

        }
    }

    //-------------------------------------------------------------------------

    /**
     * Returns the raw game description for a game, based on the name.
     */
    public static String getGameDescriptionRawFromName(final PlayerApp app, final String name)
    {
        final String filePath = GameLoader.getFilePath(name);

        // Probably loading from an external .lud file.
        if (filePath == null)
            return null;

        final StringBuilder sb = new StringBuilder();

        app.manager().setSavedLudName(filePath);

        try (InputStream in = GameLoader.class.getResourceAsStream(filePath))
        {
            try (final BufferedReader rdr = new BufferedReader(new InputStreamReader(in, "UTF-8")))
            {
                String line;
                while ((line = rdr.readLine()) != null)
                    sb.append(line + "\n");
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (final Exception e)
        {
            System.out.println("Did you change the name??");
        }

        return sb.toString();
    }

    //-------------------------------------------------------------------------

    /**
     * Load a random game from the list of possible options in GameNames.java
     */
    public static void loadRandomGame(final PlayerApp app)
    {
        final List<String> allGameNames = new ArrayList<>();
        EnumSet.allOf(GameNames.class).forEach(game -> allGameNames.add(game.ludName()));
        final Random random = new Random();
        final String chosenGameName = allGameNames.get(random.nextInt(allGameNames.size()));

        // TODO if we want to preserve per-game last-selected-options in preferences, load them here
        app.manager().settingsManager().userSelections().setRuleset(Constants.UNDEFINED);
        app.manager().settingsManager().userSelections().setSelectOptionStrings(new ArrayList<String>());

        loadGameFromName(app, chosenGameName + ".lud", new ArrayList<String>(), false);
    }

    //-------------------------------------------------------------------------

}
