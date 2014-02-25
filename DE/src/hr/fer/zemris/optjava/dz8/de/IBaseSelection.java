package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Sucelje koje predstavlja selekciju bazne jedinke u algoritmu diferencijske evolucije
 * @author Luka Sterbic
 * @version 0.1
 */
public interface IBaseSelection {

	/**
	 * Izaberi baznu jedinku
	 * @param target ciljna jedinka
	 * @param population populacija jedinka
	 * @return bazna jedinka
	 */
	public DoubleArraySolution select(DoubleArraySolution target,
			DoubleArraySolution[] population);
	
}
