package hr.fer.zemris.optjava.ant.visual;

import hr.fer.zemris.optjava.ant.AntSimulator;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Akocija koja vraca mrava i stanje svijeta u prvobitni polozaj
 * @author Luka Sterbic
 * @version 0.1
 */
public class ResetAction extends AbstractAction {

	private static final long serialVersionUID = 5030690858892600517L;
	
	private AntSimulator simulator;
	
	/**
	 * Konstruktor za ResetAction
	 * @param simulator simulator kretanja mrava
	 */
	public ResetAction(AntSimulator simulator) {
		this.simulator = simulator;
		putValue(Action.NAME, "Reset");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		simulator.reset();
	}

}
