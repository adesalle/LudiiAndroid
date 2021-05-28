package other.state;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import annotations.Hide;
import game.Game;
import game.Game.StateConstructorLock;
import game.equipment.container.Container;
import game.equipment.container.other.Dice;
import game.rules.phase.Phase;
import game.types.board.SiteType;
import game.types.play.ModeType;
import game.types.play.RoleType;
import game.types.state.GameType;
import game.util.equipment.Region;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.hash.TIntHashSet;
import main.Constants;
import main.collections.FastTIntArrayList;
import other.state.container.ContainerState;
import other.state.container.ContainerStateFactory;
import other.state.owned.Owned;
import other.state.owned.OwnedFactory;
import other.state.symmetry.SymmetryValidator;
import other.state.track.OnTrackIndices;
import other.state.zhash.HashedBitSet;
import other.state.zhash.ZobristHashGenerator;
import other.state.zhash.ZobristHashUtilities;

/**
 * Game state.
 *
 * @author cambolbro and Eric.Piette
 */
@Hide
public class State implements Serializable
{
	private static final long serialVersionUID = 1L;

	//-------------------------------------------------------------------------

	private static final int TURN_MAX_HASH = 1024;
	private static final int SCORE_MAX_HASH = 1024;
	private static final int AMOUNT_MAX_HASH = 1024;

	//-------------------------------------------------------------------------

	/** Number of players in the game. */
	private int numPlayers = 0;
	
	/** Current player to move. */
	private int mover = 0; 
	
	/** Next player to move (if no special rules override this). */
	private int next = 0;
	
	/** Previous player to move. */
	private int prev = 0;
	
	/** For every player, a bit indicating whether some condition is triggered (e.g. checkmate). */
	private int triggered = 0;
	
	/** For every player, a bit indicating whether they are stalemated. */
	private int stalemated = 0;

	/** Individual container states. */
	private final ContainerState[] containerStates;

	/** Variable for using a counter associated to the state (possibly reSet at 0 by a consequence of an action) */
	private int counter = Constants.UNDEFINED;
	
	/** Variable to store a value between two states. */
	private int tempValue = Constants.UNDEFINED;

	/**
	 * The pending values of a state. These values are cleared when another state is
	 * reached.
	 */
	private TIntHashSet pendingValues = null;

	/** Amount belonging to each player, e.g. money. */
	private int[] amount = null;

	/** The money pot. */
	private int moneyPot = 0;

	/** Current Phase for each player. */
	private int[] currentPhase;
	
	/** The sum of each Dice container */
	private int[] sumDice;

	/** The current dice values of each set of dice. */
	private int[][] currentDice;
	
	// The variable stored between different state.
	private TObjectIntMap<String> valueMap = null;

	/**
	 * To know when the dice are rolled if they are all equals. Note: Even if one
	 * die is used we know when the dice were rolled they were equal.
	 */
	private boolean diceAllEqual = false;

	/** The number of turns played successively by the same player. */
	private int numTurnSamePlayer = 0;
	
	/** The number of times the mover has been switched to a different player. */
	private int numTurn = 1;

	/** The trump suit of the game (for cards games). */
	private int trumpSuit = Constants.OFF;

	/** The propositions (represented as ints). */
	private final TIntArrayList propositions;

	/** The votes (represented as ints). */
	private final TIntArrayList votes;

	/** The values for each player. */
	private final int[] valuesPlayer;

	/** The decision after voting. */
	private int isDecided = Constants.UNDEFINED;

	/** The values stored in some states to use them in future states. */
	private FastTIntArrayList rememberingValues;
	
	/** The values stored in some states to use them in future states and associated to a name. */
	private Map<String, FastTIntArrayList> mapRememberingValues = null;

	/** The notes sent to each player during the game. */
	private TIntObjectMap<TIntObjectMap<String>> notes = null;

	/**
	 * Access to list of sites for each kind of component owned per player. First
	 * indexed by player index, then by component index.
	 *
	 * Will contain null entries for cases where a component cannot be owned by a
	 * certain player.
	 */
	protected transient Owned owned;

	/** To access where are each type of piece on each track. */
	private transient OnTrackIndices onTrackIndices;

	/**
	 * BitSet used to store all the site already visited (from & to) by each move
	 * done by the player in a sequence of turns played by the same player.
	 */
	private HashedBitSet visited = null;

	/** In case of a sequence of capture to remove (e.g. some draughts games). */
	private HashedBitSet pieceToRemove = null;

	/** Team that each player belongs to, if any. */
	private int[] teams = null;

	/** 
	 * Indexed by original player/agent index at game start. 
	 * Gives us the matching player index at current game state (may be different after swaps)
	 */
	private int[] playerOrder;

	/** All the remaining dominoes. */
	private FastTIntArrayList remainingDominoes;
	
	/** The state stored temporary by the game. */	
	private long storedState = 0L;
	
	/** Number of consecutive pass moves */
	private int numConsecutivePasses = 0;
	
	/** Assumed cap on number of consecutive pass moves (for hash code purposes) */
	private final int numConsecutivePassesHashCap;

	/*
	 * -------------------------------------------------------------------------
	 * Zobrist hashing - attempts to reduce the board state to a 64 bit number
	 * 
	 * These values are incrementally updated every time the state changes 
	 * For repetition and ko, you'll want the current board state, but this is not the whole story 
	 * The generated move tree from a given node is affected by other factors; player to move, 
	 * your countdown variables, etc.  These should be included when searching with transposition 
	 * tables if you want an accurate description of the node score
	 * -------------------------------------------------------------------------
	 */
	private long stateHash; 	// Includes container states and scores
	private long moverHash;
	private long nextHash;
	private long prevHash;
	private long activeHash;
	private long checkmatedHash;
	private long stalematedHash;
	private long pendingHash;
	
	private long scoreHash;		// Hash value for scores
	private long amountHash;	// Hash value for amounts
	
	private final long[] moverHashes;
	private final long[] nextHashes;
	private final long[] prevHashes;
	private final long[] activeHashes;
	private final long[] checkmatedHashes;
	private final long[] stalematedHashes;
	private final long[][] lowScoreHashes;
	private final long[][] highScoreHashes;
	private final long[][] lowAmountHashes;
	private final long[][] highAmountHashes;
	private final long[][] phaseHashes;
	private final long[] isPendingHashes;
	private final long[] tempHashes;
	private final long[][] playerOrderHashes;
	private final long[][] consecutiveTurnHashes;
	private final long[][] playerSwitchHashes;
	private final long[][] teamHashes;
	private final long[][] numConsecutivePassesHashes;

