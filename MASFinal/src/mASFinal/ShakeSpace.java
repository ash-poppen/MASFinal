/**
 * 
 */
package mASFinal;

import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author PaulQuint
 *
 */
public class ShakeSpace extends DefaultContext<Object> {
	Parameters params = RunEnvironment.getInstance().getParameters();
	private int recentListSize = (Integer)params.getValue("recentListSize");
	private int xDim = (Integer)params.getValue("worldWidth");
	private int yDim = (Integer)params.getValue("worldHeight");
	
	public ShakeSpace() {
		super("ShakeSpace");
		Grid<Object> grid = GridFactoryFinder.createGridFactory(null)
				.createGrid("grid", this, new GridBuilderParameters<Object>(
						new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, xDim, yDim));
	}
}
