package playerAndroid.app;

import android.content.res.AssetManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import androidUtils.ZipManager;
import androidUtils.awt.Graphics2D;
import androidUtils.awt.SVGGraphics2D;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ItemEvent;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.image.BufferedImage;
import androidUtils.swing.JFileChooser;
import androidUtils.JSONObject;
import androidUtils.awt.Dimension;
import androidUtils.awt.EventQueue;
import androidUtils.awt.Point;
import androidUtils.awt.Rectangle;
import app.PlayerApp;
import app.utils.GameSetup;
import app.utils.SettingsExhibition;
import app.views.View;
import androidUtils.swing.WindowConstants;
import game.equipment.container.board.Board;
import manager.ai.AIUtil;
import playerAndroid.app.display.MainWindowDesktop;
import playerAndroid.app.display.util.AndroidGUIUtil;
import playerAndroid.app.loading.FileLoading;
import playerAndroid.app.menu.MainMenuFunctions;
import playerAndroid.app.util.SettingsDesktop;
import playerAndroid.app.loading.GameLoading;
import playerAndroid.app.loading.TrialLoading;
import playerAndroid.app.menu.MainMenu;
import playerAndroid.app.util.UserPreferences;

import main.Constants;
import main.collections.FastArrayList;
import manager.ai.AIDetails;
import other.context.Context;
import other.location.Location;
import other.move.Move;
import tournament.Tournament;
import util.PlaneType;

public class AndroidApp extends PlayerApp {

    public static final String AppName = "Ludii Player";
    public static boolean devJar = false;

    protected static JFrameListener frame;

    protected static MainWindowDesktop view;

    /** Minimum resolution of the application. */
    private static final int minimumViewWidth = 1280;
    private static final int minimumViewHeight = 768;


    /**
     * Reference to file chooser we use for selecting JSON files (containing AI configurations)
     */
    private static JFileChooser jsonFileChooser;

    /**
     * Reference to file chooser we use for selecting JAR files (containing third-party AIs)
     */
    private static JFileChooser jarFileChooser;

    /**
     * Reference to file chooser we use for selecting AI.DEF files (containing AI configurations)
     */
    private static JFileChooser aiDefFileChooser;

    /**
     * Reference to file chooser we use for selecting LUD files
     */
    private static JFileChooser gameFileChooser;

    //-------------------------------------------------------------------------

    /** Whether the trial should be loaded from a saved file (based on file validity checks). */
    private static boolean shouldLoadTrial = false;

    //-------------------------------------------------------------------------

    /** Reference to file chooser we use for saving games we have just played */
    private static JFileChooser saveGameFileChooser;

    /** File chooser for loading games. */
    protected static JFileChooser loadGameFileChooser;

    /** File chooser for loading games. */
    private static JFileChooser loadTrialFileChooser;

    /** File chooser for loading games. */
    private static JFileChooser loadTournamentFileChooser;

    /** Last selected filepath for JSON file chooser (loaded from preferences) */
    private static String lastSelectedJsonPath;

    /** Last selected filepath for JSON file chooser (loaded from preferences) */
    private static String lastSelectedJarPath;

    /** Last selected filepath for AI.DEF file chooser (loaded from preferences) */
    private static String lastSelectedAiDefPath;

    /** Last selected filepath for Game file chooser (loaded from preferences) */
    private static String lastSelectedGamePath;

    /** Last selected filepath for JSON file chooser (loaded from preferences) */
    private static String lastSelectedSaveGamePath;

    /** Last selected filepath for JSON file chooser (loaded from preferences) */
    private static String lastSelectedLoadTrialPath;

    /** Last selected filepath for JSON file chooser (loaded from preferences) */
    private static String lastSelectedLoadTournamentPath;



    public AndroidApp(){

    }



    public static boolean shouldLoadTrial() {
        return  shouldLoadTrial;
    }

    public static JFileChooser loadTournamentFileChooser() {
        return loadTournamentFileChooser;
    }

    public static JFileChooser aiDefFileChooser() {
        return aiDefFileChooser;
    }


    public static String lastSelectedSaveGamePath()
    {
        return lastSelectedSaveGamePath;
    }

    public static void setLastSelectedSaveGamePath(final String lastSelectedSaveGamePath)
    {
        AndroidApp.lastSelectedSaveGamePath = lastSelectedSaveGamePath;
    }

    public static String lastSelectedJsonPath()
    {
        return lastSelectedJsonPath;
    }

    public static void setLastSelectedJsonPath(final String lastSelectedJsonPath)
    {
        AndroidApp.lastSelectedJsonPath = lastSelectedJsonPath;
    }