	/** @param delta incremental hash to be xored with value */ 
	public void updateStateHash(final long delta) 
	{
		stateHash ^= delta; 
	}
	
	/**
	 * Performance warning: this is slow, do not use during search!
	 * Returns the lowest hash from the containers after all symmetries have been applied 
	 * @param validator allows selection of a subset of available symmetries
	 * @param whoOnly only hash the 'who' values - this is required for games with undifferentiated pieces
	 * @return state hash value 
	 */
	public long canonicalHash(final SymmetryValidator validator, final boolean whoOnly) 
	{
		final ContainerState boardState = containerStates[0];
		
		final long canonicalBoardHash = boardState.canonicalHash(validator, this, whoOnly);
		
		return canonicalBoardHash == 0 ? stateHash : canonicalBoardHash;
	}

//	/**
//	 * @deprecated use canonicalhash(validator,whoOnly) instead
//	 * @param validator
//	 * @return state hash value for the whole state
//	 */
//	@Deprecated
//	public long canonicalHash(final SymmetryValidator validator) 
//	{
//		return canonicalHash(validator, false);
//	}
	
	/** @return state hash value */ 
	public long stateHash() { return stateHash; }
	
	/** @return pending hash value */
	protected long pendingHash() { return pendingHash; }

	/** @return hash value for number of times turn has remained the same */ 
	protected long consecutiveTurnHash() 
	{ 
		return numTurnSamePlayer < Constants.MAX_CONSECUTIVES_TURNS ? 
				consecutiveTurnHashes[0][numTurnSamePlayer] : 
				consecutiveTurnHashes[1][numTurnSamePlayer % Constants.MAX_CONSECUTIVES_TURNS]; 
	}

	/** @return hash value for number of times turn has changed */			// TODO why is this not in full hash?
	protected long playerNumSwitchesHash() 
	{ 
		return numTurn < TURN_MAX_HASH ? playerSwitchHashes[0][numTurn] : playerSwitchHashes[1][numTurn % TURN_MAX_HASH]; 
	}
	
	/**
	 * @return Hash value for number of consecutive passes
	 */
	protected long numConsecutivePassesHash()
	{
		return numConsecutivePasses < numConsecutivePassesHashCap ?
				numConsecutivePassesHashes[0][numConsecutivePasses] :
				numConsecutivePassesHashes[1][numConsecutivePasses % numConsecutivePassesHashCap];
	}

	/** @return full hash value containing all fields */ 
	public long fullHash() 
	{
		return moverHash ^ 
				nextHash ^
				prevHash ^
				activeHash ^
				checkmatedHash ^
				stalematedHash ^
				pendingHash() ^ 
				stateHash() ^ 
				consecutiveTurnHash() ^ 
				scoreHash ^
				amountHash ^
				numConsecutivePassesHash()
				;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Default constructor.
	 * 
	 * This constructor is really expensive and should only ever be called
	 * (ideally once) by the Game. Any other callers should instead copy
	 * the Game's reference state!
	 *
	 * @param game
	 * @param stateConstructorLock Dummy object
	 */
	public State(final Game game, final StateConstructorLock stateConstructorLock)
	{
		Objects.requireNonNull
		(
			stateConstructorLock, 
			"Only Game.java should call this constructor! Other callers can copy the game's stateReference instead using the copy constructor."
		);
		
		numPlayers = game.players().count();
		
		//-------------- Hash initialisation ----------------
		final ZobristHashGenerator generator = ZobristHashUtilities.getHashGenerator();
		
		lowScoreHashes = ((game.gameFlags() & GameType.HashScores) != 0L) 
							? ZobristHashUtilities.getSequence(generator, numPlayers + 1, SCORE_MAX_HASH + 1) 
							: null;
							
		highScoreHashes = ((game.gameFlags() & GameType.HashScores) != 0L) 
							? ZobristHashUtilities.getSequence(generator, numPlayers + 1, SCORE_MAX_HASH + 1) 
							: null;
		
		lowAmountHashes = ((game.gameFlags() & GameType.HashAmounts) != 0L) 
							? ZobristHashUtilities.getSequence(generator, numPlayers + 1, SCORE_MAX_HASH + 1) 
							: null;
							
		highAmountHashes = ((game.gameFlags() & GameType.HashAmounts) != 0L) 
							? ZobristHashUtilities.getSequence(generator, numPlayers + 1, SCORE_MAX_HASH + 1) 
							: null;
		
		phaseHashes = ((game.gameFlags() & GameType.HashPhases) != 0L) 
						? ZobristHashUtilities.getSequence(generator, numPlayers + 1, Constants.MAX_PHASES + 1) 
						: null;
	
		moverHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		nextHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		prevHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		activeHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		checkmatedHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		stalematedHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1);
		
		tempHashes = ZobristHashUtilities.getSequence(generator, game.equipment().totalDefaultSites() * Math.max(1,game.maxCount()) + Constants.CONSTANT_RANGE + 1); // could be negative
		playerOrderHashes = ZobristHashUtilities.getSequence(generator, numPlayers + 1, numPlayers + 1);
		consecutiveTurnHashes = ZobristHashUtilities.getSequence(generator, 2, Constants.MAX_CONSECUTIVES_TURNS);
		playerSwitchHashes = ZobristHashUtilities.getSequence(generator, 2, TURN_MAX_HASH);
		
		teamHashes = (game.requiresTeams()) 
						? ZobristHashUtilities.getSequence(generator, game.players().count() + 1, Constants.MAX_PLAYER_TEAM) 
						: null;
			
		numConsecutivePassesHashCap = 2 * game.players().count() + 1;
		numConsecutivePassesHashes = ZobristHashUtilities.getSequence(generator, 2, numConsecutivePassesHashCap);

		playerOrder = new int[game.players().count() + 1];
		for (int i = 1; i < playerOrder.length; i++)
		{
			playerOrder[i] = i;
			updateStateHash(playerOrderHashes[i][playerOrder[i]]);
		}

		//-------------- on with the plot ----------------
		
		assert (!game.hasSubgames());

		moneyPot = 0;
		isPendingHashes = ZobristHashUtilities.getSequence(generator, game.equipment().totalDefaultSites() + 2);
		stateHash = ZobristHashUtilities.INITIAL_VALUE;
		scoreHash = ZobristHashUtilities.INITIAL_VALUE;
		amountHash = ZobristHashUtilities.INITIAL_VALUE;

		containerStates = new ContainerState[game.equipment().containers().length];
		
		if (game.usesPendingValues())
			pendingValues = new TIntHashSet();

		if (game.requiresBet())
			amount = new int[numPlayers + 1];

		int id = 0;
		for (final Container container : game.equipment().containers())
			containerStates[id++] = ContainerStateFactory.createStateForContainer(generator, game, container);

		initPhase(game);

		if (game.hasHandDice())
		{
			sumDice = new int[game.handDice().size()];
			currentDice = new int[game.handDice().size()][];

			for (int i = 0; i < game.handDice().size(); i++)
			{
				final Dice d = game.handDice().get(i);
				currentDice[i] = new int[d.numLocs()];
			}
		}

		owned = OwnedFactory.createOwned(game);

		if (game.requiresVisited())
		{
			visited = new HashedBitSet(generator, game.board().numSites());
		}

		if (game.hasSequenceCapture())
		{
			pieceToRemove = new HashedBitSet(generator, game.board().numSites());
		}

		if (game.requiresTeams())
		{
			teams = new int[game.players().size()];
		}

		if (game.usesVote())
		{
			propositions = new TIntArrayList();
			votes = new TIntArrayList();
		}
		else
		{
			propositions = null;
			votes = null;
		}
		
		valuesPlayer = new int[game.players().size() + 1];
		Arrays.fill(valuesPlayer, Constants.UNDEFINED);

		if (game.usesNote())
			notes = new TIntObjectHashMap<TIntObjectMap<String>>();

		if (game.hasTrack() && game.hasInternalLoopInTrack())
			onTrackIndices = new OnTrackIndices(game.board().tracks(), game.equipment().components().length);

		if (game.hasDominoes())
			remainingDominoes = new FastTIntArrayList();

		rememberingValues = new FastTIntArrayList();
		
		if (game.usesRememberingValues())
			mapRememberingValues = new HashMap<String, FastTIntArrayList>();
		
		if (game.usesValueMap())
			valueMap = new TObjectIntHashMap<String>();
	}

