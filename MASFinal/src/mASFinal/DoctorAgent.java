/**
 * 
 */
package mASFinal;

import java.util.Collection;
import java.util.List;

import com.sun.medialib.codec.jp2k.Params;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author drkwint
 *
 */
public class DoctorAgent extends Agent {
	/**
	 * How far out on the grid the doctor can see when looking for other agents 
	 */
	int perceptionRange;
	
	int interactionTime;
	/**
	 * 
	 */
	public DoctorAgent() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		this.perceptionRange = (int) params.getValue("doctorPerceptionRange");
	}

	@SuppressWarnings("null")
	@Override
	@ScheduledMethod(start = 0, interval = 1)
	@Watch(watcheeClassName = "ShakeAgent", watcheeFieldNames = "moved", 
	query = "within_vn 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void step() {
		Context space = ContextUtils.getContext(this);
		Grid grid = (Grid)space.getProjection("grid");
		GridPoint point = grid.getLocation(this);
		
		//Use the GridCelNgh class to create GridCells for the surrounding neighborhood
		//identify list of all agents currently in perception range in each direction
		GridCellNgh<ShakeAgent> nghCreator = new GridCellNgh<ShakeAgent>(grid, point, ShakeAgent.class, this.perceptionRange, this.perceptionRange);
		List<GridCell<ShakeAgent>> gridCells = nghCreator.getNeighborhood(true);
    	//compare list with Belief State to find list of infected agents in range
		List<GridCell<ShakeAgent>> infectedCells = null;
		for (GridCell<ShakeAgent> cell : gridCells){
			GridPoint location = cell.getPoint();
			ShakeAgent agent = (ShakeAgent) grid.getObjectAt(location.getX(), location.getY());
			boolean isSick = this.beliefs.isBelievedSick(agent);
			if (isSick == true) {
				infectedCells.add(cell);
			}
		}
		//if there are infected agents in the range
		if (infectedCells.isEmpty() == false) {
			//identify which agent in infected cells has been "known" to be infected the longest
			Parameters params = RunEnvironment.getInstance().getParameters();
			int maxTime = (Integer)params.getValue("endTime");
			int[] sickest = null;
			for (GridCell<ShakeAgent> cell : infectedCells) {
				GridPoint location = cell.getPoint();
				ShakeAgent agent = (ShakeAgent) grid.getObjectAt(location.getX(), location.getY());
				 int timeOfInfection = this.beliefs.beliefTime.get(agent);
				 if (timeOfInfection < maxTime){
					 location.toIntArray(sickest);
				 }
			}
			//whichever infected agent has the oldest (i.e. lowest) belief time, move towards them
			moveTowards(grid, sickest);
	    }
		// If not otherwise engaged, roam randomly <- only do this if there are no infected agents in range
	    else {
			if (!this.isBusy) {			
				int[] targetLocation = { 0, 0 };
				moveTowards(grid, targetLocation);
			}
	    }
	}
	
	//Directs agent's movements towards 
	private void moveTowards(Grid grid, int[] targetLocation) {
		int[] randomDisplacement = { RandomHelper.nextIntFromTo(-1, 1), RandomHelper.nextIntFromTo(-1, 1) };
		grid.getLocation(this).toIntArray(targetLocation);	// this puts the integer coordinates into the array
		grid.getGridPointTranslator().translate(targetLocation, randomDisplacement);		
		if (grid.getRandomObjectAt(targetLocation) == null) grid.moveTo(this, targetLocation);
	}

	//Doctor agents cannot get infected, so this always returns zero
	@Override
	public float probabilityInfected() {
		return 0;
	}
	
	//Doctors only participate in Step 3 of encounters
	//as they will shake hands with (a.k.a. treat) any agent regardless of health
	public void encounter(Agent agent) {
			interact(agent);
	}

	//Shakes hands (a.k.a. treats) and shares information with another agent
	public void interact(Agent agent) {
		interactionTime+=10;
		shake(agent);
		shareInfo(agent);
	}
	
	@Override
	//Adds agent to recentlyInteracted list
	//Note: Curing infection during shake takes place in ShakeAgent's implementation of shake
	public void shake(Agent agent) {
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		int time = (int) currentTime;
		updateRecentInteractions(agent, time);
	}
	
	//Shares beliefs with another agent
	private void shareInfo(Agent agent) {
		this.beliefs.combineBeliefs(agent.beliefs);
	}

	//Adds agent to recent interactions list
	//USE UPDATE METHOD IN INTERACTION STATE CLASS
	@Override
	public void updateRecentInteractions(Agent agent, int timestamp) {
		Parameters params = RunEnvironment.getInstance().getParameters();
		int recentListSize = (int) params.getValue("recentListSize");
		int size = this.recentInteractions.size();
		if (size < recentListSize) {
			this.recentInteractions.put(agent, timestamp);
		} else {
			
		}
	}

}
