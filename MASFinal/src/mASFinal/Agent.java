package mASFinal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public abstract class Agent{

	/**
	 * The state of the agent's beliefs about other agents' illness
	 */
	protected BeliefState beliefs;
	
	protected boolean isBusy;
	
	/**
	 * 
	 */
	protected HashMap<Agent, Integer> recentInteractions;

	public abstract void updateRecentInteractions(Agent agent, int timestamp);
	
	public abstract void shake(Agent agent);
	
	public abstract float probabilityInfected();

	public abstract void step();

	/**
	 * Whether the agent has died of their disease
	 */
	protected boolean dead;

	public Agent() {
		super();
		this.isBusy = false;
		this.beliefs = new BeliefState();
	}

}