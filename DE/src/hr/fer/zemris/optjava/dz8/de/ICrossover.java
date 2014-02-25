package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Sucelje koje predstavlja krizanje u algoritmu diferencijske evolucije
 * @author Luka Sterbic
 * @version 0.1
 */
public interface ICrossover {
	
	/**
	 * Stvori novu jedinku krizanjem dvaju roditelja
	 * @param firstParent prvi roditalj
	 * @param secondParent drugi roditelj
	 * @return nova jedinka
	 */
	public DoubleArraySolution cross(DoubleArraySolution firstParent, DoubleArraySolution secondParent);

}