	/**
	 * Copy constructor.
	 *
	 * @param other
	 */
	public State(final State other)
	{
		// NOTE: these can be copied by reference, because immutable once initialised
		lowScoreHashes = other.lowScoreHashes;
		highScoreHashes = other.highScoreHashes;
		lowAmountHashes = other.lowAmountHashes;
		highAmountHashes = other.highAmountHashes;
		phaseHashes = other.phaseHashes;

		isPendingHashes = other.isPendingHashes;
		moverHashes = other.moverHashes;
		nextHashes = other.nextHashes;
		prevHashes = other.prevHashes;
		activeHashes = other.activeHashes;
		checkmatedHashes = other.checkmatedHashes;
		stalematedHashes = other.stalematedHashes;
		tempHashes = other.tempHashes;
		playerOrderHashes = other.playerOrderHashes;
		consecutiveTurnHashes = other.consecutiveTurnHashes;
		playerSwitchHashes = other.playerSwitchHashes;
		teamHashes = other.teamHashes;
		numConsecutivePassesHashCap = other.numConsecutivePassesHashCap;
		numConsecutivePassesHashes = other.numConsecutivePassesHashes;
		playerOrder = Arrays.copyOf(other.playerOrder, other.playerOrder.length);
		moneyPot = other.moneyPot;

		// Back to the plot
		numPlayers = other.numPlayers;
		
		stateHash = other.stateHash;
		moverHash = other.moverHash;
		nextHash = other.nextHash;
		prevHash = other.prevHash;
		activeHash = other.activeHash;
		checkmatedHash = other.checkmatedHash;
		stalematedHash = other.stalematedHash;
		pendingHash = other.pendingHash;
		scoreHash = other.scoreHash;
		amountHash = other.amountHash;
		
		trumpSuit = other.trumpSuit;
		
		mover = other.mover;
		next = other.next;
		prev = other.prev;
		triggered = other.triggered;
		stalemated = other.stalemated;

		if (other.containerStates == null)
		{
			containerStates = null;
		}
		else
		{
			containerStates = new ContainerState[other.containerStates.length];
			for (int is = 0; is < containerStates.length; is++)
				if (other.containerStates[is] == null)
					containerStates[is] = null;
				else
					containerStates[is] = other.containerStates[is].deepClone();
		}

		setCounter(other.counter);
		setTemp(other.tempValue);
		
		if (other.pendingValues != null)
			pendingValues = new TIntHashSet(other.pendingValues);

		if (other.amount != null)
			amount = Arrays.copyOf(other.amount, other.amount.length);
		
		if (other.currentPhase != null)
			currentPhase = Arrays.copyOf(other.currentPhase, other.currentPhase.length);
		
		if (other.sumDice != null)
			sumDice = Arrays.copyOf(other.sumDice, other.sumDice.length);
		
		if (other.currentDice != null)
		{
			currentDice = new int[other.currentDice.length][];
			for (int i = 0; i < currentDice.length; ++i)
			{
				currentDice[i] = Arrays.copyOf(other.currentDice[i], other.currentDice[i].length);
			}
		}
		
		if (other.visited != null)
		{
			visited = other.visited.clone();
		}

		if (other.pieceToRemove != null)
		{
			pieceToRemove = other.pieceToRemove.clone();
		}

		if (other.teams != null)
		{
			teams = Arrays.copyOf(other.teams, other.teams.length);
		}
		
		if (other.votes != null)
		{
			votes = new TIntArrayList(other.votes);
			propositions = new TIntArrayList(other.propositions);
			isDecided = other.isDecided;
		}
		else
		{
			votes = null;
			propositions = null;
			isDecided = other.isDecided;
		}

		valuesPlayer = new int[other.valuesPlayer.length];
		System.arraycopy(other.valuesPlayer, 0, valuesPlayer, 0, other.valuesPlayer.length);

		if (other.notes != null)
			notes = new TIntObjectHashMap<TIntObjectMap<String>>(other.notes);

		numTurnSamePlayer = other.numTurnSamePlayer;
		numTurn = other.numTurn;

		if (other.owned == null)
			owned = null;
		else
			owned = other.owned.copy();
		
		diceAllEqual = other.diceAllEqual;
		onTrackIndices = copyOnTrackIndices(other.onTrackIndices);

		if (other.remainingDominoes != null)
			remainingDominoes = new FastTIntArrayList(other.remainingDominoes);

		if (other.rememberingValues != null)
			rememberingValues = new FastTIntArrayList(other.rememberingValues);

		if (other.mapRememberingValues != null)
		{
			mapRememberingValues = new HashMap<String, FastTIntArrayList>();
			for (final Entry<String, FastTIntArrayList> entry : other.mapRememberingValues.entrySet())
			{
				final String key = entry.getKey();
				final FastTIntArrayList rememberingList = entry.getValue();
				final FastTIntArrayList copyRememberingList = new FastTIntArrayList(rememberingList);
				mapRememberingValues.put(key, copyRememberingList);
			}
		}

		storedState = other.storedState;
		numConsecutivePasses = other.numConsecutivePasses;
		
		if (other.valueMap != null)
			valueMap = new TObjectIntHashMap<String>(other.valueMap);
	}

