package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Klasa koja implementira mutaciju po Gaussovoj razdiobi.
 * Koponenti rjesenja dodaje slucajnu vrijednost iz N(0, sigma).
 * @author Luka Sterbic
 * @version 0.2
 */
public class GaussAddMutation implements IMutation<IntegerArraySolution> {
	
	private double sigma;
	private double probability;
	
	/**
	 * Konstruktor za GaussAddMutation
	 * @param gamma standardna devijacija
	 * @param probability vjerojatnost mutacije jedne komponente
	 */
	public GaussAddMutation(double sigma, double probability) {
		if(sigma <= 0.0) {
			throw new IllegalArgumentException(
					"Standardna devijacija mora biti veca od nule!");
		} else if(probability > 1.0 || probability < 0.0) {
			throw new IllegalArgumentException(
					"Vjerojatnost mutacije mora biti iz intervala [0, 1]!");
		}
		
		this.sigma = sigma;
		this.probability = probability;
	}

	@Override
	public void mutate(IntegerArraySolution chromosome) {
		IRNG rand = RNG.getRNG();
		
		for(int j = 0; j < chromosome.values.length; j++) {
			if(rand.nextDouble() <= probability) {
				chromosome.values[j] += Math.round(rand.nextGaussian() * sigma);
			}
		}
	}

}
