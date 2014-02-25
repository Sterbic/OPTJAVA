package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Sucelje koje predstavlja objekt koji incijalizira
 * populaciju jedinki u optimizacijskom algoritmu.
 * @author Luka Sterbic
 * @version 0.4
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
	 * Stvori kopiju zadanog rjesenja
	 * @param solution rjesenje
	 * @return kopija rjesenja
	 */
	public T createCopy(T solution);
	
	/**
	 * Provjeri da li je zadano rjesenje lagalno
	 * @param solution rjesenje
	 * @return true ako je rjesenje legalno, false inace
	 */
	public boolean isLegal(T solution);
	
	/**
	 * Napravi korekciju dobrote zadano rjesenja
	 * @param solution rjesenje
	 */
	public void correctFitness(T solution);
	
}