	//-------------------------------------------------------------------------

	/**
	 * @return List of container states
	 */
	public ContainerState[] containerStates()
	{
		return containerStates;
	}

	//-------------------------------------------------------------------------
	
	/**
	 * @return Number of players.
	 */
	public int numPlayers()
	{
		return numPlayers;
	}

	/**
	 * @return Current mover.
	 */
	public int mover()
	{
		return mover;
	}

	/**
	 * @param who
	 */
	public void setMover(final int who)
	{
		moverHash ^= moverHashes[mover];
		mover = who;
		moverHash ^= moverHashes[mover];
	}

	/**
	 * @return Next active mover.
	 */
	public int next()
	{
		return next;
	}

	/**
	 * To set the next player.
	 *
	 * @param who
	 */
	public void setNext(final int who)
	{
		nextHash ^= nextHashes[next];
		next = who;
		nextHash ^= nextHashes[next];
	}

	/**
	 * @return Previous mover.
	 */
	public int prev()
	{
		return prev;
	}

	/**
	 * To set the previous player.
	 *
	 * @param who
	 */
	public void setPrev(final int who)
	{
		prevHash ^= prevHashes[prev];
		prev = who;
		prevHash ^= prevHashes[prev];
	}

	/**
	 * Sets a player to be active or inactive.
	 *
	 * @param who Which player to set value for
	 * @param newActive New active value (true or false for active or inactive)
	 * @param active The int with bits set for currently active players
	 * 
	 * @return Returns the modified int with bits set for active players
	 */
	public int setActive
	(
		final int who,
		final boolean newActive,
		final int active
	)
	{
		int ret = active;
		final int whoBit = (1 << (who - 1));
		final boolean wasActive = (active & whoBit) != 0;
		
		if (wasActive && !newActive)
		{
			activeHash ^= activeHashes[who];
			ret &= ~whoBit;
		}
		else if (!wasActive && newActive)
		{
			activeHash ^= activeHashes[who];
			ret |= whoBit;
		}
		
		return ret;
	}

	/**
	 * Updates the zobrist hash if all players have been set to inactive in one go.
	 * 
	 * WARNING: This should only ever be called from Trial
	 */
	public void updateHashAllPlayersInactive()
	{
		activeHash = 0;
	}

	/**
	 * @param event The event triggered.
	 * @param who   The player related to the event.
	 * @return Whether player is triggered.
	 */
	public boolean isTriggered(final String event, final int who)
	{
		return (triggered & (1 << (who - 1))) != 0;
	}

	/**
	 * @param who
	 * @param triggerValue
	 */
	public void triggers
	(
		final int who,
		final boolean triggerValue
	)
	{
		final int whoBit = (1 << (who - 1));
		final boolean wasCheckmated = (triggered & whoBit) != 0;
		
		if (wasCheckmated && !triggerValue)
		{
			checkmatedHash ^= checkmatedHashes[who];
			triggered &= ~whoBit;
		}
		else if (!wasCheckmated && triggerValue)
		{
			checkmatedHash ^= checkmatedHashes[who];
			triggered |= whoBit;
		}
	}

	/**
	 * Clear all checkmates.
	 */
	public void clearTriggers()
	{
		checkmatedHash = 0;
		triggered = 0;
	}

	/**
	 * @param who
	 * @return Whether player is in stalemate.
	 */
	public boolean isStalemated(final int who)
	{
		return (stalemated & (1 << (who - 1))) != 0;
	}

	/**
	 * TODO - branching - splitting to setStalemated(who) and clearStalemated(who) would probably be clearer and more efficient
	 * To set a player in stalemate (or not).
	 *
	 * @param who
	 * @param newStalemated
	 */
	public void setStalemated
	(
		final int who,
		final boolean newStalemated
	)
	{
		final int whoBit = (1 << (who - 1));
		final boolean wasStalemated = (stalemated & whoBit) != 0;
		
		if (wasStalemated && !newStalemated)
		{
			stalematedHash ^= stalematedHashes[who];
			stalemated &= ~whoBit;
		}
		else if (!wasStalemated && newStalemated)
		{
			stalematedHash ^= stalematedHashes[who];
			stalemated |= whoBit;
		}
	}

	/**
	 * Clear all stalemates.
	 */
	public void clearStalemates()
	{
		stalematedHash = 0;
		stalemated = 0;
	}
	
	/**
	 * Helper method to convert a given "player index" (i.e. colour) into
	 * an "agent index" -- index of the "agent" (might be AI or human) who
	 * is currently playing this colour.
	 * 
	 * Normally, this will just return playerIdx again.
	 * 
	 * If players 1 and 2 swapped (e.g. in Hex), it will return an agent index
	 * of 1 for player index 2, and agent index of 2 for player index 1. This
	 * is because, after swapping, the agent who originally played as Player 1
	 * got swapped into playing as Player 2, and the agent who originally
	 * played as Player 2 got swapped into playing as Player 1.
	 * 
	 * @param playerIdx
	 * @return Index of agent.
	 */
	public int playerToAgent(final int playerIdx)
	{
		for (int p = 0; p < playerOrder.length; ++p)
		{
			if (playerOrder[p] == playerIdx)
				return p;
		}
		
		// For players >= numPlayers, we just return the given playerIdx
		if (playerIdx >= playerOrder.length)
			return playerIdx;
		
		return Constants.UNDEFINED;
	}

