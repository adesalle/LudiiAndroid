package playerAndroid.app.menu;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


import androidUtils.awt.event.ItemEvent;
import androidUtils.swing.ImageIcon;



import agentPrediction.external.AgentPredictionExternal;
import agentPrediction.external.MetricPredictionExternal;
import agentPrediction.internal.AgentPredictionInternal;
import agentPrediction.internal.models.LinearRegression;
import androidUtils.awt.EventQueue;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.image.BufferedImage;
import androidUtils.swing.JOptionPane;

import androidUtils.swing.menu.JMenu;
import androidUtils.swing.menu.JMenuBar;
import androidUtils.swing.menu.JMenuItem;
import androidUtils.swing.menu.JPopupMenu;
import androidUtils.swing.menu.JSubMenu;
import compiler.Compiler;
import playerAndroid.app.AndroidApp;
import app.PlayerApp;
import playerAndroid.app.display.dialogs.AboutDialog;
import playerAndroid.app.display.dialogs.DeveloperDialog;
import playerAndroid.app.display.dialogs.EvaluationDialog;
import playerAndroid.app.display.dialogs.GameLoaderDialog;
import playerAndroid.app.display.dialogs.ReconstructionDialog;
import playerAndroid.app.display.dialogs.SVGViewerDialog;
import playerAndroid.app.display.dialogs.TestLudemeDialog;
import playerAndroid.app.display.dialogs.MoveDialog.PossibleMovesDialog;
import playerAndroid.app.display.dialogs.editor.EditorDialog;
import playerAndroid.app.display.dialogs.visual_editor.StartVisualEditor;
import playerAndroid.app.display.screenCapture.ScreenCapture;
import playerAndroid.app.display.util.Thumbnails;
import playerAndroid.app.display.views.tabs.TabView;

import app.utils.GameSetup;
import app.utils.GameUtil;
import app.utils.PuzzleSelectionType;
import app.utils.QrCodeGeneration;
import app.views.tools.ToolView;
import approaches.random.Generator;
import contextualiser.ContextualSimilarity;
import features.feature_sets.BaseFeatureSet;
import game.Game;
import game.rules.phase.Phase;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.types.play.RepetitionType;
import gnu.trove.list.array.TIntArrayList;
import grammar.Grammar;
import graphics.svg.SVGLoader;
import main.Constants;
import main.FileHandling;
import main.StringRoutines;
import main.collections.FastArrayList;
import main.grammar.Call;
import main.grammar.Description;
import main.grammar.Report;
import main.options.GameOptions;
import main.options.Option;
import main.options.Ruleset;
import manager.ai.AIDetails;
import manager.ai.AIUtil;
import manager.network.local.LocalFunctions;
import metadata.ai.features.Features;
import metadata.ai.heuristics.Heuristics;
import other.AI;
import other.GameLoader;
import other.action.Action;
import other.action.move.remove.ActionRemove;
import other.action.state.ActionSetNextPlayer;
import other.concept.Concept;
import other.concept.ConceptComputationType;
import other.concept.ConceptDataType;
import other.concept.ConceptType;
import other.context.Context;
import other.location.FullLocation;
import other.model.Model;
import other.move.Move;
import other.trial.Trial;
import parser.Parser;
import playerAndroid.app.StartAndroidApp;
import playerAndroid.app.loading.GameLoading;
import playerAndroid.app.loading.MiscLoading;
import playerAndroid.app.loading.TrialLoading;
import playerAndroid.app.manualGeneration.ManualGeneration;
import policies.softmax.SoftmaxPolicyLinear;
import search.pns.ProofNumberSearch.ProofGoals;
import supplementary.EvalUtil;
import supplementary.experiments.EvalAIsThread;
import supplementary.experiments.EvalGamesSet;
import supplementary.experiments.ludemes.CountLudemes;
import util.StringUtil;
import utils.RandomAI;

//--------------------------------------------------------

/**
 * The app's main menu.
 *
 * @author cambolbro and Matthew.Stephenson and Eric.Piette
 */
public class MainMenuFunctions extends JMenuBar
{
    private static final long serialVersionUID = 1L;

