package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Sucelje koje predstavlja krizanje u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.1
 * @param <T> tip jedinke
 */
public interface ICrossover<T extends SingleObjectiveSolution> {
	
	/**
	 * Stvori novu jedinku krizanjem dvaju roditelja
	 * @param firstParent prvi roditalj
	 * @param secondParent drugi roditelj
	 * @return nova jedinka
	 */
	public T cross(T firstParent, T secondParent);

}
