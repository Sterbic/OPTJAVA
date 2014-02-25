package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Sucelje koje predstavlja objekt koji incijalizira
 * populaciju jedinki u optimizacijskom algoritmu.
 * @author Luka Sterbic
 * @version 0.3
 * @param <T> tip jedinke
 */
public interface IPopulationCreator<T extends SingleObjectiveSolution> {
	
	/**
	 * Stvori populaciju zadane velicine, uz inicijalizaciju populacije
	 * @param populationSize velicina populacije
	 * @return populacija
	 */
	public T[] create(int populationSize);
	
	/**
	 * Stvori polje za populaciju zadane velicine
	 * @param populationSize velicina populacije
	 * @return prazno polje za populaciju
	 */
	public T[] createEmpty(int populationSize);
	
	/**
	 * Stvori jedinku koja ce se koristiti kao poison
	 * objekt u visedretvenom okruzenju.
	 * @return poison objekt
	 */
	public T createPoison();
	
}
