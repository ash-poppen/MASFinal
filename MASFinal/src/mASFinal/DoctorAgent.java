/**
 * 
 */
package mASFinal;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

/**
 * @author drkwint
 *
 */
public class DoctorAgent extends Agent {
	/**
	 * How far the doctor can on the grid see when looking for other agents 
	 */
	double perceptionRange;
	
	/**
	 * 
	 */
	public DoctorAgent() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		this.perceptionRange = (double) params.getValue("doctorPerceptionRange");
	}

	/* (non-Javadoc)
	 * @see mASFinal.Agent#shake(mASFinal.Agent)
	 */
	@Override
	public void shake(Agent other) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mASFinal.Agent#step()
	 */
	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

}
