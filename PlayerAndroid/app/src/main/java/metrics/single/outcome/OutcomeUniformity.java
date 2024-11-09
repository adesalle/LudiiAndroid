package metrics.single.outcome;

import org.apache.commons.rng.RandomProviderState;

import game.Game;
import main.math.statistics.Stats;
import metrics.Evaluation;
import metrics.Metric;
import metrics.Utils;
import other.RankUtils;
import other.concept.Concept;
import other.context.Context;
import other.trial.Trial;

/**
 * Inverse of the per-player variance in outcomes over all trials, 
 * averaged over all players.
 * 
 * @author Dennis Soemers
 */
public class OutcomeUniformity extends Metric
{
	
	//-------------------------------------------------------------------------
	
	/** For incremnetal computation */
	protected Stats[] playerStats = null;
	
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public OutcomeUniformity()
	{
		super
		(
			"OutcomeUniformity", 
			"Inverse of the per-player variance in outcomes over all trials, averaged over all players.", 
			0.0, 
			1.0,
			Concept.OutcomeUniformity
		);
	}
	
	//-------------------------------------------------------------------------
	
	@Override
	public Double apply
	(
		final Game game,
		final Evaluation evaluation,
		final Trial[] trials,
		final RandomProviderState[] randomProviderStates
	)
	{
		final int numPlayers = game.players().count();
		
		if (numPlayers < 1)
			return null;
		
		final Stats[] playerStats = new Stats[numPlayers + 1];
		for (int p = 1; p <= numPlayers; ++p)
		{
			playerStats[p] = new Stats();
		}
		
		for (int i = 0; i < trials.length; ++i)
		{
			final Trial trial = trials[i];
			final RandomProviderState rng = randomProviderStates[i];
			final Context context = Utils.setupTrialContext(game, rng, trial);
			
			final double[] utils = RankUtils.agentUtilities(context);
			
			for (int p = 1; p <= numPlayers; ++p)
			{
				playerStats[p].addSample(utils[p]);
			}
		}
		
		double accum = 0.0;
		for (int p = 1; p <= numPlayers; ++p)
		{
			playerStats[p].measure();
			accum += playerStats[p].varn();
		}
		
		return Double.valueOf(1.0 - (accum / numPlayers));
	}
	
	//-------------------------------------------------------------------------
	
	@Override
	public void startNewTrial(final Context context, final Trial fullTrial)
	{
		// Do nothing
	}
	
	@Override
	public void observeNextState(final Context context)
	{
		// Do nothing
	}
	
	@Override
	public void observeFinalState(final Context context)
	{
		if (playerStats == null)
		{
			final int numPlayers = context.game().players().count();
			playerStats = new Stats[numPlayers + 1];
			for (int p = 1; p <= numPlayers; ++p)
			{
				playerStats[p] = new Stats();
			}
		}
		
		final double[] utils = RankUtils.agentUtilities(context);
		
		for (int p = 1; p < playerStats.length; ++p)
		{
			playerStats[p].addSample(utils[p]);
		}
	}
	
	@Override
	public double finaliseMetric(final Game game, final int numTrials)
	{
		final int numPlayers = game.players().count();
		double accum = 0.0;
		for (int p = 1; p <= numPlayers; ++p)
		{
			playerStats[p].measure();
			accum += playerStats[p].varn();
		}
		
		return 1.0 - (accum / numPlayers);
	}
	
	//-------------------------------------------------------------------------

}
