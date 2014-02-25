package hr.fer.zemris.optjava.dz10.genetic;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;

/**
 * Implementacija aritmetickog krizanja
 * @author Luka Sterbic
 * @version 0.1
 */
public class ArithmeticCrossover implements ICrossover<MultipleObjectiveSolution> {

	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent,
			MultipleObjectiveSolution secondParent) {
		MultipleObjectiveSolution child = firstParent.duplicate();
		
		for(int i = 0; i < child.values.length; i++) {
			child.values[i] += secondParent.values[i];
			child.values[i] /= 2.0;
		}
		
		return child;
	}

}
