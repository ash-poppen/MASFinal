/**
 * 
 */
package mASFinal;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author Paul Quint
 *
 */
public class ShakeSim implements ContextBuilder<Object> {
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("ShakeSim");
		Parameters params = RunEnvironment.getInstance().getParameters();
		int numAgents = (Integer)params.getValue("initialNumAgents");
		
		int xDim = (Integer)params.getValue("worldWidth");
		int yDim = (Integer)params.getValue("worldHeight");
		Grid<Object> grid = GridFactoryFinder.createGridFactory(null)
				.createGrid("grid", context, new GridBuilderParameters<Object>(
						new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, xDim, yDim));
		
		for (int i = 0; i < numAgents; i++) {
			double lie = RandomHelper.nextDoubleFromTo(0, 1);
			double paranoia = RandomHelper.nextDoubleFromTo(0, 1);
			ShakeAgent agent = new ShakeAgent(i, i);
			context.add(agent);
			int x = RandomHelper.nextIntFromTo(0, xDim - 1);
			int y = RandomHelper.nextIntFromTo(0, yDim - 1);
			while (!grid.moveTo(agent, x, y)) {
				x = RandomHelper.nextIntFromTo(0, xDim - 1);
				y = RandomHelper.nextIntFromTo(0, yDim - 1);
			}
		}
		
		int endTime = (Integer)params.getValue("endTime");
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(endTime);
		}
		
		return context;
	}

}