	//-------------------------------------------------------------------------

	/**
	 * To reset the state with another.
	 *
	 * @param other
	 * @param game
	 */
	public void resetStateTo(final State other, final Game game)
	{
		assert (!game.hasSubgames());
		
		mover = other.mover;
		next = other.next;
		prev = other.prev;
		moneyPot = other.moneyPot;
		triggered = other.triggered;
		stalemated = other.stalemated;

		if (containerStates.length != other.containerStates.length)
			throw new UnsupportedOperationException("Number of state items should be invariant.");

		for (int is = 0; is < containerStates.length; is++)
			containerStates[is] = (other.containerStates[is] == null) ? null
					: other.containerStates[is].deepClone();

		setCounter(other.counter);
		setTemp(other.tempValue);
		
		if (other.pendingValues != null)
			pendingValues = new TIntHashSet(other.pendingValues);

		if (amount != null)
		{
			for (int index = 0; index < other.amount.length; index++)
				amount[index] = other.amount[index];
		}

		moverHash = other.moverHash;
		nextHash = other.nextHash;
		prevHash = other.prevHash;
		activeHash = other.activeHash;
		checkmatedHash = other.checkmatedHash;
		stalematedHash = other.stalematedHash;
		pendingHash = other.pendingHash;
		stateHash = other.stateHash;
		scoreHash = other.scoreHash;
		playerOrder = Arrays.copyOf(other.playerOrder, other.playerOrder.length);

		if (other.currentPhase != null)
			currentPhase = Arrays.copyOf(other.currentPhase, other.currentPhase.length);

		if (sumDice != null)
			sumDice = Arrays.copyOf(other.sumDice, other.sumDice.length);

		if (currentDice != null)
		{
			currentDice = new int[other.currentDice.length][];
			for (int i = 0; i < currentDice.length; ++i)
			{
				currentDice[i] = Arrays.copyOf(other.currentDice[i], other.currentDice[i].length);
			}
		}

		owned = other.owned.copy();

		if (visited != null)
			visited.clear(this);

		if (pieceToRemove != null)
			pieceToRemove.clear(this);

		if (votes != null)
			votes.clear();

		if (propositions != null)
			propositions.clear();

		isDecided = Constants.UNDEFINED;

		if (other.teams != null)
			teams = Arrays.copyOf(other.teams, other.teams.length);

		numTurnSamePlayer = other.numTurnSamePlayer;
		numTurn = other.numTurn;
		trumpSuit = other.trumpSuit;

		System.arraycopy(other.valuesPlayer, 0, valuesPlayer, 0, other.valuesPlayer.length);

		if (notes != null)
			notes = new TIntObjectHashMap<TIntObjectMap<String>>(other.notes);

		if (game.isBoardless())
			containerStates[0].setPlayable(this, game.board().topology().centre(SiteType.Cell).get(0).index(),
					true);

		diceAllEqual = other.diceAllEqual;
		onTrackIndices = copyOnTrackIndices(other.onTrackIndices);

		if (remainingDominoes != null)
			remainingDominoes.clear();

		if (rememberingValues != null)
			rememberingValues.clear();

		if (mapRememberingValues != null)
			mapRememberingValues = new HashMap<String, FastTIntArrayList>(other.mapRememberingValues);

		storedState = other.storedState;
		numConsecutivePasses = other.numConsecutivePasses;
		
		if (other.valueMap != null)
			valueMap = new TObjectIntHashMap<String>(other.valueMap);
	}

	//-------------------------------------------------------------------------
	
