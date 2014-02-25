package hr.fer.zemris.optjava.ant.visual;

import hr.fer.zemris.optjava.ant.AntSimulator;

/**
 * Sucelje koje predstavlja listener na promijene stanja simulatora mrava
 * @author Luka Sterbic
 * @version 0.2
 */
public interface IAntSimulatorListener {
	
	/**
	 * Metoda koja ce se pozvati svaki puta kada
	 * dodje do promijene stanja simulatora
	 * @param map novo stanje svijeta
	 * @param ant novo stanje mrava
	 */
	public void update(AntSimulator simulator);

}
