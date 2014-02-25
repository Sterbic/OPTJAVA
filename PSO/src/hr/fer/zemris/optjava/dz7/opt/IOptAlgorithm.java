package hr.fer.zemris.optjava.dz7.opt;

/**
 * Sucelje koje modelira opceniti optimizacijski algoritam
 * @author Luka Sterbic
 * @version 0.1
 * @param <T> tip rjesenja
 */
public interface IOptAlgorithm<T extends SingleObjectiveSolution> {

	/**
	 * Pokreni algoritam
	 * @return rezultat algoritma
	 */
	public T run();
	
}
