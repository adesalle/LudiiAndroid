package playerAndroid.app.menu;


import static androidUtils.awt.event.InputEvent.ALT_DOWN_MASK;
import static androidUtils.awt.event.InputEvent.CTRL_DOWN_MASK;

import androidUtils.awt.Font;
import androidUtils.awt.event.InputEvent;
import androidUtils.awt.event.ItemListener;
import androidUtils.swing.ButtonGroup;
import androidUtils.swing.JCheckBoxMenuItem;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import androidUtils.JSONObject;
import androidUtils.JSONTokener;

import androidUtils.swing.JRadioButtonMenuItem;
import androidUtils.swing.KeyStroke;
import androidUtils.swing.UIManager;
import androidUtils.swing.menu.JMenu;
import androidUtils.swing.menu.JMenuBar;
import androidUtils.swing.menu.JMenuButton;
import androidUtils.swing.menu.JMenuItem;
import playerAndroid.app.AndroidApp;
import app.PlayerApp;
import playerAndroid.app.loading.MiscLoading;
import app.utils.PuzzleSelectionType;
import app.utils.SettingsExhibition;
import game.equipment.container.board.Track;
import game.types.play.RepetitionType;
import main.Constants;
import main.FileHandling;
import main.StringRoutines;
import main.options.GameOptions;
import main.options.Option;
import main.options.Ruleset;
import other.context.Context;

//--------------------------------------------------------

/**
 * The app's main menu.
 *
 * @author cambolbro and Eric.Piette
 */
public class MainMenu extends JMenuBar
{

    private static final long serialVersionUID = 1L;

    public static JMenu mainOptionsMenu;
    protected JMenu submenu;

    public JMenuItem showIndexOption;
    public JMenuItem showCoordinateOption;

    //-------------------------------------------------------------------------


    /**
     * Constructor.
     */

