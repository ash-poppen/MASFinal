/**
 * 
 */
package mASFinal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.grid.GridPoint;

/**
 * @author Paul Quint
 * @author Ashley Dyer
 * @author Aaron Post
 * 
 * The recent interactions an agent has with other agents.
 * While another agent is listed within an agent's interaction state,
 * the agent will refuse to interact with it again. 
 * 
 * 
 * The number of recent interactions tracked is determined by the parameter recentListSize
 */
public class InteractionState {

	/**
	 * timeOfInteraction is the tick time stamp at which the interaction with
	 * the other agent occurred
	 */
	Deque<>
	
	public InteractionState() {
		
	}

	/**
	 * @param a -  the agent to check recent interactions for
	 * @return False if no recent interactions were had with agent
	 * @return True if agent is in list of recent interactions
	 */
	public boolean hasRecentInteraction(Agent a) {
		if (!this.timeOfInteraction.containsKey(a)) return false;
		else return true;
	}
	
	/**
	 * Updates Interaction State with new interaction
	 * If number of recent interactions is below the max (determined by parameter recentListSize) add new HashMap
	 * If it is at the max, find the oldest entry (smallest timeOfInteraction value) and replace it
	 * @param a - agent in new interaction
	 * @param timestamp - tick the information was generated
	 */
	public void updateRecentInteractions(Agent a, int timestamp) {
		int size = this.timeOfInteraction.size();
		Parameters params = RunEnvironment.getInstance().getParameters();
		int maxSize = (int) params.getValue("recentListSize");
		if (size < maxSize) {
			this.timeOfInteraction.put(a, timestamp);
		}
		else {
			int maxTime = (Integer)params.getValue("endTime");
			int oldest = maxTime;
			Iterator entries = this.timeOfInteraction.entrySet().iterator();
			while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  Object key = thisEntry.getKey();
			  Object value = thisEntry.getValue();
				
			}
		}		
	}
	
}

