package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Sucelje koje predstavlja postupak selekcije u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.1
 */
public interface ISelection<T extends SingleObjectiveSolution> {
	
	/**
	 * Iz dane populacije odaberi jednu jedinku
	 * @param population populacija
	 * @return indeks odabrane jedinke u populaciji
	 */
	public int select(T[] population);

}
