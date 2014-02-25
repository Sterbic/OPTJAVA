package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Implementacija inicijalizacije populacije jedinki
 * koje su predstavljene poljem cijalih brojeva.
 * @author Luka Sterbic
 * @version 0.2
 */
public class IASPopulationCreator implements IPopulationCreator<IntegerArraySolution> {

	private int dimensions;
	private int[] mins;
	private int[] maxs;
	
	/**
	 * Konstruktor za IASPopulationCreator
	 * @param dimensions dimenzija polja
	 * @param mins polje maksimalnih vrijednosti po komponentama (ukljucivi)
	 * @param maxs polje minimalnih vrijednosti po komponentama (iskljucivi)
	 */
	public IASPopulationCreator(int dimensions, int[] mins, int[] maxs) {
		this.dimensions = dimensions;
		this.maxs = maxs;
		this.mins = mins;
	}

	@Override
	public IntegerArraySolution[] create(int populationSize) {
		IntegerArraySolution[] population = new IntegerArraySolution[populationSize];
		IRNG rand = RNG.getRNG();
		
		for(int i = 0; i < populationSize; i++) {
			int[] values = new int[dimensions];
			
			for(int j = 0; j < dimensions; j++) {
				values[j] = rand.nextInt(mins[j], maxs[j]);
			}
			
			population[i] = new IntegerArraySolution(values);
		}
		
		return population;
	}

	@Override
	public IntegerArraySolution[] createEmpty(int populationSize) {
		return new IntegerArraySolution[populationSize];
	}

	@Override
	public IntegerArraySolution createPoison() {
		return new IntegerArraySolution();
	}

}
