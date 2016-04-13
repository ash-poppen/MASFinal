/**
 * 
 */
package mASFinal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author drkwint
 * The beliefs an agent holds about other agents' infection status
 * 
 * Beliefs are combined at handshake, keeping all the most recent info about
 * each agent.
 */
public class BeliefState {
	/**
	 * Beliefs about the infection status of other agents.
	 * 
	 * beliefStatus is set to True if the agent is believed to be sick
	 * beliefTime is the tick timestamp at which the latest information heard
	 * 		about the agent was generated
	 */
	Map<ShakeAgent, Boolean> beliefStatus;
	Map<ShakeAgent, Integer> beliefTime;
	
	public BeliefState() {
		this.beliefStatus = new HashMap<ShakeAgent, Boolean>();
		this.beliefTime = new HashMap<ShakeAgent, Integer>();
	}
	
	/**
	 * @param a the agent whose status is in question
	 * @return False if no beliefs are held about the agent, otherwise whatever
	 * the status of the agent is believed to be
	 */
	public boolean isBelievedSick(ShakeAgent a) {
		if (!this.beliefStatus.containsKey(a)) return false;
		else return this.beliefStatus.get(a);
	}
}