    /** Thread in which we're timing random playouts. */
    private static Thread timeRandomPlayoutsThread = null;

    /** Thread in which we're comparing agents. */
    private static Thread agentComparisonThread = null;

    /** The visual editor. */
    private static StartVisualEditor startVisualEditor;

    //-------------------------------------------------------------------------

    public static void checkActionsPerformed(final AndroidApp app, final ActionEvent e)
    {

        app.bridge().settingsVC().setSelectedFromLocation(new FullLocation(Constants.UNDEFINED));
        final JMenuItem source = (JMenuItem) (e.getSource());
        final Context context = app.manager().ref().context();
        final Game game = context.game();

        if (source.getText().equals("Load Game"))
        {
            if (!app.manager().settingsManager().agentsPaused())
            {
                app.manager().settingsManager().setAgentsPaused(app.manager(), true);
            }
            GameLoading.loadGameFromMemory(app, false);
        }
        else if (source.getText().equals("Restart"))
        {
            app.addTextToStatusPanel("-------------------------------------------------\n");
            app.addTextToStatusPanel("Game Restarted.\n");

            GameUtil.resetGame(app, false);
        }
        else if (source.getText().equals("Play/Pause"))
        {
            AndroidApp.view().toolPanel().buttons.get(ToolView.PLAY_BUTTON_INDEX).press();
        }
        else if (source.getText().startsWith("IA")) {
            app.showSettingsDialog();
        }
    }

    private static void displayPredictionResults(final PlayerApp app, final Map<String, Double> agentPredictions, final boolean useClassifier, final boolean printHighestValue)
    {
        app.manager().getPlayerInterface().selectAnalysisTab();

        String bestPredictedAgentName = "None";
        double bestPredictedValue = -99999;
        for (final String agentName : agentPredictions.keySet())
        {
            final double score = agentPredictions.get(agentName).doubleValue();

            if (useClassifier)
                app.manager().getPlayerInterface().addTextToAnalysisPanel("Predicted probability for " + agentName + ": " + score + "\n");
            else
                app.manager().getPlayerInterface().addTextToAnalysisPanel("Predicted value for " + agentName + ": " + score + "\n");

            if (score > bestPredictedValue)
            {
                bestPredictedValue = score;
                bestPredictedAgentName = agentName;
            }
        }

        if (printHighestValue)
            app.manager().getPlayerInterface().addTextToAnalysisPanel("Best Predicted Agent/Heuristic is " + bestPredictedAgentName + "\n");

        app.manager().getPlayerInterface().addTextToAnalysisPanel("//-------------------------------------------------------------------------\n");
        System.out.println("Prediction complete.\n");
    }

    //-------------------------------------------------------------------------

