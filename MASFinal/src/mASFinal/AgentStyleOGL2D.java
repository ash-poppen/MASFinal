package mASFinal;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class AgentStyleOGL2D extends DefaultStyleOGL2D{
	@Override
	public Color getColor(final Object agent) {
		Agent a = (Agent) agent;
		if(a.isBusy) {
			return Color.RED;
		} else {
			return Color.BLACK;
		}
	}
}
