package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Implementacija krizanja s jednom tockom prijeloma
 * @author Luka Sterbic
 * @version 0.1
 */
public class OnePointCrossover implements ICrossover<IntegerArraySolution> {

	@Override
	public IntegerArraySolution cross(IntegerArraySolution firstParent,
			IntegerArraySolution secondParent) {
		IRNG rand = RNG.getRNG();
		
		if(rand.nextBoolean()) {
			IntegerArraySolution tmp = firstParent;
			firstParent = secondParent;
			secondParent = tmp;
		}
		
		int point = rand.nextInt(1, firstParent.values.length);
		
		IntegerArraySolution child = firstParent.duplicate();
		for(int i = point; i < child.values.length; i++) {
			child.values[i] = secondParent.values[i];
		}
		
		return child;
	}

}