    public static void checkItemStateChanges(final PlayerApp app, final ItemEvent e)
    {
        final JMenuItem source = (JMenuItem) (e.getSource());
        final Context context = app.contextSnapshot().getContext(app);

        JMenu rootParent = (JMenu)((JPopupMenu)source.getParent()).getInvoker();
        while (rootParent.getParent() instanceof JPopupMenu)
            rootParent = (JMenu)((JPopupMenu)rootParent.getParent()).getInvoker();

        if (rootParent.getText().equals("Options"))
        {
            // Check if an in-game option or ruleset has been selected
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                final Game game = context.game();
                final GameOptions gameOptions = game.description().gameOptions();

                // First, check if a predefined ruleset has been selected. Also check parent in case default ruleset variation selected.
                final JMenu sourceParent = (JMenu)((JPopupMenu)source.getParent()).getInvoker();
                final boolean rulesetSelected = GameUtil.checkMatchingRulesets(app, game, source.getText()) || GameUtil.checkMatchingRulesets(app, game, sourceParent.getText());

                // Second, check if an option has been selected
                if (!rulesetSelected && gameOptions.numCategories() > 0 && source.getParent() != null)
                {
                    final JMenu parent = (JMenu)((JPopupMenu)source.getParent()).getInvoker();

                    final List<String> currentOptions = app.manager().settingsManager().userSelections().selectedOptionStrings();

                    for (int cat = 0; cat < gameOptions.numCategories(); cat++)
                    {
                        final List<Option> options = gameOptions.categories().get(cat).options();
                        if (options.isEmpty())
                            continue; // no options in this group

                        if (!options.get(0).menuHeadings().get(0).equals(parent.getText()))
                            continue; // not this option group

                        for (final Option option : options)
                        {
                            if (option.menuHeadings().get(1).equals(source.getText()))
                            {
                                // Match!
                                final String selectedOptString = StringRoutines.join("/", option.menuHeadings());

                                // Remove any other selected options in the same category
                                for (int i = 0; i < currentOptions.size(); ++i)
                                {
                                    final String currOption = currentOptions.get(i);

                                    if
                                    (
                                            currOption.substring(0, currOption.lastIndexOf("/")).equals(
                                                    selectedOptString.substring(0, selectedOptString.lastIndexOf("/"))
                                            )
                                    )
                                    {
                                        // Found one in same category, so remove it
                                        currentOptions.remove(i);
                                        break;	// Should be no more than just this one
                                    }
                                }

                                // Now add the option we newly selected
                                currentOptions.add(selectedOptString);
                                app.manager().settingsManager().userSelections().setSelectOptionStrings(currentOptions);
                                gameOptions.setOptionsLoaded(true);

                                // Since we selected an option, we should try to auto-select ruleset
                                app.manager().settingsManager().userSelections().setRuleset
                                        (
                                                game.description().autoSelectRuleset
                                                        (
                                                                app.manager().settingsManager().userSelections().selectedOptionStrings()
                                                        )
                                        );

                                try
                                {
                                    GameSetup.compileAndShowGame(app, game.description().raw(), false);
                                }
                                catch (final Exception exception)
                                {
                                    GameUtil.resetGame(app, false);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (source.getText().equals("Show Legal Moves"))
        {
            app.bridge().settingsVC().setShowPossibleMoves(!app.bridge().settingsVC().showPossibleMoves());
        }
        else if (source.getText().equals("Show Board"))
        {
            app.settingsPlayer().setShowBoard(!app.settingsPlayer().showBoard());
        }
        else if (source.getText().equals("Show dev tooltip"))
        {
            app.settingsPlayer().setCursorTooltipDev(!app.settingsPlayer().cursorTooltipDev());
        }
        else if (source.getText().equals("Show Pieces"))
        {
            app.settingsPlayer().setShowPieces(!app.settingsPlayer().showPieces());
        }
        else if (source.getText().equals("Show Graph"))
        {
            app.settingsPlayer().setShowGraph(!app.settingsPlayer().showGraph());
        }
        else if (source.getText().equals("Show Cell Connections"))
        {
            app.settingsPlayer().setShowConnections(!app.settingsPlayer().showConnections());
        }
        else if (source.getText().equals("Show Axes"))
        {
            app.settingsPlayer().setShowAxes(!app.settingsPlayer().showAxes());
        }
        else if (source.getText().equals("Show Container Indices"))
        {
            app.bridge().settingsVC().setShowContainerIndices(!app.bridge().settingsVC().showContainerIndices());
        }
        else if (source.getText().equals("Sandbox"))
        {
            app.settingsPlayer().setSandboxMode(!app.settingsPlayer().sandboxMode());
            app.addTextToStatusPanel("Warning! Using sandbox mode may result in illegal game states.\n");
        }
        else if (source.getText().equals("Show Indices"))
        {
            app.bridge().settingsVC().setShowIndices(!app.bridge().settingsVC().showIndices());
            if (app.bridge().settingsVC().showCoordinates())
                app.bridge().settingsVC().setShowCoordinates(false);
            if (app.bridge().settingsVC().showIndices())
            {
                app.bridge().settingsVC().setShowVertexIndices(false);
                app.bridge().settingsVC().setShowEdgeIndices(false);
                app.bridge().settingsVC().setShowCellIndices(false);
            }
        }
        else if (source.getText().equals("Show Coordinates"))
        {
            app.bridge().settingsVC().setShowCoordinates(!app.bridge().settingsVC().showCoordinates());
            if (app.bridge().settingsVC().showIndices())
                app.bridge().settingsVC().setShowIndices(false);
            if (app.bridge().settingsVC().showCoordinates())
            {
                app.bridge().settingsVC().setShowVertexCoordinates(false);
                app.bridge().settingsVC().setShowEdgeCoordinates(false);
                app.bridge().settingsVC().setShowCellCoordinates(false);
            }
        }
        else if (source.getText().equals("Show Cell Indices"))
        {
            app.bridge().settingsVC().setShowCellIndices(!app.bridge().settingsVC().showCellIndices());
            if (app.bridge().settingsVC().showCellCoordinates())
                app.bridge().settingsVC().setShowCellCoordinates(false);
            if (app.bridge().settingsVC().showCellIndices())
                app.bridge().settingsVC().setShowIndices(false);
        }
        else if (source.getText().equals("Show Edge Indices"))
        {
            app.bridge().settingsVC().setShowEdgeIndices(!app.bridge().settingsVC().showEdgeIndices());
            if (app.bridge().settingsVC().showEdgeCoordinates())
                app.bridge().settingsVC().setShowEdgeCoordinates(false);
            if (app.bridge().settingsVC().showEdgeIndices())
                app.bridge().settingsVC().setShowIndices(false);
        }
        else if (source.getText().equals("Show Vertex Indices"))
        {
            app.bridge().settingsVC().setShowVertexIndices(!app.bridge().settingsVC().showVertexIndices());
            if (app.bridge().settingsVC().showVertexCoordinates())
                app.bridge().settingsVC().setShowVertexCoordinates(false);
            if (app.bridge().settingsVC().showVertexIndices())
                app.bridge().settingsVC().setShowIndices(false);
        }
        else if (source.getText().equals("Show Cell Coordinates"))
        {
            app.bridge().settingsVC().setShowCellCoordinates(!app.bridge().settingsVC().showCellCoordinates());
            if (app.bridge().settingsVC().showCellIndices())
                app.bridge().settingsVC().setShowCellIndices(false);
            if (app.bridge().settingsVC().showCellCoordinates())
                app.bridge().settingsVC().setShowCoordinates(false);
        }
        else if (source.getText().equals("Show Edge Coordinates"))
        {
            app.bridge().settingsVC().setShowEdgeCoordinates(!app.bridge().settingsVC().showEdgeCoordinates());
            if (app.bridge().settingsVC().showEdgeIndices())
                app.bridge().settingsVC().setShowEdgeIndices(false);
            if (app.bridge().settingsVC().showEdgeCoordinates())
                app.bridge().settingsVC().setShowCoordinates(false);
        }
        else if (source.getText().equals("Show Vertex Coordinates"))
        {
            app.bridge().settingsVC().setShowVertexCoordinates(!app.bridge().settingsVC().showVertexCoordinates());
            if (app.bridge().settingsVC().showVertexIndices())
                app.bridge().settingsVC().setShowVertexIndices(false);
            if (app.bridge().settingsVC().showVertexCoordinates())
                app.bridge().settingsVC().setShowCoordinates(false);
        }
        else if (source.getText().equals("Show Magnifying Glass"))
        {
            app.settingsPlayer().setShowZoomBox(!app.settingsPlayer().showZoomBox());
        }
        else if (source.getText().equals("Show AI Distribution"))
        {
            app.settingsPlayer().setShowAIDistribution(!app.settingsPlayer().showAIDistribution());
        }
        else if (source.getText().equals("Show Last Move"))
        {
            app.settingsPlayer().setShowLastMove(!app.settingsPlayer().showLastMove());
        }
        else if (source.getText().equals("Show Repetitions"))
        {
            app.manager().settingsManager().setShowRepetitions(!app.manager().settingsManager().showRepetitions());
            if (app.manager().settingsManager().showRepetitions())
                app.addTextToStatusPanel("Please restart the game to display repetitions correctly.\n");
        }
        else if (source.getText().equals("Show Ending Moves"))
        {
            app.settingsPlayer().setShowEndingMove(!app.settingsPlayer().showEndingMove());
        }
        else if (source.getText().contains("Show Track"))
        {
            for (int i = 0; i < app.bridge().settingsVC().trackNames().size(); i++)
                if (source.getText().equals(app.bridge().settingsVC().trackNames().get(i)))
                    app.bridge().settingsVC().trackShown().set(i, Boolean.valueOf(!app.bridge().settingsVC().trackShown().get(i).booleanValue()));
        }
        else if (source.getText().equals("Swap Rule"))
        {
            app.settingsPlayer().setSwapRule(!app.settingsPlayer().swapRule());
            context.game().metaRules().setUsesSwapRule(app.settingsPlayer().swapRule());
            GameUtil.resetGame(app, false);
        }
        else if (source.getText().equals("No Repetition Of Game State"))
        {
            app.settingsPlayer().setNoRepetition(!app.settingsPlayer().noRepetition());
            if (app.settingsPlayer().noRepetition())
                context.game().metaRules().setRepetitionType(RepetitionType.Positional);
            GameUtil.resetGame(app, false);
        }
        else if (source.getText().equals("No Repetition Within A Turn"))
        {
            app.settingsPlayer().setNoRepetitionWithinTurn(!app.settingsPlayer().noRepetitionWithinTurn());
            if (app.settingsPlayer().noRepetition())
                context.game().metaRules().setRepetitionType(RepetitionType.PositionalInTurn);
            GameUtil.resetGame(app, false);
        }
        else if (source.getText().equals("Save Heuristics"))
        {
            app.settingsPlayer().setSaveHeuristics(!app.settingsPlayer().saveHeuristics());
        }
        else if (source.getText().equals("Print Move Features"))
        {
            app.settingsPlayer().setPrintMoveFeatures(!app.settingsPlayer().printMoveFeatures());
        }
        else if (source.getText().equals("Print Move Feature Instances"))
        {
            app.settingsPlayer().setPrintMoveFeatureInstances(!app.settingsPlayer().printMoveFeatureInstances());
        }
        else if (source.getText().equals("Automatic"))
        {
            app.settingsPlayer().setPuzzleDialogOption(PuzzleSelectionType.Automatic);
        }
        else if (source.getText().equals("Dialog"))
        {
            app.settingsPlayer().setPuzzleDialogOption(PuzzleSelectionType.Dialog);
        }
        else if (source.getText().equals("Cycle"))
        {
            app.settingsPlayer().setPuzzleDialogOption(PuzzleSelectionType.Cycle);
        }
        else if (source.getText().equals("Illegal Moves Allowed"))
        {
            app.settingsPlayer().setIllegalMovesValid(!app.settingsPlayer().illegalMovesValid());
        }
        else if (source.getText().equals("Show Possible Values"))
        {
            app.bridge().settingsVC().setShowCandidateValues(!app.bridge().settingsVC().showCandidateValues());
        }
        else
        {
            System.out.println("NO MATCHING MENU OPTION FOUND");
        }

        EventQueue.invokeLater(() ->
        {
            app.resetMenuGUI();
            app.repaint();
        });
    }

    //---------------------------------------------------------------------

    // Returns the parent menu title of a JMenuItem a certain number of steps back (depth).
    private static String getParentTitle(final JMenuItem source, final int depth)
    {
        if (depth <= 0)
            return source.getText();

        JMenuItem currentContainer = source;
        if(currentContainer.getParent() != null)
        {
            JMenu nextContainer = ((JMenu)((JPopupMenu) currentContainer.getParent()).getInvoker());
            for (int i = 0; i < depth; i++)
                nextContainer = ((JMenu)((JPopupMenu) currentContainer.getParent()).getInvoker());

            return ((JMenu)nextContainer).getText();
        }
        return currentContainer.getText();

    }

    public static StartVisualEditor getStartVisualEditor()
    {
        return startVisualEditor;
    }

    public static void setStartVisualEditor(final StartVisualEditor startVisualEditor)
    {
        MainMenuFunctions.startVisualEditor = startVisualEditor;
    }

    //---------------------------------------------------------------------

}
