package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Klasa koja implementira mutaciju po uniformnoj razdiobi.
 * Koponenta rjesenja zamijenjuje se slucajnom vrijednosti iz U(min, max).
 * @author Luka Sterbic
 * @version 0.3
 */
public class UniformSwapMutation implements IMutation<IntegerArraySolution> {
	
	private int min;
	private int max;
	private double probability;
	
	/**
	 * Konstruktor za GaussAddMutation
	 * @param min minimalna vrijednost uniformne distribucije (ukljuciva)
	 * @param max maksimalna vrijednost uniformne distribucije (iskljuciva)
	 * @param probability vjerojatnost mutacije jedne komponente
	 */
	public UniformSwapMutation(int min, int max, double probability) {
		if(probability > 1.0 || probability < 0.0) {
			throw new IllegalArgumentException(
					"Vjerojatnost mutacije mora biti iz intervala [0, 1]!");
		}
		
		this.min = min;
		this.max = max;
		this.probability = probability;
	}

	@Override
	public void mutate(IntegerArraySolution chromosome) {
		IRNG rand = RNG.getRNG();
		
		for(int j = 0; j < chromosome.values.length; j++) {
			if(rand.nextDouble() <= probability) {
				chromosome.values[j] = rand.nextInt(min, max);
			}
		}
	}

}
