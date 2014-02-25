package hr.fer.zemris.optjava.dz10.genetic;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;

/**
 * Sucelje koje predstavlja krizanje u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.1
 * @param <T> tip jedinke
 */
public interface ICrossover<T extends MultipleObjectiveSolution> {
	
	/**
	 * Stvori novu jedinku krizanjem dvaju roditelja
	 * @param firstParent prvi roditalj
	 * @param secondParent drugi roditelj
	 * @return nova jedinka
	 */
	public T cross(T firstParent, T secondParent);

}
