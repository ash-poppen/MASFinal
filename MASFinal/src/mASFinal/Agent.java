package mASFinal;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public abstract class Agent {

	/**
	 * The state of the agent's beliefs about other agents' illness
	 */
	protected BeliefState beliefs;

	public abstract void shake(Agent other);

	public abstract void step();

	/**
	 * Whether the agent has died of their disease
	 */
	protected boolean dead;

	public Agent() {
		super();
	}

}