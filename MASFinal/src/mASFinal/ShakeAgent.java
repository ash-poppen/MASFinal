package mASFinal;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Direction;
import repast.simphony.space.grid.*;
import repast.simphony.util.ContextUtils;

/**
 * @author PaulQuint
 *
 */
public class ShakeAgent extends Agent {
	/** 
	 * Value in range [0,1]. Represents the proportion of time that has passed
	 * between disease contraction and the time when the disease kills the
	 * agent. This increments by 1/D every tick where D is the kill time of the
	 * disease. When it hits 1, the agent dies.
	 */
	float infectionLevel;
	
	/**
	 * Rate in [0, 1] which informs the decision to lie before a handshake
	 */
	float propensityToLie;
	
	/**
	 * Rate in [0,1]
	 */
	float paranoiaLevel;
	

	/**
	 * The number of handshakes accomplished
	 */
	int handshakes;
	
	Agent[] recentInteractions;
	
	int interactionTime;
	
	public ShakeAgent(float propensityToLie, float paranoiaLevel) {
		super();
		this.dead = false;
		this.infectionLevel = 0;
		this.propensityToLie = propensityToLie;
		this.paranoiaLevel = paranoiaLevel;
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int recentListSize = (int) params.getValue("recentListSize");
		this.recentInteractions = new Agent[recentListSize]; 
	}

	public void die() {
		ShakeSpace space = (ShakeSpace)ContextUtils.getContext(this);
		space.remove(this);
		dead = true;
	}
	
	@Override
	@ScheduledMethod(start = 0, interval = 1)
	public void step() {
		Context space = ContextUtils.getContext(this);
		Grid grid = (Grid)space.getProjection("grid");
		GridPoint point = grid.getLocation(this);
		
		// Update disease
		if (this.infectionLevel > 0) {
			Parameters params = RunEnvironment.getInstance().getParameters();
			float diseaseDeathTime = (float) params.getValue("diseaseDeathTime");
			this.infectionLevel += (1 / diseaseDeathTime);
			if (this.infectionLevel >= 1) {
				die();
			}
		}
		// If not otherwise engaged, roam randomly
		if (!this.isBusy) {
			int[] randomDisplacement = { RandomHelper.nextIntFromTo(-1, 1), RandomHelper.nextIntFromTo(-1, 1) };
			int[] targetLocation = { 0, 0 };
			grid.getLocation(this).toIntArray(targetLocation);	// this puts the integer coordinates into the array
			grid.getGridPointTranslator().translate(targetLocation, randomDisplacement);		
			if (grid.getRandomObjectAt(targetLocation) == null) grid.moveTo(this, targetLocation);
			
			Agent a = checkAdjacent(grid);
			if(a != null) {
				encounter(a);
			}
		}
		if(this.isBusy) {
			if(interactionTime > 0) interactionTime--;
			else this.isBusy=false;
		}
	}
	
	@Override
	public void shake(Agent agent) {
		if (agent instanceof DoctorAgent) {
			this.infectionLevel = 0;
		}
		else {
			// possibly transmit infection
			float toInfect = new Random().nextFloat();
			if(toInfect < agent.probabilityInfected()) {
				//infect
			}
			//utility + 1
			this.handshakes++;
		}
	}
	
	public void encounter(Agent agent) {
		if (agent instanceof DoctorAgent){
			interact(agent);
		}
		else{
			ShakeAgent sa = (ShakeAgent) agent;
			if (assess(sa)){
				if(converse(sa)){
					interact(sa);
				}
			}
		}
	}
	
	//Step 1 in Encounter - determine if other agent is infected
	//based on current beliefs and first hand
	//returns true if the agent will continue the engagement
	public boolean assess(ShakeAgent agent) {
		interactionTime+=1;
		memory(agent);
		if((agent.probabilityInfected() + this.paranoiaLevel)>this.assessmentThreshold) {
			return false;
		}
		return true;
	}
	
	
	public float probabilityInfected() {
		double exponent = (double) this.infectionLevel;
		return 1/(1+exp(-1*exponent));
	}
	
	//Recalls beliefs about a particular agent
	private void memory(ShakeAgent agent) {
		// TODO Auto-generated method stub
		
	}
	
	public void converse(ShakeAgent agent) {
		interactionTime+=5;
		// share with other agent if infected or not (can lie)
		agent.shareInfectedState();
		// decide whether to interact
	}
	
	//
	public boolean shareInfectedState() {
		// if not infected say so
		if(this.infectionLevel==0) {
			return false;
		} else {
			// if infected generate random float and check propensityToLie
			float toLie = new Random().nextFloat();
			if(toLie > propensityToLie) {
				return true;
			} else {
				return false;
			}
		}
	}

	//Step 3 of Encounter - Shake hands and Share Beliefs
	public void interact(Agent agent) {
		interactionTime+=10;
		shake(agent);
		shareInfo(agent);
		// interact
	}
	
	//Shares beliefs with another agent
	private void shareInfo(Agent agent) {
		this.beliefs.combineBeliefs(agent.beliefs);
	}
	
	//Updates agent's list of agents recently interacted with
	@Override
	public void updateRecentInteractions(Agent agent, int timestamp) {
		Parameters params = RunEnvironment.getInstance().getParameters();
		int recentListSize = (int) params.getValue("recentListSize");
		
	}

	/**
	 * Currently returns the last agent found from left to right
	 * @param grid
	 * @return
	 */
	public Agent checkAdjacent(Grid grid) {
		Agent agent = null;
		GridPoint point = grid.getLocation(this);
		int x = point.getX();
		int y = point.getY();
		for(int i=-1; i<=1;i++) {
			for(int j=-1; j<=1;j++) {
				int[] coords = {x+i,y+j};
				GridPoint adjPoint = new GridPoint(coords);
				if(inGrid(grid, adjPoint) && (grid.getObjectAt(coords)) instanceof Agent && !adjPoint.equals(point)) {
					agent = (Agent) grid.getObjectAt(coords);
				}
			}
		}	
		return agent;
	}
	
	private boolean inGrid(Grid grid, GridPoint point){
		int x = point.getX();
		int y = point.getY();
		if(0 <= x && x <= grid.getDimensions().getWidth() && 0 <= y && y <= grid.getDimensions().getHeight()) {
			return true;
		} else {
			return false;
		}
	}
}