    public static String lastSelectedJarPath()
    {
        return lastSelectedJarPath;
    }

    public static String lastSelectedGamePath()
    {
        return lastSelectedGamePath;
    }

    public static void setLastSelectedJarPath(final String lastSelectedJarPath)
    {
        AndroidApp.lastSelectedJarPath = lastSelectedJarPath;
    }

    public static void setLastSelectedGamePath(final String lastSelectedGamePath)
    {
        AndroidApp.lastSelectedGamePath = lastSelectedGamePath;
    }

    public static String lastSelectedLoadTrialPath()
    {
        return lastSelectedLoadTrialPath;
    }

    public static void setLastSelectedLoadTrialPath(final String lastSelectedLoadTrialPath)
    {
        AndroidApp.lastSelectedLoadTrialPath = lastSelectedLoadTrialPath;
    }

    public static String lastSelectedLoadTournamentPath()
    {
        return lastSelectedLoadTournamentPath;
    }

    public static void setLastSelectedLoadTournamentPath(final String lastSelectedLoadTournamentPath)
    {
        AndroidApp.lastSelectedLoadTournamentPath = lastSelectedLoadTournamentPath;
    }
    

    public static void setAiDefFileChooser(final JFileChooser aiDefFileChooser)
    {
        AndroidApp.aiDefFileChooser = aiDefFileChooser;
    }

    public static String lastSelectedAiDefPath()
    {
        return lastSelectedAiDefPath;
    }

    public static void setLastSelectedAiDefPath(final String lastSelectedAiDefPath)
    {
        AndroidApp.lastSelectedAiDefPath = lastSelectedAiDefPath;
    }

