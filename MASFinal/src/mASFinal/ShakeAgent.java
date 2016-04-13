package mASFinal;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Direction;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
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
	
	public ShakeAgent(float propensityToLie, float paranoiaLevel) {
		super();
		this.dead = false;
		this.infectionLevel = 0;
		this.propensityToLie = propensityToLie;
		this.paranoiaLevel = paranoiaLevel;
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
		}
	}
	
	@Override
	public void shake(Agent other) {
		this.handshakes++;
	}
}
