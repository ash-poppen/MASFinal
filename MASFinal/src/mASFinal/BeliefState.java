/**
 * 
 */
package mASFinal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Paul Quint
 * @author Ashley Dyer
 * @author Aaron Post
 * 
 * The beliefs an agent holds about other agents' infection status
 * 
 * Beliefs are combined at handshake, keeping the most recent info about
 * each agent.
 */
public class BeliefState {
	/**
	 * Beliefs about the infection status of other agents.
	 * 
	 * beliefStatus is set to True if the agent is believed to be sick
	 * 
	 */
	Map<ShakeAgent, Boolean> beliefStatus;
	
	/**
	 * beliefTime is the tick time stamp at which the latest information heard
	 * 		about the agent was generated
	 */
	Map<ShakeAgent, Integer> beliefTime;
	
	public BeliefState() {
		this.beliefStatus = new HashMap<ShakeAgent, Boolean>();
		this.beliefTime = new HashMap<ShakeAgent, Integer>();
	}
	
	/**
	 * @param a -  the agent whose status is in question
	 * @return False if no beliefs are held about the agent, otherwise whatever
	 * the status of the agent is believed to be
	 */
	public boolean isBelievedSick(Agent a) {
		if (!this.beliefStatus.containsKey(a)) return false;
		else return this.beliefStatus.get(a);
	}
	
	/**
	 * Updates beliefs with new information
	 * @param a - agent in question
	 * @param status - the understood status of the agent 
	 * @param timestamp - tick the information was generated
	 */
	public void updateBelief(ShakeAgent a, boolean status, int timestamp) {
		this.beliefStatus.put(a, status);
		this.beliefTime.put(a, timestamp);
	}
	
	/**
	 * Updates my beliefs based on the more recent beliefs of other
	 * @param other - Another agent's BeliefState which has been communicated
	 */
	public void combineBeliefs(BeliefState other) {
		Set<ShakeAgent> otherKnownAgents = other.beliefStatus.keySet();
		
		for (ShakeAgent agent : otherKnownAgents) {
			if (other.beliefTime.get(agent) > this.beliefTime.get(agent)) {
				this.updateBelief(agent, other.beliefStatus.get(agent), other.beliefTime.get(agent));
			}
		}
	}
}