	/**
	 * Method for copying another OnTrackIndices structure.
	 * 
	 * NOTE: we override this for optimisations in CopyOnWriteState.
	 * 
	 * @param otherOnTrackIndices
	 */
	protected OnTrackIndices copyOnTrackIndices(final OnTrackIndices otherOnTrackIndices)
	{
		return otherOnTrackIndices == null ? null : new OnTrackIndices(otherOnTrackIndices);
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Initialise this state for use.
	 *
	 * @param game
	 */
	public void initialise(final Game game)
	{
		moverHash = ZobristHashUtilities.INITIAL_VALUE;
		nextHash = ZobristHashUtilities.INITIAL_VALUE;
		prevHash = ZobristHashUtilities.INITIAL_VALUE;
		activeHash = ZobristHashUtilities.INITIAL_VALUE;
		checkmatedHash = ZobristHashUtilities.INITIAL_VALUE;
		stalematedHash = ZobristHashUtilities.INITIAL_VALUE;
		pendingHash = ZobristHashUtilities.INITIAL_VALUE;
		stateHash = ZobristHashUtilities.INITIAL_VALUE;
		scoreHash = ZobristHashUtilities.INITIAL_VALUE;
		amountHash = ZobristHashUtilities.INITIAL_VALUE;
		
		mover = 0;
		next = 0;
		prev = 0;
		triggered = 0;
		stalemated = 0;
		moneyPot = 0;

		numPlayers = game.players().count();

		if (game.mode().mode() != ModeType.Simulation)
		{
			setMover(1);
			if (numPlayers > 1)
				setNext(2);
			else
				setNext(1);
			setPrev(0);
		}
		else
		{
			setMover(0);
			setNext(0);
			setPrev(0);
		}

		for (final ContainerState is : containerStates)
			if (is != null)
				is.reset(this, game);
		
		if (amount != null)
		{
			for (int index = 0; index < amount.length; index++)
				amount[index] = 0;
		}

		Arrays.fill(valuesPlayer, Constants.UNDEFINED);

		if (game.usesNote())
			notes = new TIntObjectHashMap<TIntObjectMap<String>>();

		initPhase(game);

		if (game.usesVote())
		{
			isDecided = Constants.UNDEFINED;
			votes.clear();
			propositions.clear();
		}

		// We init the team to each player to it self
		if (teams != null)
			for (int i = 1; i < teams.length; i++)
				teams[i] = i;

		diceAllEqual = false;
	}

	//-------------------------------------------------------------------------

	@Override
	public String toString()
	{
		String str = "";

		str += "info: num=" + numPlayers() + ", mvr=" + mover() + ", nxt=" + next() + ", prv=" + prev() + ".\n";

		str += Arrays.toString(containerStates) + "\n";
				
		return str;
	}

	//-------------------------------------------------------------------------

	/**
	 * @param player
	 * @return the amount of a player.
	 */
	public int amount(final int player)
	{
		return amount[player];
	}

	/**
	 * @return the pot.
	 */
	public int pot()
	{
		return moneyPot;
	}

	/**
	 * To modify the pot.
	 * 
	 * @param pot The pot value.
	 */
	public void setPot(final int pot)
	{
		moneyPot = pot;
	}
	
	/**
	 * To set a value for a specific player.
	 * 
	 * @param player The player
	 * @param value  The value
	 */
	public void setValueForPlayer(final int player, final int value)
	{
		valuesPlayer[player] = value;
	}

	/**
	 * @param player The player
	 * @return The value of a specific player.
	 */
	public int getValue(final int player)
	{
		return valuesPlayer[player];
	}

	/**
	 * Add a value to the map
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(final String key, final int value)
	{
		valueMap.put(key, value);
	}

	/**
	 * @param key
	 * @return value for this key, or Constants.OFF if not found
	 */
	public int getValue(final String key)
	{
		if (!valueMap.containsKey(key))
			return Constants.OFF;
		
		return valueMap.get(key);
	}

	/**
	 * To add a note to the list of note.
	 * 
	 * @param move    The index of the move.
	 * @param player  The index of the player.
	 * @param message The note to add.
	 */
	public void addNote(final int move, final int player, final String message)
	{
		if (notes == null)
		{
			System.out.println("** State.addNote(): Null notes.");
			return;
		}

		final TIntObjectMap<String> notesForMove = notes.get(move);

		if (notesForMove == null)
			notes.put(move, new TIntObjectHashMap<String>());

		notes.get(move).put(player, message);
	}

	/**
	 * @param move   The index of the move.
	 * @param player The index of the player.
	 * 
	 * @return The note of send to a player at a specific move.
	 */
	public String getNote(final int move, final int player)
	{
		final TIntObjectMap<String> notesForMove = notes.get(move);

		if (notesForMove == null)
			return null;

		return notes.get(move).get(player);
	}
	
	/**
	 * Modify the amount of a player.
	 * 
	 * @param player
	 * @param newAmount
	 */
	public void setAmount(final int player, final int newAmount)
	{
		if (player > 0 && player < amount.length)
		{
			updateAmountHash(player);
			amount[player] = newAmount;
			updateAmountHash(player);
		}
	}

	private void updateAmountHash(final int player) 
	{
		if (lowAmountHashes != null) // Otherwise, amount hashes are not used for this game even if amounts are
		{
			if (amount[player] <= AMOUNT_MAX_HASH) 
				amountHash ^= lowAmountHashes[player][amount[player]];
			else
				amountHash ^= highAmountHashes[player][amount[player] % AMOUNT_MAX_HASH];
		}
	}
	
	/**
	 * Modify the score of a player.
	 * 
	 * WARNING: this should only be called directly from Context!
	 * 
	 * @param player
	 * @param score
	 * @param scoreArray The array of scores that we wish to modify
	 */
	public void setScore(final int player, final int score, final int[] scoreArray) 
	{
		updateScoreHash(player, scoreArray);		
		scoreArray[player] = score;
		updateScoreHash(player, scoreArray);
	}

	/**
	 * Modify the payoff of a player.
	 * 
	 * WARNING: this should only be called directly from Context!
	 * 
	 * @param player
	 * @param payoff
	 * @param payoffsArray The array of payoffs that we wish to modify
	 */
	public void setPayoff(final int player, final double payoff, final double[] payoffsArray)
	{
		payoffsArray[player] = payoff;
	}

	private void updateScoreHash(final int player, final int[] scoreArray) 
	{
		if (lowScoreHashes != null) // Otherwise, score hashes are not used for this game even if scores are
		{
			if (scoreArray[player] > SCORE_MAX_HASH) 
				scoreHash ^= highScoreHashes[player][(scoreArray[player]) % SCORE_MAX_HASH];
			else if (scoreArray[player] < 0)
				scoreHash ^= highScoreHashes[player][(-scoreArray[player]) % SCORE_MAX_HASH];
			else
				scoreHash ^= lowScoreHashes[player][scoreArray[player]];
		}
	}
	
	private void updatePendingHash(final int pendingVal)
	{
		final int idx = pendingVal + 1;
		if (idx < isPendingHashes.length)
			pendingHash ^= isPendingHashes[idx];
		else
			pendingHash ^= isPendingHashes[idx % isPendingHashes.length];
	}
	
	/**
	 * Update number of consecutive passes
	 * @param lastMoveWasPass
	 */
	public void updateNumConsecutivePasses(final boolean lastMoveWasPass)
	{
		if (lastMoveWasPass)
			++numConsecutivePasses;
		else
			numConsecutivePasses = 0;
	}
	
	/**
	 * @return Counter.
	 */
	public int counter()
	{
		return counter;
	}
	
	/**
	 * To increment the counter.
	 */
	public void incrCounter()
	{
		counter++;
	}
	
	/**
	 * Sets counter.
	 * @param counter
	 */
	public void setCounter(final int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * @return tempValue.
	 */
	public int temp()
	{
		return tempValue;
	}

	/**
	 * Sets the temp value.
	 * @param tempValue
	 */
	public void setTemp(final int tempValue)
	{
		updateStateHash(tempHashes[this.tempValue + Constants.CONSTANT_RANGE + 1]); // Allows tempValue to be Off or End
		this.tempValue = tempValue;
		updateStateHash(tempHashes[this.tempValue + Constants.CONSTANT_RANGE + 1]);
	}

	/**
	 * @return The pending values.
	 */
	public TIntHashSet pendingValues()
	{
		return pendingValues;
	}
	
	/**
	 * @return The propositions.
	 */
	public TIntArrayList propositions()
	{
		return propositions;
	}

	/**
	 * Clear the propositions.
	 */
	public void clearPropositions()
	{
		propositions.clear();
	}

	/**
	 * Clear the votes.
	 */
	public void clearVotes()
	{
		votes.clear();
	}

	/**
	 * @return The votes (represented as ints).
	 */
	public TIntArrayList votes()
	{
		return votes;
	}

	/**
	 * @return The decision of the vote.
	 */
	public int isDecided()
	{
		return isDecided;
	}

	/**
	 * To set the decision of the vote.
	 * 
	 * @param isDecided The message.
	 */
	public void setIsDecided(final int isDecided)
	{
		this.isDecided = isDecided;
	}

	/**
	 * To add a pending value
	 * 
	 * @param value The value to put in pending.
	 */
	public void setPending(final int value)
	{
		final int pendingValue = (value == Constants.UNDEFINED) ? 1 : value;
		updatePendingHash(pendingValue);
		pendingValues.add(pendingValue);
	}
	
	/**
	 * @return True if the state is in pending.
	 */
	public boolean isPending()
	{
		return !pendingValues.isEmpty();
	}

	/**
	 * To clear the pending values
	 */
	public void rebootPending() 
	{
		if (pendingValues != null)
		{
			final TIntIterator it = pendingValues.iterator();
			while (it.hasNext())
			{
				updatePendingHash(it.next());
			}
			pendingValues.clear();
		}
	}

	/**
	 * @param indexPlayer 
	 * @return current index of the phase of a player.
	 */
	public int currentPhase(final int indexPlayer)
	{
		// Matches have null for currentPhase
		return currentPhase != null ? currentPhase[indexPlayer] : 0;
	}

	/**
	 * To set the current phase of a player
	 * @param indexPlayer 
	 * @param newPhase 
	 */
	public void setPhase(final int indexPlayer, final int newPhase)
	{
		if (phaseHashes != null) 
			updateStateHash(phaseHashes[indexPlayer][currentPhase[indexPlayer]]);
		
		currentPhase[indexPlayer] = newPhase;
		
		if (phaseHashes != null) 
			updateStateHash(phaseHashes[indexPlayer][currentPhase[indexPlayer]]);
	}

	/**
	 * To return the sum of the dice of a Dice container.
	 * 
	 * @param index
	 * @return Sum of dice
	 */
	public int sumDice(final int index)
	{
		return sumDice[index];
	}

	/**
	 * For the copy constructor.
	 * @return Sum of dice array
	 */
	public int[] sumDice()
	{
		return sumDice;
	}

	/**
	 * @param sumDice
	 */
	public void setSumDice(final int[] sumDice)
	{
		this.sumDice = sumDice;
	}

	/**
	 * To reinit the sum of the dice.
	 */
	public void reinitSumDice()
	{
		for (int i = 0; i < sumDice.length; i++)
			sumDice[i] = 0;
	}

	/**
	 * To return the current dice of a Dice container.
	 * 
	 * @param index
	 * @return Current dice for (container?) index
	 */
	public int[] currentDice(final int index)
	{
		return currentDice[index];
	}

	/**
	 * To set the boolean diceAllEqual
	 * 
	 * @param value
	 */
	public void setDiceAllEqual(final boolean value)
	{
		diceAllEqual = value;
	}

	/**
	 * @return If the dice are all equal when they are rolled.
	 */
	public boolean isDiceAllEqual()
	{
		return diceAllEqual;
	}

	/**
	 * For the copy constructor.
	 * 
	 * @return All current dice
	 */
	public int[][] currentDice()
	{
		return currentDice;
	}

	/**
	 * @param currentDice
	 */
	public void setCurrentDice(final int[][] currentDice)
	{
		this.currentDice = currentDice;
	}

	/**
	 * To reinit the current dice.
	 */
	public void reinitCurrentDice()
	{
		for (int i = 0; i < currentDice.length; i++)
		{
			for (int j = 0; j < currentDice[i].length; j++)
			{
				currentDice[i][j] = 0;
			}
		}
	}

	/**
	 * @return Owned sites per component
	 */
	public Owned owned()
	{
		return owned;
	}

	/**
	 * To update the sumDice of the dice container.
	 * 
	 * @param dieValue 
	 * @param indexHand
	 */
	public void updateSumDice(final int dieValue, final int indexHand)
	{
		sumDice[indexHand] += dieValue;
	}

	/**
	 * To update the current dice of the dice container.
	 * 
	 * @param dieValue 
	 * @param dieIndex 
	 * @param indexHand
	 */
	public void updateCurrentDice(final int dieValue, final int dieIndex, final int indexHand)
	{
		currentDice[indexHand][dieIndex] = dieValue;
	}

	/**
	 * To reinit the visited BitSet.
	 */
	public void reInitVisited()
	{
		visited.clear(this);
	}

	/**
	 * @param site
	 * @return true if the site is already visited
	 */
	public boolean isVisited(final int site)
	{
		return visited.get(site);
	}

	/**
	 * To update the visited bitSet with the site visited.
	 *
	 * @param site
	 */
	public void visit(final int site)
	{
		visited.set(this, site, true);
	}

	/**
	 * To reinit the visited BitSet.
	 */
	public void reInitCapturedPiece()
	{
		pieceToRemove.clear(this);
	}

	/**
	 * @param site
	 * @return true if the piece in the site has to be removed.
	 */
	public boolean isPieceToRemove(final int site)
	{
		return pieceToRemove.get(site);
	}

	/**
	 * To update the pieceToRemove bitSet with the site of the piece to remove.
	 *
	 * @param site
	 */
	public void setPieceToRemove(final int site)
	{
		pieceToRemove.set(this, site, true);
	}
	
	/**
	 * @return BitSet of sites from which to remove pieces  (e.g. after finishing a sequence of 
	 * 	capturing hops in International Draughts)
	 */
	public HashedBitSet piecesToRemove()
	{
		return pieceToRemove;
	}

	/**
	 * @param pid
	 * @param tid
	 * @return true if the player pid is in the team tid
	 */
	public boolean playerInTeam(final int pid, final int tid)
	{
		if (teams == null || pid >= teams.length)
			return false;

		return teams[pid] == tid;
	}

	/**
	 * To put a player in a team
	 * 
	 * @param pid 
	 * @param tid 
	 */
	public void setPlayerToTeam(final int pid, final int tid)
	{
		updateStateHash(teamHashes[pid][teams[pid]]);
		teams[pid] = tid;
		updateStateHash(teamHashes[pid][teams[pid]]);
	}

	/**
	 * @param pid
	 * @return The index of the team of the player pid
	 */
	public int getTeam(final int pid)
	{
		if (teams == null || pid >= teams.length)
			return Constants.UNDEFINED;

		return teams[pid];
	}

	/**
	 * All the pieces to remove.
	 * 
	 * @return Region containing all sites to remove
	 */
	public Region regionToRemove()
	{
		if (pieceToRemove == null)
			return new Region();
		
		final TIntArrayList sitesToRemove = new TIntArrayList();
		
		for (int site = pieceToRemove.nextSetBit(0); site >= 0; site = pieceToRemove.nextSetBit(site + 1))
		{
			sitesToRemove.add(site);
		}

		return new Region(sitesToRemove.toArray());
	}
	
	/**
	 * @return sameTurnPlayed.
	 */
	public int numTurnSamePlayer()
	{
		return numTurnSamePlayer;
	}

	/**
	 * To reinit the number of turn played by the same player
	 */
	public void reinitNumTurnSamePlayer()
	{
		numTurnSamePlayer = 0;
		++numTurn;
	}

	/**
	 * to increment the number of turn played by the same player
	 */
	public void incrementNumTurnSamePlayer()
	{
		numTurnSamePlayer++;
	}
	
	/**
	 * @return How often did we switch over to a new player as mover?
	 */
	public int numTurn()
	{
		return numTurn;
	}

	//-------------------------------------------------------------------------
	
	@Override
	public boolean equals(final Object other)
	{
		if (!(other instanceof State))
		{
			return false;
		}

		final State otherState = (State) other;
		return fullHash() == otherState.fullHash();
	}
	
	@Override
	public int hashCode()
	{
		return (int)(fullHash()&0xFF_FF_FF_FF);	// Bottom 32 bits of full hash
	}

	/**
	 * To init the phase of the initial state.
	 * 
	 * @param game
	 */
	public void initPhase(final Game game)
	{
		if (game.rules() != null && game.rules().phases() != null)
		{
			currentPhase = new int[game.players().count() + 1];
			for (int pid = 1; pid <= game.players().count(); pid++)
			{
				for (int indexPhase = 0; indexPhase < game.rules().phases().length; indexPhase++)
				{
					final Phase phase = game.rules().phases()[indexPhase];
					
					final RoleType roleType = phase.owner();
					if (roleType == null) 
						continue;
					
					final int phaseOwner = roleType.owner();
					if (phaseOwner == pid || roleType == RoleType.Shared)
					{
						currentPhase[pid] = indexPhase;
						break;
					}
				}
			}

//			System.out.println("INIT PHASES:");
//			for (int i = 1; i <= currentPhase.length; i++)
//				System.out.println("Player " + i + " Phase = " + currentPhase[i - 1]);
		}
	}

	/**
	 * @return The current suit trump.
	 */
	public int trumpSuit()
	{
		return trumpSuit;
	}

	/**
	 * To set the trump suit.
	 * 
	 * @param trumpSuit
	 */
	public void setTrumpSuit(final int trumpSuit)
	{
		this.trumpSuit = trumpSuit;
	}

	/**
	 * @return The structure to get the indices of each element on the track.
	 */
	public OnTrackIndices onTrackIndices()
	{
		return onTrackIndices;
	}

	//-------------------------------------------------------------------------
	
	/**
	 * To swap the player in the list.
	 * 
	 * @param player1 The first player.
	 * @param player2 The second player.
	 */
	public void swapPlayerOrder(final int player1, final int player2)
	{
		int currentIndex1 = 0, currentindex2 = 0;

		for (int i = 1; i < playerOrder.length; i++)
		{
			if (playerOrder[i] == player1)
				currentIndex1 = i;

			if (playerOrder[i] == player2)
				currentindex2 = i;
		}

		final int temp = playerOrder[currentIndex1];
		
		updateStateHash(playerOrderHashes[currentIndex1][playerOrder[currentIndex1]]);
		playerOrder[currentIndex1] = playerOrder[currentindex2];
		updateStateHash(playerOrderHashes[currentIndex1][playerOrder[currentIndex1]]);
		
		updateStateHash(playerOrderHashes[currentindex2][playerOrder[currentindex2]]);
		playerOrder[currentindex2] = temp;
		updateStateHash(playerOrderHashes[currentindex2][playerOrder[currentindex2]]);
	}

	/**
	 * @param playerId The player.
	 * @return The index in the order of a player.
	 */
	public int currentPlayerOrder(final int playerId)
	{
		return playerOrder[playerId]; 
	}

	/**
	 * @param playerId The player.
	 * @return The index of the player in the original order.
	 */
	public int originalPlayerOrder(final int playerId)
	{
		for (int p = 1; p < playerOrder.length; p++)
			if (playerOrder[p] == playerId)
				return p;
		
		for (int po = 0; po < playerOrder.length; po++)
			System.out.println("playerOrder[" + po + "] = " + playerOrder[po]);
		
		throw new RuntimeException("Player " + playerId + " has disappeared after swapping!");
		//return Constants.UNDEFINED;
	}
	
	/**
	 * @return True if the order has changed.
	 */
	public boolean orderHasChanged()
	{
		for (int p = 1; p < playerOrder.length; p++)
			if (playerOrder[p] != p)
				return true;
		return false;
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * @return The remaining dominoes
	 */
	public TIntArrayList remainingDominoes()
	{
		return remainingDominoes;
	}

	// -------------------------------------------------------------------------

	/**
	 * @return The values stored in previous states.
	 */
	public FastTIntArrayList rememberingValues()
	{
		return rememberingValues;
	}

	/**
	 * @return The values stored in previous states and associated to a name.
	 */
	public Map<String, FastTIntArrayList> mapRememberingValues()
	{
		return mapRememberingValues;
	}

	// -------------------------------------------------------------------------

	/**
	 * @return The state stored in the game.
	 */
	public long storedState()
	{
		return storedState;
	}

	/**
	 * To store a state of the game
	 * 
	 * @param state The state to store.
	 */
	public void storeCurrentState(final State state)
	{
		storedState = state.stateHash();
	}

	// -------------------------------------------------------------------------

}