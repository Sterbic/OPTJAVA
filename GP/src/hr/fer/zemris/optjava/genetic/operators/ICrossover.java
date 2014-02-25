package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Sucelje koje predstavlja krizanje u genetskom algoritmu
 * @author Luka Sterbic
 * @version 0.2
 * @param <T> tip jedinke
 */
public interface ICrossover<T extends SingleObjectiveSolution> {
	
	/**
	 * Stvori novu jedinku krizanjem dvaju roditelja, pretpostavlja se
	 * da su metodi predane kopije roditelja koje se smiju mijenjati
	 * @param firstParent prvi roditalj
	 * @param secondParent drugi roditelj
	 * @return nova jedinka
	 */
	public CrossoverResult<T> cross(T firstParent, T secondParent);

	/**
	 * Klasa koja modelira rezultat krizanja
	 * @author Luka Sterbic
	 * @version 0.1
	 * @param <T> tip jedinke
	 */
	public static class CrossoverResult<T extends SingleObjectiveSolution> {
		
		public T first;
		public T second;
		
		/**
		 * Konstruktor za CrossoverResult
		 * @param first prvo dijete
		 * @param second drugo dijete
		 */
		public CrossoverResult(T first, T second) {
			this.first = first;
			this.second = second;
		}
		
	}
	
}
