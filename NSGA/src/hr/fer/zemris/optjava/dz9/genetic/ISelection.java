package hr.fer.zemris.optjava.dz9.genetic;

import hr.fer.zemris.optjava.dz9.opt.MultipleObjectiveSolution;

/**
 * Sucelje koje predstavlja postupak selekcije u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.1
 */
public interface ISelection<T extends MultipleObjectiveSolution> {
	
	/**
	 * Iz dane populacije odaberi jednu jedinku
	 * @param population populacija
	 * @return indeks odabrane jedinke u populaciji
	 */
	public int select(T[] population);

}
