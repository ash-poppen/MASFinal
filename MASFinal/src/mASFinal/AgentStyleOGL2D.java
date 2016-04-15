package mASFinal;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class AgentStyleOGL2D extends DefaultStyleOGL2D{
	
	/**
	 * Doctors = Blue
	 * Healthy ShakeAgents = Black
	 * Infected Agents: 
	 * 0-0.2 = Yellow 
	 * 0.2-0.4 = Orange
	 * 0.4-0.6 = Pink
	 * 0.6-0.8 = Magenta
	 * 0.8-1 = Red
	 */
	@Override
	public Color getColor(final Object agent) {
		if (agent instanceof DoctorAgent){
			return Color.BLUE;
		} 
		else if (agent instanceof ShakeAgent) {
			ShakeAgent a = (ShakeAgent) agent;
			if(a.infectionLevel > 0) {
				if (a.infectionLevel > 0.2) {
					if (a.infectionLevel > 0.4) {
						if (a.infectionLevel > 0.6) {
							if (a. infectionLevel > 0.8) {
								return Color.RED;
							} else {
								return Color.MAGENTA;
							}
						} else {
							return Color.PINK;
						}
					} else {
						return Color.ORANGE;
					}
				} else {
					return Color.YELLOW;
				}
			}
			return Color.BLACK;
	}
}
