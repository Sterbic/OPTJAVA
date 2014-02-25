package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Implementacija uniformnog krizanja
 * @author Luka Sterbic
 * @version 0.1
 */
public class UniformCrossover implements ICrossover<IntegerArraySolution> {
	
	@Override
	public IntegerArraySolution cross(IntegerArraySolution firstParent,
			IntegerArraySolution secondParent) {
		IRNG rand = RNG.getRNG();
		IntegerArraySolution child = firstParent.duplicate();
		
		for(int i = 0; i < child.values.length; i++) {
			if(rand.nextBoolean()) {
				child.values[i] = secondParent.values[i];
			}
		}
		
		return child;
	}

}