    public MainMenu(final PlayerApp app)
    {
        // No menu for exhibition app.
        if (app.settingsPlayer().usingMYOGApp() || SettingsExhibition.exhibitionVersion)
            return;

        final ActionListener al = app;
        final ItemListener il = app;

        JMenu menuItem;
        JCheckBoxMenuItem cbMenuItem;

        UIManager.put("Menu.font", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("MenuItem.font", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("CheckBoxMenuItem.font", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("RadioButtonMenuItem.font", new Font("Arial", Font.PLAIN, 16));

        //---------------------------------------------------------------------

        menuItem = new JMenu("IA");
        JMenuButton button = add(menuItem);
        button.addActionListener(al);
        menuItem = new JMenu("Load Game");
        button = add(menuItem);
        button.addActionListener(al);

        menuItem = new JMenu("Restart");
        button = add(menuItem);
        button.addActionListener(al);

        menuItem = new JMenu("Play/Pause");
        button = add(menuItem);
        button.addActionListener(al);;
        setProportionalWeights(true);
    }




    //-------------------------------------------------------------------------


    /**
     * Update the options menu listing with any options found for the current game.
     * @param context
     */

    public static void updateOptionsMenu(final PlayerApp app, final Context context, final JMenu optionsMenu) {
        if (optionsMenu != null) {
            optionsMenu.removeAll();

            final GameOptions gameOptions = context.game().description().gameOptions();
            final List<String> currentOptions =
                    gameOptions.allOptionStrings
                            (
                                    app.manager().settingsManager().userSelections().selectedOptionStrings()
                            );

            // List possible options
            for (int cat = 0; cat < gameOptions.numCategories(); cat++) {
                // Create a submenu for this option group
                final List<Option> options = gameOptions.categories().get(cat).options();
                if (options.isEmpty())
                    continue;  // no options for this group

                final List<String> headings = options.get(0).menuHeadings();
                if (headings.size() < 2) {
                    System.out.println("** Not enough headings for menu option group: " + headings);
                    return;
                }

                final JMenu submenu = new JMenu(headings.get(0));
                optionsMenu.add(submenu);

                // Group the choices for this option group together
                final ButtonGroup group = new ButtonGroup();
                for (int i = 0; i < options.size(); i++) {
                    final Option option = options.get(i);

                    if (option.menuHeadings().size() < 2) {
                        System.out.println("** Not enough headings for menu option: " + option.menuHeadings());
                        return;
                    }

                    if (!option.menuHeadings().contains("Incomplete"))    // Eric wants to hide incomplete options
                    {
                        final JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(option.menuHeadings().get(1));
                        rbMenuItem.setSelected(currentOptions.contains(StringRoutines.join("/", option.menuHeadings())));
                        rbMenuItem.addItemListener(app);
                        group.add(rbMenuItem);
                        submenu.add(rbMenuItem);
                    }
                }

                MenuScroller.setScrollerFor(submenu, 20, 50, 0, 0);
            }

            // Auto-select ruleset if necessary
            if (app.manager().settingsManager().userSelections().ruleset() == Constants.UNDEFINED)
                app.manager().settingsManager().userSelections().setRuleset(context.game().description().autoSelectRuleset(currentOptions));

            // List predefined rulesets
            final List<Ruleset> rulesets = context.game().description().rulesets();
            if (rulesets != null && !rulesets.isEmpty()) {
                optionsMenu.addSeparator();

                final ButtonGroup rulesetGroup = new ButtonGroup();

                for (int rs = 0; rs < rulesets.size(); rs++) {
                    final Ruleset ruleset = rulesets.get(rs);

                    if (!ruleset.optionSettings().isEmpty() && !ruleset.heading().contains("Incomplete"))    // Eric wants to hide unimplemented and incomplete rulesets
                    {
                        if (ruleset.variations().isEmpty()) {
                            final JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(ruleset.heading());
                            rbMenuItem.setSelected(app.manager().settingsManager().userSelections().ruleset() == rs);
                            rbMenuItem.addItemListener(app);
                            rulesetGroup.add(rbMenuItem);
                            optionsMenu.add(rbMenuItem);
                        } else {
                            final JMenu submenuRuleset = new JMenu(ruleset.heading());
                            optionsMenu.add(submenuRuleset);

                            final JRadioButtonMenuItem rbMenuDefaultItem = new JRadioButtonMenuItem("Default");
                            rbMenuDefaultItem.setSelected(app.manager().settingsManager().userSelections().ruleset() == rs);
                            rbMenuDefaultItem.addItemListener(app);
                            rulesetGroup.add(rbMenuDefaultItem);
                            submenuRuleset.add(rbMenuDefaultItem);

                            // Group the choices for this option group together
                            for (final String rulesetOptionName : ruleset.variations().keySet()) {
                                final JMenu rbMenuItem = new JMenu(rulesetOptionName);
                                submenuRuleset.add(rbMenuItem);

                                // Group the choices for this option group together
                                final ButtonGroup rulesetOptionsGroup = new ButtonGroup();
                                for (int i = 0; i < ruleset.variations().get(rulesetOptionName).size(); i++) {
                                    final String rulesetOption = ruleset.variations().get(rulesetOptionName).get(i);

                                    final JRadioButtonMenuItem rulesetOptionMenuItem = new JRadioButtonMenuItem(rulesetOption);
                                    rulesetOptionMenuItem.setSelected(currentOptions.contains(rulesetOptionName + "/" + rulesetOption));
                                    rulesetOptionMenuItem.addItemListener(app);
                                    rulesetOptionsGroup.add(rulesetOptionMenuItem);
                                    rbMenuItem.add(rulesetOptionMenuItem);
                                }
                            }

                            MenuScroller.setScrollerFor(submenuRuleset, 20, 50, 0, 0);
                        }
                    }
                }
            }

            MenuScroller.setScrollerFor(optionsMenu, 20, 50, 0, 0);
        }
    }

    //-------------------------------------------------------------------------


    /**
     * Finds all demos in the Player module.
     * @return List of our demos
     */

    private static String[] findDemos()
    {
        // Try loading from JAR file
        String[] choices = FileHandling.getResourceListing(MainMenuOld.class, "demos/", ".json");
        if (choices == null)
        {
            try
            {
                // Try loading from memory in IDE
                // Start with known .json file
                final URL url = MainMenuOld.class.getResource("/demos/Hnefatafl - Common.json");
                String path = new File(url.toURI()).getPath();
                path = path.substring(0, path.length() - "Hnefatafl - Common.json".length());

                // Get the list of .json files in this directory and subdirectories
                final List<String> names = new ArrayList<>();
                visitFindDemos(path, names);

                Collections.sort(names);
                choices = names.toArray(new String[names.size()]);
            }
            catch (final URISyntaxException exception)
            {
                exception.printStackTrace();
            }
        }
        return choices;
    }

    //-------------------------------------------------------------------------


    /**
     * Recursive helper method for finding all demos
     * @param path
     * @param names
     */

    private static void visitFindDemos(final String path, final List<String> names)
    {
        final File root = new File(path);
        final File[] list = root.listFiles();

        if (list == null)
            return;

        for (final File file : list)
        {
            if (file.isDirectory())
            {
                visitFindDemos(path + file.getName() + File.separator, names);
            }
            else
            {
                if (file.getName().contains(".json"))
                {
                    // Add this demo name to the list of choices
                    final String name = new String(file.getName());
                    names.add(path.substring(path.indexOf(File.separator + "demos" + File.separator)) + name);
                }
            }
        }
    }

    //-------------------------------------------------------------------------

}
