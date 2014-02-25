package hr.fer.zemris.optjava.ant.visual;

import hr.fer.zemris.optjava.ant.AntSimulator;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Akcija koja ce pokrenuti jedan korak marava
 * @author Luka Sterbic
 * @version 0.1
 */
public class NextMoveAction extends AbstractAction {

	private static final long serialVersionUID = 7561280332226442532L;

	private AntSimulator simulator;
	
	/**
	 * Konstruktor za NextMoveAction
	 * @param simulator simulator kretanja mrava
	 */
	public NextMoveAction(AntSimulator simulator) {
		this.simulator = simulator;
		putValue(Action.NAME, "Next");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		simulator.next();
	}

}
