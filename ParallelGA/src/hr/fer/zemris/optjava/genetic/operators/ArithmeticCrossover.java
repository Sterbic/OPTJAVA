package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;


/**
 * Implementacija aritmetickog krizanja
 * @author Luka Sterbic
 * @version 0.1
 */
public class ArithmeticCrossover implements ICrossover<IntegerArraySolution> {

	@Override
	public IntegerArraySolution cross(IntegerArraySolution firstParent,
			IntegerArraySolution secondParent) {
		IntegerArraySolution child = firstParent.duplicate();
		
		for(int i = 0; i < child.values.length; i++) {
			child.values[i] += secondParent.values[i];
			child.values[i] = Math.round(child.values[i] / 2.0f);
		}
		
		return child;
	}

}