    public void createAndroidApp()
    {

        for (int i = 0; i < Constants.MAX_PLAYERS + 1; i++) // one extra for the shared player
        {
            final JSONObject json = new JSONObject()
                    .put("AI", new JSONObject()
                            .put("algorithm", "Human")
                    );

            manager().aiSelected()[i] = new AIDetails(manager(), json, i, "Human");
        }

        try
        {
            createFrame();
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void createFrame() throws SQLException{
        try
        {
            UserPreferences.loadPreferences(this);
        }
        catch (final Exception e)
        {
            //System.out.println("Failed to create preferences file.");
            e.printStackTrace();
        }

        try
        {
            frame = new JFrameListener(AppName, this);
            frame.setMinimumSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));
            frame.setSize(new Dimension(SettingsDesktop.defaultWidth, SettingsDesktop.defaultHeight));

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // Logo
            try
            {
                AssetManager manager = StartAndroidApp.getAppContext().getAssets();
                final InputStream resource = manager.open("img/ludii-logo-100x100.png");
                final Bitmap btp = BitmapFactory.decodeStream(resource);
                final BufferedImage image = new BufferedImage(btp);
                frame.setIconImage(image);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }

            System.out.println("default " + SettingsDesktop.defaultWidth + " " + SettingsDesktop.defaultHeight);

            view = new MainWindowDesktop(this);
            view.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

            frame.setContentPane(view);


            if (SettingsExhibition.exhibitionVersion)
            {
                //frame.setUndecorated(true);
                frame.setResizable(false);
                frame.setSize(SettingsExhibition.exhibitionDisplayWidth, SettingsExhibition.exhibitionDisplayHeight);
            }

            try
            {
                if (settingsPlayer().defaultX() == -1 || settingsPlayer().defaultY() == -1)
                    frame.setLocationRelativeTo(null);
                else
                    frame.setLocation(settingsPlayer().defaultX(), settingsPlayer().defaultY());

                //if (settingsPlayer().frameMaximised())
                    //frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
            }
            catch (final Exception e)
            {
                frame.setLocationRelativeTo(null);
            }

            frame.setVisible(true);

            FileLoading.createFileChoosers();
            //setCurrentGraphicsDevice(frame.getGraphicsConfiguration().getDevice());

            // gets called when the app is closed (save preferences and trial)
            Runtime.getRuntime().addShutdownHook(new Thread(this::appClosedTasks));


            loadInitialGame(true);


        }
        catch (final Exception e)
        {
            System.out.println("Failed to create application frame.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the initial game.
     * Either the default game of the previously loaded game when the application was last closed.
     */
    protected void loadInitialGame(final boolean firstTry)
    {

            try {
                if (SettingsExhibition.exhibitionVersion) {
                    GameLoading.loadGameFromMemory(this, SettingsExhibition.exhibitionGamePath, false);

                    if (SettingsExhibition.againstAI) {
                        final JSONObject json = new JSONObject().put("AI",
                                new JSONObject()
                                        .put("algorithm", "UCT")
                        );
                        AIUtil.updateSelectedAI(manager(), json, 2, "UCT");
                        manager().aiSelected()[2].setThinkTime(SettingsExhibition.thinkingTime);
                    }

                    bridge().settingsVC().setShowPossibleMoves(true);

                    return;
                }

                if (firstTry)
                    TrialLoading.loadStartTrial(this);


                if (manager().ref().context() == null) {
                    if (firstTry)
                        GameLoading.loadGameFromMemory(this, Constants.DEFAULT_GAME_PATH, false);
                    else {
                        settingsPlayer().setLoadedFromMemory(true);
                        GameSetup.compileAndShowGame(this, Constants.FAIL_SAFE_GAME_DESCRIPTION, false);
                        EventQueue.invokeLater(() ->
                        {
                            setTemporaryMessage("Failed to start game. Loading default game (Tic-Tac-Toe).");
                        });
                    }
                }


                for (int i = 1; i <= manager().ref().context().game().players().count(); i++)
                    if (aiSelected()[i] != null)
                        AIUtil.updateSelectedAI(manager(), manager().aiSelected()[i].object(), i, manager().aiSelected()[i].menuItemName());

                frame.setJMenuBar(new MainMenu(this));


            } catch (final Exception e) {
                e.printStackTrace();

                    if (firstTry) {
                        // Try to load the default game.
                        manager().setSavedLudName(null);
                        settingsPlayer().setLoadedFromMemory(true);
                        setLoadTrial(false);
                        loadInitialGame(false);
                    }

                    if (manager().savedLudName() != null) {
                        addTextToStatusPanel("Failed to start game: " + manager().savedLudName() + "\n");
                    } else if (manager().ref().context().game().name() != null) {
                        addTextToStatusPanel("Failed to start external game description.\n");
                    }
            }

    }

    /** Tasks that are performed when the application is closed. */
    public void appClosedTasks()
    {
        if (SettingsExhibition.exhibitionVersion)
            return;

        manager().settingsNetwork().restoreAiPlayers(manager());

        // Close all AI objects
        for (final AIDetails ai : manager().aiSelected())
            if (ai.ai() != null)
                ai.ai().closeAI();

        if (manager().ref().context().game().equipmentWithStochastic())
            manager().ref().context().trial().reset(manager().ref().context().game());

        // Save the current trial
        final File file = new File("." + File.separator + "ludii.trl");
        TrialLoading.saveTrial(this, file);

        // Save the rest of the preferences
        UserPreferences.savePreferences(this);

        ZipManager.clearCache(StartAndroidApp.getAppContext());
    }

    public AIDetails[] aiSelected()
    {
        return manager().aiSelected();
    }

    public static MainWindowDesktop view()
    {
        return view;
    }

    public static JFrameListener frame()
    {
        return frame;
    }
    @Override
    public List<View> getPanels()
    {
        return view.getPanels();
    }

    public static JFileChooser jsonFileChooser()
    {
        return jsonFileChooser;
    }

    public static void setJsonFileChooser(final JFileChooser jsonFileChooser)
    {
        AndroidApp.jsonFileChooser = jsonFileChooser;
    }

    public static JFileChooser jarFileChooser()
    {
        return jarFileChooser;
    }

    public static JFileChooser gameFileChooser()
    {
        return gameFileChooser;
    }

    public static void setJarFileChooser(final JFileChooser jarFileChooser)
    {
        AndroidApp.jarFileChooser = jarFileChooser;
    }

    public static void setGameFileChooser(final JFileChooser gameFileChooser)
    {
        AndroidApp.gameFileChooser = gameFileChooser;
    }

    public static JFileChooser saveGameFileChooser()
    {
        return saveGameFileChooser;
    }

    public static void setSaveGameFileChooser(final JFileChooser saveGameFileChooser)
    {
        AndroidApp.saveGameFileChooser = saveGameFileChooser;
    }

    public static JFileChooser loadTrialFileChooser()
    {
        return loadTrialFileChooser;
    }

    public static void setLoadTrialFileChooser(final JFileChooser loadTrialFileChooser)
    {
        AndroidApp.loadTrialFileChooser = loadTrialFileChooser;
    }


    public static void setLoadTournamentFileChooser(final JFileChooser loadTournamentFileChooser)
    {
        AndroidApp.loadTournamentFileChooser = loadTournamentFileChooser;
    }

    @Override
    public void drawBoard(final Context context, final Graphics2D g2d, final Rectangle2D boardDimensions)
    {
        System.out.println("AndroidApp 508");
        if (graphicsCache().boardImage() == null)
        {
            System.out.println("boardImage = null");
            final Board board = context.board();
            bridge().getContainerStyle(board.index()).render(PlaneType.BOARD, context);

            final String svg = bridge().getContainerStyle(board.index()).containerSVGImage();
            if (svg == null || svg.equals(""))
                return;

            graphicsCache().setBoardImage(SVGGraphics2D.getBufferedImageFrom64(svg));

        }


        if (!context.game().metadata().graphics().boardHidden())
        {
            g2d.drawImage(graphicsCache().boardImage(), 0, 0, null);

        }
    }

    @Override
    public void drawGraph(final Context context, final Graphics2D g2d, final Rectangle2D boardDimensions)
    {
        if (graphicsCache().graphImage() == null || context.board().isBoardless())
        {
            final Board board = context.board();
            bridge().getContainerStyle(board.index()).render(PlaneType.GRAPH, context);

            final String svg = bridge().getContainerStyle(board.index()).graphSVGImage();
            if (svg == null)
                return;

            graphicsCache().setBoardImage(SVGGraphics2D.getBufferedImageFrom64(svg));
        }

        g2d.drawImage(graphicsCache().graphImage(), 0, 0, null);
    }
    @Override
    public Tournament tournament() {
        return null;
    }

    @Override
    public void setTournament(Tournament tournament) {

    }

    @Override
    public void reportError(String error) {

    }

    @Override
    public void repaintComponentBetweenPoints(Context context, Location moveFrom, Point startPoint, Point endPoint) {
        AndroidGUIUtil.repaintComponentBetweenPoints(this, context, moveFrom, startPoint, endPoint);
    }

    @Override
    public void showPuzzleDialog(int site) {

    }

    @Override
    public void showPossibleMovesDialog(Context context, FastArrayList<Move> possibleMoves) {

    }

    @Override
    public void saveTrial() {

    }

    @Override
    public void playSound(String soundName) {

    }

    @Override
    public void setVolatileMessage(String text) {

    }

    public static void setLoadTrial(final boolean shouldLoadTrial)
    {
        AndroidApp.shouldLoadTrial = shouldLoadTrial;
    }

    @Override
    public void writeTextToFile(String fileName, String log) {

    }

    @Override
    public void resetMenuGUI() {

    }

    @Override
    public void showSettingsDialog() {

    }

    @Override
    public void showOtherDialog(FastArrayList<Move> otherPossibleMoves) {

    }

    @Override
    public void showInfoDialog() {

    }

    @Override
    public int width() {
        return minimumViewWidth;
    }

    @Override
    public int height() {
        return minimumViewHeight;
    }
    @Override
    public Rectangle[] playerSwatchList()
    {
        return view.playerSwatchList;
    }

    @Override
    public Rectangle[] playerNameList()
    {
        return view.playerNameList;
    }

    @Override
    public boolean[] playerSwatchHover()
    {
        return view.playerSwatchHover;
    }

    @Override
    public boolean[] playerNameHover()
    {
        return view.playerNameHover;
    }


    @Override
    public void repaint(Rectangle rect) {
        view.isPainting = true;
        view.repaint(rect);
    }

    @Override
    public JSONObject getNameFromJar() {
        return null;
    }

    @Override
    public JSONObject getNameFromJson() {
        return null;
    }

    @Override
    public JSONObject getNameFromAiDef() {
        return null;
    }

    @Override
    public void loadGameFromName(String name, List<String> options, boolean debug) {
        GameLoading.loadGameFromName(this, name, options, debug);
    }

    @Override
    public void addTextToStatusPanel(String text) {

    }

    @Override
    public void addTextToAnalysisPanel(String text) {

    }

    @Override
    public void selectAnalysisTab() {

    }

    @Override
    public void repaint() {
        view.isPainting = true;
        view.repaint();
    }

    @Override
    public void reportForfeit(int playerForfeitNumber) {

    }

    @Override
    public void reportTimeout(int playerForfeitNumber) {

    }

    @Override
    public void reportDrawAgreed() {

    }

    @Override
    public void updateFrameTitle(boolean alsoUpdateMenu) {

    }

    @Override
    public void updateTabs(Context context) {

    }

    @Override
    public void repaintTimerForPlayer(int playerId)
        {

        }

    @Override
    public void setTemporaryMessage(String text) {

    }

    @Override
    public void refreshNetworkDialog() {

    }


    @Override
    public void actionPerformed(final ActionEvent e) {
        MainMenuFunctions.checkActionsPerformed(this, e);
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
        MainMenuFunctions.checkItemStateChanges(this, e);
    }
}
