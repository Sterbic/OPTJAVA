package hr.fer.zemris.optjava.dz10.genetic;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;

/**
 * Sucelje koje predstavlja mutaciju u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.2
 * @param <T> tip jedinke
 */
public interface IMutation<T extends MultipleObjectiveSolution> {
	
	/**
	 * Mutiraj zadanu jedinku
	 * @param t jedinka
	 */
	public void mutate(T chromosome);

}
