package mASFinal;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author PaulQuint
 *
 */
public class ShakeAgent {
	int accumulatedValue;
	boolean dead;
	int diseaseAge;
	boolean isDiseased;
	
	public ShakeAgent() {
		dead = false;
	}

	public void die() {
		ShakeSpace space = (ShakeSpace)ContextUtils.getContext(this);
		space.remove(this);
		dead = true;
	}
	
	@ScheduledMethod(start = 0, interval = 1)
	public void step() {
		Context space = ContextUtils.getContext(this);
		Grid grid = (Grid)space.getProjection("grid");
		GridPoint point = grid.getLocation(this);
		
		if (isDiseased) {
			Parameters params = RunEnvironment.getInstance().getParameters();
			int diseaseDeathTime = (int) params.getValue("diseaseDeathTime"); 
			if (++diseaseAge > diseaseDeathTime) {
				die();
			}
		}
	}
}
