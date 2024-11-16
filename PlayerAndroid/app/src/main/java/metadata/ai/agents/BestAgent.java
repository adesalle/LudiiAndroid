package metadata.ai.agents;

import main.StringRoutines;

/**
 * Describes the name of an algorithm or agent that is typically expected to
 * be the best-performing algorithm available in Ludii for this game.
 *
 * @author Dennis Soemers
 * @remarks Some examples of names that Ludii can currently recognise are
 * ``Random'', ``Flat MC'', ``Alpha-Beta'', ``UCT'', ``MC-GRAVE'',
 * and ``Biased MCTS''.
 */
public final class BestAgent implements Agent {

    //-------------------------------------------------------------------------

    /**
     * Agent name
     */
    private final String agent;

    //-------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param agent The name of the (expected) best agent for this game.
     * @example (bestAgent " UCT ")
     */
    public BestAgent(final String agent) {
        this.agent = agent;
    }

    //-------------------------------------------------------------------------

    /**
     * @return The agent string
     */
    public String agent() {
        return agent;
    }

    //-------------------------------------------------------------------------

    @Override
    public String toString() {
        return "(bestAgent " + StringRoutines.quote(agent) + ")";
    }

    //-------------------------------------------------------------------------

}
